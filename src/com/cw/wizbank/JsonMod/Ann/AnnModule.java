package com.cw.wizbank.JsonMod.Ann;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.Ann.bean.AnnBean;
import com.cw.wizbank.JsonMod.Ann.bean.ReceiptBean;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * 用于处理与公告有关的信息
 */
public class AnnModule extends ServletModule {
	public static final    String MODULENAME    = "rec";
	AnnModuleParam modParam;

	public AnnModule() {
		super();
		modParam = new AnnModuleParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException {
		try {
				if(this.prof == null || this.prof.usr_ent_id == 0){	// 若还是未登录
					throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
				} else {	// 若已经登陆
					
					if (modParam.getCmd().equalsIgnoreCase("get_all_ann")) {						
						Ann ann = new Ann();
						Vector tc_lst=new Vector();
						Vector annVc = new Vector();
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();						
							// 获取用户相关的培训中心	
							tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);	
							// 根据培训中心获取相应的公告
						}
						annVc = ann.getAnnByTC(con, prof, modParam, tc_lst,  wizbini);
						
						if(tc_lst !=null && tc_lst.size()>0)
							resultJson.put("tcr_lst", tc_lst);						
						annVc = ann.getAnnByTC(con, prof, modParam, tc_lst,  wizbini);
											
						Map annMap = new HashMap();
						annMap.put("ann_lst",annVc);
						annMap.put("total", new Integer(modParam.getTotal_rec()));
						resultJson.put("ann",annMap);				
					} else if (modParam.getCmd().equalsIgnoreCase("get_all_ann_page")) {
						//公告列表分页、排序（独立出来，只需刷新列表部分）
						Ann ann=new Ann();
						Vector annVc = ann.getAnnByTc_ID(con, prof, modParam, modParam.getTcr_id(), false,  wizbini);
						Map annMap=new HashMap();
						annMap.put("ann_lst",annVc);
						annMap.put("total",  new Integer(modParam.getTotal_rec()));
						resultJson.put("ann",annMap);
					} else if (modParam.getCmd().equalsIgnoreCase("get_ann_detail")) {
						Ann ann=new Ann();
						resultJson.put("ann",ann.getAnnDetail(con, modParam.getMsg_id()));
					} else if(modParam.getCmd().equalsIgnoreCase("get_receipt_views")){
						Ann ann = new Ann();
						long msg_id = modParam.getMsg_id();
						StringBuffer xmlBuf = ann.getReceiptViews(con,msg_id);
						AnnBean annBean = ann.getAnnDetail(con, msg_id);
						xmlBuf.append("<msg id=\"").append(annBean.getMsg_id()).append("\" title=\"")
						.append(annBean.getMsg_title()).append("\">").append("</msg>");
						String xml = formatXML(xmlBuf.toString(), MODULENAME);
		                if (modParam.getCmd().equalsIgnoreCase("get_receipt_views")) {
		                    generalAsHtml(xml, out,   modParam.getStylesheet());
		                } else {
		                    static_env.outputXML(out, xml);
		                }
					}else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					} 
					
				}
		} catch (qdbException e) {
			throw new cwException(e.getMessage(), e);
		}
	}	
}
