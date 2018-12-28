package com.cw.wizbank.newmessage;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.newmessage.entity.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.supplier.utils.XMLHelper;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.entity.AppPushMessage;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.services.AppPushService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.web.WzbApplicationContext;
import com.cwn.wizbank.wechat.service.WechatService;
import com.oreilly.servlet.MultipartRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageService {
	
	public static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	/**
	 * weteam 消息推送中转页面
	 */
	public static final String WETEAM_TRANSFER_URL = "/mobile/views/weteam/change.html";
	
	static WechatService  wechatService = (WechatService) WzbApplicationContext.getBean("wechatService");
	static AppPushService  appPushService = (AppPushService) WzbApplicationContext.getBean("appPushService");
	
	/**
	 * 查询列表转成xml
	 * @param con
	 * @param modParam
	 * @param b
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public String getAllTemplate2Xml(Connection con, MessageModuleParam modParam, loginProfile prof) throws IllegalArgumentException, IllegalAccessException, SQLException {
		StringBuffer result = new StringBuffer();
		MessageTemplate msgTemplate = null;
		long tcr_id = prof.my_top_tc_id;
		if(prof.common_role_id.equalsIgnoreCase("ADM")){
			tcr_id = ViewTrainingCenter.getRootTcId(con);
		}
		List<MessageTemplate> list = getList(con, modParam, tcr_id);

		result.append("<templates>");
		for (int i = 0; i < list.size(); i++) {
			msgTemplate = list.get(i);
            /*if(!AccessControlWZB.hasCPDFunction()){//如果没有开放cpd功能
                if(msgTemplate.getMtp_type().equals("CPTD_OUTSTANDING_LEARNER") || msgTemplate.getMtp_type().equals("CPTD_OUTSTANDING_SUPERVISOR")){
                    modParam.getCwPage().setTotalRec(modParam.getCwPage().totalRec-1);
                    continue;
                }
            }*/
			result.append(XMLHelper.javaBeanToXML(msgTemplate));
		}
		result.append("</templates>");
		result.append(modParam.getCwPage().asXML());
		return result.toString();
	}
	
	/**
	 * 获取列表
	 * @param con
	 * @param modParam
	 * @return
	 * @throws SQLException
	 */
	
	public List<MessageTemplate> getList(Connection con, MessageModuleParam param, long top_tcr_id) throws SQLException {
		List<MessageTemplate> list = new ArrayList<MessageTemplate>();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		cwPagination cwPage = param.getCwPage();
		 long root_tcr_id = ViewTrainingCenter.getRootTcId(con);
		 if(top_tcr_id != root_tcr_id){
			 //如果是LN模式，先把邮件模块从顶层中把把模板复制一份出来。
			 coptyTemplates(con,  root_tcr_id, top_tcr_id) ;
		 }

		if (cwPage.sortCol == null || cwPage.sortCol.length() == 0) {
			cwPage.sortCol = "mtp_id";
		}
		if (cwPage.sortOrder == null || cwPage.sortOrder.length() == 0) {
			cwPage.sortOrder = "asc";
		}

		StringBuffer sql = new StringBuffer("select mtp_id, mtp_type, mtp_subject, mtp_active_ind, mtp_web_message_ind, mtp_tcr_id, mtp_update_ent_id, mtp_update_timestamp from messageTemplate where mtp_tcr_id = ?  ");

		sql.append(" order by " + cwPage.sortCol + " " + cwPage.sortOrder);
		try {
			stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int index = 1;
			stmt.setLong(index++, top_tcr_id);

			rs = stmt.executeQuery();
			int cnt = 0;
			rs.last();
			
			rs.beforeFirst();
			MessageTemplate msgTemplate;
			while (rs.next()) {
				if("EMPTY_TYPE".equals(rs.getString("mtp_type")) || "KNOW_ADD_MESSAGE".equals(rs.getString("mtp_type"))){
					continue;
				} else if(!AccessControlWZB.hasCPDFunction() && 
						(rs.getString("mtp_type").equals("CPTD_OUTSTANDING_LEARNER") || rs.getString("mtp_type").equals("CPTD_OUTSTANDING_SUPERVISOR"))){//如果没有开放cpd功能
					continue;
				}else{
					cnt++;
					if ((cnt > (cwPage.curPage - 1) * cwPage.pageSize && cnt <= (cwPage.curPage) * cwPage.pageSize)) {
						msgTemplate = new MessageTemplate();
						msgTemplate.setMtp_id(rs.getLong("mtp_id"));
						msgTemplate.setMtp_type(rs.getString("mtp_type"));
						msgTemplate.setMtp_subject(rs.getString("mtp_subject"));
						msgTemplate.setMtp_active_ind(rs.getBoolean("mtp_active_ind"));
						msgTemplate.setMtp_web_message_ind(rs.getBoolean("mtp_web_message_ind"));
						msgTemplate.setMtp_tcr_id(rs.getLong("mtp_tcr_id"));
						msgTemplate.setMtp_update_ent_id(rs.getLong("mtp_update_ent_id"));
						msgTemplate.setMtp_update_timestamp(rs.getTimestamp("mtp_update_timestamp"));
						
						list.add(msgTemplate);
					}
				}
			}
			cwPage.totalRec = cnt;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return list;
	}
	
	/**
	 * 找出顶级培训中心存在，但指定二级培训中心不存中的邮件模板，并复制到二级培训中心
	 * @param con
	 * @param root_tcr_id  顶级培训中心ID
	 * * @param des_tcr_id  二级培训中心ID
	 * @return
	 * @throws SQLException
	 */
	
	public void coptyTemplates(Connection con, long root_tcr_id, long dis_tcr_id) throws SQLException {
		if(root_tcr_id == dis_tcr_id){
			return;
		}
		
		if(root_tcr_id < 1){
			root_tcr_id = ViewTrainingCenter.getRootTcId(con);
		}
		//先拿到下级培训中没有模板
		MessageTemplate msgTemplate = new MessageTemplate();
		List<Long>  list = msgTemplate.getRecordForCopy( con,  root_tcr_id,  dis_tcr_id);
		if(list != null && list.size() > 0){
			for(long mtp_id : list){
				long new_mtp_id = msgTemplate.copyMessageTemplate( con,  dis_tcr_id,  mtp_id) ;
				if(new_mtp_id > 0){
					MessageParamName.copyRecord( con, mtp_id, new_mtp_id); 
				}
			}
		}
		
	}

	/**获取模板信息
	 * @param con
	 * @param mtp_id
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String getTemplateDetailXML(Connection con, long mtp_id, boolean isUpd) throws SQLException, IllegalArgumentException, IllegalAccessException {
		StringBuffer result = new StringBuffer();
		MessageTemplate msgTemplate = new MessageTemplate();
		msgTemplate.setMtp_id(mtp_id);
		msgTemplate.setUpd(isUpd);
		msgTemplate.get(con);
		
		msgTemplate.setMtp_header_img(ContextPath.getContextPath() + msgTemplate.getMtp_header_img());
		
		msgTemplate.setMtp_footer_img(ContextPath.getContextPath() + msgTemplate.getMtp_footer_img());
		
		result.append(XMLHelper.javaBeanToXML(msgTemplate));
		
		MessageParamName msgParam = new MessageParamName();
		List<MessageParamName> list = msgParam.getByMtpId(con, mtp_id);
		result.append("<params>");
		for (int i = 0; i < list.size(); i++) {
			msgParam = list.get(i);
			result.append(XMLHelper.javaBeanToXML(msgParam));
		}
		result.append("</params>");
		return result.toString();
	}

	/**修改模板
	 * @param con
	 * @param prof
	 * @param modParam
	 * @throws Exception
	 */
	public void updateTemplate(Connection con, WizbiniLoader wizbini, loginProfile prof, MessageModuleParam modParam, MultipartRequest multi) throws Exception {
		MessageTemplate msgTemplate = new MessageTemplate();
		msgTemplate.setMtp_id(modParam.getMtp_id());
		msgTemplate.setMtp_subject(modParam.getMtp_subject());
		msgTemplate.setMtp_content(modParam.getMtp_content());
		msgTemplate.setMtp_active_ind(modParam.isMtp_active_ind());
		msgTemplate.setMtp_web_message_ind(modParam.isMtp_web_message_ind());
		
		msgTemplate.setMtp_update_ent_id(prof.usr_ent_id);
		msgTemplate.setMtp_update_timestamp(modParam.getCur_time());
		if(("default").equalsIgnoreCase(modParam.getHeader_img_select())){
			msgTemplate.setMtp_header_img(modParam.getHeader_img());
		} else if(("local").equalsIgnoreCase(modParam.getHeader_img_select())){
			saveImage(multi, wizbini, msgTemplate, "mtp_header_img", prof.my_top_tc_id);
		}
		
		if(("default").equalsIgnoreCase(modParam.getFooter_img_select())){
			msgTemplate.setMtp_footer_img(modParam.getFooter_img());
		} else if(("local").equalsIgnoreCase(modParam.getFooter_img_select())){
			saveImage(multi, wizbini, msgTemplate, "mtp_footer_img", prof.my_top_tc_id);
		}
		
		msgTemplate.update(con);
	}
	
	/**上传模版图片
	 * @param multi
	 * @param wizbini
	 * @param msgTemplate
	 * @param name
	 * @param tcrId
	 * @throws Exception
	 */
	public void saveImage(MultipartRequest multi, WizbiniLoader wizbini, MessageTemplate msgTemplate, String name, long tcrId) throws Exception{
		File image = multi.getFile(name);
		if (image != null) {
			//保存上传的图片
			String saveDirPath = wizbini.getFileUploadMsgDirAbs() + dbUtils.SLASH + tcrId;
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String filename = image.getName();
			String new_filename = System.currentTimeMillis() + "";
			String type = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			new_filename +=  "." + type;

			File targetFile = new File(saveDirPath, new_filename);
			image.renameTo(targetFile);
			if(("mtp_header_img").equalsIgnoreCase(name)){
				msgTemplate.setMtp_header_img("/" + wizbini.cfgSysSetupadv.getFileUpload().getMsgDir().getName() + "/" + tcrId + "/" + new_filename);
			} else if(("mtp_footer_img").equalsIgnoreCase(name)){
				msgTemplate.setMtp_footer_img("/" + wizbini.cfgSysSetupadv.getFileUpload().getMsgDir().getName() + "/" + tcrId + "/" + new_filename);
			}
		}
	}
	
	/**插入Message, 无附件
	 * @throws cwException 
	 */
	public void insMessage(Connection con, MessageTemplate mtp,
			String sender_usr_id, long[] rec_ent_id_array, long[] cc_ent_id_array,
			Timestamp sendTime, String[] contents,long itm_id) throws SQLException, qdbException, cwException {
		insMessage(con, mtp, sender_usr_id, rec_ent_id_array, cc_ent_id_array, sendTime, contents, null, itm_id, null);
	}
	
	/**notify
	 * @throws cwException 
	 */
	public void insMessage(Connection con, MessageTemplate mtp,
			String sender_usr_id, long[] rec_ent_id_array, long[] cc_ent_id_array,
			Timestamp sendTime, String[] contents,long itm_id, String cc_email_address) throws SQLException, qdbException, cwException {
		insMessage(con, mtp, sender_usr_id, rec_ent_id_array, cc_ent_id_array, sendTime, contents, null, itm_id, cc_email_address);
	}

	/**插入Message
	 * @param con
	 * @param prof
	 * @param sender_usr_id
	 * @param rec_ent_id_array
	 * @param cc_ent_id_array
	 * @param attachment 附件，多个逗号分隔
	 * @param sendTime
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwException 
	 */
	public long insMessage(Connection con, MessageTemplate mtp,
			String sender_usr_id, long[] rec_ent_id_array, long[] cc_ent_id_array,
			Timestamp sendTime, String[] contents, String attachment,long itm_id, String cc_email_address) throws SQLException, qdbException, cwException {
		CommonLog.debug("-------Insert Message, Mtp_type="+mtp.getMtp_type());
		Timestamp cur_time = cwSQL.getTime(con);
		String email_content = contents[0];
		String pc_content = contents[1];
		String mobile_content = contents[2];
		String pc_admin_content = "";
		if(contents.length>5){
			pc_admin_content = contents[5];
		}
		//String pc_admin_content = contents[5];
		
		long sender_ent_id = dbRegUser.getEntId(con, sender_usr_id);
		
		//插入邮件:同一封邮件多个收件人插入一条记录，同时插入一条未发送的记录
		EmailMessage emsg = new EmailMessage();
		emsg.setEmsg_itm_id(itm_id);
		emsg.setEmsg_mtp_id(mtp.getMtp_id());
		emsg.setEmsg_send_ent_id(sender_ent_id);
		emsg.setEmsg_rec_ent_ids(dbUtils.longArray2String(rec_ent_id_array, ","));
		if(cc_ent_id_array != null && cc_ent_id_array.length>0){
			emsg.setEmsg_cc_ent_ids(dbUtils.longArray2String(cc_ent_id_array, ","));
		}
		emsg.setEmsg_subject(mtp.getMtp_subject());
		
		emsg.setEmsg_content(email_content);
		emsg.setEmsg_attachment(attachment);
		emsg.setEmsg_target_datetime(sendTime);
		emsg.setEmsg_create_ent_id(sender_ent_id);
		emsg.setEmsg_create_timestamp(cur_time);
		
		if(cc_email_address != null) {
			emsg.setEmsg_cc_email(cc_email_address);
		}

		MessageDao mdao = new MessageDao();
		mdao.insEmailMessage(con, emsg);
		
		EmailMsgRecHistory emrh = new EmailMsgRecHistory();
		if(emsg.getEmsg_id()>0){
			emrh.setEmrh_emsg_id(emsg.getEmsg_id());
			emrh.setEmrh_status(EmailMsgRecHistory.SEND_TYPE_NO);
			emrh.setEmrh_sent_datetime(null);
			emrh.setEmrh_attempted(0);
			mdao.insEmsgHis(con, emrh);
		}
		
		long emailMessagId = MessageDao.getEmailMessagMaxId(con);
		
		//插入站内信:同一封邮件多个收件人插入多条记录
		if(mtp.isMtp_web_message_ind()){
			WebMessage wmsg = new WebMessage();
			
			wmsg.setWmsg_mtp_id(mtp.getMtp_id());
			wmsg.setWmsg_send_ent_id(sender_ent_id);
			wmsg.setWmsg_subject(mtp.getMtp_subject());
			wmsg.setWmsg_type(WebMessage.WMSG_TYPE_SYS);
			wmsg.setWmsg_content_pc(pc_content);
			wmsg.setWmsg_admin_content_pc(pc_admin_content);
			wmsg.setWmsg_content_mobile(mobile_content);
			wmsg.setWmsg_attachment(attachment);
			wmsg.setWmsg_target_datetime(sendTime);
			wmsg.setWmsg_create_ent_id(sender_ent_id);
			wmsg.setWmsg_create_timestamp(cur_time);
			
			
			for(int i=0; i<rec_ent_id_array.length; i++){
				
				mdao.insWebMessage(con, wmsg, rec_ent_id_array[i]);
				long wmsg_id = MessageDao.getWebMessagMaxId(con);
				
				//插入邮件和赞内信的关系表
				MessageDao.insertRelationBetweenEmailMessageAndWebMessage(con, emailMessagId, wmsg_id);
			
			}
			
			if(contents != null && contents.length >= 4  && contents[3] != null && contents[3].trim().length() > 0){
				String content = "";
				String url = "";
				if( contents.length >= 4){
					content = contents[3];
				}
				if( contents.length >= 5  && contents[4] != null && contents[4].trim().length() > 0){
					url = contents[4].trim();
				}
				
				String subject = null;
				if(mtp!=null&&mtp.getCourseTitle()!=null&&mtp.getCourseTitle().length()>0){
					subject = mtp.getCourseTitle();
				}else{
					subject = wmsg.getWmsg_subject();
				}
				
				String mtp_type = null;
				if(mtp!=null){
					mtp_type = mtp.getMtp_type();
				}
				
			}
				
		}
		
		
		return emsg.getEmsg_id();
	}
	
	public static void pushMsgToAppOrWechat(List<String> list){
		if(list != null && list.size() > 0){
			
			String sender_ent_id = list.get(0);
			String rec_ent_id = list.get(1);
			String wmsg_id = list.get(2);
			String subject = list.get(3);
			String content = list.get(4);
			
			//推送到微信
			wechatService.pushMsgToWechat(Long.parseLong(sender_ent_id),Long.parseLong(rec_ent_id),Long.parseLong(wmsg_id),subject,content);
			
			//推送到APP
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type","message");
			param.put("msg_id", wmsg_id);
			
			AppPushMessage apm = new AppPushMessage(subject, content, param);
			appPushService.pushMessage2User(Long.parseLong(rec_ent_id), apm);
		}
	}
	

	/**
	 * 推送消息到 weteam
	 * @param sender_ent_id 登录人的id
	 * @param wmsg_subject 信息标题
	 * @param content 信息内容
	 * @param url 中转地址
	 * @throws SQLException 
	 */
	public static void pushMsg(String rec_user, String title,
			String content, String url,String mtp_type){
		
String apiUrl = getApiUrl();
		
		if(apiUrl == null || apiUrl.trim().length() <= 0 ){
			return;
		}
		
		
		if(content != null && content.trim().length()>0){
			try {
				content = URLEncoder.encode(content, "utf-8");
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
		
		
		StringBuffer postData = new StringBuffer();
		postData.append("loginname="+rec_user)
				.append("&title="+title)
				.append("&content="+content)
				.append("&mtp_type="+mtp_type);
		
		if(url != null && url.length() > 0){
			postData.append("&url="+url);
		}
		
		String result = null;
		
		try {
			result = SendHttpRequest.sendUrl_new(apiUrl, postData.toString());
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		}finally{
			logger.info("weteamResult", result);
		}
		
	}

	
	private static String getApiUrl(){

		StringBuffer result = new StringBuffer();
		String webTeamUrl = Application.SYS_WEBTEAM_URL;
		if(webTeamUrl != null && webTeamUrl.length() > 0){
			result.append(webTeamUrl).append("/feed/add");
		}else{
			return null;
		}
		return result.toString();
//		return "http://localhost:6666/app/admin/wechat/weixin";
	}


	/**获取课程有关消息内容
	 * @param mtp
	 *@param msg_type:email, pc, mobile
     * @param ent_id 
	 * @return
	 * @throws SQLException 
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 */
	public String[] getMsgContent(Connection con, MessageTemplate mtp, long lrn_ent_id, long action_ent_id, long app_itm_id, long app_id, long[] ent_id) throws SQLException, cwSysMessage, qdbException {
		return this.getMsgContent(con, mtp, lrn_ent_id, new long[]{action_ent_id}, app_itm_id, app_id, ent_id);
	}
	
	public String[] getMsgContent(Connection con, MessageTemplate mtp, long lrn_ent_id, long[] action_ent_ids, long app_itm_id, long app_id, long[] ent_id) throws SQLException, cwSysMessage, qdbException {
		String[] contents = new String[6];
		long cos_itm_id = app_itm_id;
		if (aeItem.getRunInd(con, app_itm_id)){
			aeItemRelation ir = new aeItemRelation();
			ir.ire_child_itm_id = app_itm_id;
			cos_itm_id = ir.getParentItemId(con);
		}
		Timestamp[] start_end_date = aeItem.getItemStartEndDate(con, app_id);
		
		//url前缀
		boolean login_url = false;
		String	email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
		String	pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
		String	mobile_per_link = null;
		if("ENROLLMENT_CONFIRMED".equalsIgnoreCase(mtp.getMtp_type())) {
			mobile_per_link = "javascript:clicked('../course/detail.html?itmId=" + EncryptUtil.cwnEncrypt(app_itm_id) + "', true)";
		} else {
			mobile_per_link = mtp.getMtp_content_mobile_link();
		}
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		Map<String, String> weteam_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			String value = "";
			if(mpn_name.equalsIgnoreCase("[Learner name]")){
				value = dbRegUser.getDisplayBil(con, lrn_ent_id);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Course name]")){
				value = aeItem.getItemTitle(con, cos_itm_id);
				content = content.replace(mpn_name, value);
				
				//weteam 添加 课程 title
				mtp.setCourseTitle(value);
			}else if(mpn_name.equalsIgnoreCase("[Course code]")){
				value = aeItem.getItemCode(con, cos_itm_id);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Training Administrator]") || (mpn_name.equalsIgnoreCase("[Approval Name]"))){
				StringBuffer temp = new StringBuffer();
				for(long action_ent_id : action_ent_ids) {
					temp.append(dbRegUser.getDisplayBil(con, action_ent_id)).append(",");
				}
				temp.delete(temp.length()-1, temp.length());
				content = content.replace(mpn_name, temp.toString());
			}else if(mpn_name.equalsIgnoreCase("[Class name]")){
				value = aeItem.getItemTitle(con, app_itm_id);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Course period start date]")){
				if(null!=start_end_date && null!=start_end_date[0] ){
					value = new SimpleDateFormat("yyyy/MM/dd").format(start_end_date[0]);
					content = content.replace(mpn_name, value);
				}
			}else if(mpn_name.equalsIgnoreCase("[Course period end date]")){
				if(null!=start_end_date && null!=start_end_date[1] ){
					value = new SimpleDateFormat("yyyy/MM/dd").format(start_end_date[1]);
					content = content.replace(mpn_name, value);
				}
			}else if(mpn_name.equalsIgnoreCase("[Learner(s)]")){
				//JI, REMINDER需要，每次只会插入一条msg记录，不会有多个收件人；
				value = dbRegUser.getDisplayBil(con, lrn_ent_id);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Link to login page]")){
				login_url = true;
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img",getEmailLinkPre(mtp.getMtp_header_img()));
				mobile_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				weteam_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				mobile_img_map.put("mtp_footer_img",  getEmailLinkPre(mtp.getMtp_footer_img()));
				weteam_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
			}
		}

		String email_content = content;
		String pc_content = content;
		String pc_admin_content = content;
		String mobile_content = content;
		String weteam_content = content;
		if(login_url) {
			WizbiniLoader wizbini = qdbAction.wizbini;
			if (!"ENROLLMENT_NEXT_APPROVERS".equals(mtp.getMtp_type())) {
				long action_ent_id = (action_ent_ids != null && action_ent_ids.length > 0) ? action_ent_ids[0] : null;
				for (int i = 0; i < ent_id.length; i++) {
					if ("ENROLLMENT_CONFIRMED".equals(mtp.getMtp_type())) {
						email_per_link += app_itm_id + "&userID=" + lrn_ent_id + "&appID=" + app_id + "&action_ent_id=" + action_ent_id + "&acc_ent_id=" + ent_id[i] + "&type=enroll";
						pc_per_link += EncryptUtil.cwnEncrypt(app_itm_id);
					}
					if ("ENROLLMENT_NEW".equals(mtp.getMtp_type())) {
						email_per_link += app_itm_id + "&userID=" + lrn_ent_id + "&appID=" + app_id + "&action_ent_id=" + action_ent_id + "&acc_ent_id=" + ent_id[i] + "&type=enroll";
						pc_per_link += EncryptUtil.cwnEncrypt(app_itm_id);
					} else if ("ENROLLMENT_NEXT_APPROVERS".equals(mtp.getMtp_type())) {
						email_per_link += app_itm_id + "&userID=" + lrn_ent_id + "&appID=" + app_id + "&action_ent_id=" + action_ent_id + "&acc_ent_id=" + ent_id[i] + "&type=approval";
						pc_per_link += EncryptUtil.cwnEncrypt(app_itm_id);
					}
				}
			}

				email_content = repalceLoginUrl(content, email_per_link);
				pc_content = repalceLoginUrl(content, pc_per_link);
				pc_admin_content = pc_admin_content.replace("[Link to login page]", "");
			mobile_content = repalceLoginUrl(content, mobile_per_link);
				weteam_content = clearLoginUrl(content);

		}
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			pc_admin_content = mtp.changeMtpContentImg(pc_img_map, pc_admin_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
			weteam_content = mtp.changeMtpContentImgForWebTeam(weteam_img_map, weteam_content);
		}
		
		contents[0] = email_content;
		contents[1] = pc_content;
		contents[2] = mobile_content;
		contents[3] = weteam_content;
		contents[5] = pc_admin_content;
		
		if(mobile_per_link != null && mobile_per_link.length() > 0){
			contents[4] = MessageService.WETEAM_TRANSFER_URL+"?url="+mobile_per_link;
		}
		
		return contents;
	}
	
	
	   /**获取CPD未完成时数邮件内容
     * @param mtp
     *@param msg_type:email, pc, mobile
     * @return
     * @throws SQLException 
     * @throws cwSysMessage 
     * @throws qdbException 
     */
    public String[] getCpdOutstandingMsgContent(Connection con, MessageTemplate mtp, long lrn_ent_id, String licenseAlias) throws SQLException, cwSysMessage, qdbException {
        String[] contents = new String[6];
        
        //url前缀
        String  email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
        String  pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
        String  mobile_per_link = mtp.getMtp_content_mobile_link();
        
        //邮件头部和底部图片
        boolean hasImageInd = false;
        Map<String, String> email_img_map = new HashMap<String, String>();
        Map<String, String> pc_img_map = new HashMap<String, String>();
        Map<String, String> mobile_img_map = new HashMap<String, String>();
        Map<String, String> weteam_img_map = new HashMap<String, String>();
        
        mtp.setMtp_subject(mtp.getMtp_subject().replace("[License Alias]", licenseAlias)); 
        
        String content = mtp.getMtp_content();
        Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
        for(int i=0; i<mpn_vec.size(); i++){
            String mpn_name = mpn_vec.get(i);
            String value = "";
            if(mpn_name.equalsIgnoreCase("[Learner name]")){
                value = dbRegUser.getDisplayBil(con, lrn_ent_id);
                content = content.replace(mpn_name, value);
            }else if(mpn_name.equalsIgnoreCase("[License Alias]")){
                value = licenseAlias;
                content = content.replace(mpn_name, value);
            }else if(mpn_name.equalsIgnoreCase("[Header image]")){
                hasImageInd = true;
                email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
                pc_img_map.put("mtp_header_img",getEmailLinkPre(mtp.getMtp_header_img()));
                mobile_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
                weteam_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
            }else if(mpn_name.equalsIgnoreCase("[Footer image]")){
                hasImageInd = true;
                email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
                pc_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
                mobile_img_map.put("mtp_footer_img",  getEmailLinkPre(mtp.getMtp_footer_img()));
                weteam_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
            }
        }

        String email_content = content;
        String pc_content = content;
        String pc_admin_content = content;
        String mobile_content = content;
        String weteam_content = content;
        if(hasImageInd){
            email_content = mtp.changeMtpContentImg(email_img_map, email_content);
            pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
            pc_admin_content = mtp.changeMtpContentImg(pc_img_map, pc_admin_content);
            mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
            weteam_content = mtp.changeMtpContentImgForWebTeam(weteam_img_map, weteam_content);
        }
        
        contents[0] = email_content;
        contents[1] = pc_content;
        contents[2] = mobile_content;
        contents[3] = weteam_content;
        contents[5] = pc_admin_content;
        
        if(mobile_per_link != null && mobile_per_link.length() > 0){
            contents[4] = MessageService.WETEAM_TRANSFER_URL+"?url="+mobile_per_link;
        }
        
        return contents;
    }

	
	/**
	 * 去掉链接地址
	 * @param content
	 * @return
	 */
	private String clearLoginUrl(String content) {
		String rs = content;
		rs = content.replace("[Link to login page]", "");
		return rs;
	}
	
	private String repalceLoginUrl(String content, String per_link){
		String rs = content;
		if(per_link == null){
			per_link = "";
		}
		rs = content.replace("[Link to login page]", "<a href=\""+ per_link + " \" >Go</a>");
		return rs;
	}
	/**获取注册审批/拒绝用户消息内容 [USR_REG_DISAPPROVE|USR_REG_APPROVE]
	 * @param con
	 * @param mtp
	 * @param usr_display_bil
	 * @param usr_ste_usr_id
	 * @param usr_pwd
	 * @param reason
	 * @return
	 * @throws SQLException 
	 */
	public String[] getApprovUsrMsgContent(Connection con, MessageTemplate mtp, String usr_display_bil, String usr_ste_usr_id, String usr_pwd, String reason) throws SQLException {
		String[] contents = new String[3];
		//url前缀
		boolean login_url = false;
		String	email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
		String	pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
		String	mobile_per_link = mtp.getMtp_content_mobile_link();
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			String value = "";
			if(mpn_name.equalsIgnoreCase("[Learner name]")){
				value = usr_display_bil;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Login id]")){
				value = usr_ste_usr_id;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Learner password]")){
				value = usr_pwd;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Refuse reason]")){
				value = reason;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Link to login page]")){
				login_url = true;
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img", mtp.getMtp_header_img());
				mobile_img_map.put("mtp_header_img", mtp.getMtp_header_img());
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
				mobile_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
			}
		}

		String email_content = content;
		String pc_content = content;
		String mobile_content = content;
		if(login_url){
			email_content = repalceLoginUrl(content, email_per_link);
			pc_content = repalceLoginUrl(content, pc_per_link);
			mobile_content = repalceLoginUrl(content, mobile_per_link);
		}
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
		}
		
		contents[0] = email_content;
		contents[1] = pc_content;
		contents[2] = mobile_content;
		
		return contents;
	}
	
	/**导入用户/报名成功消息内容[USER_IMPORT_SUCCESS|ENROLLMENT_IMPORT_SUCCESS]
	 * @param con
	 * @param mtp
	 * @param usr_ent_id
	 * @param name
	 * @param startTime
	 * @param endTime
	 * @param success_total
	 * @param unsuccess_total
	 * @return
	 * @throws SQLException 
	 */
	public String[] getImportMsgContent(Connection con, MessageTemplate mtp,long usr_ent_id, String src_file, long startTime, long endTime,
			String success_total, String unsuccess_total) throws SQLException {
	     
		String[] contents = new String[3];
		//url前缀
		boolean login_url = false;
		String	email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
		String	pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
		String	mobile_per_link = mtp.getMtp_content_mobile_link();
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			String value = "";
			if(mpn_name.equalsIgnoreCase("[User name]")){
				value = dbRegUser.getDisplayBil(con, usr_ent_id);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Src file]")){
				value = src_file;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Import start date]")){
				value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startTime);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Import end date]")){
				value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endTime);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Success total]")){
				value = success_total;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Unsuccess total]")){
				value = unsuccess_total;
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Link to login page]")){
				login_url = true;
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img", mtp.getMtp_header_img());
				mobile_img_map.put("mtp_header_img", mtp.getMtp_header_img());
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
				mobile_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
			}
		}

		String email_content = content;
		String pc_content = content;
		String mobile_content = content;
		if(login_url){
			email_content = repalceLoginUrl(content, email_per_link);
			pc_content = repalceLoginUrl(content, pc_per_link);
			mobile_content = repalceLoginUrl(content, mobile_per_link);
		}
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
		}
		
		contents[0] = email_content;
		contents[1] = pc_content;
		contents[2] = mobile_content;
		
		return contents;
	}

	public String[] getSysPerfMsgContent(Connection con, MessageTemplate mtp,
			long active_user, long warning_user, long blocking_user, Timestamp createTime) throws SQLException {
		String[] contents = new String[3];
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			String value = "";
			if(mpn_name.equalsIgnoreCase("[Active user]")){
				value = active_user+"";
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Warning user]")){
				value = warning_user+"";
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Blocking user]")){
				value = blocking_user+"";
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Warning date]")){
				value = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(createTime);
				content = content.replace(mpn_name, value);
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img", mtp.getMtp_header_img());
				mobile_img_map.put("mtp_header_img", mtp.getMtp_header_img());
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
				mobile_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
			}
		}
		
		String email_content = content;
		String pc_content = content;
		String mobile_content = content;
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
		}

		contents[0] = email_content;
		contents[1] = pc_content;
		contents[2] = mobile_content;
		return contents;
	}
	
	/**获取忘记密码消息内容
	 * @param con
	 * @param mtp
	 * @param usr_ent_id
	 * @param getResetPwdMsgContent
	 * @param req_times
	 * @return
	 * @throws SQLException
	 */
	public String[] getResetPwdMsgContent(Connection con, MessageTemplate mtp, long usr_ent_id, int link_max_days, String req_times, String reset_link, String lang) throws SQLException {
	     
		String[] contents = new String[3];
		//url前缀
		boolean reste_pwd_url = false;
		String	email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
		String	pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
		String	mobile_per_link = mtp.getMtp_content_mobile_link();
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			if(mpn_name.equalsIgnoreCase("[Request time]")){
				content = content.replace(mpn_name, req_times);
			}else if(mpn_name.equalsIgnoreCase("[Max days]")){
				content = content.replace(mpn_name, link_max_days+"");
			}else if(mpn_name.equalsIgnoreCase("[Link to reset password]")){
				reste_pwd_url = true;
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img", mtp.getMtp_header_img());
				mobile_img_map.put("mtp_header_img", mtp.getMtp_header_img());
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
				mobile_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
			}
		}

		String email_content = content;
		String pc_content = content;
		String mobile_content = content;
		if(reste_pwd_url){
			email_content = content.replace("[Link to reset password]", "<a href=\'"+email_per_link+reset_link+"&lang="+lang + "\' >"+email_per_link+reset_link+"</a>");
			pc_content = content.replace("[Link to reset password]", "<a href=\'"+pc_per_link+reset_link+"&lang="+lang + "\' >"+pc_per_link+reset_link + "</a>");
			mobile_content = content.replace("[Link to reset password]", "<a href=\'"+mobile_per_link+reset_link+"&lang="+lang + "\' >"+mobile_per_link+reset_link + "</a>");
		}
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
		}
		
		contents[0] = email_content;
		contents[1] = pc_content;
		contents[2] = mobile_content;
		
		return contents;
	}
	
    /**班级报名成功时插入JI, Reminder两封邮件
     * @param con
     * @param prof
     * @param itm_id: app_itm_id
     * @param app_ent_id: 学员
     * @param templateType
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     * @throws qdbException
     */
    public void insItemJINotify(Connection con, loginProfile prof, long itm_id, long app_ent_id, long app_id, String templateType, Timestamp sendTime)
    	throws SQLException, cwException, cwSysMessage, qdbException {
        
        String sender_usr_id;
        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = prof.usr_ent_id;
        usr.getByEntId(con);
        sender_usr_id = usr.usr_id;
                 
        //插入邮件及邮件内容
        MessageService msgService = new MessageService();
        String mtp_type = templateType;
        
		MessageTemplate mtp = new MessageTemplate();
		mtp.setMtp_tcr_id(ViewTrainingCenter.getTopTc(con, prof.usr_ent_id, false));
		mtp.setMtp_type(mtp_type);
		mtp.getByTcr(con);
		
		// 获取课程所有的培训管理员
		Vector<aeItemAccess> vectory = aeItemAccess.getItemAccessByItem(con, itm_id);
		long[] ent_ids = new long[vectory.size()];
		for(int i = 0 ; i < vectory.size() ; i++) {
			aeItemAccess aia = vectory.get(i);
			ent_ids[i] = aia.iac_ent_id;
		}
		// 更新邮件内容
        String[] contents = msgService.getMsgContent(con, mtp, app_ent_id, ent_ids, itm_id, app_id,new long[]{app_ent_id});
        msgService.insMessage(con, mtp, sender_usr_id, new long[]{app_ent_id}, new long[0], sendTime, contents,itm_id);
        return;
	}

    /**发送邮件 , one message for all recipients
     * @param con
     * @param msg_id
     * @param static_env
     * @throws SQLException
     * @throws cwException
     * @return attempted 已经发送的次数
     */
    public static long sendMessage(Connection con, long msg_id, qdbEnv static_env)
        throws SQLException, cwException {
    			
    			MessageDao mdao = new MessageDao();
    			EmailMessage emsg = new EmailMessage();
    			emsg.setEmsg_id(msg_id);
    			mdao.getEmailMessage(con, emsg);
    			boolean send_result = false;
    			MessageOutbox msgBox = new MessageOutbox(static_env);
                try{
                	send_result = msgBox.send(con, "HTML", emsg);
                }catch( Exception e ) {
                	msgBox.errorToLog(con, msg_id, e.getMessage());
                }
                
                Vector<Long> msgIdVec = new Vector<Long>();
                msgIdVec.add(msg_id);
                long attempted = mdao.getAttempt(con, msg_id)+1;
                Timestamp sent_datetime = cwSQL.getTime(con);
                String status = "";
                if(send_result){
                	status = EmailMsgRecHistory.SEND_TYPE_YES;
                }else{
                	status = EmailMsgRecHistory.SEND_TYPE_NO;
                }
                
                mdao.updEmsgHisStatus(con, msgIdVec, status, sent_datetime, attempted);
                con.commit();
                return attempted;
        }
    
    
	public String getEmailLinkPre(String link) {
		if(link == null){
			link = Application.MAIL_SCHEDULER_DOMAIN;
		}
		else if(Application.MAIL_SCHEDULER_DOMAIN != null ){
			if(Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && link.startsWith("/")){
				link = Application.MAIL_SCHEDULER_DOMAIN + link.substring(1);
			}else if((Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && !link.startsWith("/")) 
					|| (!Application.MAIL_SCHEDULER_DOMAIN.endsWith("/") && link.startsWith("/"))){
				link = Application.MAIL_SCHEDULER_DOMAIN + link;
			}else{
				link = Application.MAIL_SCHEDULER_DOMAIN +"/"+link;
			}
		}
		return link;
	}
	
	
	/**获取新增在线问答消息内容
	 * @param con
	 * @param mtp
	 * @param usr_ent_id
	 * @param getResetPwdMsgContent
	 * @param req_times
	 * @return
	 * @throws SQLException
	 */
	public String[] getKnowQuestionContent(Connection con, MessageTemplate mtp, loginProfile prof, KnowQuestion knowQuestion, String learnerName) throws SQLException {
	     
		String[] contents = new String[4];
		//url前缀
		boolean know_url = false;
		String	email_per_link = getEmailLinkPre(mtp.getMtp_content_email_link());
		String	pc_per_link = getEmailLinkPre(mtp.getMtp_content_pc_link());
		String	mobile_per_link = getEmailLinkPre(mtp.getMtp_content_mobile_link());
		
		//邮件头部和底部图片
		boolean hasImageInd = false;
		Map<String, String> email_img_map = new HashMap<String, String>();
		Map<String, String> pc_img_map = new HashMap<String, String>();
		Map<String, String> mobile_img_map = new HashMap<String, String>();
		
		String content = mtp.getMtp_content();
		String title = mtp.getMtp_subject();
		Vector<String> mpn_vec = MessageParamName.getParamNameVec(con, mtp.getMtp_id());
		for(int i=0; i<mpn_vec.size(); i++){
			String mpn_name = mpn_vec.get(i);
			if(mpn_name.equalsIgnoreCase("[Learner name]")){
				content = content.replace(mpn_name, learnerName);
			}else if(mpn_name.equalsIgnoreCase("[Question title]")){
				content = content.replace(mpn_name, knowQuestion.getQue_title());
			}else if(mpn_name.equalsIgnoreCase("[User name]")){
				content = content.replace(mpn_name, prof.usr_display_bil);
				title = title.replace(mpn_name, prof.usr_display_bil);
			}else if(mpn_name.equalsIgnoreCase("[Link to KnowDetail]")){
				know_url = true;
			}else if(mpn_name.equalsIgnoreCase("[Header image]")){
				hasImageInd = true;
				email_img_map.put("mtp_header_img", getEmailLinkPre(mtp.getMtp_header_img()));
				pc_img_map.put("mtp_header_img", mtp.getMtp_header_img());
				mobile_img_map.put("mtp_header_img", mtp.getMtp_header_img());
			}else if(mpn_name.equalsIgnoreCase("[Footer image]")){
				hasImageInd = true;
				email_img_map.put("mtp_footer_img", getEmailLinkPre(mtp.getMtp_footer_img()));
				pc_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
				mobile_img_map.put("mtp_footer_img", mtp.getMtp_footer_img());
			}
		}

		String email_content = content;
		String pc_content = content;
		String mobile_content = content;
		if(know_url){
			email_content = content.replace("[Link to KnowDetail]", "<a style='color:#428bca;' href="+email_per_link + knowQuestion.getQue_id() + "?path=message >"+email_per_link + knowQuestion.getQue_id() +"</a>");
			pc_content = content.replace("[Link to KnowDetail]", "<a style='color:#428bca;' href= "+pc_per_link + knowQuestion.getQue_id() + "?path=message >"+pc_per_link + knowQuestion.getQue_id()  + "</a>");
//			mobile_content = content.replace("[Link to KnowDetail]", "<a style='color:#428bca;' href="+mobile_per_link + "?type="+knowQuestion.getQue_type()+"&id="+knowQuestion.getQue_id()+" >"+mobile_per_link + "?type="+knowQuestion.getQue_type()+"&id="+ knowQuestion.getQue_id()  + "</a>");
			mobile_content = content.replace("[Link to KnowDetail]", "<a style='color:#428bca;' href='javascript:clicked(\"../know/detail.html?type="+knowQuestion.getQue_type()+"&id="+knowQuestion.getQue_id()+"\",true);' >"+mobile_per_link + "?type="+knowQuestion.getQue_type()+"&id="+ knowQuestion.getQue_id()  + "</a>");
		}
		
		if(hasImageInd){
			email_content = mtp.changeMtpContentImg(email_img_map, email_content);
			pc_content = mtp.changeMtpContentImg(pc_img_map, pc_content);
			mobile_content = mtp.changeMtpContentImg(mobile_img_map, mobile_content);
		}
		
		contents[0] = title;
		contents[1] = email_content;
		contents[2] = pc_content;
		contents[3] = mobile_content;
		
		return contents;
	}
	
	/**
	 * 根据emailMessage id 获取站内信列表
	 * @param emsgId
	 * @return
	 * @throws SQLException 
	 */
	public static List<WebMessage> getWebMessageListByEmsgId(Connection con,long emsgId) throws SQLException{
		return MessageDao.selectWebMessageListByEmsgId(con,emsgId);
	}
}
