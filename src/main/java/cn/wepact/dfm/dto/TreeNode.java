package cn.wepact.dfm.dto;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	
	private Integer id;
	
	private String label;
	
	private Integer pid;
	
	private List<TreeNode> children = new ArrayList<TreeNode>();

	public Integer getId() {
		return id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	
	
	/**
	 * 生成树状图父类
	 * @param treeNodes
	 * @return
	 */
	public static List<TreeNode> buildByRecursive(List<TreeNode> treeNodes) {
		List<TreeNode> trees = new ArrayList<TreeNode>();
		for (TreeNode treeNode : treeNodes) {
			if (treeNode.getPid()==0) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 生成树状图子类
	 * @param treeNode
	 * @param treeNodes
	 * @return
	 */
	public static TreeNode findChildren(TreeNode treeNode,
			List<TreeNode> treeNodes) {
		for (TreeNode it : treeNodes) {
			if (treeNode.getId().equals(it.getPid())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<TreeNode>());
				}
				treeNode.getChildren().add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}


}
