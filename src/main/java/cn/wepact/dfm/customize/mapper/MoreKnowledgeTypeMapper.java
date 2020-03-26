package cn.wepact.dfm.customize.mapper;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.generator.entity.KnowledgeType;

import java.util.List;

public interface MoreKnowledgeTypeMapper {

	List<KnowledgeType> list(Pagination<KnowledgeType> param);

	int totalCount(Pagination<KnowledgeType> param);

	int findInKnowledgeTable(Integer id);

	/**
	 * 根据类别名称返回数据
	 * @param typeName 类别名称
	 * @return KnowledgeType
	 */
	KnowledgeType findByTypeName(String typeName);
}
