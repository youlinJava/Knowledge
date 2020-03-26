package cn.wepact.dfm.service;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.customize.mapper.MoreCallcenterCommonLanguageMapper;
import cn.wepact.dfm.generator.entity.CallcenterCommonLanguage;
import cn.wepact.dfm.generator.mapper.CallcenterCommonLanguageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CallcenterCommonLanguageService {
	@Resource
	CallcenterCommonLanguageMapper callcenterCommonLanguageMapper;
	@Resource
	MoreCallcenterCommonLanguageMapper moreCallcenterCommonLanguageMapper;

	public List<CallcenterCommonLanguage> list() {
		// TODO Auto-generated method stub
		return callcenterCommonLanguageMapper.selectAll();
	}
	//获取分页列表
	public Pagination<CallcenterCommonLanguage> listPaging(Pagination<CallcenterCommonLanguage> pagination) {
		// TODO Auto-generated method stub
		List<CallcenterCommonLanguage> lst = moreCallcenterCommonLanguageMapper.list(pagination);
		int totalCount = moreCallcenterCommonLanguageMapper.totalCount(pagination);
		return pagination.setResult(lst).setTotalCount(totalCount);
	}

	public CallcenterCommonLanguage getOne(Integer id) {
		return callcenterCommonLanguageMapper.selectByPrimaryKey(id);
	}

	public CallcenterCommonLanguage getOneByConditions(CallcenterCommonLanguage paramObj) {
		// TODO Auto-generated method stub
		return callcenterCommonLanguageMapper.selectOne(paramObj);
	}

	//删除常用语
	@Transactional
	public BaseRespBean deleteByPrimaryKey(Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		callcenterCommonLanguageMapper.deleteByPrimaryKey(id);
		respBean.setSuccessMsg();
		return respBean;
	}

	//新增常用语
	@Transactional
	public BaseRespBean insert(CallcenterCommonLanguage callcenterCommonLanguage) {
		BaseRespBean respBean=new BaseRespBean();
		callcenterCommonLanguageMapper.insertSelective(callcenterCommonLanguage);
		respBean.setSuccessMsg();
		return respBean;
	}

	//修改常用语
	@Transactional
	public BaseRespBean update(CallcenterCommonLanguage callcenterCommonLanguage) {
		BaseRespBean respBean=new BaseRespBean();
		//修改知识类别
		callcenterCommonLanguageMapper.updateByPrimaryKeySelective(callcenterCommonLanguage);
		respBean.setSuccessMsg();
		return respBean;
	}
}
