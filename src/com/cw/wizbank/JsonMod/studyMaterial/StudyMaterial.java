package com.cw.wizbank.JsonMod.studyMaterial;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.bean.TrainingCenterBean;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.know.Know;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialCatalogBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialSrhCriteriaBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.ScormStructureBean;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbScormResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class StudyMaterial {
	private loginProfile prof = null;
	private Connection con = null;
	private WizbiniLoader wizbini = null;
	private StudyMaterialModuleParam modParam = null;

	public static final int MATERIAL_CAT_DETAIL_COLUMN_NUM = 3;

	public StudyMaterial(WizbiniLoader wizbini, loginProfile prof, Connection con, StudyMaterialModuleParam modParam) {
		this.wizbini = wizbini;
		this.prof = prof;
		this.modParam = modParam;
		this.con = con;
	}

	protected Vector getMyTcrList() throws SQLException {
		Course cos = new Course();
		Vector tcrVec = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
		return tcrVec;
	}

	protected long getMyNestestTcr(Vector myTcrList) {
		long myNestestTcrId = 0;
		if(myTcrList != null && myTcrList.size() > 0) {
			TCBean tcBean = (TCBean)myTcrList.get(0);
			myNestestTcrId = tcBean.getTcr_id();
        }
		return myNestestTcrId;
	}

	public TrainingCenterBean getTcrById(long tcrId) throws SQLException  {
		Course cos = new Course();
		return cos.getTCById(con, tcrId);
	}

	public Vector getSubCatListOfLrn(long catId) throws SQLException {
		Vector curCatIdVec = null;
		if (catId == 0) {
			curCatIdVec = dbObjective.getRootFolderListOfLrn(con, this.wizbini, prof.root_ent_id, modParam.getTcr_id(), null, null);
		} else {
			curCatIdVec = dbObjective.getChildOjbByObjId(con, catId);
		}
		return curCatIdVec;
	}

	public static final String KEY_CAT_ID_LST = dbObjective.KEY_CAT_ID_LST;
	public static final String KEY_PARENT_CAT = dbObjective.KEY_PARENT_CAT;
	public HashMap getSubCatListByIds(Vector curFolderIdVec) throws SQLException {
		return dbObjective.getSubCatIdListByIds(con, curFolderIdVec);
	}

	public Vector getMaterialCatalogList(Vector curFolderIdVec, HashMap relatedMap) throws SQLException {
		MaterialSrhCriteriaBean srhCriteriaBean = getSrhCriteriaByParams();

		Vector curCatVec = new Vector();
		if(curFolderIdVec != null) {
			Vector relatedFolderIdVec = cwUtils.unionVectors(curFolderIdVec, (Vector)relatedMap.get(dbObjective.KEY_CAT_ID_LST), false);
			Hashtable folderResCountTab = null;
			if(relatedFolderIdVec != null && relatedFolderIdVec.size() > 0) {
				folderResCountTab = dbObjective.getResCountOfCatalogs(con, relatedFolderIdVec, srhCriteriaBean);
			}

			//compute the count of folder
			HashMap catResMap = computeFolderResInfo(folderResCountTab, (HashMap)relatedMap.get(dbObjective.KEY_PARENT_CAT));
			Vector relatedCatVec = dbObjective.getObjListByIds(con, curFolderIdVec);

			curCatVec = updateCatListInfo(relatedCatVec, catResMap, modParam.getTcr_id());
		}

		//filter catalog that has no resource for search
		if(srhCriteriaBean != null) {
			curCatVec = filterCatNoRes(curCatVec);
		}

		return curCatVec;
	}

	private Vector filterCatNoRes(Vector curCatVec) {
		Vector resultCatVec = new Vector();
		if(curCatVec != null) {
			for(Iterator catIter = curCatVec.iterator(); catIter.hasNext();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) catIter.next();
				if(catBean.getCount() > 0) {
					resultCatVec.add(catBean);
				}
			}
		}
		return resultCatVec;
	}

	private HashMap computeFolderResInfo(Hashtable folderResCountTab, HashMap parentFolderMap) {
		HashMap parentResCountMap = new HashMap();
		if(folderResCountTab != null) {
			Enumeration folderKeys = folderResCountTab.keys();
			while(folderKeys.hasMoreElements()) {
				Long folderId = (Long)folderKeys.nextElement();
				Integer curFolderResCount = (Integer)folderResCountTab.get(folderId);
				Long parentFolderId = null;
				if(parentFolderMap.get(folderId) != null) {
					parentFolderId = (Long) parentFolderMap.get(folderId);
				} else {
					parentFolderId = folderId;
				}

				Integer parentFolderResCount = (Integer) parentResCountMap.get(parentFolderId);
				if(parentFolderResCount == null) {
					parentFolderResCount = curFolderResCount;
				} else {
					parentFolderResCount = new Integer(parentFolderResCount.intValue() + curFolderResCount.intValue());
				}

				parentResCountMap.put(parentFolderId, parentFolderResCount);
			}
		}
		return parentResCountMap;
	}

	private Vector updateCatListInfo(Vector catVec, HashMap parentFolderMap, long tcrId) {
		Vector updatedCatVec = new Vector();
		if(catVec != null && catVec.size() >0) {
			for(int catIndex = 0; catIndex < catVec.size(); catIndex++) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) catVec.elementAt(catIndex);
				Integer resCount = (Integer) parentFolderMap.get(new Long(catBean.getId()));
				if(resCount != null) {
					catBean.setCount(resCount.intValue());
				}
				if(catBean.getTcr_id() <= 0) {
					catBean.setTcr_id(tcrId);
				}
				updatedCatVec.add(catBean);
			}
		}
		return updatedCatVec;
	}

	public MaterialCatalogBean getParentCat(long catId) throws SQLException {
		MaterialCatalogBean parentCatBean = null;
		if(catId == 0) {
    		parentCatBean = this.getCatalogBeanById(0);
		} else {
			parentCatBean = dbObjective.getParentObj(con, catId);
		}
		return parentCatBean;
	}

	public MaterialCatalogBean getCatalogBeanById(long objId) throws SQLException {
		MaterialCatalogBean catBean = new MaterialCatalogBean();
		catBean.setId(objId);
		catBean.setTcr_id(modParam.getTcr_id());
		catBean.setDesc(dbObjective.getDesc(con, objId));
		return catBean;
	}

	public static final String KEY_RES_LIST = dbObjective.KEY_RES_LIST;
	public static final String KEY_RES_COUNT = dbObjective.KEY_RES_COUNT;
	public HashMap getMaterialList(Vector folderIdList) throws SQLException {
		HashMap resMap = null;
		resMap = dbObjective.getResListOfFolders(con, folderIdList, getSrhCriteriaByParams(), modParam.getStart(), modParam.getLimit());
		return resMap;
	}

	public Vector getCatAncetorList() throws SQLException {
		String objAncester = dbObjective.getObjAncester(con, modParam.getCat_id());
		long[] objAncesterIdList = cwUtils.splitToLong(objAncester, dbObjective.INTERVAL_OBJ_ANCESTER);
		Vector parentCatVec = null;
		if(objAncesterIdList != null) {
			parentCatVec = dbObjective.getObjListByIds(con, cwUtils.long2vector(objAncesterIdList), "obj_id");
		}
		return parentCatVec;
	}

	protected Vector getDirectSubCatOfLrn() throws SQLException {
		return dbObjective.getChildOjbByObjId(con, modParam.getCat_id());
	}

	public Vector getAllRelatedCatIds(Vector curCatIdVec, HashMap relatedSubCatMap, long catId) {
		Vector childFolderIdVec = relatedSubCatMap == null ? null : (Vector)relatedSubCatMap.get(dbObjective.KEY_CAT_ID_LST);
		Vector allRelatedFolderIdVec = cwUtils.unionVectors(curCatIdVec, childFolderIdVec, false);
		if(allRelatedFolderIdVec == null) {
			allRelatedFolderIdVec = new Vector();
		}
		if(catId != 0) {
			allRelatedFolderIdVec.add(new Long(catId));
		}
		return allRelatedFolderIdVec;
	}

	public void setSrhCriteriaBySess(HttpSession sess) throws cwException {
		String searchId = modParam.getSearch_id();
        if (searchId == null || "".equals(searchId)) {
            throw new cwException("search_id is null");
        }
        HashMap searchMap = (HashMap) sess.getAttribute(searchId);
        String searchKey = null;
        String[] keyTypeLst = null;
        String[] resSubtypeLst = null;
        long catId = 0;
        String[] resDifficultyLst = null;
        long tcrId = 0;


        String [] searchValArr = null;
        searchValArr = (String[]) searchMap.get("srh_key");
        if(searchValArr != null && searchValArr.length > 0) {
        	searchKey = searchValArr[0];
        }
        keyTypeLst = (String[]) searchMap.get("key_type_lst");
        resSubtypeLst = (String[]) searchMap.get("res_subtype_lst");
        searchValArr = (String[]) searchMap.get("cat_id");
        if(searchValArr != null && searchValArr.length > 0 && searchValArr[0].trim().length() > 0) {
        	catId = Long.parseLong(searchValArr[0].trim());
        }
        searchValArr = (String[]) searchMap.get("tcr_id");
        if(searchValArr != null && searchValArr.length > 0 && searchValArr[0].trim().length() > 0) {
        	tcrId = Long.parseLong(searchValArr[0].trim());
        }
        resDifficultyLst = (String[]) searchMap.get("difficulty_lst");

        modParam.setSrh_key(searchKey);
        modParam.setKey_type_lst(keyTypeLst);
        modParam.setRes_subtype_lst(resSubtypeLst);
        //高级搜索选择的分类ID
        if(modParam.getCat_id() <= 0) {
        	modParam.setCat_id(catId);
        }
        //高级搜索时和搜索结果页面中点击所有分类
        if(modParam.getCat_id() <= 0 && modParam.getTcr_id() <= 0) {
        	modParam.setTcr_id(tcrId);
        }

        modParam.setRes_difficulty_lst(cwUtils.stringArray2LongArray(resDifficultyLst));
	}

	private MaterialSrhCriteriaBean getSrhCriteriaByParams() {
		boolean isValidSrh = false;
		MaterialSrhCriteriaBean srhCriteriaBean = new MaterialSrhCriteriaBean();
		if(modParam.getSrh_key() != null && modParam.getSrh_key().length() > 0) {
			srhCriteriaBean.setSrh_key(modParam.getSrh_key());
			isValidSrh = true;
		}
		if(modParam.getKey_type_lst() != null && modParam.getKey_type_lst().length > 0) {
			srhCriteriaBean.setKey_type_lst(modParam.getKey_type_lst());
			isValidSrh = true;
		}
		if(modParam.getRes_subtype_lst() != null && modParam.getRes_subtype_lst().length > 0) {
			srhCriteriaBean.setRes_subtype_lst(modParam.getRes_subtype_lst());
			isValidSrh = true;
		}
		if(modParam.getCat_id() > 0) {
			srhCriteriaBean.setCat_id(modParam.getCat_id());
			isValidSrh = true;
		}
		if(modParam.getRes_difficulty_lst() != null && modParam.getRes_difficulty_lst().length > 0) {
			srhCriteriaBean.setDifficulty_lst(modParam.getRes_difficulty_lst());
			isValidSrh = true;
		}
		if(!isValidSrh) {
			srhCriteriaBean = null;
		}

		return srhCriteriaBean;
	}

	/**
	 * 生成培训中心目录树
	 * @return
	 * @throws SQLException
	 */
	protected Vector getTcList() throws SQLException {
		ViewTrainingCenter viewTCR = new ViewTrainingCenter();
        List tcrList = viewTCR.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
        Vector treeBeanVec = Course.getTreeBeanByTcrLst(tcrList);
        return treeBeanVec;
	}

	protected Vector getCatNodeListByTc(WizbiniLoader wizbini) throws SQLException {
		Vector curCatIdVec = new Vector();
		if ("TC".equalsIgnoreCase(modParam.getNode_type())) {
			modParam.setTcr_id(modParam.getNode_id());
			curCatIdVec = dbObjective.getRootFolderListOfLrn(con, wizbini, prof.root_ent_id, modParam.getTcr_id(), null, null);
		} else {
			modParam.setCat_id(modParam.getNode_id());
			curCatIdVec = dbObjective.getChildOjbByObjId(con, modParam.getCat_id());
		}
		Vector catListVec = dbObjective.getObjListByIds(con, curCatIdVec);
		Vector nodeVec = convertCatToNodeList(catListVec);
		return nodeVec;
	}

	private Vector convertCatToNodeList(Vector catListVec) throws SQLException {
		Vector nodeVec = new Vector();
		if(catListVec != null) {
			for(Iterator catIter = catListVec.iterator(); catIter.hasNext();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) catIter.next();
				JsonTreeBean jNode = new JsonTreeBean();
				jNode.setId(catBean.getId());
				jNode.setText(catBean.getDesc());
				jNode.setType(cwTree.NODE_TYPE_CATALOG);
				nodeVec.add(jNode);
			}
		}
		return nodeVec;
	}

	protected long getTcrIdByCatalog(long catId) throws cwException{
		long tcrId = 0;
		dbObjective obj = new dbObjective();
		obj.obj_id = catId;
		try {
			obj.get(con);
		} catch(qdbException e) {
			throw new cwException(e.getMessage());
		}
		if (obj.obj_tcr_id <= 0) {
			Vector ancestorVec = dbObjective.convert2vec(obj.obj_ancester);
			tcrId = ((Long) ancestorVec.get(0)).longValue();
		} else {
			tcrId = obj.obj_tcr_id;
		}
		return tcrId;
	}

	public void setSrhTcrId() throws cwException {
		if(modParam.getTcr_id() == 0 && modParam.getCat_id() != 0) {
    		long tcrId = this.getTcrIdByCatalog(modParam.getCat_id());
    		modParam.setTcr_id(tcrId);
    	}
	}

	protected boolean isGetByTc() {
		boolean isGetByTc = false;
		if(modParam.getTcr_id() > 0 && modParam.getCat_id() == 0) {
			isGetByTc = true;
		}
		return isGetByTc;
	}

	/**
	 * 详细分类：获取第一层和第二层资源目录
	 * @return
	 * @throws SQLException
	 */
	public Vector getCatList() throws SQLException {
		Vector topCatIdVec = dbObjective.getRootFolderListOfLrn(con, this.wizbini, prof.root_ent_id, modParam.getTcr_id(), null, null);
		HashMap relatedCatMap = null;
		if (topCatIdVec != null && topCatIdVec.size() > 0) {
			relatedCatMap = dbObjective.getSubCatIdListByIds(con, topCatIdVec);
		}
		Vector topCatVec = null;
		if (relatedCatMap != null) {
			Vector relatedFolderIdVec = cwUtils.unionVectors(topCatIdVec, (Vector) relatedCatMap.get(dbObjective.KEY_CAT_ID_LST), false);
			Hashtable catResCountTab = null;
			if (relatedFolderIdVec != null && relatedFolderIdVec.size() > 0) {
				catResCountTab = dbObjective.getResCountOfCatalogs(con, relatedFolderIdVec, null);
			}
			HashMap catResMap = computeFolderResInfo(catResCountTab, (HashMap) relatedCatMap.get(dbObjective.KEY_PARENT_CAT));
			Vector relatedCatVec = dbObjective.getObjListByIds(con, topCatIdVec);
			topCatVec = updateCatListInfo(relatedCatVec, catResMap, modParam.getTcr_id());
			// sub-catalog
			Vector subCatIdVec = dbObjective.getChildOjbByObjId(con, cwUtils.vec2longArray(topCatIdVec));
			HashMap subRelatedCatMap = dbObjective.getSubCatIdListByIds(con, subCatIdVec);
			Vector subRelatedFolderIdVec = cwUtils.unionVectors(subCatIdVec, (Vector) subRelatedCatMap.get(dbObjective.KEY_CAT_ID_LST), false);
			Hashtable subCatResCountTab = null;
			if (subRelatedFolderIdVec != null && subRelatedFolderIdVec.size() > 0) {
				subCatResCountTab = dbObjective.getResCountOfCatalogs(con, subRelatedFolderIdVec, null);
			}
			HashMap subCatResMap = computeFolderResInfo(subCatResCountTab, (HashMap) subRelatedCatMap.get(dbObjective.KEY_PARENT_CAT));
			Vector subRelatedCatVec = dbObjective.getObjListByIds(con, subCatIdVec);
			Vector subCatVec = updateCatListInfo(subRelatedCatVec, subCatResMap, modParam.getTcr_id());

			// combination
			HashMap parentSubCatMap = getParentIdAndSubCatLstMap((HashMap) relatedCatMap.get(dbObjective.KEY_PARENT_CAT), subCatVec);
			topCatVec = getRootAndSubCatStruct(topCatVec, parentSubCatMap);
		}

		return splitCatalogVec(topCatVec, MATERIAL_CAT_DETAIL_COLUMN_NUM);
	}

	public Vector splitCatalogVec(Vector vec, int splitNum) {
		return Know.splitCatalogVec(vec, splitNum);
	}

	private HashMap getParentIdAndSubCatLstMap(HashMap catRelationMap, Vector subCatVec) {
		HashMap subCatLstMap = new HashMap();
		if(subCatVec != null) {
			for(Iterator catIter = subCatVec.iterator(); catIter.hasNext();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) catIter.next();
				long catId = catBean.getId();
				Long parentCatId = (Long)catRelationMap.get(new Long(catId));
				Vector targetSubCatVec = (Vector) subCatLstMap.get(parentCatId);
				if(targetSubCatVec == null) {
					targetSubCatVec = new Vector();
				}
				targetSubCatVec.add(catBean);
				subCatLstMap.put(parentCatId, targetSubCatVec);
			}
		}
		return subCatLstMap;
	}

	private Vector getRootAndSubCatStruct(Vector rootCatVec, HashMap subCatLstMap) {
		Vector resultVec = new Vector();
		if(rootCatVec != null) {
			for(int catIndex = 0; catIndex < rootCatVec.size();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) rootCatVec.elementAt(catIndex++);
				Vector subCatVec = (Vector) subCatLstMap.get(new Long(catBean.getId()));
				catBean.setChildren(subCatVec);
				resultVec.add(catBean);
			}
		}
		return rootCatVec;
	}

	public MaterialCatalogBean getTopCatByCatId(Vector subCatVec) throws cwException, SQLException {
		dbObjective obj = new dbObjective();
		obj.obj_id = modParam.getCat_id();
		try {
			obj.get(con);
		} catch(qdbException e) {
			throw new cwException(e.getMessage());
		}
		MaterialCatalogBean catBean = getCatBeanByObj(obj);
		//set the resource count of catalog
		catBean.setCount(getResCountOfTopCat(subCatVec));
		return catBean;
	}

	private int getResCountOfTopCat(Vector subCatVec) throws SQLException{
		int resCount = 0;
		Vector objIdVec = new Vector();
		Long catIdObj = new Long(modParam.getCat_id());
		objIdVec.add(catIdObj);
		Hashtable catResCountTab = dbObjective.getResCountOfCatalogs(con, objIdVec, null);
		resCount = catResCountTab.get(catIdObj) == null? 0 : ((Integer)catResCountTab.get(catIdObj)).intValue();

		//resource  count in sub-catalog
		if(subCatVec != null) {
			for(Iterator subIter = subCatVec.iterator(); subIter.hasNext();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) subIter.next();
				resCount += catBean.getCount();
			}
		}
		return resCount;
	}

	private MaterialCatalogBean getCatBeanByObj(dbObjective obj) {
		MaterialCatalogBean catBean = new MaterialCatalogBean();
		catBean.setId(obj.obj_id);
		catBean.setText(obj.obj_desc);
		catBean.setDesc(obj.obj_desc);
		catBean.setTcr_id(obj.obj_tcr_id);
		return catBean;
	}

	public Vector getCatListByCatId() throws SQLException {
		Vector curCatIdVec = dbObjective.getChildOjbByObjId(con, modParam.getCat_id());

		Vector objVec = dbObjective.getChildListByObjId(con, modParam.getCat_id());
		Vector childObjIdVec = new Vector();
		HashMap ancesterMap = new HashMap();
		for(Iterator iter = objVec.iterator(); iter.hasNext();) {
			dbObjective obj = (dbObjective) iter.next();
			childObjIdVec.add(new Long(obj.obj_id));
			if(obj.obj_ancester != null) {
				long[] objAncesterLst = cwUtils.splitToLong(obj.obj_ancester, " , ");
				for(int objIndex = 0; objIndex < objAncesterLst.length; objIndex++) {
					long parentObjId = objAncesterLst[objIndex];
					Vector childIdVec = null;
					if(ancesterMap.get(new Long(parentObjId)) != null) {
						childIdVec = (Vector) ancesterMap.get(new Long(parentObjId));
					} else {
						childIdVec = new Vector();
					}
					childIdVec.add(new Long(obj.obj_id));
					ancesterMap.put(new Long(parentObjId), childIdVec);
				}
			}
		}
		//all resource count of catalog
		Hashtable catResCountTab = dbObjective.getResCountOfCatalogs(con, childObjIdVec, null);

		//get obj_desc map of objective
		HashMap catDescMap = dbObjective.getDescByObjIds(con, childObjIdVec);

		//compute resource count of catalog
		Vector curCatVec = new Vector();
		for(Iterator iter = curCatIdVec.iterator(); iter.hasNext();) {
			Long catIdObj = (Long) iter.next();
			int resCount =  catResCountTab.get(catIdObj) == null ? 0 : ((Integer)catResCountTab.get(catIdObj)).intValue();
			Vector subIdVec = (Vector) ancesterMap.get(catIdObj);
			Vector subCatVec = null;
			if(subIdVec != null) {
				subCatVec = new Vector();
				for(Iterator subIter = subIdVec.iterator(); subIter.hasNext();) {
					Long subCatIdObj = (Long) subIter.next();
					int resCountSub =  catResCountTab.get(subCatIdObj) == null ? 0 : ((Integer)catResCountTab.get(subCatIdObj)).intValue();
					resCount += resCountSub;

					Vector childIdVec = (Vector) ancesterMap.get(subCatIdObj);
					if(childIdVec != null) {
						for(Iterator childIter = childIdVec.iterator(); childIter.hasNext();) {
							Long childCatIdObj = (Long) childIter.next();
							int resCountChild =  catResCountTab.get(childCatIdObj) == null ? 0 : ((Integer)catResCountTab.get(childCatIdObj)).intValue();
							resCountSub += resCountChild;
						}
					}
					MaterialCatalogBean subCatBean = new MaterialCatalogBean();
					subCatBean.setId(subCatIdObj.longValue());
					subCatBean.setText((String)catDescMap.get(subCatIdObj));
					subCatBean.setCount(resCountSub);
					subCatVec.add(subCatBean);
				}
			}
			MaterialCatalogBean catBean = new MaterialCatalogBean();
			catBean.setId(catIdObj.longValue());
			catBean.setText((String)catDescMap.get(catIdObj));
			catBean.setCount(resCount);
			catBean.setChildren(subCatVec);
			curCatVec.add(catBean);
		}

		return curCatVec;
	}

	public Vector updateResList(Vector resVec) throws SQLException {
		Vector resIdVec = getScormResIds(resVec);
		HashMap scromStructureMap = dbScormResource.getStructureMapByResIds(con, resIdVec);
		return updateFullPathAndStructure(resVec, scromStructureMap);
	}

	/**
	 * 得到SCORM课件的资源ID
	 * @param resVec
	 * @return
	 */
	private Vector getScormResIds(Vector resVec) {
		Vector resIdVec = new Vector();
		if(resVec != null) {
			for(Iterator resIter = resVec.iterator(); resIter.hasNext();) {
				MaterialBean materialBean = (MaterialBean) resIter.next();
				String resSubtype = materialBean.getSubtype();
				if(dbResource.RES_SUBTYPE_RES_SCO.equalsIgnoreCase(resSubtype)){
					resIdVec.add(new Long(materialBean.getId()));
				}
			}
		}
		return resIdVec;
	}
	
	/**
	 * 更新教材的资源全路径和得到SCORM课件的模块内容
	 * @param resVec
	 * @param scromStructureMap
	 * @return
	 */
	protected Vector updateFullPathAndStructure(Vector resVec, HashMap scromStructureMap) {
		Vector resultVec = new Vector();
		if(resVec != null) {
			for(Iterator resIter = resVec.iterator(); resIter.hasNext();) {
				MaterialBean materialBean = (MaterialBean) resIter.next();
				//set full path
				String srcType = materialBean.getSrc_type();
				if(srcType.equals("FILE") || srcType.equals("ZIPFILE")) {
					String fullPath = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl())
						+ materialBean.getId() + "/" + materialBean.getSrc_link();
					materialBean.setFull_path(fullPath);
				}
				//set scorm structure
				if(dbResource.RES_SUBTYPE_RES_SCO.equalsIgnoreCase(materialBean.getSubtype())) {
					String scormStructureXml = (String) scromStructureMap.get(new Long(materialBean.getId()));
					Vector scormStructVec = getScormXMLContent(scormStructureXml);
					materialBean.setStructure_lst(scormStructVec);
				}
				resultVec.add(materialBean);
			}
		}
		return resultVec;
	}

	protected Vector getScormXMLContent(String scormStructureXml) {
		ScormXMLparser myXMLparser = null;
		Vector resVec = new Vector();
		if (scormStructureXml != null) {
			try {
				StringReader in = new StringReader(scormStructureXml);
				SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxFactory.newSAXParser();
				myXMLparser = new ScormXMLparser();
				saxParser.parse(new InputSource(in), myXMLparser);
				in.close();
				resVec = myXMLparser.resVec;
			} catch (ParserConfigurationException e) {
				e.getMessage();
			} catch (SAXException e) {
				e.getMessage();
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return resVec;
	}

	public class ScormXMLparser extends DefaultHandler {
		Vector resVec = new Vector();
		private String tag_name;
		private String res_title;
		private String res_src_type;
		private String res_src_link;

		ScormXMLparser() {
		}

		public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if(qName.equals("item")) {
				res_title = attributes.getValue("title");
			}
			if (qName.equals("itemtype") || qName.equals("src_link")) {
				tag_name = qName;
			} else {
				tag_name = null;
			}
		}

		public void characters(char buf[], int offset, int len) throws SAXException {
			if (tag_name != null && tag_name.equals("itemtype")) {
				String srcType = new String(buf, offset, len).trim();
				//在保证读取其它属性时，不会被设置为空
				if(srcType != null && !"".equals(srcType)) {
					res_src_type = srcType;
				}
			} else if (tag_name != null && tag_name.equals("src_link")) {
				String srcLink = new String(buf, offset, len).trim();
				if(srcLink != null && !"".equals(srcLink)) {
					res_src_link = srcLink;
				}
			}
			if(tag_name != null && "FDR".equalsIgnoreCase(res_src_type)) {
				ScormStructureBean scormBean = new ScormStructureBean();
				scormBean.setTitle(res_title);
				scormBean.setSrc_type(res_src_type);
				resVec.add(scormBean);
				res_title = null;
				res_src_type = null;
				res_src_link = null;
			} else if(tag_name != null && "MOD".equalsIgnoreCase(res_src_type) && res_src_link != null) {
				ScormStructureBean scormBean = new ScormStructureBean();
				scormBean.setTitle(res_title);
				scormBean.setSrc_type(res_src_type);
				scormBean.setSrc_link(res_src_link);
				resVec.add(scormBean);
				res_title = null;
				res_src_type = null;
				res_src_link = null;
			}
		}
	}

	protected HashMap getScormStructureMap(Vector resIdVec) throws SQLException {
		return dbScormResource.getStructureMapByResIds(con, resIdVec);
	}

	public Vector getCatListOfHome() throws SQLException {
		Vector topCatIdVec = dbObjective.getRootFolderListOfLrn(con, this.wizbini, prof.root_ent_id, modParam.getTcr_id(), modParam.getSort(), null);
		HashMap relatedCatMap = null;
		if (topCatIdVec != null && topCatIdVec.size() > 0) {
			relatedCatMap = dbObjective.getSubCatIdListByIds(con, topCatIdVec);
		}
		Vector topCatVec = null;
		if (relatedCatMap != null) {
			Vector relatedFolderIdVec = cwUtils.unionVectors(topCatIdVec, (Vector) relatedCatMap.get(dbObjective.KEY_CAT_ID_LST), false);
			Hashtable catResCountTab = null;
			if (relatedFolderIdVec != null && relatedFolderIdVec.size() > 0) {
				catResCountTab = dbObjective.getResCountOfCatalogs(con, relatedFolderIdVec, null);
			}
			HashMap catResMap = computeFolderResInfo(catResCountTab, (HashMap) relatedCatMap.get(dbObjective.KEY_PARENT_CAT));
			Vector relatedCatVec = dbObjective.getObjListByIds(con, topCatIdVec);
			topCatVec = updateCatListInfo(relatedCatVec, catResMap, modParam.getTcr_id());
			// sub-catalog
			Vector subCatIdVec = dbObjective.getChildOjbByObjId(con, cwUtils.vec2longArray(topCatIdVec));
			Vector subRelatedCatVec = dbObjective.getObjListByIds(con, subCatIdVec);
			HashMap subCatResMap = new HashMap();
			Vector subCatVec = updateCatListInfo(subRelatedCatVec, subCatResMap, modParam.getTcr_id());

			// combination
			HashMap parentSubCatMap = getParentIdAndSubCatLstMap((HashMap) relatedCatMap.get(dbObjective.KEY_PARENT_CAT), subCatVec);
			topCatVec = getRootAndSubCatStruct(topCatVec, parentSubCatMap);
		}
		return topCatVec;
	}
}