package cn.wepact.dfm.controller;
import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.dto.KnowledgeDto;
import cn.wepact.dfm.dto.KnowledgeHistory;
import cn.wepact.dfm.dto.KnowledgeTableDto;
import cn.wepact.dfm.dto.knowledgeListDTO;


import cn.wepact.dfm.generator.entity.Knowledge;
import cn.wepact.dfm.service.KnowledgeListService;
import cn.wepact.dfm.vo.KnowledgeVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/knowledgeList")
public class KnowledgeListController {

	@Resource
	KnowledgeListService knowledgeListService;

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
			if (knowledgeVo.getKnowledge().getId() == null) {
				// 新增知识类别
				log.info("新增知识开始" + knowledgeVo.getKnowledge().getKnowledgeTitle());
				respBean = knowledgeListService.insert(knowledgeVo);
				log.info("新增知识结束" + knowledgeVo.getKnowledge().getKnowledgeTitle());
			} else {
				// 修改知识类别
				log.info("修改知识开始" + knowledgeVo.getKnowledge().getKnowledgeTitle());
				respBean = knowledgeListService.update(knowledgeVo);
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
			KnowledgeDto dto = knowledgeListService.getOne(id);
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
	@PostMapping("/tableListPaging")
	public GeneralRespBean<Pagination<KnowledgeTableDto>> tableListPaging(@RequestBody Pagination<KnowledgeTableDto> pagination) {
		GeneralRespBean<Pagination<KnowledgeTableDto>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取知识列表开始");
			Pagination<KnowledgeTableDto> KnowledgeTableList = knowledgeListService.TableListPaging(pagination);
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
			Pagination<KnowledgeHistory> knowledgeHistoryList = knowledgeListService.historyListPaging(pagination);
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

	@ApiOperation(value = "获取知识列表分頁", notes = "")
	@PostMapping("/listPaging")
	public GeneralRespBean<Pagination<knowledgeListDTO>> listPaging(@RequestBody Pagination<knowledgeListDTO> pagination) {
		GeneralRespBean<Pagination<knowledgeListDTO>> respBean = new GeneralRespBean<>();
		try {
			log.info("获取知识列表开始");
			Pagination<knowledgeListDTO> knowledgeList = knowledgeListService.listPaging(pagination);
			respBean.setCode(Constant.SUCCESS_CODE);
			respBean.setMsg(Constant.SUCCESS_MSG);
			respBean.setData(knowledgeList);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("获取知识列表结束");
		return respBean;
	}

	/**
	 * 删除知识
	 * @param id 知识主键
	 * @return BaseRespBean
	 */
	@ApiOperation(value = "删除知识", notes = "")
	@RequestMapping("/deleteKnowledge/{id}")
	public BaseRespBean deleteKnowledgeType(@PathVariable Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("删除知识开始" + id);
			respBean = knowledgeListService.deleteByPrimaryKey(id);



		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("删除知识结束" + id);
		return respBean;
	}

	/**
	 * 导入文件
	 * @param  req 文件
	 * @return BaseRespBean
	 */
	@ApiOperation(value = "导入文件", notes = "")
	@PostMapping("/uploadknowledgeFile")
	public GeneralRespBean<List<Knowledge>> uploadknowledgeFile(HttpServletRequest req , HttpServletResponse response, String creatorName, String createBy) throws Exception {
		GeneralRespBean<List<Knowledge>> respBean = knowledgeListService.uploadEmployeeInfoFile(req,creatorName,createBy);
		try {
			log.info("导入文件开始" );
			//response.addDateHeader("res", respBean);



		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("导入文件结束" );
		return respBean;
	}

	@ApiOperation(value = "导出文件", notes = "")
	@PostMapping("/exportKnowledgeFile")
	public ResponseEntity exportKnowledgeFile(@RequestBody Pagination<knowledgeListDTO> pagination ) throws Exception {
		GeneralRespBean<List<knowledgeListDTO>> respBean = new GeneralRespBean<>();
		//Pagination<knowledgeListDTO> pagination = new Pagination<>();
		// knowledgeListService.knowledgeInformationExport(pagination);
		ResponseEntity responseEntity = null;
		try {
			log.info("导入文件开始" );
			responseEntity = knowledgeListService.knowledgeInformationExport(pagination);
			//response.addDateHeader("res", respBean);
		} catch (Exception e) {
			e.printStackTrace();
			respBean.setCode(Constant.ERROR_CODE);
			respBean.setMsg(Constant.ERROR_MSG);
			log.error(e.getMessage());
		}
		log.info("导入文件结束" );
		return responseEntity;
	}

	@ApiOperation(value = "修改知识状态", notes = "")
	@PostMapping("/knowledgeStatus")
	public BaseRespBean knowledgeStatus(@RequestBody Knowledge knowledge) {
		BaseRespBean respBean = new BaseRespBean();
		try {
			log.info("修改知识状态开始" + knowledge.getId());
			respBean = knowledgeListService.updateKnowledgeStatus(knowledge);

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
