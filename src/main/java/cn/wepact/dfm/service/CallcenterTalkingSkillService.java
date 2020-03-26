package cn.wepact.dfm.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.customize.mapper.MoreCallcenterTalkingSkillMapper;
import cn.wepact.dfm.generator.entity.CallcenterTalkingSkill;
import cn.wepact.dfm.generator.mapper.CallcenterTalkingSkillMapper;


@Service
public class CallcenterTalkingSkillService {

	@Resource
	CallcenterTalkingSkillMapper callcenterTalkingSkillMapper;
	
	@Resource
	MoreCallcenterTalkingSkillMapper moreCallcenterTalkingSkillMapper;
	
	public Pagination<CallcenterTalkingSkill> listPaging(Pagination<CallcenterTalkingSkill> param) {
		
		List<CallcenterTalkingSkill> lst = moreCallcenterTalkingSkillMapper.listpaging(param);
		int totalCount = moreCallcenterTalkingSkillMapper.totalCount(param);
		return param.setResult(lst).setTotalCount(totalCount);
	}

	public CallcenterTalkingSkill getOne(Integer id) {
		
		return callcenterTalkingSkillMapper.selectByPrimaryKey(id);
	}

	  /**
	     * 新增知识类别
	 * @param callcenterTalkingSkill
	 * @return
	 */
	@Transactional
	public BaseRespBean insert(CallcenterTalkingSkill callcenterTalkingSkill) {
		BaseRespBean respBean=new BaseRespBean();
		callcenterTalkingSkill.setCreateTime(new Date());
		//新增分类
		callcenterTalkingSkillMapper.insertSelective(callcenterTalkingSkill);
		respBean.setSuccessMsg();
		return respBean;
	}

	public CallcenterTalkingSkill getOneByConditions(CallcenterTalkingSkill paramObj) {
	
		return callcenterTalkingSkillMapper.selectOne(paramObj);
	}
	/**
     * 删除分类
     * @param id
     * @return
     */
	public BaseRespBean deleteByPrimaryKey(Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		callcenterTalkingSkillMapper.deleteByPrimaryKey(id);
			respBean.setSuccessMsg();
			return respBean;
		}
	
	/**
	 * 修改分类
	* @param callcenterTalkingSkill
	* @return
	*/
	@Transactional
	public BaseRespBean update(CallcenterTalkingSkill callcenterTalkingSkill) {
		BaseRespBean respBean=new BaseRespBean();
        //修改知识类别
		callcenterTalkingSkillMapper.updateByPrimaryKeySelective(callcenterTalkingSkill);
        respBean.setSuccessMsg();
        return respBean;
	}


}
