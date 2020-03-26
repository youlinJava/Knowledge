package cn.wepact.dfm.customize.mapper;

import java.util.List;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.generator.entity.KnowledgeType;

public interface MoreKnowledgeTypeMapper {

	List<KnowledgeType> list(Pagination<KnowledgeType> param);

	int totalCount(Pagination<KnowledgeType> param);

	int findInKnowledgeTable(Integer id);

}
