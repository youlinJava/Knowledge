package cn.wepact.dfm.service;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeListMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeTypeMapper;
import cn.wepact.dfm.dto.knowledgeListDTO;
import cn.wepact.dfm.generator.entity.*;
import cn.wepact.dfm.generator.mapper.KnowledgeMapper;
import cn.wepact.dfm.util.ExcelExportUtil;
import cn.wepact.dfm.util.TransitionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;


@Service
@Slf4j
public class KnowledgeListService {

	@Resource
	MoreKnowledgeListMapper moreKnowledgeListMapper;

	@Resource
	KnowledgeMapper knowledgeMapper;

	@Resource
	MoreKnowledgeTypeMapper moreKnowledgeTypeMapper;

	@Resource
	MoreKnowledgeMapper moreKnowledgeMapper;


	public Pagination<knowledgeListDTO> listPaging(Pagination<knowledgeListDTO> pagination) {
		List<knowledgeListDTO> lst = moreKnowledgeListMapper.list(pagination);
		int totalCount = moreKnowledgeListMapper.totalCount(pagination);
		return pagination.setResult(lst).setTotalCount(totalCount);
	}

	/**
	 * 根据Id进行逻辑删除
	 * @param id id主键
	 * @return BaseRespBean
	 */
    public BaseRespBean deleteByPrimaryKey(Integer id) {
		BaseRespBean respBean = new BaseRespBean();
		Knowledge knowledge = knowledgeMapper.selectByPrimaryKey(id);
		if (knowledge == null){
			respBean.setMsg(Constant.ERROR_MSG);
		}
		// knowledgeMapper.deleteByPrimaryKey(id);
		respBean.setSuccessMsg();
		return respBean;
    }

	/**
	 * 导入知识列表
	 * @param request 请求体
	 * @param creatorName 请求人
	 * @param createBy 请求人ID
	 * @return GeneralRespBean<List<Knowledge>>
	 * @throws Exception errmsg
	 */
    @Transactional
	public GeneralRespBean<List<Knowledge>> uploadEmployeeInfoFile(HttpServletRequest request,String creatorName, String createBy)throws Exception {
		InputStream inputStream = null;
		GeneralRespBean<List<Knowledge>> resp = new GeneralRespBean<>();
		List<String> visualList = new ArrayList<>();
		visualList.add("普通员工");
		visualList.add("专业用户");
		visualList.add("呼叫中心");
		visualList.add("非共享员工");
		try{
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multiRequest.getFile("file");
			inputStream = file.getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			// 获取第一个sheet页
			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
			// 行
			int rowNum = sheet.getLastRowNum();

			List<Knowledge> knowledgeList = new ArrayList<>();
			List<KnowledgeRegion> knowledgeRegionList = new ArrayList<>();
			List<KnowledgeOrg> knowledgeOrgList = new ArrayList<>();
			List<KnowledgeEmployeetype> knowledgeEmployeetypeList = new ArrayList<>();
			List<KnowledgeJobposition> knowledgeJobpositionList = new ArrayList<>();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			XSSFDataFormat df = workbook.createDataFormat(); // 此处设置数据格式
			XSSFCellStyle hssfCellStyleDate = workbook.createCellStyle();
			hssfCellStyleDate.setDataFormat(df.getFormat("yyyy/M/d"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			boolean errorFlag = true;
			// 创建临时文件夹
			String tmpPath = Files.createTempDirectory("excel-").toString();
			System.out.println(tmpPath);
			File files = new File(tmpPath + File.separator + "error.xlsx");
			tmpPath = URLEncoder.encode(tmpPath, "utf-8");
			tmpPath = tmpPath.replace("%2F", "%3F");

			CellStyle cellStyle = ExcelExportUtil.getStyle(workbook, true, false);
			CellStyle fontStyle = ExcelExportUtil.getStyle(workbook, false, true);

			Set<String> sett= new HashSet<>();
			for (int i = 1; i <= rowNum; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				StringBuilder errorMsg = new StringBuilder();
				boolean empTypeFlag = true;
				Knowledge knowledge = new Knowledge();
				KnowledgeRegion knowledgeRegion = new KnowledgeRegion();
				KnowledgeOrg knowledgeOrg = new KnowledgeOrg();
				KnowledgeEmployeetype knowledgeEmployeetype  = new KnowledgeEmployeetype();
				KnowledgeJobposition knowledgeJobposition = new KnowledgeJobposition();
				// 分类验证
				if (!StringUtils.isEmpty(row.getCell(0))){
					knowledge.setCategoryId(1);
				}else {
					errorMsg.append("分类不可为空，");
					Cell cell0 = row.createCell(0);
					cell0.setCellStyle(cellStyle);
				}
				// 标题验证
				if (!StringUtils.isEmpty(row.getCell(1))){
					if (sett.add(row.getCell(1).toString())){
						knowledge.setKnowledgeTitle(row.getCell(1).toString());
					}else {
						errorMsg.append("标题重复");
						Cell cell1 = row.createCell(1);
						cell1.setCellStyle(cellStyle);
					}
				}else {
					errorMsg.append("标题不可为空");
					Cell cell1 = row.createCell(1);
					cell1.setCellStyle(cellStyle);
				}
				// 地域验证
				if (!StringUtils.isEmpty(row.getCell(2))){
					knowledgeRegion.setCityCode(row.getCell(2).toString());
				}else {
					errorMsg.append("地域不可为空");
					Cell cell2 = row.createCell(2);
					cell2.setCellStyle(cellStyle);
				}
				// 组织验证
				if (!StringUtils.isEmpty(row.getCell(3))){
					knowledgeOrg.setOrgCode(row.getCell(3).toString());
				}else {
					errorMsg.append("组织不可为空");
					Cell cell3 = row.createCell(3);
					cell3.setCellStyle(cellStyle);
				}
				// 有效期验证
				Cell cell4 = row.getCell(4);
				if (!StringUtils.isEmpty(row.getCell(4))) {
					try {
						if (HSSFDateUtil.isCellDateFormatted(cell4)) {
							knowledge.setValidTime(cell4.getDateCellValue());
						} else {
							errorMsg.append("有效期格式不正确，");
							cell4.setCellStyle(cellStyle);
						}
					} catch (Exception e) {
						errorMsg.append("有效期格式不正确，");
						cell4.setCellStyle(cellStyle);
					}
				} else {
					errorMsg.append("有效期不能为空,");
					cell4 = row.createCell(4);
					cell4.setCellStyle(cellStyle);
				}
				// 可见范围验证
				if (!StringUtils.isEmpty(row.getCell(5))){
					String visualStr = row.getCell(5).toString();
					String params = "";
					List<String> lis = TransitionUtil.stringToList(visualStr);
					for (String str: visualList) {
						if (lis.contains(str)) {
							params += "1";
						} else {
							params += "0";
						}
					}
					if (StringUtils.isEmpty(params) || "0000".equals(params)){
						errorMsg.append("可见范围不合法,");
						Cell cell5 = row.createCell(5);
						cell5.setCellStyle(cellStyle);
					} else {
						knowledge.setVisibleTo(params);
					}
				}else {
					errorMsg.append("可见范围不可为空,");
					Cell cell5 = row.createCell(5);
					cell5.setCellStyle(cellStyle);
				}
				// 人员类型验证
				if (!StringUtils.isEmpty(row.getCell(6))){
					boolean flag = true;
					// TODO: 2020/3/23  人员类型存在验证？？？？
					String categoryName = row.getCell(6).toString();
					List<String> lis = Arrays.asList(categoryName.split(","));
					if (flag){
						knowledgeEmployeetype.setEmployeetypeId(row.getCell(6).toString());
					} else {
						errorMsg.append("人员类型不存在,");
						Cell cell6 = row.createCell(6);
						cell6.setCellStyle(cellStyle);
					}
				}
				// 岗位序列验证验证
				if (!StringUtils.isEmpty(row.getCell(7))){
					// TODO: 2020/3/23  岗位序列存在验证？？？？
					String categoryName = row.getCell(7).toString();
					List<String> lis = Arrays.asList(categoryName.split(","));
					boolean flag = true;
					if (flag){
						knowledgeJobposition.setJobPositionCode(row.getCell(7).toString());
					} else {
						errorMsg.append("岗位序列不存在,");
						Cell cell7 = row.createCell(7);
						cell7.setCellStyle(cellStyle);
					}
				}
				// 产品验证
				if (!StringUtils.isEmpty(row.getCell(8))){
					// TODO: 2020/3/23  产品存在验证？？？？
					boolean flag = true;
					if (flag){
						// TODO: 2020/3/23  产品外键固定  后续更改
						knowledge.setProductId(1);
					} else {
						errorMsg.append("产品不存在,");
						Cell cell8 = row.createCell(8);
						cell8.setCellStyle(cellStyle);
					}
				}else {
					errorMsg.append("产品不可为空,");
					Cell cell8 = row.createCell(8);
					cell8.setCellStyle(cellStyle);
				}
				// 知识类别验证
				if (!StringUtils.isEmpty(row.getCell(9))){
					KnowledgeType knowledgeType = moreKnowledgeTypeMapper.findByTypeName(row.getCell(9).toString());
					if (knowledgeType != null){
						knowledge.setTypeId(knowledgeType.getId());
					} else {
						errorMsg.append("知识类别不存在,");
						Cell cell9 = row.createCell(9);
						cell9.setCellStyle(cellStyle);
					}
				}
				// 关键字验证
				if (!StringUtils.isEmpty(row.getCell(10))){
					knowledge.setKeyword(row.getCell(10).toString());
				}else {
					errorMsg.append("关键字不可为空,");
					Cell cell10 = row.createCell(10);
					cell10.setCellStyle(cellStyle);
				}
				// 简介赋值
				if (!StringUtils.isEmpty(row.getCell(11))){
					knowledge.setKnowledgeDesc(row.getCell(11).toString());
				}
				// 内容赋值
				if (!StringUtils.isEmpty(row.getCell(12))){
					knowledge.setKnowledgeContent(row.getCell(12).toString());
				} else {
					errorMsg.append("内容不可为空,");
					Cell cell12 = row.createCell(12);
					cell12.setCellStyle(cellStyle);
				}
				knowledge.setCreateBy(createBy);
				knowledge.setCreateTime(new Date());
				knowledge.setUpdateBy(createBy);
				knowledge.setUpdateTime(new Date());
				knowledge.setCreatorName(creatorName);
				knowledge.setUpdatorName(creatorName);

				Cell cell13 =  row.createCell(13);
				cell13.setCellValue(String.valueOf(errorMsg));
				cell13.setCellStyle(fontStyle);
				if (!StringUtils.isEmpty(errorMsg.toString())){
					errorFlag = false;
				}
				knowledgeList.add(knowledge);
				knowledgeRegionList.add(knowledgeRegion);
				knowledgeOrgList.add(knowledgeOrg);
				knowledgeEmployeetypeList.add(knowledgeEmployeetype);
				knowledgeJobpositionList.add(knowledgeJobposition);
			}
			// 将excel放入临时文件夹
			FileOutputStream fos = new FileOutputStream(files.getPath());
			workbook.write(fos);
			fos.close();
			if(errorFlag){
				if (!knowledgeList.isEmpty()){
					Integer id = 1;
					for (int i = 0 ;i< knowledgeList.size();i++){
//						knowledgeMapper.insertSelective(knowledgeList.get(i));
//						id = knowledgeList.get(i).getId();
						knowledgeRegionList.get(i).setKnowledgeId(id);
						knowledgeOrgList.get(i).setKnowledgeId(id);
						knowledgeEmployeetypeList.get(i).setKnowledgeId(id);
						knowledgeJobpositionList.get(i).setKnowledgeId(id);
					}
					resp.setCode(Constant.SUCCESS_CODE);
					resp.setMsg("成功导入" + knowledgeList.size() + "条数据");
					resp.setData(knowledgeList);
				}else {
					resp.setCode(Constant.SUCCESS_CODE);
					resp.setMsg("0");
				}
			}else {
				resp.setCode(Constant.ERROR_CODE);
				resp.setMsg(tmpPath);
			}
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return null;
	}



	/**
	 * 导出知识信息
	 */
	public ResponseEntity<org.springframework.core.io.Resource> knowledgeInformationExport( Pagination<knowledgeListDTO> pagination) throws IOException {
		log.info("导出人员信息Excel personnelInformationExport start");
		String fileName = "excel/knowledge.xlsx";

		String exportPath = Files.createTempDirectory("excel-personnelInformationExport-").toString();
		log.info("导出人员信息Excel personnelInformationExport exportPath:{}", exportPath);

		String exportFileName = System.currentTimeMillis() + ".xlsx";
		log.info("导出人员信息Excel personnelInformationExport exportFileName:{}", exportFileName);

		/* 读取模板 */
		org.springframework.core.io.Resource resource = new ClassPathResource(fileName);
		/* 赋值后生成新的文件 */
		Workbook workbook = new XSSFWorkbook(resource.getInputStream());

		pagination.setPageNo(1);
		pagination.setPageSize(9999999);

		// 导出全部信息
		List<knowledgeListDTO> personnelInformationAll = moreKnowledgeListMapper.list(pagination);
		fillPersonnelInformationExcelDataAll(workbook, personnelInformationAll);
		log.info("导出人员信息Excel personnelInformationExport personnelInformationAll size:{}", personnelInformationAll.size());

		workbook.write(new FileOutputStream(new File(exportPath + File.separator + exportFileName).getPath()));
		/* 下载新文件 */
		FileInputStream fis = new FileInputStream(new File(exportPath + File.separator + exportFileName));
		InputStreamResource isr = new InputStreamResource(fis);

		HttpHeaders header = new HttpHeaders();
		String exportName = "知识导出-";
		exportName += new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";
		log.info("导出知识信息Excel personnelInformationExport exportName:{}", exportName);
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(exportName, "utf-8"));
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		log.info("导出人员信息Excel personnelInformationExport end");

		return ResponseEntity.ok()
				.headers(header)
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(isr);
	}


	private void fillPersonnelInformationExcelDataAll(Workbook workbook, List<knowledgeListDTO> personnelInformationAll) {
		int rowNumber;
		for (int i = 0; i < personnelInformationAll.size(); i++) {
			knowledgeListDTO dto = personnelInformationAll.get(i);
			rowNumber = i + 1;
			// 知识分类
			setPersonnelInformationExportValue(rowNumber, 0, workbook, dto.getCategoryName());
			// 知识标题
			setPersonnelInformationExportValue(rowNumber, 1, workbook, dto.getKnowledgeTitle());
			// 地域
			setPersonnelInformationExportValue(rowNumber, 2, workbook, dto.getKnowledgeTitle());
			// 组织名称
			setPersonnelInformationExportValue(rowNumber, 3, workbook, dto.getOrgName());
			// 有效日期
			setPersonnelInformationExportValue(rowNumber, 4, workbook, dto.getValidTime() == null ? null : new SimpleDateFormat("yyyy-M-d").format(dto.getValidTime()));
			// 可见范围 todo 是否要在文件中显示
			setPersonnelInformationExportValue(rowNumber, 5, workbook, "");
			// 人员类型 todo personType 目前没有接口
			setPersonnelInformationExportValue(rowNumber, 6, workbook, dto.getEmployeetypeId());
			// 岗位序列 todo 目前没有接口
			setPersonnelInformationExportValue(rowNumber, 7, workbook, dto.getJobPositionCode());
			// 产品
			setPersonnelInformationExportValue(rowNumber, 8, workbook, dto.getProductName());
			// 知识类别
			setPersonnelInformationExportValue(rowNumber, 9, workbook, dto.getTypeName());
			// 关键字
			setPersonnelInformationExportValue(rowNumber, 10, workbook, dto.getKeyword());
			// 简介
			setPersonnelInformationExportValue(rowNumber, 11, workbook, dto.getKnowledgeDesc());
			// 内容
			setPersonnelInformationExportValue(rowNumber, 12, workbook, dto.getKnowledgeContent());
		}

	}
	private void setPersonnelInformationExportValue(int rowNumber, int cellNumber, Workbook workbook, Object value) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(FillPatternType.NO_FILL);
		cellStyle.setLocked(false);

		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);

		short borderColor = IndexedColors.BLACK.getIndex();
		cellStyle.setTopBorderColor(borderColor);
		cellStyle.setBottomBorderColor(borderColor);
		cellStyle.setLeftBorderColor(borderColor);
		cellStyle.setRightBorderColor(borderColor);

		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(rowNumber) == null ? sheet.createRow(rowNumber) : sheet.getRow(rowNumber);
		Cell cell = row.getCell(cellNumber, CREATE_NULL_AS_BLANK);
		cell.setCellStyle(cellStyle);
		if (ObjectUtils.isEmpty(value)) {
			cell.setCellValue("");
		} else if (value instanceof Double) {
			Double convert = (Double) value;
			cell.setCellValue(convert);
		} else if (value instanceof Integer) {
			Integer convert = (Integer) value;
			cell.setCellValue(convert);
		} else if (value instanceof Date) {
			Date convert = (Date) value;
			cell.setCellValue(convert);
		} else if (value instanceof String) {
			String convert = (String) value;
			cell.setCellValue(convert);
		}
	}

}
