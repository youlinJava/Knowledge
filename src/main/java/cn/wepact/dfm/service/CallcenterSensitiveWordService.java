package cn.wepact.dfm.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.customize.mapper.MoreCallcenterSensitiveWordMapper;
import cn.wepact.dfm.dto.CallcenterSensitiveWordDto;
import cn.wepact.dfm.dto.KnowledgeCategoryDTO;
import cn.wepact.dfm.generator.entity.CallcenterSensitiveWord;
import cn.wepact.dfm.generator.entity.CallcenterWebCustomerSetting;
import cn.wepact.dfm.generator.entity.KnowledgeCategory;
import cn.wepact.dfm.generator.mapper.CallcenterSensitiveWordMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CallcenterSensitiveWordService {
	
	@Resource
	MoreCallcenterSensitiveWordMapper moreCallcenterSensitiveWordMapper;
	
	@Resource
	CallcenterSensitiveWordMapper callcenterSensitiveWordMapper;
	
	 /**
	 * 获取呼叫中心敏感词
	 * @param pagination 查询信息
	 * @return GeneralRespBean<Pagination<CallcenterSensitiveWord>>
	 */
	public GeneralRespBean<Pagination<CallcenterSensitiveWordDto>> findAllByCondition(Pagination<CallcenterSensitiveWordDto> callcenterSensitiveWordDto) {
		GeneralRespBean<Pagination<CallcenterSensitiveWordDto>> respBean = new GeneralRespBean<>();
		log.info("分页获取呼叫中心敏感词表格数据开始");
		try {
			log.info("分页获取呼叫中心敏感词表格数据");
			List<CallcenterSensitiveWordDto> lst = moreCallcenterSensitiveWordMapper.findAllByCondition(callcenterSensitiveWordDto);
			log.info("获取页码信息");
			int totalCount = moreCallcenterSensitiveWordMapper.totalCount(callcenterSensitiveWordDto);
			callcenterSensitiveWordDto.setResult(lst).setTotalCount(totalCount);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(callcenterSensitiveWordDto);
		} catch (Exception e){
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("分页获取呼叫中心敏感词表格数据结束");
		return respBean;
	}

	public CallcenterSensitiveWord getOneByConditions(CallcenterSensitiveWord paramObj) {
		return callcenterSensitiveWordMapper.selectOne(paramObj);
	}
	
	/**
     * 新增呼叫中心敏感词
	* @param advertising
	* @return
	*/
	@Transactional
	public BaseRespBean insert(CallcenterSensitiveWord callcenterSensitiveWord) {
		BaseRespBean respBean=new BaseRespBean();

		callcenterSensitiveWord.setCreateTime(new Date());
		callcenterSensitiveWord.setUpdateTime(new Date());
    	//新增呼叫中心敏感词
		callcenterSensitiveWordMapper.insertSelective(callcenterSensitiveWord);
    	respBean.setSuccessMsg();
    	return respBean;
	}
	/**
     * 修改呼叫中心敏感词
	* @param advertising
	* @return
	*/
	public BaseRespBean update(CallcenterSensitiveWord callcenterSensitiveWord) {
		BaseRespBean respBean=new BaseRespBean();
		callcenterSensitiveWord.setCreateTime(new Date());
		callcenterSensitiveWord.setUpdateTime(new Date());
    	//修改呼叫中心敏感词
		callcenterSensitiveWordMapper.updateByPrimaryKeySelective(callcenterSensitiveWord);
    	respBean.setSuccessMsg();
    	return respBean;
	}

	public CallcenterSensitiveWordDto getOne(Integer id) {
		CallcenterSensitiveWordDto callcenterSensitiveWordDto = moreCallcenterSensitiveWordMapper.getOne(id);
		return callcenterSensitiveWordDto;
	}
	/**
	 * 删除呼叫中心敏感词
	 * @param id
	 * @return
	 */
	public BaseRespBean deleteByPrimaryKey(Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		// 判断是否可以删除
		int usedCount = moreCallcenterSensitiveWordMapper.findInKnowledgeTable(id);
		if (usedCount != 0) {
			callcenterSensitiveWordMapper.deleteByPrimaryKey(id);
			respBean.setSuccessMsg();
		}
		return respBean;
	}

}
