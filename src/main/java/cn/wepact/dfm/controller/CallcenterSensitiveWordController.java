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
import cn.wepact.dfm.dto.CallcenterSensitiveWordDto;
import cn.wepact.dfm.generator.entity.CallcenterSensitiveWord;
import cn.wepact.dfm.service.CallcenterSensitiveWordService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/callcenterSensitiveWord")
public class CallcenterSensitiveWordController {

	@Resource
	CallcenterSensitiveWordService callcenterSensitiveWordService;
	
	@ApiOperation(value = "获取呼叫中心关键词数据", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<CallcenterSensitiveWordDto>> listPaging(@RequestBody Pagination<CallcenterSensitiveWordDto> callcenterSensitiveWordDto){
		return callcenterSensitiveWordService.findAllByCondition(callcenterSensitiveWordDto);
	}
	
	@ApiOperation(value = "保存呼叫中心关键词", notes = "")
	@PostMapping("/saveCallcenterSensitiveWord")
	public BaseRespBean saveCallcenterSensitiveWord(@RequestBody CallcenterSensitiveWord callcenterSensitiveWord) {
		BaseRespBean respBean = new BaseRespBean();
		String keyWord = callcenterSensitiveWord.getKeyWord();
		Integer id = callcenterSensitiveWord.getId();
		try {
			log.info("保存呼叫中心关键词开始" + keyWord);
			//判断该呼叫中心关键词是否已经存在
			CallcenterSensitiveWord paramObj= new CallcenterSensitiveWord();
			paramObj.setKeyWord(keyWord);
			CallcenterSensitiveWord cs = callcenterSensitiveWordService.getOneByConditions(paramObj);
			if(cs!=null) {
				if(null == id) {
					respBean.setCode(Constant.ERROR_CODE);
					respBean.setMsg("呼叫中心关键词已经存在！");
					return respBean;
				}else {
					if(cs.getId().intValue()!= id.intValue()) {
						respBean.setCode(Constant.ERROR_CODE);
						respBean.setMsg("呼叫中心关键词已经存在！");
						return respBean;
					}
				}
			}
			if (id == null) {
				// 新增知识类别
				log.info("新增呼叫中心关键词开始" + keyWord);
				respBean = callcenterSensitiveWordService.insert(callcenterSensitiveWord);
				log.info("新增呼叫中心关键词结束" + keyWord);
			} else {
				// 修改知识类别
				log.info("修改呼叫中心关键词开始" + keyWord);
				respBean = callcenterSensitiveWordService.update(callcenterSensitiveWord);
				log.info("修改呼叫中心关键词结束" + keyWord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存呼叫中心关键词结束" + keyWord);
		return respBean;
	}
	/**
	   * 获取一条知识分类
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条呼叫中心关键词", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<CallcenterSensitiveWordDto> getOne(@PathVariable Integer id) {
		GeneralRespBean<CallcenterSensitiveWordDto> respBean = new GeneralRespBean<CallcenterSensitiveWordDto>();
		try {
			log.info("获取一条呼叫中心关键词" + id);
			CallcenterSensitiveWordDto callcenterSensitiveWordDto = callcenterSensitiveWordService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(callcenterSensitiveWordDto);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取一条呼叫中心关键词" + id);
		return respBean;
	}
	/**
	   * 删除知识分类
	 * @param KnowledgeCategory
	 * @return
	 */
	@ApiOperation(value = "删除呼叫中心关键词", notes = "")
	@RequestMapping("/deleteCallcenterSensitiveWord/{id}")
	public BaseRespBean deleteCallcenterSensitiveWord(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除呼叫中心关键词开始" + id);
			respBean = callcenterSensitiveWordService.deleteByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除呼叫中心关键词结束" + id);
		return respBean;
	}
	
}
