//package cn.wepact.dfm.util;
//
//import cn.wepact.dfm.dto.TreeNode;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 递归封装树形菜单
// *
// */
//public class Tree {
//    List<TreeNode> nodes = new ArrayList<TreeNode>();
//
//    public Tree(List<TreeNode> nodes) {
//        super();
//        this.nodes = nodes;
//    }
//
//    /**
//     * 构建树形结构
//     *
//     * @return
//     */
//    public List<TreeNode> buildTree() {
//        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
//        List<TreeNode> rootNodes = getRootNodes();
//        for (TreeNode rootNode : rootNodes) {
//            buildChildNodes(rootNode);
//            treeNodes.add(rootNode);
//        }
//        return treeNodes;
//    }
//
//    /**
//     * 递归子节点
//     *
//     * @param node
//     */
//    public void buildChildNodes(TreeNode node) {
//        List<TreeNode> children = getChildNodes(node);
//        if (!children.isEmpty()) {
//            for (TreeNode child : children) {
//                buildChildNodes(child);
//            }
//            node.setChildren(children);
//        }
//    }
//
//    /**
//     * 获取父节点下所有的子节点
//     *
//     * @param pnode
//     * @return
//     */
//    public List<TreeNode> getChildNodes(TreeNode pnode) {
//        List<TreeNode> childNodes = new ArrayList<TreeNode>();
//        for (TreeNode n : nodes) {
//            if (pnode.getId().equals(n.getParentId())) {
//                childNodes.add(n);
//            }
//        }
//        return childNodes;
//    }
//
//    /**
//     * 判断是否为根节点
//     *
//     * @param node
//     * @return
//     */
//    public boolean rootNode(TreeNode node) {
//        boolean isRootNode = true;
//        for (TreeNode n : nodes) {
//            if (node.getParentId().equals(n.getId())) {
//                isRootNode = false;
//                break;
//            }
//        }
//        return isRootNode;
//    }
//
//    /**
//     * 获取集合中所有的根节点
//     *
//     * @return
//     */
//    public List<TreeNode> getRootNodes() {
//        List<TreeNode> rootNodes = new ArrayList<TreeNode>();
//        for (TreeNode n : nodes) {
//            if (rootNode(n)) {
//                rootNodes.add(n);
//            }
//        }
//        return rootNodes;
//    }
//}
