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
import cn.wepact.dfm.customize.mapper.MoreKnowledgeCategoryMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeCategoryOrgMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeCategoryProductMapper;
import cn.wepact.dfm.dto.KnowledgeCategoryDTO;
import cn.wepact.dfm.dto.TreeNode;
import cn.wepact.dfm.generator.entity.KnowledgeCategory;
import cn.wepact.dfm.generator.entity.KnowledgeCategoryOrg;
import cn.wepact.dfm.generator.entity.KnowledgeCategoryProduct;
import cn.wepact.dfm.generator.mapper.KnowledgeCategoryMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeCategoryOrgMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeCategoryProductMapper;
import cn.wepact.dfm.vo.KnowledgeCategoryVo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KnowledgeCategoryService {

	@Resource
	KnowledgeCategoryMapper knowledgeCategoryMapper;
	
	@Resource
	MoreKnowledgeCategoryMapper moreKnowledgeCategoryMapper;
	
	@Resource
	KnowledgeCategoryOrgMapper knowledgeCategoryOrgMapper;
	
	@Resource
	KnowledgeCategoryProductMapper knowledgeCategoryProductMapper;

	@Resource
	MoreKnowledgeCategoryOrgMapper moreKnowledgeCategoryoryOrgMapper;
	
	@Resource
	MoreKnowledgeCategoryProductMapper moreKnowledgeCategoryProductMapper;
	

	/**
	 * 获取知识分类树状图
	 * @return
	 */
	public List<TreeNode> treeList() {
		List<TreeNode> treeList= moreKnowledgeCategoryMapper.treeList();
		return TreeNode.buildByRecursive(treeList);
	}

	
	/**
	 * 获取知识分类表格数据
	 * @param pagination 查询信息
	 * @return GeneralRespBean<Pagination<KnowledgeCategoryDTO>>
	 */
	public GeneralRespBean<Pagination<KnowledgeCategoryDTO>> findAllByCondition(Pagination<KnowledgeCategoryDTO> pagination) {
		GeneralRespBean<Pagination<KnowledgeCategoryDTO>> respBean = new GeneralRespBean<>();
		log.info("分页获取知识分类维护页面表格数据开始");
		try {
			log.info("分页获取知识分类维护页面表格数据");
			List<KnowledgeCategoryDTO> lst = moreKnowledgeCategoryMapper.findAllByCondition(pagination);
			log.info("获取页码信息");
			int totalCount = moreKnowledgeCategoryMapper.totalCount(pagination);
			pagination.setResult(lst).setTotalCount(totalCount);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(pagination);
		} catch (Exception e){
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("分页获取知识分类维护页面表格数据结束");
		return respBean;
	}
	
	public KnowledgeCategory getOneByConditions(KnowledgeCategory paramObj) {
		return knowledgeCategoryMapper.selectOne(paramObj);
	}

	/**
     * 新增知识类别
	* @param advertising
	* @return
	*/
	@Transactional
	public BaseRespBean insert(KnowledgeCategoryVo categoryVo) {
		BaseRespBean respBean=new BaseRespBean();
		KnowledgeCategory kn = categoryVo.getCategory();
		kn.setCreateTime(new Date());
		kn.setUpdateTime(new Date());
    	//新增知识分类
    	knowledgeCategoryMapper.insertSelective(kn);
    	//新增知识分类对应单位权限
    	insertOrg(kn,categoryVo);
    	//新增知识分类对应产品关联
    	insertProduct(kn,categoryVo);
    	respBean.setSuccessMsg();
    	return respBean;
	}
	
	private void insertOrg(KnowledgeCategory kn, KnowledgeCategoryVo categoryVo) {
		moreKnowledgeCategoryoryOrgMapper.delectByCategoryId(kn.getId());
		for (KnowledgeCategoryVo.org item : categoryVo.getOrgList()) {
			KnowledgeCategoryOrg categoryOrg = new KnowledgeCategoryOrg();
			categoryOrg.setCategoryId(kn.getId());
			categoryOrg.setOrgCode(item.getOrgCode());
			categoryOrg.setOrgName(item.getOrgName());
			knowledgeCategoryOrgMapper.insert(categoryOrg);
		}
	}

	private void insertProduct(KnowledgeCategory kn, KnowledgeCategoryVo categoryVo) {
		moreKnowledgeCategoryProductMapper.delectByCategoryId(kn.getId());
		for (KnowledgeCategoryVo.product item : categoryVo.getProductList()) {
			KnowledgeCategoryProduct categoryProduct = new KnowledgeCategoryProduct();
			categoryProduct.setCategoryId(kn.getId());
			categoryProduct.setProductId(String.valueOf(item.getCode()));
			categoryProduct.setProductName(item.getLabel());
			knowledgeCategoryProductMapper.insert(categoryProduct);
		}
	}
	 /**
     * 修改知识类别
	* @param advertising
	* @return
	*/
	@Transactional
	public BaseRespBean update(KnowledgeCategoryVo categoryVo) {
		BaseRespBean respBean=new BaseRespBean();
		KnowledgeCategory kn = categoryVo.getCategory();
		kn.setCreateTime(new Date());
		kn.setUpdateTime(new Date());
    	//修改知识分类
    	knowledgeCategoryMapper.updateByPrimaryKeySelective(kn);
    	//新增知识分类对应单位权限
    	insertOrg(kn,categoryVo);
    	//新增知识分类对应产品关联
    	insertProduct(kn,categoryVo);
    	respBean.setSuccessMsg();
    	return respBean;
	}


	public KnowledgeCategoryDTO getOne(Integer id) {
		KnowledgeCategoryDTO categoryDto = moreKnowledgeCategoryMapper.getOne(id);
		return categoryDto;
	}

	/**
	 * 删除知识分类
	 * @param id
	 * @return
	 */
	public BaseRespBean deleteByPrimaryKey(Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		// 判断是否可以删除
		int usedCount = moreKnowledgeCategoryMapper.findInKnowledgeTable(id);
		if(usedCount==0) {//没有被knowledge表引用，可以进行删除
			knowledgeCategoryMapper.deleteByPrimaryKey(id);
			moreKnowledgeCategoryoryOrgMapper.delectByCategoryId(id);
			moreKnowledgeCategoryProductMapper.delectByCategoryId(id);
			respBean.setSuccessMsg();
		}
		return respBean;
	}
	
}
