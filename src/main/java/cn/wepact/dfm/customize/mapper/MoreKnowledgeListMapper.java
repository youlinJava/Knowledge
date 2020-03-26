package cn.wepact.dfm.customize.mapper;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.dto.knowledgeListDTO;

import java.util.List;

public interface MoreKnowledgeListMapper {

    List<knowledgeListDTO> list(Pagination<knowledgeListDTO> param);

    int totalCount(Pagination<knowledgeListDTO> param);
}
