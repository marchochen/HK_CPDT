package com.cw.wizbank.report;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.db.view.ViewAttemptsNum;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.util.cwException;

public class TransCosData extends TransData {

	public HashMap transferData(Connection con, Vector v,long rootId) throws cwException,SQLException {
		int flag = 0;
		int totalAttemp;
		HashMap hs = new HashMap();
		Set assistSet = new HashSet();
		Vector keyVec = new Vector();
		//ViewAttemptsNum va = new ViewAttemptsNum();
		int ats_num=0;
		//ArrayList workList=new ArrayList();
		Vector status = aeAttendanceStatus.getAllByRoot(con,rootId);
		StatisticBean reportSummary=new StatisticBean(status);

		if (v == null)
			//throw new cwException("There is no data to be transfered!");
			return null;
		dataSize = v.size();

		// a HashMap using usr_ent_id as key, ViewLearnerReport.data as value
		for (int i = 0; i < dataSize; i++) {
			flag = 0;
			ArrayList workList = null;
			StatisticBean sb = null;

			data = (ViewLearnerReport.Data) v.elementAt(i);
			dataKey = new Long(data.t_id);
			
			if(data.usr_ent_id!=0 && data.att_ats_id!=0 && data.att_create_timestamp!=null){
		     	assistKey = new Long(data.usr_ent_id);           
			    assistSet.add(assistKey);
			}
			if (hs.containsKey(dataKey)&&(data.usr_ent_id!=0 && data.att_ats_id!=0 && data.att_create_timestamp!=null)) {
				workList = (ArrayList) hs.get(dataKey);
				sb = (StatisticBean) workList.get(0);
				if(sb.getHasValue()==0){
						// remove it if it is a rubbish data during first insert
						workList.remove(1);
						sb.setHasValue(1);
								}
				workList.add(data);				
				//sb.setHasValue(1);
			} 
			// ignore it if the new data is a rubbish data
			if(hs.containsKey(dataKey) && (data.usr_ent_id==0 || data.att_ats_id==0 || data.att_create_timestamp==null)){
				continue;
			}
			if(!hs.containsKey(dataKey)) {
				flag = 1;
				keyVec.addElement(dataKey);
				workList = new ArrayList();
				sb = new StatisticBean(status);
				if(data.usr_ent_id==0 || data.att_create_timestamp == null || data.att_ats_id == 0)
					sb.setHasValue(0);
					//workList.add(sb);
					//continue;
				else
					sb.setHasValue(1);
				hs.put(dataKey, workList);
				workList.add(sb);
				workList.add(data);
			}
			if(sb.getHasValue()==1){
			sb.setTotalEnroll(sb.getTotalEnroll() + 1);
			sb.setStatus(data.att_ats_id, sb.getStatus(data.att_ats_id) + 1);
			sb.addTimeSpent(data.cov_total_time);
			sb.addScore(data.cov_score);
			}
		}
		int groupSize=keyVec.size();
		ViewLearnerReport.Data data1 = null;
		StatisticBean sb = null;
		StringBuffer param = null;
		
		//report summary
		for(int i=0;i<groupSize;i++){
			List groupList = (ArrayList)hs.get((Long)keyVec.elementAt(i));
			int  m=groupList.size();
			sb = (StatisticBean)groupList.get(0);
			if(sb.getHasValue()==1){
			param = new StringBuffer();
			long[] traceIds =new long[m-1];
			
				for(int j=1;j<m;j++){
					data1 = (ViewLearnerReport.Data) groupList.get(j);
					traceIds[j-1]=data1.app_tkh_id;
					/*if(j<m-1){
						param.append("(mov_ent_id = '").append(data1.usr_ent_id).append("' and mov_cos_id='").append(data1.cos_res_id).append("' and mov_tkh_id='").append(data1.app_tkh_id).append("') or");
					}
					if(j==m-1){
						param.append("(mov_ent_id = '").append(data1.usr_ent_id).append("' and mov_cos_id='").append(data1.cos_res_id).append("' and mov_tkh_id='").append(data1.app_tkh_id).append("')");	
					}*/
				}
				ViewAttemptsNum vn = new ViewAttemptsNum(con,traceIds);
				totalAttemp = vn.getAttemptsNum();	
				int attemptUsers =vn.getAttemptUsers();
				vn.dropTable();
				sb.setTotalAttemp(totalAttemp);	
				sb.setAttemptUsers(attemptUsers);
				
			// statistics of one group, write into report Summary
			reportSummary.addTotalEnroll(sb.getTotalEnroll());
			reportSummary.addAttemptUsers(sb.getAttemptUsers());
			for(int j=0;j<sb.getStatusSize();j++){
				long atsId =new Long(((aeAttendanceStatus) status.elementAt(j)).getAtsId()).longValue();
				
				reportSummary.addStatus(atsId,sb.getStatus(atsId));
			}
			reportSummary.addAttempts(sb.getTotalAttemp());
			reportSummary.addScore(sb.getTotalScore());
			
			reportSummary.addTimeSpent(sb.getTotalTimeSpent());						
		  }
		}
			reportSummary.setTotalLrn(assistSet.size());
			reportSummary.setTotalCos(groupSize);
			//reportSummary.setAttemptUsers(dataSize);
		    
		hs.put("keyVec", keyVec);
		hs.put("status", status);
		hs.put("reportSummary",reportSummary);
		return hs;
	}

}
