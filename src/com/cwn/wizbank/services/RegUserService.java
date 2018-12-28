package com.cwn.wizbank.services;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.*;
import com.cwn.wizbank.persistence.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.vo.UserVo;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DES3Util;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.wechat.service.WechatService;

/**
 * service 实现
 */
@Service
public class RegUserService extends BaseService<RegUser> {
	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	RegUserMapper regUserMapper;

	@Autowired
	RegUserExtensionMapper regUserExtensionMapper;

	@Autowired
	EntityMapper entityMapper;

	@Autowired
	EntityRelationMapper entityRelationMapper;

	@Autowired
	UserGroupMapper userGroupMapper;

	@Autowired
	AcEntityRoleMapper acEntityRoleMapper;

	@Autowired
	UsrPwdResetHisMapper usrPwdResetHisMapper;

	@Autowired
	InstructorCosService instructorCosService;
	
	@Autowired
	AcRoleService acRoleService;

	@Autowired
	AeAppnApprovalListService aeAppnApprovalListService;

	@Autowired
	AeItemAccessService aeItemAccessService;

	@Autowired
	EntityRelationService entityRelationService;

	@Autowired
	SuperviseTargetEntityMapper superviseTargetEntityMapper;

	@Autowired
	AeAppnTargetEntityMapper aeAppnTargetEntityMapper;
	
	@Autowired
	WechatService wechatService;
	
	@Autowired
	AcSiteMapper acSiteMapper;
	
	@Autowired
	UserPasswordHistoryService userPasswordHistoryService;

	@Autowired
	TcTrainingCenterOfficerService tcTrainingCenterOfficerService;
	
	@Autowired
	CpdUtilService cpdUtilService;

	@Autowired
	UserGradeMapper userGradeMapper;

	@Autowired
	UserPositionMapper userPositionMapper;

	@Autowired
	UserPositionRelationMapper userPositionRelationMapper;

	public void setRegUserMapper(RegUserMapper regUserMapper) {
		this.regUserMapper = regUserMapper;
	}

	/**
	 * 获取个人档案信息
	 * 
	 * @param my_usr_ent_id
	 *            我的用户id
	 * @param he_usr_ent_id
	 *            目标用户id
	 */
	public RegUser getUserDetail(long my_usr_ent_id, long he_usr_ent_id) {
		RegUser regUser = new RegUser();
		regUser.setUsr_ent_id(he_usr_ent_id);
		SnsAttention snsAttention = new SnsAttention();
		snsAttention.setS_att_source_uid(my_usr_ent_id);
		snsAttention.setS_att_target_uid(he_usr_ent_id);
		regUser.setSnsAttention(snsAttention);
		regUser = regUserMapper.selectUserDetail(regUser);
		ImageUtil.combineImagePath(regUser);
		return regUser;
	}

	public RegUser getUserDetail(long my_usr_ent_id) {
		RegUser regUser = regUserMapper.get(my_usr_ent_id);
		if (regUser != null) {
			ImageUtil.combineImagePath(regUser);
		}
		return regUser;
	}

	public RegUser getUserDetailByUserId(String usr_id) {
		RegUser regUser = regUserMapper.getByUsrId(usr_id);
		if (regUser != null) {
			ImageUtil.combineImagePath(regUser);
		}
		return regUser;
	}
	
	public RegUser getUserDetailByUserSteUsrId(String usr_ste_usr_id) {
		RegUser regUser = regUserMapper.getByUsrSteUsrId(usr_ste_usr_id);
		if (regUser != null) {
			ImageUtil.combineImagePath(regUser);
		}
		return regUser;
	}

	/**
	 * 更新个人档案信息
	 * 
	 * @param regUser
	 * @param usrEntId
	 *            用户id
	 * @param birthday
	 *            出生日期
	 * @param join_datetime
	 *            加入公司日期
	 * @param image
	 *            个人头像
	 * @return
	 */
	public void updateUserDetail(WizbiniLoader wizbini, RegUser regUser,
			long usrEntId, String birthday, String join_datetime,
			MultipartFile image) throws Exception {
		// 日期格式转换
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		regUser.setUsr_ent_id(usrEntId);
		if (!birthday.equalsIgnoreCase("")) {
			regUser.setUsr_bday(sdf.parse(birthday));
		}
		if (!join_datetime.equalsIgnoreCase("")) {
			regUser.setUsr_join_datetime(sdf.parse(join_datetime));
		}
		regUser.setUsr_upd_date(getDate());
		if(null != regUser.getUsr_display_bil()){
			regUser.setUsr_display_bil(cwUtils.esc4Json(regUser.getUsr_display_bil()));
		}
		regUserMapper.update(regUser);

		RegUserExtension regUserExtension = regUser.getUserExt();
		if (image != null) {
			// 保存上传的图片
			String saveDirPath = wizbini.getFileUploadUsrDirAbs()
					+ dbUtils.SLASH + usrEntId;
			dbUtils.delFiles(saveDirPath);
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String filename = image.getOriginalFilename();
			String new_filename = System.currentTimeMillis() + "";
			String type = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			new_filename += "." + type;

			File targetFile = new File(saveDirPath, new_filename);
			image.transferTo(targetFile);
			regUserExtension.setUrx_extra_43(new_filename);
		}
		regUserExtension.setUrx_usr_ent_id(usrEntId);
		regUserExtensionMapper.update(regUserExtension);
	}

	/**
	 * 更新个人档案
	 * 
	 * @param wizbini
	 * @param regUser
	 * @param usrEntId
	 * @throws Exception
	 */
	public void updateUserDetail(WizbiniLoader wizbini, RegUser regUser,
			long usrEntId, MultipartFile image) throws Exception {
		regUser.setUsr_ent_id(usrEntId);
		regUser.setUsr_upd_date(getDate());
		regUserMapper.update(regUser);
		RegUserExtension regUserExtension = regUser.getUserExt();
		if (regUserExtension == null)
			regUserExtension = new RegUserExtension();
		if (image != null) {
			// 保存上传的图片
			String saveDirPath = wizbini.getFileUploadUsrDirAbs()
					+ dbUtils.SLASH + usrEntId;
			dbUtils.delFiles(saveDirPath);
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String filename = image.getOriginalFilename();
			String new_filename = System.currentTimeMillis() + "";
			String type = filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
			new_filename += "." + type;

			File targetFile = new File(saveDirPath, new_filename);
			image.transferTo(targetFile);
			regUserExtension.setUrx_extra_43(new_filename);
		}
		regUserExtension.setUrx_usr_ent_id(usrEntId);
		regUserExtensionMapper.update(regUserExtension);
	}

	/**
	 * 修改密码
	 * 
	 * @param usr_ent_id
	 *            用户id
	 * @param old_password
	 *            旧密码
	 * @param new_password
	 *            新密码
	 * @param usr_ste_usr_id
	 *            用户名
	 * @return
	 */
	public String updateUserPwd(long usr_ent_id, String old_password,
			String new_password, String usr_ste_usr_id,String client_type) throws Exception {
		RegUser regUser = new RegUser();
		String oldPassword = dbRegUser.encrypt(old_password, new StringBuffer(
				usr_ste_usr_id).reverse().toString());
		regUser.setUsr_pwd(oldPassword);
		regUser.setUsr_ent_id(usr_ent_id);
		regUser = regUserMapper.selectUserDetail(regUser);

		if (regUser == null) {
			return "usr_old_password_error";
		}
		
		Timestamp date = getDate();
		
		String newPassword = dbRegUser.encrypt(new_password, new StringBuffer(
				usr_ste_usr_id).reverse().toString());
		
		if(oldPassword!=null && oldPassword.equals(newPassword)){
			return "old_and_new_similar";
		}
		
		if(!StringUtils.isEmpty(Application.PASSWORD_POLICY_COMPARE_COUNT) && Integer.parseInt(Application.PASSWORD_POLICY_COMPARE_COUNT)>0){
			if(userPasswordHistoryService.isExistPwd(usr_ent_id,Integer.parseInt(Application.PASSWORD_POLICY_COMPARE_COUNT),newPassword)){
				return "usr_password_has_existed";
			}
		}
		
		UserPasswordHistory passwordHistory = new UserPasswordHistory(usr_ent_id,oldPassword,usr_ent_id,client_type,date);
		userPasswordHistoryService.add(passwordHistory);
		
		regUser.setUsr_pwd(newPassword);
		regUser.setUsr_pwd_upd_timestamp(date);
		regUser.setUsr_pwd_need_change_ind(0l);
		regUserMapper.update(regUser);
		
		//用户修改密码后，不管是学员自己修改，或管理员帮他修改。修改完后要自动解除微信绑定
		wechatService.unbindWizbankByUserSteEntId(regUser.getUsr_ste_usr_id());

		return "update_ok";
	}

	/**
	 * 添加新用户
	 * 
	 * @param regUser
	 * @param birthday
	 *            出生日期
	 * @param join_datetime
	 *            加入日期
	 * @return
	 */
	public void userRegister(RegUser regUser, String birthday,
			String join_datetime) throws Exception {
		long usg_id = userGroupMapper.selectUsgId("TEMP_USG") == null ? 0
				: Long.parseLong(userGroupMapper.selectUsgId("TEMP_USG"));
		if (usg_id == 0) {
			Entity usg_entity = new Entity();
			usg_entity.setEnt_type("USG");
			usg_entity.setEnt_upd_date(getDate());
			usg_entity.setEnt_ste_uid("TEMP_USG");
			usg_entity.setEnt_syn_ind(0L);
			entityMapper.insert(usg_entity);

			usg_id = usg_entity.getEnt_id();
			UserGroup usg = new UserGroup();
			usg.setUsg_ent_id(usg_id);
			usg.setUsg_code("TEMP_USG");
			usg.setUsg_display_bil("TEMP_USG");
			usg.setUsg_ent_id_root(1L);
			userGroupMapper.insert(usg);

			EntityRelation usgErt = new EntityRelation();
			usgErt.setErn_child_ent_id(usg_id);
			usgErt.setErn_ancestor_ent_id(1L);
			usgErt.setErn_order(1l);
			usgErt.setErn_type("USG_PARENT_USG");
			usgErt.setErn_parent_ind(1l);
			usgErt.setErn_remain_on_syn(0l);
			usgErt.setErn_create_timestamp(getDate());
			usgErt.setErn_create_usr_id("s1u3");
			entityRelationMapper.insert(usgErt);
		}

		Entity entity = new Entity();
		entity.setEnt_type("USR");
		entity.setEnt_upd_date(getDate());
		entity.setEnt_syn_ind(0L);
		entityMapper.insert(entity);

		// 建立用户和用户组的关系
		EntityRelation entityRelation = new EntityRelation();
		entityRelation.setErn_child_ent_id(entity.getEnt_id());
		entityRelation.setErn_ancestor_ent_id(1L);
		entityRelation.setErn_order(1l);
		entityRelation.setErn_type("USR_PARENT_USG");
		entityRelation.setErn_parent_ind(0l);
		entityRelation.setErn_syn_timestamp(getDate());
		entityRelation.setErn_remain_on_syn(0l);
		entityRelation.setErn_create_timestamp(getDate());
		entityRelation.setErn_create_usr_id("s1u3");
		entityRelationMapper.insert(entityRelation);

		entityRelation.setErn_ancestor_ent_id(usg_id);
		entityRelation.setErn_order(2l);
		entityRelation.setErn_parent_ind(1l);
		entityRelationMapper.insert(entityRelation);

		// 建立用户和角色的关系
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AcEntityRole acEntityRole = new AcEntityRole();
		acEntityRole.setErl_ent_id(entity.getEnt_id());
		acEntityRole.setErl_creation_timestamp(getDate());
		acEntityRole.setErl_eff_start_datetime(format
				.parse("1753-01-01 00:00:00"));
		acEntityRole.setErl_eff_end_datetime(format
				.parse("9999-12-31 23:59:59"));
		acEntityRole.setRol_ext_id(AcRole.ROLE_NLRN_1);
		acEntityRoleMapper.insert(acEntityRole);

		// 添加用户
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (!birthday.equalsIgnoreCase("")) {
			regUser.setUsr_bday(sdf.parse(birthday));
		}
		if (!join_datetime.equalsIgnoreCase("")) {
			regUser.setUsr_join_datetime(sdf.parse(join_datetime));
		}
		regUser.setUsr_id("s1u" + entity.getEnt_id());
		regUser.setUsr_ent_id(entity.getEnt_id());
		regUser.setUsr_pwd(dbRegUser.encrypt(regUser.getUsr_pwd(),
				new StringBuffer(regUser.getUsr_ste_usr_id()).reverse()
						.toString()));
		regUser.setUsr_signup_date(getDate());
		regUser.setUsr_last_login_date(getDate());
		regUser.setUsr_status("PENDING");
		regUser.setUsr_upd_date(getDate());
		regUser.setUsr_ste_ent_id(1L);
		regUser.setUsr_approve_timestamp(getDate());
		regUser.setUsr_pwd_upd_timestamp(getDate());
		regUser.setUsr_syn_rol_ind(1L);
		regUser.setUsr_source("wizBank");
		regUser.setUsr_choice_tcr_id(0L);
		regUserMapper.register(regUser);

		RegUserExtension regUserExtension = regUser.getUserExt();
		regUserExtension.setUrx_usr_ent_id(entity.getEnt_id());
		regUserExtensionMapper.insert(regUserExtension);
	}

	/**
	 * 检测用户名是否已存在
	 * 
	 * @param usr_ste_usr_id
	 *            用户名
	 * @return
	 */
	public boolean checkUserName(String usr_ste_usr_id) {
		RegUser regUser = new RegUser();
		regUser.setUsr_ste_usr_id(usr_ste_usr_id);
		if (regUserMapper.checkUserExist(regUser) == null) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 检测用户名是否超过2个以上重名，有就不能让其登录
	 * 
	 * @param usr_ste_usr_id
	 *            用户名
	 * @return
	 */
	public boolean checkUserNameCount(String usr_ste_usr_id) {
		RegUser regUser = new RegUser();
		regUser.setUsr_ste_usr_id(usr_ste_usr_id);
		if (regUserMapper.checkUserNameCount(regUser) > 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取用户重置密码信息
	 * 
	 * @param prh_id
	 *            重置密码id
	 * @param max_attempt
	 *            最大尝试数
	 * @param link_last_days
	 *            时间限期
	 * @return
	 */
	public UsrPwdResetHis getUsrPwdResetHis(long prh_id, long max_attempt,
			long link_last_days) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("prh_id", prh_id);
		map.put("max_attempt", max_attempt);
		map.put("link_last_days", link_last_days);
		map.put("curTime", getDate());
		return usrPwdResetHisMapper.selectUsrPwdResetHis(map);
	}

	public void updatePrhStatus(long prh_id) {
		usrPwdResetHisMapper.updatePrhStatusN(prh_id);
	}

	/**
	 * 获取用户列表
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
		public List<UserVo> getList(Params params) throws Exception {
		List<UserVo> userList = regUserMapper.getUserList(params);

		if (userList == null)
			return null;

		for (UserVo user : userList) {
			if (user == null || StringUtils.isEmpty(user.getUsr_pwd())) {
				continue;
			}
			// 解密密码
			String pwd = dbRegUser.decrypt(user.getUsr_pwd(), new StringBuffer(
					user.getUsr_login_id()).reverse().toString());
			user.setUsr_pwd(new String(DES3Util.encryptEde(pwd)));

			CommonLog.debug(" !!!!!!!!!!!   " + user.getUsr_pwd());

			user.setUsr_pwd(new String(DES3Util.decryptEde(user.getUsr_pwd())));

			CommonLog.debug(" !!!!!!!!!!!   " + user.getUsr_pwd());
		}
		// throw new MessageException("你才");
		return userList;
	}

	public long addUsr(UserVo usrVo, long usgEntId, String roleStr,
			long usr_ent_id, long root_ent_id) throws MessageException,
			ParseException {
		if (regUserMapper.isUserExist(usrVo.getUsr_login_id())) {
			throw new MessageException("401-用户已存在");
		} else {
			Entity entity = new Entity();
			entity.setEnt_type("USR");
			entity.setEnt_upd_date(getDate());
			entity.setEnt_syn_ind(0L);
			entityMapper.insert(entity);

			usrVo.setUsr_ent_id(entity.getEnt_id());
			RegUser usr = new RegUser();
			usr.setUsr_ent_id(entity.getEnt_id());
			usr.setUsr_display_bil(usrVo.getUsr_name());
			usr.setUsr_ste_usr_id(usrVo.getUsr_login_id());
			usr.setUsr_ste_ent_id(1l);
			usr.setUsr_email(usrVo.getUsr_email());
			usr.setUsr_gender(usrVo.getUsr_sex());
			usr.setUsr_signup_date(getDate());
			usr.setUsr_last_login_date(getDate());
			usr.setUsr_status("OK");
			usr.setUsr_upd_date(getDate());
			usr.setUsr_syn_rol_ind(1l);
			usr.setUsr_id("s" + root_ent_id + "u" + entity.getEnt_id());
			usr.setUsr_pwd_upd_timestamp(getDate());
			usr.setUsr_pwd_need_change_ind(0l);
			usr.setUsr_tel_1(usrVo.getUsr_phone());

			if (StringUtils.isNotEmpty(usrVo.getUsr_pwd())) {
				try {
					String pwd = dbRegUser.encrypt(usrVo.getUsr_pwd(),
							new StringBuffer(usrVo.getUsr_login_id()).reverse()
									.toString());
					usr.setUsr_pwd(pwd);
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new MessageException("402-密码加密出错");
				}
			}

			regUserMapper.insert(usr);
			long usrEntId =  usr.getUsr_ent_id();
			//RegUserExtension
			RegUserExtension extension = new RegUserExtension();
			extension.setUrx_usr_ent_id(usrEntId);
			regUserExtensionMapper.insert(extension);

			// 角色
			if (roleStr != null && roleStr.trim().length() > 0) {
				String[] rols = roleStr.split(",");
				for (String rol : rols) {
					AcEntityRole aer = new AcEntityRole();
					aer.setErl_ent_id(usrEntId);
					AcRole role = null;
					if ("LRNR".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								AcRole.ROLE_NLRN_1);
					} else if ("TAMD".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								AcRole.ROLE_TADM_1);
					} else if ("SADM".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								AcRole.ROLE_ADM_1);
					}
					if (role == null) {
						throw new MessageException("404-未找角色 " + rol);
					} else {
						aer.setErl_rol_id(role.getRol_id());
					}
					aer.setErl_creation_timestamp(getDate());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					aer.setErl_eff_end_datetime(sdf
							.parse("9999-12-31 23:59:59.000"));
					aer.setErl_eff_start_datetime(getDate());
					aer.setRol_ext_id(role.getRol_ext_id());
					acEntityRoleMapper.insert(aer);
				}
			} else {
				AcEntityRole aer = new AcEntityRole();
				aer.setErl_ent_id(usrEntId);
				AcRole role = null;
				role = acRoleService.getAcRoleMapper().getByExtId(AcRole.ROLE_NLRN_1);
				aer.setErl_creation_timestamp(getDate());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				aer.setErl_eff_end_datetime(sdf
						.parse("9999-12-31 23:59:59.000"));
				aer.setErl_eff_start_datetime(getDate());
				aer.setRol_ext_id(role.getRol_ext_id());
				acEntityRoleMapper.insert(aer);

			}
			//用户岗位名称
			if(usrVo.getUsg_position() != null){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("title", usrVo.getUsg_position());
				List<UserPosition> positions = userPositionMapper.getList(param);
				if(positions.size() > 0){
					UserPosition pos = positions.get(0);
					UserPositionRelation posRelation = new UserPositionRelation();
					posRelation.setUpr_upt_id(pos.getUpt_id());
					posRelation.setUpr_usr_ent_id(usr.getUsr_ent_id());
					userPositionRelationMapper.insert(posRelation);
				}
			}

			// 用户组
			UserGroup usg = this.userGroupMapper.get(usgEntId);
			if (usg == null) {
				throw new MessageException("405-用户组不存在");
			} else {
				//职级名称
				long ugrId = 7;
				if(usrVo.getUsg_grade() != null){
					UserGrade userGrade = userGradeMapper.getByTitle(usrVo.getUsg_grade());
					if(userGrade != null){
						ugrId = userGrade.getUgr_ent_id();
					}
				}

				long[] usr_attribute_ent_ids = new long[] { usgEntId, ugrId };
				String[] usr_attribute_relation_types = new String[] {
						dbEntityRelation.ERN_TYPE_USR_PARENT_USG,
						dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR };

				try {
					forCallOldAPIService.saveEntityRelation(null, usrEntId,
							usr_attribute_ent_ids,
							usr_attribute_relation_types, false, "s1u3");
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new MessageException(e.getMessage());
				}
				return usrEntId;
			}
		}
	}

	public void edit(UserVo usrVo, long usgEntId, String roleStr,
			long usr_ent_id) throws MessageException, ParseException {
		RegUser usr = this.regUserMapper.getByLoginId(usrVo.getUsr_login_id());
		if (usr == null) {
			throw new MessageException("401-用户不存在");
		} else {
			usr.setUsr_display_bil(usrVo.getUsr_name());
			usr.setUsr_email(usrVo.getUsr_email());
			usr.setUsr_gender(usrVo.getUsr_sex());
			usr.setUsr_tel_1(usrVo.getUsr_phone());
			if (StringUtils.isNotEmpty(usrVo.getUsr_pwd())) {
				try {
					String pwd = dbRegUser.encrypt(usrVo.getUsr_pwd(),
							new StringBuffer(usrVo.getUsr_login_id()).reverse()
									.toString());
					usr.setUsr_pwd(pwd);
				} catch (Exception e) {
					CommonLog.error(e.getMessage(),e);
					throw new MessageException("402-密码加密出错");
				}
			}
			if (StringUtils.isNotEmpty(usrVo.getUsr_del_ind())) {
				usr.setUsr_status(usrVo.getUsr_del_ind());
			}
			regUserMapper.update(usr);
			long usrEntId = usr.getUsr_ent_id();
			// 角色
			if (StringUtils.isNotEmpty(roleStr)) {
				String[] rols = roleStr.split(",");
				if (rols.length > 0) {
					acEntityRoleMapper.delByUsrEntId(usrEntId);
				}
				for (String rol : rols) {
					AcEntityRole aer = new AcEntityRole();
					aer.setErl_ent_id(usrEntId);
					AcRole role = null;
					if ("LRNR".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								"NLRN_1");
					} else if ("TAMD".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								"TADM_1");
					} else if ("SADM".equals(rol)) {
						role = acRoleService.getAcRoleMapper().getByExtId(
								"ADM_1");
					}
					if (role == null) {
						throw new MessageException("403-未找角色 " + rol);
					} else {
						aer.setErl_rol_id(role.getRol_id());
					}
					aer.setErl_creation_timestamp(getDate());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					aer.setErl_eff_end_datetime(sdf
							.parse("9999-12-31 23:59:59.000"));
					aer.setErl_eff_start_datetime(getDate());
					aer.setRol_ext_id(role.getRol_ext_id());
					acEntityRoleMapper.insert(aer);
				}
			}
			//用户岗位名称
			if(usrVo.getUsg_position() != null){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("title", usrVo.getUsg_position());
				List<UserPosition> positions = userPositionMapper.getList(param);
				if(positions.size() > 0){
					UserPosition pos = positions.get(0);
					UserPositionRelation posRelation = new UserPositionRelation();
					userPositionRelationMapper.delete(usr.getUsr_ent_id());
					posRelation.setUpr_upt_id(pos.getUpt_id());
					posRelation.setUpr_usr_ent_id(usr.getUsr_ent_id());
					userPositionRelationMapper.insert(posRelation);
				}
			}
			//职级名称
			if(usrVo.getUsg_grade() != null){
				UserGrade userGrade = userGradeMapper.getByTitle(usrVo.getUsg_grade());
				if(userGrade != null){
					long[] usr_attribute_ent_ids = new long[] { userGrade.getUgr_ent_id() };
					String[] usr_attribute_relation_types = new String[] { dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR };
					try {
						forCallOldAPIService.saveEntityRelation(null, usrEntId,
								usr_attribute_ent_ids,
								usr_attribute_relation_types, false, "s1u3");
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
						throw new MessageException("999-" + e.getMessage());
					}
				}
			}
			// 用户组
			if (usgEntId > 0) {
				UserGroup usg = this.userGroupMapper.get(usgEntId);
				if (usg == null) {
					throw new MessageException("405-用户组不存在");
				} else {

					long[] usr_attribute_ent_ids = new long[] { usgEntId };
					String[] usr_attribute_relation_types = new String[] { dbEntityRelation.ERN_TYPE_USR_PARENT_USG };

					try {
						forCallOldAPIService.saveEntityRelation(null, usrEntId,
								usr_attribute_ent_ids,
								usr_attribute_relation_types, false, "s1u3");
					} catch (Exception e) {
						CommonLog.error(e.getMessage(),e);
						throw new MessageException("999-" + e.getMessage());
					}

					return;
				}
			}
		}
	}

	public void del(String usrSteUsrId, long op_usr_ent_id, long root_ent_id,
			String usr_id) throws MessageException {

		RegUser regUser = regUserMapper.getByLoginId(usrSteUsrId);
		if(regUser == null) {
			throw new MessageException("401-用户不存在");
		}
		Long usr_ent_id = regUser.getUsr_ent_id();
		
		long usr_ste_ent_id = root_ent_id;

		// check if the user has any pending application for his/her approval
		if (aeAppnApprovalListService.getAeAppnApprovalListMapper()
				.hasPendingApprovalAppn(usr_ent_id) > 0) {
			throw new MessageException("402-用户还有没审批的记录，不能删除");
		}

		/*
		 * Vector vData = null; StringBuffer sb = null; AccessControlWZB acl =
		 * new AccessControlWZB(); Hashtable tadm = acl.getRole(con,
		 * usr_ste_ent_id, AccessControlWZB.ROL_STE_UID_TADM);
		 */
		String adm_ext_id = acRoleService.getRole(usr_ste_ent_id, "TADM");
		List<AeItemAccess> vSoleTADMItem = aeItemAccessService
				.getSoleAccessItem(usr_ent_id, adm_ext_id);
		StringBuffer sb;
		if (vSoleTADMItem.size() > 0) {
			// get training admin label
			sb = new StringBuffer();
			for (int i = 0; i < vSoleTADMItem.size(); i++) {
				if (i != 0) {
					sb.append(", ");
				}
				sb.append(dbUtils.esc4XML((vSoleTADMItem.get(i).getItem()
						.getItm_title())));
				sb.append(" (")
						.append(dbUtils.esc4XML(vSoleTADMItem.get(i).getItem()
								.getItm_code())).append(")");
			}
			throw new MessageException("403-由於此用户是以下课程的唯一一位，故未能删除此用户。"
					+ sb.toString());
		}

		// check if the user is the sole instr of any course
		// get instr rol_ext_id
		String instr_ext_id = acRoleService.getRole(usr_ste_ent_id, "INSTR");
		List<AeItemAccess> vSoleINSTRItem = aeItemAccessService.getSoleAccessItem(usr_ent_id,
				instr_ext_id);
		if (vSoleINSTRItem.size() > 0) {
			sb = new StringBuffer();
			for (int i = 0; i < vSoleINSTRItem.size(); i++) {
				if (i != 0) {
					sb.append(", ");
				}
				sb.append(dbUtils.esc4XML((vSoleTADMItem.get(i).getItem()
						.getItm_title())));
				sb.append(" (")
						.append(dbUtils.esc4XML(vSoleTADMItem.get(i).getItem()
								.getItm_code())).append(")");
			}
			throw new MessageException("403-由於此用户是以下课程的唯一一位，故未能删除此用户。"
					+ sb.toString());
		} else {
			aeItemAccessService.getAeItemAccessMapper().delByEntId(usr_ent_id);
		}

		int cnt = entityRelationService.getCount(usr_ent_id, "USR_PARENT_USG",
				1);
		if (cnt == 1) {
			this.changeStatus("DELETED", getDate(), usr_ent_id);
			this.updateForDel(usr_id, getDate(), usr_ent_id);
		} else if (cnt <= 0) {
			throw new MessageException("404-用户状态无法修改");
		}

		// del the entity from tcTrainingOfficer,and del him form acEntityRole
		// of the role.
		if (acRoleService.hasRole(usr_ent_id, "TADM_1") > 0) {
			tcTrainingCenterOfficerService.delOfficerRoleFromTc(usr_ent_id,
					"TADM_1");
		}
		entityRelationService.delAsChild(usr_id, "USR_PARENT_USG", usr_ent_id);
		entityRelationService.delAsChild(usr_id, "USR_CURRENT_UGR", usr_ent_id);
		userPositionRelationMapper.delete(usr_ent_id);

		if (cnt == 1) {
			entityRelationService.delAllEntityRelation(usr_id, usr_ent_id);
		}
		// delete supervise target entity (direct supervisor, group supervisor
		// relations)
		superviseTargetEntityMapper.delBySourceEntId(usr_ent_id);

		aeAppnTargetEntityMapper.delByUsrEntId(usr_ent_id);

	}

	private void changeStatus(String status, Timestamp date, long usr_ent_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_status", status);
		map.put("usr_upd_date", date);
		map.put("usr_ent_id", usr_ent_id);
		this.regUserMapper.changeStatus(map);
	}

	private void updateForDel(String usr_id, Timestamp date, long usr_ent_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usr_id", usr_id);
		map.put("ent_delete_timestamp", date);
		map.put("usr_ent_id", usr_ent_id);
		this.regUserMapper.updateForDel(map);
		this.regUserMapper.insertUserRelationHistory(map);
	}

	public void editPwd(UserVo usrVo, long usr_ent_id) throws MessageException {
		if (StringUtils.isNotEmpty(usrVo.getUsr_pwd())) {
			RegUser dbUser = this.regUserMapper.getByLoginId(usrVo
					.getUsr_login_id());
			try {
				String pwd = dbRegUser.encrypt(usrVo.getUsr_pwd(),
						new StringBuffer(usrVo.getUsr_login_id()).reverse()
								.toString());
				dbUser.setUsr_pwd(pwd);
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
				throw new MessageException("密码加密出错");
			}
			this.regUserMapper.update(dbUser);
		} else {
			throw new MessageException("密码不能为空");
		}
	}

	public Page<RegUser> getInstructors(Page<RegUser> page, long tcrId, String searchContent, boolean instrOnly) {
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("searchContent", searchContent);
		page.getParams().put("instrOnly", instrOnly);
		this.regUserMapper.getInstructors(page);
		List<RegUser> list = page.getResults();
		if (list != null && !list.isEmpty()) {
			for (RegUser user : list) {
				ImageUtil.combineImagePath(user);
			}
		}
		return page;
	}

	public RegUserMapper getRegUserMapper() {
		return regUserMapper;
	}



	public RegUser getInstructorDetail(long instrId, long usr_ent_id) throws DataNotFoundException {
		RegUser user = regUserMapper.getInstructor(instrId);
		if(user == null) {
			throw new DataNotFoundException();
		}
		user.getInstr().setCosList(instructorCosService.getInstructorCos(instrId));;
		ImageUtil.combineImagePath(user);
		return user;
	}


	public Page<RegUser> findUserList(Page<RegUser> page, String search_name) {
		page.getParams().put("search_name", search_name);

		if(null!= page.getParams().get("search_name")){
			String searchText = String.valueOf(page.getParams().get("search_name"));
			page.getParams().put("search_name",  "%" + searchText.trim().toLowerCase() + "%");
		}
		
		regUserMapper.findUserList(page);
		List<RegUser> list = page.getResults();
		if (list != null && !list.isEmpty()) {
			for (RegUser user : list) {
				ImageUtil.combineImagePath(user);
			}
		}
		return page;
	}
	
	/**
	 * 获取培训中心【tcrId】下的用户数量
	 * @param tcrId 培训中心
	 * @param type 请求标识，null表示所有用户 ，mobile表示使用app的用户数，weixin表示使用公众账号的用户
	 * @return
	 */
	public long getRegUserCountByTcrId(long tcrId,String type){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tcrId", tcrId);
		params.put("type",type);
		return this.regUserMapper.getRegUserCountByTcrId(params);
	}
	
	public List<RegUser> getDelUptAffectUsr(String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ids", ids);
		return regUserMapper.getDelUptAffectUsr(map);
	}
	
	/**
	 * 检测用户旧密码
	 * 
	 * @param usr_ent_id
	 *            用户id
	 * @param old_password
	 *            旧密码
	 * @param usr_ste_usr_id
	 *           
	 * @throws qdbException
	 * @return
	 */
	public boolean checkOldPassword(long usr_ent_id, String old_password,String usr_ste_usr_id) throws qdbException {
		RegUser regUser = new RegUser();
		// dbRegUser的encrypt方法是为了给密码进行加密
		regUser.setUsr_pwd(dbRegUser.encrypt(old_password, new StringBuffer(usr_ste_usr_id).reverse().toString()));
		regUser.setUsr_ent_id(usr_ent_id);
		regUser = regUserMapper.selectUserDetail(regUser);

		if (regUser == null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 用户是否需要修改密码
	 * @param usr_ent_id
	 * @return
	 */
	public Boolean pwdNeedChangeInd(long usr_ent_id){
		return regUserMapper.selectUsrPwdNeedChangeInd(usr_ent_id);
	}
	
	/**
	 * 用户修改密码的时间
	 * @param usr_ent_id
	 * @return
	 */
	public Timestamp usrPwdUpdTimestamp(long usr_ent_id){
		return regUserMapper.selectUsrPwdUpdTimestamp(usr_ent_id);
	}
	
	public Page<RegUser> getPageUserByGroupId(Page<RegUser> page ,String usg_ids,loginProfile prof,WizbiniLoader wizbini
			,String searchText,boolean showSubordinateInd) {
		page.getParams().put("usg_ids", usg_ids);
		page.getParams().put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		page.getParams().put("my_top_tc_id", prof.my_top_tc_id);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		page.getParams().put("current_role", prof.current_role);
		page.getParams().put("search_name", searchText);
		page.getParams().put("showSubordinateInd", showSubordinateInd);
		//当前角色是否与培训中心关联
		page.getParams().put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
		regUserMapper.getPageUserByGroupId(page);
		return page;
	}

	/**
	 * 导出用户信息
	 * @param prof
	 * @param wizbini
	 * @param uIdList 指定导出的用户id
	 * @param gIdList 指定导出的用户组id
	 * @return
	 */
	public String  expor(loginProfile prof, WizbiniLoader wizbini, String uIdList, String gIdList){
		// 创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("Sheet1");  
        // 在sheet中添加表头第0行
        HSSFRow row = sheet.createRow((int) 0);  
        // 创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        sheet.createRow((int) 0).setHeight((short) 400);
        //边框颜色 黑色
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框 
        //获取列表头名称
        getCell(row, style,prof.cur_lan); 
        
        //读取数据库中数据
        //指定用户
        List<String> usrlist = new ArrayList<String>();
        if(null != uIdList && !uIdList.equals("")){
         String[] uIdArray = uIdList.split(",");
         usrlist = Arrays.asList(uIdArray);  
        }
        //指定用户组
        List<String> usglist = new ArrayList<String>();
        if(null != gIdList && !gIdList.equals("")){
         String[] gIdArray = gIdList.split(",");
         usglist = Arrays.asList(gIdArray);  
        }
         //当用户未指定用户和用户组时，查询当前用户能管理的所有用户组为条件，导出用户
        if(usrlist.size() == 0 && usglist.size() == 0){
        	
        }
        //条件
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("usrlist", usrlist);
        map.put("usglist", usglist);
        map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
        map.put("my_top_tc_id", prof.my_top_tc_id);
        map.put("usr_ent_id", prof.usr_ent_id);
        map.put("current_role", prof.current_role);
        //当前角色是否与培训中心关联
        map.put("is_role_tc_ind", AccessControlWZB.isRoleTcInd(prof.current_role));
        
        //获取getUgrAndUsgByUsrId
        List<Map> getUgrAndUsgByUsrId = regUserMapper.getUgrAndUsgByUsrId();
        Map<String,Object> mapUgrAndUsgByUsrId =new HashMap<String, Object>();
        for(int i = 0;i < getUgrAndUsgByUsrId.size(); i++ ){
        	String Key = getUgrAndUsgByUsrId.get(i).get("ERN_CHILD_ENT_ID").toString()+getUgrAndUsgByUsrId.get(i).get("TYPE").toString();
        	mapUgrAndUsgByUsrId.put(Key, getUgrAndUsgByUsrId.get(i).get("CODE"));
        }
        
        List<RegUser> regUserList =regUserMapper.getUserInfoByGroupOrUserId(map);
        for (int i = 0; i < regUserList.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            RegUser regUser = (RegUser) regUserList.get(i);
            String usgKey = regUser.getUsr_ent_id() + "USG";
            String ugrKey = regUser.getUsr_ent_id() + "UGR";
            regUser.setUsr_usg_code(mapUgrAndUsgByUsrId.get(usgKey)+"");
            regUser.setUgr_code(mapUgrAndUsgByUsrId.get(ugrKey)+"");  //数据问题，有空值
            //创建单元格，并设置值  
            createAndSetCell(row,regUser);
        }   
        
        //生成文件名   并 将文件存到指定位置  
        String fileName = "expor_user_"+new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date())+".xls";
        try  
        {  
        	String basePath = wizbini.getFileUploadTmpDirAbs() + File.separator;
        	File distPath = new File(basePath);
			if (!distPath.exists()) {
				distPath.mkdirs();
			}
            FileOutputStream fout = new FileOutputStream(basePath+fileName);  
            wb.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
        
        return fileName;
        
	}

    public void getCell(HSSFRow row,HSSFCellStyle style,String labellang)
    {
    	 String[] labels = new String[] {
    			 LabelContent.get(labellang, "label_core_user_management_26"), LabelContent.get(labellang, "label_core_user_management_27"), LabelContent.get(labellang, "label_core_user_management_28"),
    			 LabelContent.get(labellang, "label_core_user_management_29"), LabelContent.get(labellang, "label_core_user_management_30"), LabelContent.get(labellang, "label_core_user_management_31"),
    			 LabelContent.get(labellang, "label_core_user_management_32"), LabelContent.get(labellang, "label_core_user_management_33"), LabelContent.get(labellang, "label_core_user_management_34"),
    			 LabelContent.get(labellang, "label_core_user_management_35"), LabelContent.get(labellang, "label_core_user_management_36"), LabelContent.get(labellang, "label_core_user_management_37"),
    			 LabelContent.get(labellang, "label_core_user_management_38"), LabelContent.get(labellang, "label_core_user_management_39"), LabelContent.get(labellang, "label_core_user_management_40"),
    			 LabelContent.get(labellang, "label_core_user_management_41"), LabelContent.get(labellang, "label_core_user_management_42"), LabelContent.get(labellang, "label_core_user_management_43"),
    			 LabelContent.get(labellang, "label_core_user_management_44"), LabelContent.get(labellang, "label_core_user_management_45"), LabelContent.get(labellang, "label_core_user_management_46"),
    			 LabelContent.get(labellang, "label_core_user_management_47"), LabelContent.get(labellang, "label_core_user_management_48")
    	 };
    	 
    	 for(int i = 0; i<labels.length; i++){
    			 HSSFCell cell = row.createCell(i);  
		         cell.setCellValue(labels[i]);  
		         cell.setCellStyle(style);  
    	 }
    }

    public void createAndSetCell(HSSFRow row, RegUser regUser){
    	 row.createCell(0).setCellValue(regUser.getUsr_ste_usr_id());  
         row.createCell(1).setCellValue(""); //密码为空
         row.createCell(2).setCellValue(regUser.getUsr_display_bil());
         row.createCell(3).setCellValue(regUser.getUsr_nickname());
         row.createCell(4).setCellValue(regUser.getUsr_gender());
         if(null != regUser.getUsr_bday()){
          row.createCell(5).setCellValue(DateUtil.getInstance().formateDate(regUser.getUsr_bday()).replaceAll("-", "/"));
         }
         row.createCell(6).setCellValue(regUser.getUsr_email());
         row.createCell(7).setCellValue(regUser.getUsr_tel_1());
         row.createCell(8).setCellValue(regUser.getUsr_fax_1());
         row.createCell(9).setCellValue(regUser.getUsr_extra_2());
         row.createCell(10).setCellValue(regUser.getUsr_weixin_id());
         row.createCell(11).setCellValue(regUser.getUsr_job_title());
         if(null != regUser.getUsr_join_datetime()){
          row.createCell(12).setCellValue(DateUtil.getInstance().formateDate(regUser.getUsr_join_datetime()).replaceAll("-", "/"));
         }
         if(null != regUser.getUrx_extra_datetime_11()){
          row.createCell(13).setCellValue(DateUtil.getInstance().formateDate(regUser.getUrx_extra_datetime_11()).replaceAll("-", "/"));
         }
         row.createCell(14).setCellValue(regUser.getUrx_extra_44());
         row.createCell(15).setCellValue(regUser.getUrx_extra_45());
         row.createCell(16).setCellValue(regUser.getUsr_usg_code());  
         row.createCell(17).setCellValue(regUser.getUgr_code()); 
         row.createCell(18).setCellValue(regUser.getUpt_code());
         if(regUser.getAcRole().size() > 0){
         	StringBuffer userRole = new StringBuffer();
         	for(int u=0; u<regUser.getAcRole().size(); u++){
         		if(u==0){
         		  userRole.append(regUser.getAcRole().get(u).getRol_ste_uid()); 	
         		}else{
         		  userRole.append(",").append(regUser.getAcRole().get(u).getRol_ste_uid()); 	
         		}
         	}
           row.createCell(19).setCellValue(userRole.toString());
         }
         if(regUser.getRegUser().size() > 0){
         	StringBuffer userDirectSupervisors = new StringBuffer();
         	for(int u=0; u<regUser.getRegUser().size(); u++){
         		if(u==0){
         		  userDirectSupervisors.append(regUser.getRegUser().get(u).getUsr_ste_usr_id()); 	
         		}else{
         		  userDirectSupervisors.append(",").append(regUser.getRegUser().get(u).getUsr_ste_usr_id()); 	
         		}
         	}
           row.createCell(20).setCellValue(userDirectSupervisors.toString());
         }
         if(null != regUser.getUsr_app_approval_usg_code()){
         	row.createCell(21).setCellValue(regUser.getUsr_app_approval_usg_code());
         }
         if(regUser.getUserGroup().size() > 0){
         	StringBuffer userSupervisedGroups = new StringBuffer();
         	for(int u=0; u<regUser.getUserGroup().size(); u++){
         		if(u==0){
         		  if(null != regUser.getUserGroup().get(u)){
         			 userSupervisedGroups.append(regUser.getUserGroup().get(u).getUsg_code());  
         		  }	
         		}else{
         		  if(null != regUser.getUserGroup().get(u)){
         		     userSupervisedGroups.append(",").append(regUser.getUserGroup().get(u).getUsg_code()); 	
         		  }
         		}
         	}
           row.createCell(22).setCellValue(userSupervisedGroups.toString());
         }
    }
    
    public List<RegUser> findUserListForCpdExcel(Map<String,Object> map){
        List<RegUser> regUsers =  regUserMapper.findUserListForCpdExcel(map);
        return regUsers;
        
    }
    
    
    /**
     * 学员的直属上司
     * @param usrIds
     * @return
     */
    public List<SuperviseTargetEntity> getDirectSupervise(List<Long> usrIds){
        Map<String,Object> map = new HashMap<String, Object>();
        if(usrIds!=null){
            long[] usrIdArray = new long[usrIds.size()];
            for (int i = 0; i < usrIds.size(); i++) {
                usrIdArray[i] = usrIds.get(i);
            }
            map.put("spt_target_ent_id", usrIdArray);
        }
        
        List<SuperviseTargetEntity> superviseTargetEntities =  superviseTargetEntityMapper.getDirectSupervise(map);
        return superviseTargetEntities;
        
    }
    
    
    /**
     * 学员的部门上司
     * @param usrIds
     * @return
     */
    public List<SuperviseTargetEntity> getGroupSupervise(List<Long> usrIds){
        Map<String,Object> map = new HashMap<String, Object>();
        if(usrIds!=null){
            long[] usrIdArray = new long[usrIds.size()];
            for (int i = 0; i < usrIds.size(); i++) {
                usrIdArray[i] = usrIds.get(i);
            }
            map.put("ern_child_ent_id", usrIdArray);
        }
        
        List<SuperviseTargetEntity> superviseTargetEntities =  superviseTargetEntityMapper.getGroupSupervise(map);
        return superviseTargetEntities;
        
    }
    
    public List<RegUser> findsuperviseForCpdExcel(Map<String,Object> map){
        List<RegUser> regUsers =  regUserMapper.findsuperviseForCpdExcel(map);
        return regUsers;
        
    }
    
    
    
    
    
    
    public List<RegUser> getUserListForCpdTa(int exportUser,Long[] usrIds,Long[] groupIds,
        	long usrEntId,String currentRole ,Long myTopTcId,Long ctId ,int period ){
        	Map param = new HashMap<String , Object>();
        	param.put("exportUser", exportUser);
        	param.put("usrIds", usrIds);
        	param.put("groupIds", groupIds);
        	param.put("isRoleTcInd", AccessControlWZB.isRoleTcInd(currentRole));
        	param.put("userEntId", usrEntId);
        	param.put("userRole", currentRole);
        	param.put("myTopTcId", myTopTcId);
        	param.put("ctId", ctId);
        	param.put("period", period);
        	
        	Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int curYear = cal.get(Calendar.YEAR);
            
        	//获取大牌周期结束时间
        	CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(period,ctId);
        	if(null != cpdPeriodVO && null != cpdPeriodVO.getEndTime()){
        		param.put("cpdEndTime", cpdPeriodVO.getEndTime());
        	}
        	
        	CpdPeriodVO cpdPeriodVOs = cpdUtilService.getCurrentPeriod(ctId);
        	
        	List<RegUser> list = new ArrayList<RegUser>(); 
        	if(curYear == period || (null != cpdPeriodVOs && cpdPeriodVOs.getPeriod() == period) ){
        		list = regUserMapper.findUserListForCpdTa(param);
        	}else{
        		list = regUserMapper.findUserListForCpdHistoryTa(param);
        	}
        	
        	return list;
        }
    
}