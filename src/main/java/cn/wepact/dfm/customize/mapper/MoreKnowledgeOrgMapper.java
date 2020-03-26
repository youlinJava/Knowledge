package cn.wepact.dfm.customize.mapper;

import cn.wepact.dfm.generator.entity.KnowledgeOrg;
import tk.mybatis.mapper.common.Mapper;

public interface MoreKnowledgeOrgMapper{

	void delectByKnowledgeId(Integer id);

}