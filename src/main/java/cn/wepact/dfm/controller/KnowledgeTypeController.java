package cn.wepact.dfm.controller;

import cn.wepact.dfm.account.client.OrgFeignClient;
import cn.wepact.dfm.account.entity.MoreUser;
import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.generator.entity.KnowledgeType;
import cn.wepact.dfm.service.KnowledgeTypeService;
import cn.wepact.dfm.util.AuthorizationUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/knowledgeType")
public class KnowledgeTypeController {

	@Resource
	KnowledgeTypeService knowledgeTypeService;

	@Resource
	AuthorizationUtil authorizationUtil;

	@Autowired
	private OrgFeignClient orgFeignClient;

	@RequestMapping("/getUserOrgsTree")
	public Object getUserOrgsTree() {

		Object o=null;
		try {
			log.info("获取用户组织信息");
			o =orgFeignClient.getOrg2Tree();

		} catch (Exception e) {
			e.printStackTrace();

			log.error(e.getMessage());
		}
		log.info("获取用户组织信息结束");
		return o;
	}

	/**
	 * 获取一条知识类别
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条知识类别", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<KnowledgeType> getOne(@PathVariable Integer id) {
		GeneralRespBean<KnowledgeType> respBean = new GeneralRespBean<KnowledgeType>();
		try {
			log.info("获取一条知识类别" + id);
			KnowledgeType knowledgeType = knowledgeTypeService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(knowledgeType);
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
	 * 删除知识类别
	 * @param KnowledgeType
	 * @return
	 */
	@ApiOperation(value = "删除知识类别", notes = "")
	@RequestMapping("/deleteKnowledgeType/{id}")
	public BaseRespBean deleteKnowledgeType(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除知识类别开始" + id);
			respBean = knowledgeTypeService.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除知识类别结束" + id);
		return respBean;
	}

	/**
	 * 获取知识类别列表分頁
	 * @param KnowledgeType
	 * @return
	 */
	@ApiOperation(value = "获取知识类别列表分頁", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<KnowledgeType>> listPaging(@RequestBody Pagination<KnowledgeType> pagination) {
		GeneralRespBean<Pagination<KnowledgeType>> respBean = new GeneralRespBean<>();
		// MoreUser mu=authorizationUtil.getUser();
		try {
			log.info("获取知识类别列表开始");
			Pagination<KnowledgeType> KnowledgeTypeList = knowledgeTypeService.listPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(KnowledgeTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取知识类别列表结束");
		return respBean;
	}

	/**
	 * 保存知识类别
	 * @param KnowledgeType
	 * @return
	 */
	@ApiOperation(value = "保存知识类别", notes = "")
	@PostMapping("/saveKnowledgeType")
	public BaseRespBean saveKnowledgeType(@RequestBody KnowledgeType knowledgeType) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("保存知识类别开始" + knowledgeType.getTypeName());
			//判断该知识类别是否已经存在
			KnowledgeType paramObj= new KnowledgeType();
			paramObj.setTypeName(knowledgeType.getTypeName());
			KnowledgeType kt = knowledgeTypeService.getOneByConditions(paramObj);
			if(kt!=null) {
				if(knowledgeType.getId() == null) {
					respBean.setCode(Constant.ERROR_CODE);
					respBean.setMsg("知识类别名字已经存在！");
					return respBean;
				}else {
					if(kt.getId().intValue()!=knowledgeType.getId().intValue()) {
						respBean.setCode(Constant.ERROR_CODE);
						respBean.setMsg("知识类别名字已经存在！");
						return respBean;
					}
				}
			}
			if (knowledgeType.getId() == null) {
				// 新增知识类别
				log.info("新增知识类别开始" + knowledgeType.getTypeName());
				respBean = knowledgeTypeService.insert(knowledgeType);
				log.info("新增知识类别结束" + knowledgeType.getTypeName());
			} else {
				// 修改知识类别
				log.info("修改知识类别开始" + knowledgeType.getTypeName());
				respBean = knowledgeTypeService.update(knowledgeType);
				log.info("修改知识类别结束" + knowledgeType.getTypeName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存知识类别结束" + knowledgeType.getTypeName());
		return respBean;
	}

	/**
	 * 获取知识类别列表

	 * @return
	 */
	@ApiOperation(value = "获取知识类别列表", notes = "")
	@RequestMapping("/typeList")
	public GeneralRespBean<List<KnowledgeType>> typeList() {
		GeneralRespBean<List<KnowledgeType>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取知识类别列表开始");
			List<KnowledgeType> KnowledgeTypeList = knowledgeTypeService.typeList();
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(KnowledgeTypeList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取知识类别列表结束");
		return respBean;
	}

}
