package cn.wepact.dfm.customize.mapper;

import java.util.List;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.dto.KnowledgeDto;
import cn.wepact.dfm.dto.KnowledgeHistory;
import cn.wepact.dfm.dto.KnowledgeTableDto;

public interface MoreKnowledgeMapper{

	String getVersionNum();
	
	KnowledgeDto getOne(Integer id);

	List<KnowledgeTableDto> list(Pagination<KnowledgeTableDto> pagination);

	int totalCount(Pagination<KnowledgeTableDto> pagination);

	List<KnowledgeHistory> historylist(Pagination<KnowledgeHistory> pagination);

	int historytotalCount(Pagination<KnowledgeHistory> pagination);
}