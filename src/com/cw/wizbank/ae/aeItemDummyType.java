package com.cw.wizbank.ae;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.util.LangLabel;

public class aeItemDummyType {
	
	private static final String ITEM_DUMMY_TYPE_CACHE = "ITEM_DUMMY_TYPE_CACHE";
    public static final String ITEM_DUMMY_TYPE_COS = "COS";
    public static final String ITEM_DUMMY_TYPE_EXAM = "EXAM";
    public static final String ITEM_DUMMY_TYPE_REF = "REF";
    public static final String ITEM_DUMMY_TYPE_BLEND = "BLEND";
    public static final String ITEM_DUMMY_TYPE_INTEGRATED = "INTEGRATED";
    public static final String ITEM_DUMMY_TYPE_DELIMITER = "|-|";
    
    /**
     * Generate dummy item type.
     * @param itm_type
     * @param blend_ind
     * @param exam_ind
     * @param ref_ind
     * @return
     */
    public static String getDummyItemType(String itm_type, boolean blend_ind, boolean exam_ind, boolean ref_ind) {
    	if (itm_type == null || itm_type.length() == 0)
    		return null;
    	StringBuffer dummytype = new StringBuffer(itm_type);
    	if(!itm_type.equalsIgnoreCase(ITEM_DUMMY_TYPE_INTEGRATED)) {
    		if (blend_ind) {
    			dummytype.append(ITEM_DUMMY_TYPE_DELIMITER).append(ITEM_DUMMY_TYPE_BLEND);
    		}
    		if (exam_ind) {
    			dummytype.append(ITEM_DUMMY_TYPE_DELIMITER).append(ITEM_DUMMY_TYPE_EXAM);
    		} else if (ref_ind) {
    			dummytype.append(ITEM_DUMMY_TYPE_DELIMITER).append(ITEM_DUMMY_TYPE_REF);
    		} else {
    			dummytype.append(ITEM_DUMMY_TYPE_DELIMITER).append(ITEM_DUMMY_TYPE_COS);
    		}
    	}
    	return dummytype.toString();
    }
    
    /**
     * generate sql segments for dummy itemtypes.
     * @param dummytype_lst[]  dummy item type.eg: CLASSROOM|-|BLEND|-|COS
     * @param tableAlias aeItem table alias used in your sql.
     * @param firstAnd indicate if need to add a "and" at the begining of generated sql segments.
     * @return
     */
    public static String genSqlByItemDummyType(String[] dummytype_lst, String tableAlias, boolean firstAnd) {
    	StringBuffer sql = new StringBuffer();
        if (dummytype_lst != null && dummytype_lst.length > 0) {
        	sql.append(firstAnd ? " and ":" ").append("(");           	
        	for (int i =0; i < dummytype_lst.length ; i++) {
        		sql.append((i > 0 ? " or " : "") + "(").append(aeItemDummyType.genSqlByItemDummyType(dummytype_lst[i], tableAlias, false)).append(")");
        	}
        	sql.append(")");
        }
        return sql.toString();
    }
    /**
     * generate sql segments for dummy itemtype.
     * @param dummytype  dummy item type.eg: CLASSROOM|-|BLEND|-|COS
     * @param tableAlias aeItem table alias used in your sql.
     * @param firstAnd indicate if need to add a "and" at the begining of generated sql segments.
     * @return
     */
    public static String genSqlByItemDummyType(String dummytype, String tableAlias, boolean firstAnd) {
    	String table = (tableAlias == null || tableAlias.length() == 0) ? "" : tableAlias + ".";
    	StringBuffer sql = new StringBuffer();
    	boolean hasBlend=false;
    	if (dummytype == null || dummytype.length() == 0){
    		return "";
    	}
    	if (dummytype.indexOf(ITEM_DUMMY_TYPE_DELIMITER) >= 0) {
    		StringTokenizer token = new StringTokenizer(dummytype, ITEM_DUMMY_TYPE_DELIMITER);
    		int count = 0;
    		while (token.hasMoreTokens()) {
    			String tmp = token.nextToken();
    			if (count == 0) {
    				if (tmp.indexOf(" ") == -1) {
       					sql.append(firstAnd ? " and ":" ").append(table).append("itm_type = '").append(tmp).append("'");
    				}
    			} else {
    				if (ITEM_DUMMY_TYPE_BLEND.equalsIgnoreCase(tmp)) {
    					hasBlend=true;
    					sql.append(" and ").append(table).append("itm_blend_ind = 1 ");
    				} else if (ITEM_DUMMY_TYPE_EXAM.equalsIgnoreCase(tmp)) {
    					sql.append(" and ").append(table).append("itm_exam_ind = 1 ");
    				} else if (ITEM_DUMMY_TYPE_REF.equalsIgnoreCase(tmp)) {
    					sql.append(" and ").append(table).append("itm_ref_ind = 1 ");
    				} else if (ITEM_DUMMY_TYPE_COS.equalsIgnoreCase(tmp)) {
    					sql.append(" and ").append(table).append("itm_exam_ind = 0 ");
    					sql.append(" and ").append(table).append("itm_ref_ind = 0 ");
    				} 
    			}
    			count++;
    		}
    		if(!hasBlend){
    			sql.append(" and ").append(table).append("itm_blend_ind = 0 ");
    		}
    	} else {
			if (ITEM_DUMMY_TYPE_COS.equalsIgnoreCase(dummytype) || ITEM_DUMMY_TYPE_INTEGRATED.equalsIgnoreCase(dummytype)) {
				sql.append(firstAnd ? " and ":" ").append(table).append("itm_exam_ind = 0 ");
				sql.append(" and ").append(table).append("itm_ref_ind = 0 ");
				if(ITEM_DUMMY_TYPE_INTEGRATED.equalsIgnoreCase(dummytype)) {
					sql.append(" and ").append(table).append("itm_integrated_ind = 1 ");
				} else {
					sql.append(" and ").append(table).append("itm_integrated_ind = 0 ");
				}
			} else if (ITEM_DUMMY_TYPE_EXAM.equalsIgnoreCase(dummytype)) {
				sql.append(firstAnd ? " and ":" ").append(table).append("itm_exam_ind = 1 ");
				sql.append(" and ").append(table).append("itm_ref_ind = 0 ");
			} else if (ITEM_DUMMY_TYPE_REF.equalsIgnoreCase(dummytype)) {
				sql.append(firstAnd ? " and ":" ").append(table).append("itm_exam_ind = 0 ");
				sql.append(" and ").append(table).append("itm_ref_ind = 1 ");
			}
    	}
    	return sql.toString();
    }
    
    private static String CLS_BLEND_TYPE = "CLASSROOM"+ITEM_DUMMY_TYPE_DELIMITER+"BLEND";
    
    /**
     * get item type label by given dummy type and language.
     * @param inLang current language.eg: "prof.cur_lang"
     * @param dummytype item dummy type.
     * @return
     */
    public static String getItemLabelByDummyType(String inLang, String dummytype) {
    	if (dummytype == null || dummytype.length() == 0 )
    		return "";
    	HashMap cache = wizbCacheManager.getInstance().getCachedHashmap(ITEM_DUMMY_TYPE_CACHE, true);
    	boolean en_blank = LangLabel.LangCode_en_us.equalsIgnoreCase(inLang) || LangLabel.Encoding_en_us.equalsIgnoreCase(inLang);
    	HashMap cur_lang_map = (HashMap) cache.get(inLang);
    	if (cur_lang_map == null) {
    		cur_lang_map = new HashMap();
    		cache.put(inLang, cur_lang_map);
    	}

    	if("ALL".equalsIgnoreCase(dummytype)){
    		dummytype="COS";
    	}

    	String outValue = (String) cur_lang_map.get(dummytype);
    	if (outValue == null) {
	        if (LangLabel.allLangLabel != null) {
	            HashMap curLangLabel = (HashMap) LangLabel.allLangLabel.get(inLang);
	            if (curLangLabel != null) {
	                outValue = (String) curLangLabel.get(dummytype);
	            }
	            if (outValue == null) {
	            	outValue = "";
	            	String tmpType = dummytype;
	            	//hard code here for blended type
	            	
	            	if (dummytype.startsWith(CLS_BLEND_TYPE)) {
	            		tmpType = dummytype.substring(dummytype.indexOf(CLS_BLEND_TYPE) + CLS_BLEND_TYPE.length() - ITEM_DUMMY_TYPE_DELIMITER.length() - 2, dummytype.length());
	            	}
	        		StringTokenizer token = new StringTokenizer(tmpType, ITEM_DUMMY_TYPE_DELIMITER);
	        		while (token.hasMoreTokens()) {
	        			String tmp = token.nextToken();
	        			if (!ITEM_DUMMY_TYPE_REF.equalsIgnoreCase(tmp)) {
		        			String tmp_lab = (String) curLangLabel.get(tmp); 
		        			if (tmp_lab != null) {
		        	            outValue += tmp_lab + (en_blank ? " " : "");
		        			} else {
		        				StringBuffer result = new StringBuffer();
		        				result.append("!!!").append(inLang).append(".").append(tmp);
		        				outValue += result.toString();
		        			}
	        			}
	        		}
	            }
	        }
	        if (outValue == null) {
	            StringBuffer result = new StringBuffer();
	            result.append("!!!").append(inLang).append(".").append(dummytype);
	            outValue = result.toString();
	        }
	        cur_lang_map.put(dummytype, outValue);
    	}
        return outValue;
    }
}
