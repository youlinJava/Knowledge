package cn.wepact.dfm.controller;


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
import cn.wepact.dfm.generator.entity.CallcenterTalkingSkill;
import cn.wepact.dfm.service.CallcenterTalkingSkillService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/callcenterTalkingSkill")
public class CallcenterTalkingSkillController {

	@Resource
	CallcenterTalkingSkillService callcenterTalkingSkillService;

	/**
	 * 获取分类列表分頁
	 * @param CallcenterTalkingSkill
	 * @return
	 */
	@ApiOperation(value = "获取分类列表分頁", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<CallcenterTalkingSkill>> listPaging(@RequestBody Pagination<CallcenterTalkingSkill> pagination) {
		GeneralRespBean<Pagination<CallcenterTalkingSkill>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取分类列表开始");
			Pagination<CallcenterTalkingSkill> CallTermsList = callcenterTalkingSkillService.listPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(CallTermsList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取分类列表结束");
		return respBean;
	}

	/**
	   * 获取一条分类
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条分类", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<CallcenterTalkingSkill> getOne(@PathVariable Integer id) {
		GeneralRespBean<CallcenterTalkingSkill> respBean = new GeneralRespBean<CallcenterTalkingSkill>();
		try {
			log.info("获取一条分类" + id);
			CallcenterTalkingSkill callcenterTalkingSkill = callcenterTalkingSkillService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(callcenterTalkingSkill);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取一条分类结束" + id);
		return respBean;
	}
	
	/**
	 * 删除分类
	 * @param CallcenterTalkingSkill
	 * @return
	 */		
	@ApiOperation(value = "删除分类", notes = "")
	@RequestMapping("/deleteCallcenterTalkingSkill/{id}")
	public BaseRespBean deleteCallTerms(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除分类开始" + id);
			respBean = callcenterTalkingSkillService.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除分类结束" + id);
		return respBean;
	}
	
	/**
	 * 保存分类
	 * @param CallcenterTalkingSkill
	 * @return
	 */
	@ApiOperation(value = "保存分类", notes = "")
	@PostMapping("/saveCallcenterTalkingSkill")
	public BaseRespBean saveKnowledgeType(@RequestBody CallcenterTalkingSkill callcenterTalkingSkill) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("保存分类开始" + callcenterTalkingSkill.getCategory());
			//判断该分类是否已经存在
			CallcenterTalkingSkill paramObj= new CallcenterTalkingSkill();
			paramObj.setCategory(callcenterTalkingSkill.getCategory());
			CallcenterTalkingSkill kt = callcenterTalkingSkillService.getOneByConditions(paramObj);
			if(kt!=null) {
				if(callcenterTalkingSkill.getId() == null) {
					respBean.setCode(Constant.ERROR_CODE);
					respBean.setMsg("分类已经存在！");
					return respBean;
				}else {
					if(kt.getId().intValue()!=callcenterTalkingSkill.getId().intValue()) {
						respBean.setCode(Constant.ERROR_CODE);
						respBean.setMsg("分类已经存在！");
						return respBean;
					}
				}
			}
			if (callcenterTalkingSkill.getId() == null) {
				// 新增分类
				log.info("新增分类开始" + callcenterTalkingSkill.getCategory());
				respBean = callcenterTalkingSkillService.insert(callcenterTalkingSkill);
				log.info("新增分类结束" + callcenterTalkingSkill.getCategory());
			} else {
				// 修改分类
				log.info("修改分类开始" + callcenterTalkingSkill.getCategory());
				respBean = callcenterTalkingSkillService.update(callcenterTalkingSkill);
				log.info("修改分类结束" + callcenterTalkingSkill.getCategory());
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存分类结束" + callcenterTalkingSkill.getCategory());
		return respBean;
	}

}

