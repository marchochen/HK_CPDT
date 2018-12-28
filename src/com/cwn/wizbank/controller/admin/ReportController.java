package com.cwn.wizbank.controller.admin;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ExamModuleResultStatisticService;

/**
 * 
 * 报表controller
 *
 */
@Controller("report")
@RequestMapping("report")
public class ReportController {
	
	@Autowired
	private ExamModuleResultStatisticService eaxmModuleResultStatisticService;
	
	/**
	 * 导出课程考试模块结果统计信息
	 * @param mod_id 模块id
	 * @throws ParseException 
	 */
	@RequestMapping("export/eaxmModuleResultStatistic")
	@HasPermission(value = {AclFunction.FTN_AMD_TRAINING_REPORT_MGT,AclFunction.FTN_AMD_EXAM_MAIN_VIEW,AclFunction.FTN_AMD_EXAM_MAIN_CONTENT,
			AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE,AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION})
	public ResponseEntity<byte[]> exportEaxmModuleResultStatistic(long mod_id,loginProfile prof) throws ParseException{
		byte[] byteArr = eaxmModuleResultStatisticService.exportResult(mod_id,prof.cur_lan);
		
		String dfileName = "report.xlsx";
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName); 
		return new ResponseEntity<byte[]>(byteArr, headers, HttpStatus.OK);
	}
	
}


