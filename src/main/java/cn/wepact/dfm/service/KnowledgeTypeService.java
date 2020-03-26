package cn.wepact.dfm.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeTypeMapper;
import cn.wepact.dfm.generator.entity.KnowledgeType;
import cn.wepact.dfm.generator.mapper.KnowledgeTypeMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class KnowledgeTypeService {
	
	@Resource
	KnowledgeTypeMapper knowledgeTypeMapper;
	
	@Resource
	MoreKnowledgeTypeMapper moreKnowledgeTypeMapper;
	
	
	public List<KnowledgeType> list() {
		// TODO Auto-generated method stub
		return knowledgeTypeMapper.selectAll();
	}

	public Pagination<KnowledgeType> listPaging(Pagination<KnowledgeType> pagination) {
		// TODO Auto-generated method stub	
		List<KnowledgeType> lst = moreKnowledgeTypeMapper.list(pagination);
		int totalCount = moreKnowledgeTypeMapper.totalCount(pagination);
		return pagination.setResult(lst).setTotalCount(totalCount);
	}

	public KnowledgeType getOne(Integer id) {
		return knowledgeTypeMapper.selectByPrimaryKey(id);
	}


	/**
     * 删除知识类别
     * @param id
     * @return
     */
    @Transactional
	public BaseRespBean deleteByPrimaryKey(Integer id) {
    	BaseRespBean respBean = new BaseRespBean();
		// 判断是否可以删除
		int usedCount = moreKnowledgeTypeMapper.findInKnowledgeTable(id);
		
		if(usedCount==0) {//没有被knowledge表引用，可以进行删除
			knowledgeTypeMapper.deleteByPrimaryKey(id);
			respBean.setSuccessMsg();
		}
		return respBean;
	}

    /**
              * 新增知识类别
     * @param advertising
     * @return
     */
    @Transactional
	public BaseRespBean insert(KnowledgeType knowledgeType) {
    	BaseRespBean respBean=new BaseRespBean();
    	knowledgeType.setCreateTime(new Date());
    	knowledgeType.setUpdateTime(new Date());
    	//新增知识类别
    	knowledgeTypeMapper.insertSelective(knowledgeType);
    	respBean.setSuccessMsg();
    	return respBean;
	}

    /**
             * 修改知识类别
     * @param advertising
     * @return
     */
    @Transactional
	public BaseRespBean update(KnowledgeType knowledgeType) {
    	BaseRespBean respBean=new BaseRespBean();
    	knowledgeType.setUpdateTime(new Date());
        //修改知识类别
        knowledgeTypeMapper.updateByPrimaryKeySelective(knowledgeType);
        respBean.setSuccessMsg();
        return respBean;
	}

	public KnowledgeType getOneByConditions(KnowledgeType paramObj) {
		return knowledgeTypeMapper.selectOne(paramObj);
	}

	public List<KnowledgeType> typeList() {
		return knowledgeTypeMapper.selectAll();
	}


}
