package cn.wepact.dfm.controller;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.generator.entity.CallcenterCommonLanguage;
import cn.wepact.dfm.service.CallcenterCommonLanguageService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/CallcenterCommonLanguage")
public class CallcenterCommonLanguageController {

	@Resource
	CallcenterCommonLanguageService callcenterCommonLanguageService;

	@ApiOperation(value = "获取常用语列表分頁", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<CallcenterCommonLanguage>> listPaging(@RequestBody Pagination<CallcenterCommonLanguage> pagination) {
		GeneralRespBean<Pagination<CallcenterCommonLanguage>> respBean = new GeneralRespBean<>();
		// MoreUser mu=authorizationUtil.getUser();
		try {
			log.info("获取常用语列表开始");
			Pagination<CallcenterCommonLanguage> CallcenterCommonLanguageList = callcenterCommonLanguageService.listPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(CallcenterCommonLanguageList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取常用语列表结束");
		return respBean;
	}

	/**
	 * 获取一条常用语
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条常用语", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<CallcenterCommonLanguage> getOne(@PathVariable Integer id) {
		GeneralRespBean<CallcenterCommonLanguage> respBean = new GeneralRespBean<CallcenterCommonLanguage>();
		try {
			log.info("获取一条常用语" + id);
			CallcenterCommonLanguage callcenterCommonLanguage = callcenterCommonLanguageService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(callcenterCommonLanguage);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取一条常用语结束" + id);
		return respBean;
	}

	//删除常用语
	@ApiOperation(value = "删除常用语", notes = "")
	@RequestMapping("/deleteCallcenterCommonLanguage/{id}")
	public BaseRespBean deleteCallcenterCommonLanguage(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除常用语开始" + id);
			respBean = callcenterCommonLanguageService.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除常用语结束" + id);
		return respBean;
	}

	//保存常用语
	@ApiOperation(value = "保存常用语", notes = "")
	@PostMapping("/saveCallcenterCommonLanguage")
	public BaseRespBean saveCallcenterCommonLanguage(@RequestBody CallcenterCommonLanguage callcenterCommonLanguage) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("保存常用语开始" + callcenterCommonLanguage.getCategory());
			//判断该常用语是否已经存在
			CallcenterCommonLanguage paramObj= new CallcenterCommonLanguage();
			paramObj.setCategory(callcenterCommonLanguage.getCategory());
			CallcenterCommonLanguage ccl = callcenterCommonLanguageService.getOneByConditions(paramObj);
			if(ccl!=null) {
				if(callcenterCommonLanguage.getId() == null) {
					respBean.setCode(Constant.ERROR_CODE);
					respBean.setMsg("常用语已经存在！");
					return respBean;
				}else {
					if(ccl.getId().intValue()!=callcenterCommonLanguage.getId().intValue()) {
						respBean.setCode(Constant.ERROR_CODE);
						respBean.setMsg("常用语已经存在！");
						return respBean;
					}
				}
			}
			if (callcenterCommonLanguage.getId() == null) {
				// 新增知识类别
				log.info("新增常用语开始" + callcenterCommonLanguage.getCategory());
				respBean = callcenterCommonLanguageService.insert(callcenterCommonLanguage);
				log.info("新增常用语结束" + callcenterCommonLanguage.getCategory());
			} else {
				// 修改知识类别
				log.info("修改常用语开始" + callcenterCommonLanguage.getCategory());
				respBean = callcenterCommonLanguageService.update(callcenterCommonLanguage);
				log.info("修改常用语结束" + callcenterCommonLanguage.getCategory());
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存常用语结束" + callcenterCommonLanguage.getCategory());
		return respBean;
	}

}
