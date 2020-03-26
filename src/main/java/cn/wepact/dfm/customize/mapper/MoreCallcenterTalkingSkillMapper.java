package cn.wepact.dfm.customize.mapper;

import java.util.List;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.generator.entity.CallcenterTalkingSkill;

public interface MoreCallcenterTalkingSkillMapper {

	List<CallcenterTalkingSkill> listpaging(Pagination<CallcenterTalkingSkill> param);

	int totalCount(Pagination<CallcenterTalkingSkill> param);

}
