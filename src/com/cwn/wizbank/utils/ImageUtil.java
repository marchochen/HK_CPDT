package com.cwn.wizbank.utils;

import org.springframework.util.StringUtils;

import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.InstructorInf;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SitePoster;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.entity.UserSpecialExpert;
import com.cwn.wizbank.entity.UserSpecialTopic;
import com.cwn.wizbank.entity.vo.AeItemVo;
import com.cwn.wizbank.entity.vo.SearchResultVo;

public class ImageUtil {

	/**
	 * 组合图片路径
	 * @param obj
	 */
	public static Object combineImagePath(Object obj) {
		
		WizbiniLoader wizbini = aeAction.wizbini;
		
		if(obj instanceof RegUser){
			RegUser user = (RegUser) obj;
			String photo = user.getUsr_photo();
			if(StringUtils.isEmpty(photo)){
				photo = ImagePath.userPhotoFile;
			} else {
				photo = user.getUsr_ent_id() + "/" + photo;
			}
			String userPath = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
			photo = "/" + userPath + "/" + photo;
			user.setUsr_photo(photo);
		} else if(obj instanceof loginProfile){
			loginProfile prof = (loginProfile) obj;
			String photo = prof.getUsr_photo();
			if(StringUtils.isEmpty(photo)){
				photo = ImagePath.userPhotoFile;
			} else {
				photo = prof.getUsr_ent_id() + "/" + photo;
			}
			String userPath = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
			photo = "/" + userPath + "/" + photo;
			prof.setUsr_photo(photo);
		} else if(obj instanceof InstructorInf){
			InstructorInf user = (InstructorInf) obj;
			String photo = user.getIti_img();
			if(StringUtils.isEmpty(photo)){
				photo = ImagePath.instrPhotoFile;
			} else {
				photo = user.getIti_ent_id() + "/" + photo;
			}
			String userPath = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
			if(wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().isRelative()){
				photo = ContextPath.getContextPath() + "/" + userPath + "/" + photo;
			} else {
				photo = userPath + photo;		
			}
			user.setIti_img(photo);
		} else if(obj instanceof AeItem){
			AeItem item = (AeItem) obj;
			if(item != null){
				String icon = item.getItm_icon();
				if(icon != null){
					if(icon.indexOf("/") > -1)   //课程列表取图片地址处理
					{
						icon = icon.substring(icon.lastIndexOf("/")+1);
					}
					if(StringUtils.isEmpty(icon)){
						icon = "/" + ImagePath.courseImage;
						item.setItm_icon(icon);
					} else {
						if(item.getParent() != null && item.getParent().getItm_id() != null && AeItem.CLASSROOM.equals(item.getItm_type())){
							icon = "/" + wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl() + "/" + item.getParent().getItm_id() + "/" + icon;
						}else{
							icon = "/" + wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl() + "/" + item.getItm_id() + "/" + icon;
						}
						item.setItm_icon(icon);
					}
				}else{
					 icon = "/" + wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl() + "/default/kc_c_02.jpg";
					 item.setItm_icon(icon);
				}
			}
		} else if(obj instanceof SnsGroup){
			SnsGroup snsGroup = (SnsGroup) obj;
			String card = snsGroup.getS_grp_card();
			if(StringUtils.isEmpty(card)){
				card = "/"  + ImagePath.groupImage;
				
				snsGroup.setCard_actual_path(card);
			} else {
				card = wizbini.cfgSysSetupadv.getFileUpload().getGroupDir().getUrl() 
				 + "/" + snsGroup.getS_grp_id() + "/" + card;
				if(wizbini.cfgSysSetupadv.getFileUpload().getGroupDir().isRelative()){
					card = "/" + card;
				}
				snsGroup.setCard_actual_path(card);
			}
		} else if(obj instanceof SitePoster){
			SitePoster pst = (SitePoster)obj;
			String url = pst.getSp_media_file();
			String path = wizbini.cfgSysSetupadv.getFileUpload().getPosterDir().getUrl() + "/cw/";
			if(wizbini.cfgSysSetupadv.getFileUpload().getPosterDir().isRelative()){
				path = "/" + path;
			}
			if(!StringUtils.isEmpty(url)){
				pst.setSp_media_file(path + url);
				url = null;
			}
			url = pst.getSp_media_file1();
			if(!StringUtils.isEmpty(url)){
				pst.setSp_media_file1(path + url);
			}
			url = pst.getSp_media_file2();
			if(!StringUtils.isEmpty(url)){
				pst.setSp_media_file2(path + url);
			}
			url = pst.getSp_media_file3();
			if(!StringUtils.isEmpty(url)){
				pst.setSp_media_file3(path + url);
			}
			url = pst.getSp_media_file4();
			if(!StringUtils.isEmpty(url)){
				pst.setSp_media_file4(path + url);
			}			
		}else if(obj instanceof Article){
			Article article=(Article)obj;
			String url=article.getArt_icon_file();
			String path=null;
			if(StringUtils.isEmpty(url)){
				path= "/" +ImagePath.articleImage;	//現在用的课程的默认图片			
			}else{
				path= wizbini.cfgSysSetupadv.getFileUpload().getArticleDir().getUrl()+"/"+article.getArt_id()+"/"+url;	
				if(wizbini.cfgSysSetupadv.getFileUpload().getArticleDir().isRelative()){
					path = "/" + path;
				}
			}
			article.setArt_icon_file(path);
		} else if(obj instanceof KbAttachment){
			KbAttachment kbAttachment=(KbAttachment)obj;
			String url=kbAttachment.getKba_url();
			String path=null;
			if(StringUtils.isEmpty(url)){
				path= ImagePath.knowledgeImage;	//現在用的知识的默认图片			
			}else{
				path= url;
			}
			kbAttachment.setKba_url(path);
		}else if(obj instanceof UserPositionLrnMap){
			UserPositionLrnMap lrnMap=(UserPositionLrnMap)obj;
			String url=lrnMap.getUpm_img();
			String path=null;
			if(StringUtils.isEmpty(url)){
				path= "/" +ImagePath.articleImage;	//現在用的课程的默认图片			
			}else{
				path= wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().getUrl()+"/"+lrnMap.getUpm_id()+"/"+url;	
				if(wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().isRelative()){
					path = "/" + path;
				}
			}
			lrnMap.setAbs_img(path);
		}else if(obj instanceof UserSpecialTopic){
			UserSpecialTopic topic=(UserSpecialTopic)obj;
			String url=topic.getUst_img();
			String path=null;
			if(StringUtils.isEmpty(url)){
				path= "/" +ImagePath.articleImage;	//現在用的课程的默认图片			
			}else{
				path= wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().getUrl()+"/"+topic.getUst_id()+"/"+url;	
				if(wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().isRelative()){
					path = "/" + path;
				}
			}
			topic.setAbs_img(path);
		}else if(obj instanceof UserPosition){
			UserPosition position=(UserPosition)obj;
			String url=position.getUpm_img();
			String path=null;
			if(StringUtils.isEmpty(url)){
				path= "/" +ImagePath.articleImage;	//現在用的课程的默认图片			
			}else{
				path= wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().getUrl()+"/"+position.getUpm_id()+"/"+url;	
				if(wizbini.cfgSysSetupadv.getFileUpload().getPositionDir().isRelative()){
					path = "/" + path;
				}
			}
			position.setAbs_img(path);
		}else if(obj instanceof UserSpecialExpert){
		UserSpecialExpert user = (UserSpecialExpert) obj;
		String photo = user.getUsr_photo();
		if(StringUtils.isEmpty(photo)){
			photo = ImagePath.instrPhotoFile;
		} else {
			photo = user.getUse_ent_id() + "/" + photo;
		}
		String userPath = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
		if(wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().isRelative()){
			photo = ContextPath.getContextPath() + "/" + userPath + "/" + photo;
		} else {
			photo = userPath + photo;		
		}
		user.setAbs_img(photo);
	}else if(obj instanceof AeItemVo){
		AeItemVo item = (AeItemVo) obj;
		String icon = item.getItm_icon();
		if(StringUtils.isEmpty(icon)){
			icon = "/" + ImagePath.courseImage;
			item.setItm_icon(icon);
		} else {
			icon = "/" + wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl() + "/" + item.getItm_id() + "/" + icon;
			item.setItm_icon(icon);
		}
	} else if(obj instanceof LiveItem){
		LiveItem liveItem = (LiveItem) obj;
		String card = liveItem.getLv_image();
		if(card.indexOf("/") > -1){   //课程列表取图片地址处理
			card = card.substring(card.lastIndexOf("/")+1);
		}
		if(StringUtils.isEmpty(card)){
			card = ContextPath.getContextPath() + "/"  + ImagePath.liveImage;
			liveItem.setLv_image_path(card);
		} else {
			card = wizbini.cfgSysSetupadv.getFileUpload().getLiveDir().getUrl() + "/" + card;
			if(wizbini.cfgSysSetupadv.getFileUpload().getLiveDir().isRelative()){
				card = ContextPath.getContextPath() + "/" + card;
			}
			liveItem.setLv_image_path(card);
		}
	}
		return obj;
	}
	
	
	/**
	 * 组合图片路径
	 * @param obj
	 */
	public static Object doImagePath(SearchResultVo obj) {
		
		WizbiniLoader wizbini = aeAction.wizbini;
		//String [] types=new String[]{"All","Course","Exam","Open","Message","Article","Group","Answer","Contacts"};
		String photo = obj.getPhoto();

		if("Contacts".equals(obj.getType())){
			if(StringUtils.isEmpty(photo)){
				photo = "user.png";
			} else {
				photo = obj.getId() + "/" + photo;
			}
			String userPath = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
			photo = "/" + userPath + "/" + photo;
		} else if("Course".equals(obj.getType()) || "Exam".equals(obj.getType()) || "Open".equals(obj.getType())){
			
			//这个判断是判断以前数据选择默认图库的图片时。把文件夹名字也一起保存进数据库里面导致图片路径错误。过滤掉文件夹，只需图片名字
			if(photo!= null && !photo.equals("") && photo.indexOf("/") > -1){
				photo = photo.substring(photo.lastIndexOf("/")+1);
			}
			
			if(StringUtils.isEmpty(photo)){
				photo = "/" + ImagePath.courseImage;
			} else {
				photo = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl() + "/" + obj.getId() + "/" + photo;
				if(wizbini.cfgSysSetupadv.getFileUpload().getItmDir().isRelative()){
					photo = "/" + photo;
				}
			}
		} else if("Group".equals(obj.getType())){
			if(StringUtils.isEmpty(photo)){
				photo = "/"  + ImagePath.groupImage;
				
			} else {
				photo = wizbini.cfgSysSetupadv.getFileUpload().getGroupDir().getUrl() 
				 + "/" + obj.getId() + "/" + photo;
				if(wizbini.cfgSysSetupadv.getFileUpload().getGroupDir().isRelative()){
					photo = "/" + photo;
				}
			}
		} else if("Article".equals(obj.getType())){
			if(StringUtils.isEmpty(photo)){
				photo = ImagePath.articleImage;	//現在用的课程的默认图片			
			}else{
				photo = wizbini.cfgSysSetupadv.getFileUpload().getArticleDir().getUrl() + "/" + obj.getId() + "/"+photo;			
				if( wizbini.cfgSysSetupadv.getFileUpload().getArticleDir().isRelative()){
					photo = "/" + photo;
				}
			}
		} else if("Knowledge".equals(obj.getType())){
			if(StringUtils.isEmpty(photo)){
				photo = ImagePath.knowledgeImage;	//現在用的知识的默认图片			
			}
		}
		obj.setPhoto(photo);
		return obj;
	}
	
}
