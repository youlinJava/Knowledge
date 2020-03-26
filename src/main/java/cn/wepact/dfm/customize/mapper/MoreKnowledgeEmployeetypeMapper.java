package cn.wepact.dfm.customize.mapper;

import cn.wepact.dfm.generator.entity.KnowledgeEmployeetype;
import tk.mybatis.mapper.common.Mapper;

public interface MoreKnowledgeEmployeetypeMapper extends Mapper<KnowledgeEmployeetype> {

	void delectByKnowledgeId(Integer id);
}