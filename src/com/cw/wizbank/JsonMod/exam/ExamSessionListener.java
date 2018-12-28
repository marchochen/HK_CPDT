package com.cw.wizbank.JsonMod.exam;

import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.cwn.wizbank.utils.CommonLog;

public class ExamSessionListener implements HttpSessionBindingListener {
	public Long usrId;
	
	public ExamSessionListener(long usr_ent_id) {
		this.usrId = new Long(usr_ent_id);
	}
	
	public void valueBound(HttpSessionBindingEvent e) {
		CommonLog.info("添加ExamSession监听器");
	}
	
	/*
		移去的时候（即调用 HttpSession 对象的 removeAttribute 方法的时候或 Session Time out 的时候）会执行valueUnbound方法
	*/
	public void valueUnbound(HttpSessionBindingEvent e) {
		List itmLst = (List)ExamController.usrExamHash.get(usrId);
		ExamPassport pass = null;
		long itm_id;
		if (itmLst != null) {
			for (int i = 0; i < itmLst.size(); i++) {
				itm_id = ((Long)itmLst.get(i)).longValue();
				pass = ExamController.getExamPassFromHash(itm_id, usrId.longValue());
				if (pass.isPause) {
					pass.isLogin = false;
				} else {
					ExamController.examEndByLearner(itm_id, usrId.longValue());
				}
			}
			ExamController.usrExamHash.remove(usrId);
		}
	}
}
