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
import cn.wepact.dfm.vo.Employeetype;
import cn.wepact.dfm.vo.Jobposition;
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
		List<Product> pLst = new ArrayList<Product>();
		Object o=null;
		try {
			log.info("获取产品信息");
			Product yi =new Product();
			Product er = new Product();
			Product san = new Product();
			List<Product> sanList = new ArrayList<Product>();
			List<Product> erList = new ArrayList<Product>();
			san.setLabel("入转离1-3-3");
			san.setCode("入转离1-3-3");
			san.setId(6);
			sanList.add(san);
			san = new Product();
			san.setLabel("入转离1-3-2");
			san.setCode("入转离1-3-2");
			san.setId(5);
			sanList.add(san);
			san.setLabel("入转离1-3-1");
			san.setCode("入转离1-3-1");
			san.setId(4);
			sanList.add(san);
			er.setCode("入转离1-1");
			er.setLabel("入转离1-1");
			er.setId(3);
			er.setChildren(sanList);
			erList.add(er);
			sanList = new ArrayList<Product>();
			er = new Product();
			san = new Product();
			san.setLabel("入转离2-3-3");
			san.setCode("入转离2-3-3");
			san.setId(7);
			sanList.add(san);
			san = new Product();
			san.setLabel("入转离2-3-2");
			san.setCode("入转离2-3-2");
			san.setId(8);
			sanList.add(san);
			san.setLabel("入转离2-3-1");
			san.setCode("入转离2-3-1");
			san.setId(9);
			sanList.add(san);
			er.setCode("入转离2-2");
			er.setLabel("入转离2-2");
			er.setId(2);
			er.setChildren(sanList);
			erList.add(er);
			yi.setLabel("入转离");
			yi.setCode("1");
			yi.setId(1);
			yi.setChildren(erList);
			pLst.add(yi);
			yi =new Product();
			san = new Product();
			er = new Product();
			sanList = new ArrayList<Product>();
			erList = new ArrayList<Product>();
			san.setLabel("人事档案1-3-3");
			san.setCode("人事档案1-3-3");
			san.setId(10);
			sanList.add(san);
			san = new Product();
			san.setLabel("人事档案1-3-2");
			san.setCode("人事档案1-3-2");
			san.setId(11);
			sanList.add(san);
			san.setLabel("人事档案1-3-1");
			san.setCode("人事档案1-3-1");
			san.setId(12);
			sanList.add(san);
			er.setCode("人事档案1-1");
			er.setLabel("人事档案1-1");
			er.setId(13);
			er.setChildren(sanList);
			erList.add(er);
			sanList = new ArrayList<Product>();
			er = new Product();
			san = new Product();
			san.setLabel("人事档案2-3-3");
			san.setCode("人事档案2-3-3");
			san.setId(14);
			sanList.add(san);
			san = new Product();
			san.setLabel("人事档案2-3-2");
			san.setCode("人事档案2-3-2");
			san.setId(15);
			sanList.add(san);
			san.setLabel("人事档案2-3-1");
			san.setCode("人事档案2-3-1");
			san.setId(16);
			sanList.add(san);
			er.setCode("人事档案2-2");
			er.setLabel("人事档案2-2");
			er.setId(17);
			er.setChildren(sanList);
			erList.add(er);
			sanList = new ArrayList<Product>();
			yi.setLabel("人事档案");
			yi.setCode("2");
			yi.setId(18);
			yi.setChildren(erList);
			pLst.add(yi);
			yi =new Product();
			san = new Product();
			er = new Product();
			sanList = new ArrayList<Product>();
			erList = new ArrayList<Product>();
			san.setLabel("社保1-3-3");
			san.setCode("社保1-3-3");
			san.setId(19);
			sanList.add(san);
			san = new Product();
			san.setLabel("社保1-3-2");
			san.setCode("社保1-3-2");
			san.setId(20);
			sanList.add(san);
			san.setLabel("社保1-3-1");
			san.setCode("社保1-3-1");
			san.setId(21);
			sanList.add(san);
			er.setCode("社保1-1");
			er.setLabel("社保1-1");
			er.setId(36);
			er.setChildren(sanList);
			erList.add(er);
			sanList = new ArrayList<Product>();
			er = new Product();
			san = new Product();
			san.setLabel("社保2-3-3");
			san.setCode("社保2-3-3");
			san.setId(22);
			sanList.add(san);
			san = new Product();
			san.setLabel("社保2-3-2");
			san.setCode("社保2-3-2");
			san.setId(23);
			sanList.add(san);
			san.setLabel("社保2-3-1");
			san.setCode("社保2-3-1");
			san.setId(24);
			sanList.add(san);
			er.setCode("社保2-2");
			er.setLabel("社保2-2");
			er.setId(35);
			er.setChildren(sanList);
			erList.add(er);
			sanList = new ArrayList<Product>();
			yi.setLabel("社保");
			yi.setCode("3");
			yi.setId(34);
			yi.setChildren(erList);
			pLst.add(yi);
			
			yi =new Product();
			san = new Product();
			er = new Product();
			sanList = new ArrayList<Product>();
			erList = new ArrayList<Product>();
			san.setLabel("公积金1-3-3");
			san.setCode("公积金1-3-3");
			san.setId(25);
			sanList.add(san);
			san = new Product();
			san.setLabel("公积金1-3-2");
			san.setCode("公积金1-3-2");
			san.setId(26);
			sanList.add(san);
			san.setLabel("公积金1-3-1");
			san.setCode("公积金1-3-1");
			san.setId(27);
			sanList.add(san);
			er.setCode("公积金1-1");
			er.setLabel("公积金1-1");
			er.setChildren(sanList);
			er.setId(33);
			erList.add(er);
			sanList = new ArrayList<Product>();
			er = new Product();
			san = new Product();
			san.setLabel("公积金2-3-3");
			san.setCode("公积金2-3-3");
			san.setId(28);
			sanList.add(san);
			san = new Product();
			san.setLabel("公积金2-3-2");
			san.setCode("公积金2-3-2");
			san.setId(29);
			sanList.add(san);
			san.setLabel("公积金2-3-1");
			san.setCode("公积金2-3-1");
			san.setId(30);
			sanList.add(san);
			er.setCode("公积金2-2");
			er.setLabel("公积金2-2");
			er.setChildren(sanList);
			er.setId(32);
			erList.add(er);
			sanList = new ArrayList<Product>();
			yi.setLabel("公积金");
			yi.setCode("4");
			yi.setChildren(erList);
			yi.setId(31);
			pLst.add(yi);
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
	
	@RequestMapping("/getEmployeetype")
	public Object getEmployeetypeList() {
		List<Employeetype> pLst = new ArrayList<Employeetype>();
		try {
			log.info("获取人员类型开始");
			Employeetype emm = new Employeetype();
			emm.setEmployeetypeId("1");
			emm.setEmployeetypeName("合同制员工");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("2");
			emm.setEmployeetypeName("劳务人员");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("3");
			emm.setEmployeetypeName("实习生");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("4");
			emm.setEmployeetypeName("派驻人员");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("5");
			emm.setEmployeetypeName("聘用人员");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("6");
			emm.setEmployeetypeName("非全日制用工");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("7");
			emm.setEmployeetypeName("进站博士后");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("8");
			emm.setEmployeetypeName("外部董监事");
			pLst.add(emm);
			emm = new Employeetype();
			emm.setEmployeetypeId("9");
			emm.setEmployeetypeName("其他人员");
			pLst.add(emm);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("获取人员类型结束");
		return pLst;
	}
	
	@RequestMapping("/getJobposition")
	public Object getJobpositionList() {
		List<Jobposition> pLst = new ArrayList<Jobposition>();
		try {
			log.info("获取岗位序列开始");
			Jobposition job = new Jobposition();
			job.setJobPositionCode("1");
			job.setJobPositionName("行政类-高管");
			pLst.add(job);
			job = new Jobposition();
			job.setJobPositionCode("2");
			job.setJobPositionName("行政类-中层");
			pLst.add(job);
			job = new Jobposition();
			job.setJobPositionCode("3");
			job.setJobPositionName("工程技术类");
			pLst.add(job);
			job = new Jobposition();
			job.setJobPositionCode("4");
			job.setJobPositionName("工程技术类-研发技术");
			pLst.add(job);
			job = new Jobposition();
			job.setJobPositionCode("5");
			job.setJobPositionName("技能工人");
			pLst.add(job);
			job = new Jobposition();
			job.setJobPositionCode("6");
			job.setJobPositionName("专业管理类");
			pLst.add(job);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("获取岗位序列结束");
		return pLst;
	}
	
	
}
