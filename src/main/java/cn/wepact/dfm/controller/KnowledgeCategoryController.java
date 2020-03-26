package cn.wepact.dfm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.dto.KnowledgeCategoryDTO;
import cn.wepact.dfm.dto.TreeNode;
import cn.wepact.dfm.generator.entity.KnowledgeCategory;
import cn.wepact.dfm.generator.entity.KnowledgeType;
import cn.wepact.dfm.service.KnowledgeCategoryService;
import cn.wepact.dfm.vo.KnowledgeCategoryVo;
import cn.wepact.dfm.vo.Product;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/knowledgeCategory")
public class KnowledgeCategoryController {
	
	@Resource
	KnowledgeCategoryService knowledgeCategoryService;

	/**
	 * 获取知识分类树状图
	 * @param KnowledgeCategory
	 * @return
	 */
	@ApiOperation(value = "获取知识分类树状图", notes = "")
	@PostMapping("/treeList")
	public GeneralRespBean<List<TreeNode>> treeList() {
		GeneralRespBean<List<TreeNode>> respBean = new GeneralRespBean<>();
		// MoreUser mu=authorizationUtil.getUser();
		try {
			log.info("获取知识分类树状图开始");
			List<TreeNode> KnowledgeCategoryList = knowledgeCategoryService.treeList();
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(KnowledgeCategoryList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取知识分类树状图结束");
		return respBean;
	}
	

	@ApiOperation(value = "获取知识分类数据", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<KnowledgeCategoryDTO>>listPaging(@RequestBody Pagination<KnowledgeCategoryDTO> pagination){
		return knowledgeCategoryService.findAllByCondition(pagination);
	}
	
	/**
	   * 获取一条知识分类
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条知识分类", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<KnowledgeCategoryDTO> getOne(@PathVariable Integer id) {
		GeneralRespBean<KnowledgeCategoryDTO> respBean = new GeneralRespBean<KnowledgeCategoryDTO>();
		try {
			log.info("获取一条知识类别" + id);
			KnowledgeCategoryDTO categoryDto = knowledgeCategoryService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(categoryDto);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取一条知识类别结束" + id);
		return respBean;
	}
	
	/**
	 * 保存知识分类
	 * @param KnowledgeCategory
	 * @return
	 */
	
	@ApiOperation(value = "保存知识分类", notes = "")
	@PostMapping("/saveKnowledgeCategory")
	public BaseRespBean saveKnowledgeCategory(@RequestBody KnowledgeCategoryVo categoryVo) {
		BaseRespBean respBean = new BaseRespBean();
		String categoryName = categoryVo.getCategory().getCategoryName();
		Integer id = categoryVo.getCategory().getId();;
		try {
			log.info("保存知识分类开始" + categoryName);
			//判断该知识类别是否已经存在
			KnowledgeCategory paramObj= new KnowledgeCategory();
			paramObj.setCategoryName(categoryName);
			KnowledgeCategory kt = knowledgeCategoryService.getOneByConditions(paramObj);
			if(kt!=null) {
				if(null == id) {
					respBean.setCode(Constant.ERROR_CODE);
					respBean.setMsg("知识分类名字已经存在！");
					return respBean;
				}else {
					if(kt.getId().intValue()!= id.intValue()) {
						respBean.setCode(Constant.ERROR_CODE);
						respBean.setMsg("知识分类名字已经存在！");
						return respBean;
					}
				}
			}
			if (id == null) {
				// 新增知识类别
				log.info("新增知识分类开始" + categoryName);
				respBean = knowledgeCategoryService.insert(categoryVo);
				log.info("新增知识分类结束" + categoryName);
			} else {
				// 修改知识类别
				log.info("修改知识分类开始" + categoryName);
				respBean = knowledgeCategoryService.update(categoryVo);
				log.info("修改知识分类结束" + categoryName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存知识类别结束" + categoryName);
		return respBean;
	}

	
	@RequestMapping("/getProductList")
	public Object getProductList() {		
		Object o=null;
		try {
			log.info("获取产品信息");
			Product p =new Product();
			List<Product> pLst = new ArrayList<Product>();
			p.setLabel("入转离");
			p.setCode("1");
			pLst.add(p);
			p =new Product();
			p.setLabel("人事档案");
			p.setCode("2");
			pLst.add(p);
			p =new Product();
			p.setLabel("社保");
			p.setCode("3");
			pLst.add(p);
			p =new Product();
			p.setLabel("公积金");
			p.setCode("4");
			pLst.add(p);
			o = pLst;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("获取产品信息结束");
		return o;
	}

	/**
	   * 删除知识分类
	 * @param KnowledgeCategory
	 * @return
	 */
	@ApiOperation(value = "删除知识分类", notes = "")
	@RequestMapping("/deleteKnowledgeCategory/{id}")
	public BaseRespBean deleteKnowledgeCategory(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除知识分类开始" + id);
			respBean = knowledgeCategoryService.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除知识分类结束" + id);
		return respBean;
	}
}
