package com.cwn.wizbank.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.JsonMod.commonBean.GoldenManOptionBean;
import com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean;
import com.cwn.wizbank.entity.ReportSpec;
import com.cwn.wizbank.entity.vo.LearnerReportParamVo;
import com.cwn.wizbank.persistence.ReportSpecMapper;
import com.cwn.wizbank.utils.Page;

/**
 * service 实现
 */
@Service
public class ReportSpecService extends BaseService<ReportSpec> {

	@Autowired
	ReportSpecMapper ReportSpecMapper;

	public void setReportSpecMapper(ReportSpecMapper ReportSpecMapper) {
		this.ReportSpecMapper = ReportSpecMapper;
	}

	/**
	 * 获取报表列表
	 * 
	 * @param rsp_ent_id
	 *            用户id
	 * @param rsp_id
	 *            报表id
	 * @param rte_type
	 *            报表类型
	 * @return
	 */
	public Page<ReportSpec> getReportSpecList(Page<ReportSpec> page,
			long rsp_ent_id, long rsp_id, String rte_type) {
		if (rsp_id > 0) {
			page.getParams().put("rsp_id", rsp_id);
		}
		page.getParams().put("rsp_ent_id", rsp_ent_id);
		page.getParams().put("rte_type", rte_type);
		ReportSpecMapper.selectReportSpecList(page);
		if (rsp_id == 0) {
			// 遍历已保存报表内容
			for (ReportSpec reportSpec : page.getResults()) {
				StaffReportBean staffReportBean = new StaffReportBean();
				JSONObject jobj = JSONObject.fromObject(reportSpec
						.getRsp_content());
				if (jobj.containsKey("att_create_start_datetime")) {
					Timestamp temptime = Timestamp.valueOf((String) jobj
							.get("att_create_start_datetime"));
					staffReportBean.setAtt_create_start_datetime(temptime);
				}
				if (jobj.containsKey("att_create_end_datetime")) {
					Timestamp temptime = Timestamp.valueOf(jobj
							.getString("att_create_end_datetime"));
					staffReportBean.setAtt_create_end_datetime(temptime);
				}
				if (jobj.containsKey("ats_id")) {
					staffReportBean.setAts_id(jobj.getInt("ats_id"));
				}
				if (jobj.containsKey("s_usg_ent_id_lst")) {
					staffReportBean.setS_usg_ent_id_lst(jobj
							.getString("s_usg_ent_id_lst"));
				}
				if (jobj.containsKey("gmUsrOption")) {
					JSONObject obj = JSONObject.fromObject(jobj
							.get("gmUsrOption"));
					GoldenManOptionBean option = (GoldenManOptionBean) JSONObject
							.toBean(obj, GoldenManOptionBean.class);
					staffReportBean.setGmUsrOption(option);
				}
				if (jobj.containsKey("gmItmOption")) {
					JSONObject obj = JSONObject.fromObject(jobj
							.get("gmItmOption"));
					GoldenManOptionBean option = (GoldenManOptionBean) JSONObject
							.toBean(obj, GoldenManOptionBean.class);
					staffReportBean.setGmItmOption(option);
				}
				if (jobj.containsKey("gmTndOption")) {
					JSONObject obj = JSONObject.fromObject(jobj
							.get("gmTndOption"));
					GoldenManOptionBean option = (GoldenManOptionBean) JSONObject
							.toBean(obj, GoldenManOptionBean.class);
					staffReportBean.setGmTndOption(option);
				}
				reportSpec.setRsp_content(null);
				reportSpec.setStaffReportBean(staffReportBean);
			}
		}
		return page;
	}

	/**
	 * 删除已保存报表
	 * 
	 * @param rsp_id
	 *            报表id
	 * @return
	 */
	public void delReportSpec(long rsp_id) {
		ReportSpecMapper.delete(rsp_id);
	}

	/**
	 * 查询id
	 * 
	 * @param rsp_id
	 *            报表id
	 * @return
	 */
	public ReportSpec selectReportSpecId(long rsp_id) {
		ReportSpec reportSpec = ReportSpecMapper.selectId(rsp_id);
		return reportSpec;
	}


}