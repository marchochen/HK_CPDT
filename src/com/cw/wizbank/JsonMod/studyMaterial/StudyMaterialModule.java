package com.cw.wizbank.JsonMod.studyMaterial;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import net.sf.json.JSONArray;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;

public class StudyMaterialModule extends ServletModule {
	
	public static final String GET_SM_CENTER  = "get_sm_center";
	public static final String ADV_SRH_RES_PREP  = "adv_srh_res_prep";
	public static final String ADV_SRH_RES  = "adv_srh_res";
	public static final String GET_TC_CAT_TREE  = "get_tc_cat_tree";
	public static final String GET_CAT_DETAIL_LST  = "get_cat_detail_lst";
	
	StudyMaterialModuleParam modParam = null;
	
	public StudyMaterialModule() {
		super();
		modParam  = new StudyMaterialModuleParam(); 
		param = modParam;
	}
	
	public void process() throws cwException, IOException, SQLException {
    	// for access control
    	StudyMaterialAccess modAccess = new StudyMaterialAccess(prof, con, modParam);
    	modAccess.process();
    	StudyMaterial studyMaterial = new StudyMaterial(wizbini, prof, con, modParam);
        if (modParam.getCmd().equalsIgnoreCase(GET_SM_CENTER)) {
        	setMyTcrInfo(studyMaterial);
        	long catId = 0; 
        	if(modParam.getCat_id() != 0) {
        		setNavigationLink(studyMaterial);
        		catId = modParam.getCat_id();
        		modParam.setCat_id(0);
        	}
        	Vector curCatIdVec = studyMaterial.getSubCatListOfLrn(catId);
        	//set search criteria null
        	setParamsForHome();
        	
        	HashMap relatedSubCatMap = setParentAndSubCat(studyMaterial, curCatIdVec, catId);
        	//resources list
        	HashMap resMap = new HashMap();
    		HashMap resOfFolderMap = null;
    		Vector allRelatedFolderIdVec = studyMaterial.getAllRelatedCatIds(curCatIdVec, relatedSubCatMap, catId);
    		if(allRelatedFolderIdVec != null) {
    			modParam.setCat_id(catId);
    			resOfFolderMap = studyMaterial.getMaterialList(allRelatedFolderIdVec);
    		}
    		Vector resVec = (Vector) resOfFolderMap.get(StudyMaterial.KEY_RES_LIST);
    		resMap.put("res_lst", studyMaterial.updateResList(resVec));
    		resMap.put("total", resOfFolderMap.get(StudyMaterial.KEY_RES_COUNT));
    		resultJson.put("resources", resMap);
        } else if(modParam.getCmd().equalsIgnoreCase(ADV_SRH_RES_PREP)) {
        	resultJson.put("tc_res_cat_lst", studyMaterial.getTcList());
        } else if(modParam.getCmd().equalsIgnoreCase(GET_TC_CAT_TREE)) {
        	Vector nodeVec = studyMaterial.getCatNodeListByTc(wizbini);
            JSONArray array = JSONArray.fromObject(nodeVec);
            out.print(array.toString());
        } else if(modParam.getCmd().equalsIgnoreCase(ADV_SRH_RES)) {
        	boolean isGetByTc = studyMaterial.isGetByTc();
        	studyMaterial.setSrhCriteriaBySess(sess);
        	studyMaterial.setSrhTcrId();
        	if(modParam.getTcr_id() > 0) {
    			resultJson.put("cur_tcr", studyMaterial.getTcrById(modParam.getTcr_id()));
    		}
        	if(isGetByTc) {
        		modParam.setCat_id(0);
        	}
        	if(modParam.getCat_id() != 0) {
        		setNavigationLink(studyMaterial);
        	}
        	Vector curCatIdVec = studyMaterial.getSubCatListOfLrn(modParam.getCat_id());
        	HashMap relatedSubCatMap = setParentAndSubCat(studyMaterial, curCatIdVec, modParam.getCat_id());
        	//resources list
        	HashMap resMap = new HashMap();
    		HashMap resOfFolderMap = null;
    		Vector allRelatedFolderIdVec = studyMaterial.getAllRelatedCatIds(curCatIdVec, relatedSubCatMap, modParam.getCat_id());
    		if(allRelatedFolderIdVec != null) {
    			resOfFolderMap = studyMaterial.getMaterialList(allRelatedFolderIdVec);
    		}
    		Vector resVec = (Vector) resOfFolderMap.get(StudyMaterial.KEY_RES_LIST);
    		resMap.put("res_lst", studyMaterial.updateResList(resVec));
    		resMap.put("total", resOfFolderMap.get(StudyMaterial.KEY_RES_COUNT));
    		resultJson.put("resources", resMap);
        	
        } else if(modParam.getCmd().equalsIgnoreCase(GET_CAT_DETAIL_LST)) {
        	setMyTcrInfo(studyMaterial);
        	if(modParam.getCat_id() == 0) { 
        		resultJson.put("cat_lst", studyMaterial.getCatList());
        	} else {
        		Vector catVec = studyMaterial.getCatListByCatId();
        		resultJson.put("cat_lst", studyMaterial.splitCatalogVec(catVec, StudyMaterial.MATERIAL_CAT_DETAIL_COLUMN_NUM));
        		resultJson.put("top_cat", studyMaterial.getTopCatByCatId(catVec));
        	}
        }
	}
	
	private void setParamsForHome() throws cwException {
		if(modParam.getSrh_key() != null && modParam.getSrh_key().trim().length() == 0) {
			modParam.setSrh_key(null);
		}
		if(modParam.getKey_type_lst() != null && modParam.getKey_type_lst()[0] != null && modParam.getKey_type_lst()[0].trim().length() == 0) {
			modParam.setKey_type_lst(null);
		}
		if(modParam.getRes_subtype_lst() != null && modParam.getRes_subtype_lst()[0] != null && modParam.getRes_subtype_lst()[0].trim().length() == 0) {
			modParam.setRes_subtype_lst(null);
		}
	}

	private HashMap setParentAndSubCat(StudyMaterial studyMaterial, Vector curCatIdVec, long catId) throws SQLException {
		HashMap catMap = new HashMap();
		catMap.put("parent_node", studyMaterial.getParentCat(catId));
		HashMap relatedSubCatMap = studyMaterial.getSubCatListByIds(curCatIdVec);
		catMap.put("sub_cat_lst", studyMaterial.getMaterialCatalogList(curCatIdVec, relatedSubCatMap));
		resultJson.put("cat", catMap);
		return relatedSubCatMap;
	}

	private void setNavigationLink(StudyMaterial studyMaterial) throws SQLException {
		HashMap navMap = new HashMap();
		navMap.put("cur_node", studyMaterial.getCatalogBeanById(modParam.getCat_id()));
		navMap.put("parent_nav", studyMaterial.getCatAncetorList());
		resultJson.put("nav_link", navMap);
	}

	private void setMyTcrInfo(StudyMaterial studyMaterial) throws SQLException {
		Vector myTcrList = studyMaterial.getMyTcrList();
		resultJson.put("tcr_lst", myTcrList);
		long tcrId = modParam.getTcr_id();
		if(tcrId == 0) {
			tcrId = studyMaterial.getMyNestestTcr(myTcrList);
		}
		resultJson.put("cur_tcr", studyMaterial.getTcrById(tcrId));
		modParam.setTcr_id(tcrId);
	}
	
}
