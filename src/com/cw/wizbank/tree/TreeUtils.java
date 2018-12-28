package com.cw.wizbank.tree;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.studyMaterial.bean.MaterialCatalogBean;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.tree.cwTree.nodeInfo;

@SuppressWarnings("rawtypes")
public class TreeUtils {

	public static TreeNode nodeInfoToTreeNode(nodeInfo node) {
		TreeNode treeNode = new TreeNode();
		treeNode.setId(node.id);
		treeNode.setName(node.title);
		treeNode.setType(cwTree.NODE_TYPE_TC);
		treeNode.setParent(true);
		treeNode.setOpen(true);
		return treeNode;
	}

	public static Vector convertCatToNodeList(Vector catListVec) {
		Vector<TreeNode> nodeVector = new Vector<TreeNode>();
		if (catListVec != null) {
			for (Iterator catIter = catListVec.iterator(); catIter.hasNext();) {
				MaterialCatalogBean catBean = (MaterialCatalogBean) catIter.next();

				TreeNode treeNode = new TreeNode();
				treeNode.setId(catBean.getId());
				treeNode.setName(catBean.getDesc());
				treeNode.setType(cwTree.NODE_TYPE_CATALOG);
				treeNode.setParent(true);
				treeNode.setOpen(true);
				nodeVector.add(treeNode);
			}
		}
		return nodeVector;
	}

	public static Vector getTreeNodeVectorByTcrList(WizbiniLoader wizbini, List tcrList) {
		Vector<TreeNode> nodeVector = new Vector<TreeNode>();
		if (tcrList != null) {
			for (Iterator iter = tcrList.iterator(); iter.hasNext();) {
				TCBean tcr = (TCBean) iter.next();

				if (wizbini != null && !wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
					if (tcr.getTcr_id() == 1) {
						continue;
					}
				}

				TreeNode treeNode = new TreeNode();
				treeNode.setId(tcr.getTcr_id());
				treeNode.setName(tcr.getTcr_title());
				treeNode.setType(cwTree.NODE_TYPE_TC);
				treeNode.setParent(true);
				treeNode.setOpen(true);
				nodeVector.addElement(treeNode);
			}
		}
		return nodeVector;
	}

	public static Vector getTreeNodeByCatVector(Vector catVec) {
		Vector<TreeNode> nodeVector = new Vector<TreeNode>();
		if (catVec != null) {
			for (Iterator iter = catVec.iterator(); iter.hasNext();) {
				aeCatalog catalog = (aeCatalog) iter.next();

				TreeNode treeNode = new TreeNode();
				treeNode.setId(catalog.cat_treenode.tnd_id);
				treeNode.setName(catalog.cat_title);
				treeNode.setType(cwTree.NODE_TYPE_CATALOG);
				treeNode.setParent(true);
				treeNode.setOpen(true);
				nodeVector.addElement(treeNode);
			}
		}

		return nodeVector;
	}
}
