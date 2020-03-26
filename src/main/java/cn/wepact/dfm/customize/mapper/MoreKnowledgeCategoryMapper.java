package cn.wepact.dfm.customize.mapper;

import java.util.List;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.dto.KnowledgeCategoryDTO;
import cn.wepact.dfm.dto.TreeNode;

public interface MoreKnowledgeCategoryMapper {

	List<TreeNode> treeList();
	
	List<KnowledgeCategoryDTO> findAllByCondition(Pagination<KnowledgeCategoryDTO> param);
	 
	int totalCount(Pagination<KnowledgeCategoryDTO> param);

	KnowledgeCategoryDTO getOne(Integer id);

	int findInKnowledgeTable(Integer id);
}
