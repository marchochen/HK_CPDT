package com.cw.wizbank.report;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


import com.cw.wizbank.util.*;

public class TransDataFactory {
     private String userCol;
     
	static final String RTE_TYPE_LEARNERLRN = "LEARNING_ACTIVITY_LRN";
	static final String RTE_TYPE_LEARNERCOS = "LEARNING_ACTIVITY_COS";
     
     public TransDataFactory(){  	
     }
     public TransData getTransData(String colName) throws cwException{
     	TransData td=null;
     	if(colName.equalsIgnoreCase(this.RTE_TYPE_LEARNERLRN)){
     		td = new TransLrnData();
     	}
     	if(colName.equalsIgnoreCase(this.RTE_TYPE_LEARNERCOS)){
     		td = new TransCosData();
     	}
        return td;
     }
}
