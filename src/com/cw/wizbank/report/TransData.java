package com.cw.wizbank.report;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.*;
import java.sql.*;

import com.cw.wizbank.ae.db.view.*;
import com.cw.wizbank.util.*;
 
public abstract class TransData {
	public static String LEARNING_ACTIVITY_COS = "LEARNING_ACTIVITY_COS";
	public static String LEARNING_ACTIVITY_LRN = "LEARNING_ACTIVITY_LRN";
	
   int dataSize=0;
   ViewLearnerReport.Data data;
   Long dataKey;
   Long assistKey;
   
   
   public abstract HashMap transferData(Connection con,Vector v,long rootId)throws cwException,SQLException;
}
