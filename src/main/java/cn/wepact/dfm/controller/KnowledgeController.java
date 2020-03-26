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
import cn.wepact.dfm.dto.KnowledgeDto;
import cn.wepact.dfm.dto.KnowledgeHistory;
import cn.wepact.dfm.dto.KnowledgeTableDto;
import cn.wepact.dfm.generator.entity.Knowledge;
import cn.wepact.dfm.service.KnowledgeService;
import cn.wepact.dfm.vo.KnowledgeVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {
	
	@Resource
	KnowledgeService knowledgeService;
	
	/**
	 * 保存知识类别	
	 * @param Knowledge
	 * @return
	 */
	@ApiOperation(value = "保存知识", notes = "")
	@PostMapping("/saveKnowledge")
	public BaseRespBean saveKnowledgeType(@RequestBody KnowledgeVo knowledgeVo) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("保存知识类别开始" + knowledgeVo.getKnowledge().getKnowledgeTitle());
//			//判断该知识是否已经存在
//			Knowledge paramObj= new Knowledge();
//			paramObj.setKnowledgeTitle(knowledgeVo.getKnowledge().getKnowledgeTitle());
//			Knowledge kt = knowledgeService.getOneByConditions(paramObj);
//			if(kt!=null) {
//				if(knowledgeVo.getKnowledge().getKnowledgeCode() == null) {
//					respBean.setCode(Constant.ERROR_CODE);
//					respBean.setMsg("知识标题已经存在！");
//					return respBean;
//				}else {
//					if(!(kt.getKnowledgeCode().equals(knowledgeVo.getKnowledge().getKnowledgeCode()))) {
//						respBean.setCode(Constant.ERROR_CODE);
//						respBean.setMsg("知识标题已经存在！");
//						return respBean;
//					}
//				}
//			}
			if (knowledgeVo.getKnowledge().getId() == null) {
				// 新增知识类别
				log.info("新增知识开始" + knowledgeVo.getKnowledge().getKnowledgeTitle());
				respBean = knowledgeService.insert(knowledgeVo);
				log.info("新增知识结束" + knowledgeVo.getKnowledge().getKnowledgeTitle());
			} else {
				// 修改知识类别
				log.info("修改知识开始" + knowledgeVo.getKnowledge().getKnowledgeTitle());
				respBean = knowledgeService.update(knowledgeVo);
				log.info("修改知识结束" + knowledgeVo.getKnowledge().getKnowledgeTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("保存知识类别结束" + knowledgeVo.getKnowledge().getKnowledgeTitle());
		return respBean;
	}

	/**
	   * 获取一条知识分类
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "获取一条知识分类", notes = "")
	@RequestMapping("/getOne/{id}")
	public GeneralRespBean<KnowledgeDto> getOne(@PathVariable Integer id) {
		GeneralRespBean<KnowledgeDto> respBean = new GeneralRespBean<KnowledgeDto>();
		try {
			log.info("获取一条知识类别" + id);
			KnowledgeDto dto = knowledgeService.getOne(id);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(dto);
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
	 * 获取知识分頁
	 * @param KnowledgeType
	 * @return
	 */
	@ApiOperation(value = "获取知识类别列表分頁", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<KnowledgeTableDto>> listPaging(@RequestBody Pagination<KnowledgeTableDto> pagination) {
		GeneralRespBean<Pagination<KnowledgeTableDto>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取知识列表开始");
			Pagination<KnowledgeTableDto> KnowledgeTableList = knowledgeService.listPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(KnowledgeTableList);
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
	 * 获取知识历史分頁
	 * @param KnowledgeType
	 * @return
	 */
	@ApiOperation(value = "获取知识历史分頁", notes = "")
	@PostMapping("/historyListPaging")
	public GeneralRespBean<Pagination<KnowledgeHistory>> historyListPaging(@RequestBody Pagination<KnowledgeHistory> pagination) {
		GeneralRespBean<Pagination<KnowledgeHistory>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取知识列表开始");
			Pagination<KnowledgeHistory> knowledgeHistoryList = knowledgeService.historyListPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(knowledgeHistoryList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取知识类别列表结束");
		return respBean;
	}
	
	@ApiOperation(value = "修改知识状态", notes = "")
	@PostMapping("/knowledgeStatus")
	public BaseRespBean knowledgeStatus(@RequestBody Knowledge knowledge) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("修改知识状态开始" + knowledge.getId());
			respBean = knowledgeService.updateKnowledgeStatus(knowledge);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("修改知识状态结束" + knowledge.getId());
		return respBean;
	}
}
