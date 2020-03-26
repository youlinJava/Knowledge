package cn.wepact.dfm.customize.mapper;

import java.util.List;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.dto.CallcenterSensitiveWordDto;

public interface MoreCallcenterSensitiveWordMapper {

	List<CallcenterSensitiveWordDto> findAllByCondition(Pagination<CallcenterSensitiveWordDto> callcenterSensitiveWordDto);

	int totalCount(Pagination<CallcenterSensitiveWordDto> callcenterSensitiveWordDto);

	CallcenterSensitiveWordDto getOne(Integer id);

	int findInKnowledgeTable(Integer id);

	
}
