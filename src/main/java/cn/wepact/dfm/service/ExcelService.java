//package cn.wepact.dfm.service;
//
//import cn.wepact.dfm.common.util.Constant;
//import cn.wepact.dfm.util.ExcelExportUtil;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.json.JSONObject;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.math.BigDecimal;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//@Slf4j
//public class ExcelService {
//
//	public GeneralRespBean<List<LeaveUploadEmployeeInfoData>> uploadEmployeeInfoFile(
//            HttpServletRequest request, String userNo, String leaveWay, String applyNo) throws Exception {
//		GeneralRespBean<List<LeaveUploadEmployeeInfoData>> resp = new GeneralRespBean<List<LeaveUploadEmployeeInfoData>>();
//		InputStream inputStream = null;
//		try {
//			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//			MultipartFile file = multiRequest.getFile("file");
//			inputStream = file.getInputStream();
//			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//			// 获取第一个sheet页
//			XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
//			int rowNum = sheet.getLastRowNum(); // 行
//			List<LeaveUploadEmployeeInfoData> list = new ArrayList<LeaveUploadEmployeeInfoData>();
//			LeaveUploadEmployeeInfoData uploadEmployeeInfoData = new LeaveUploadEmployeeInfoData();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = new Date();
//			XSSFDataFormat df = workbook.createDataFormat(); // 此处设置数据格式
//			XSSFCellStyle hssfCellStyleDate = workbook.createCellStyle();
//			hssfCellStyleDate.setDataFormat(df.getFormat("yyyy/M/d"));//
//			// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			boolean errorFlag = true;
//			// 创建临时文件夹
//			String tmpPath = Files.createTempDirectory("excel-").toString();
//			System.out.println(tmpPath);
//			File files = new File(tmpPath + File.separator + "error.xlsx");
//			tmpPath = URLEncoder.encode(tmpPath, "utf-8");
//			tmpPath = tmpPath.replace("%2F", "%3F");
//
//			CellStyle cellStyle = ExcelExportUtil.getStyle(workbook, true, false);
//			CellStyle fontStyle = ExcelExportUtil.getStyle(workbook, false, true);
//
//			List<String> orgList = new ArrayList<>();
//			Set<String> sett= new HashSet<String>();
//			String leaveClass = null;
//			for (int i = 1; i <= rowNum; i++) {
//				XSSFRow row = sheet.getRow(i);
//				if (row != null) {
//					String errorMsg = "";
//					boolean empTypeFlag = true;
//					uploadEmployeeInfoData = new LeaveUploadEmployeeInfoData();
//					if (objectIsNotEmpty(row.getCell(2))) {
//						if(sett.add(row.getCell(2).toString())){
//							uploadEmployeeInfoData.setJobNumber(row.getCell(2).toString());
//						}else{
//							errorMsg = errorMsg + "员工编号重复，";
//							Cell cell2 = row.getCell(2);
//							cell2.setCellStyle(cellStyle);
//						}
//					} else {
//						errorMsg = errorMsg + "人员编号不能为空，";
//						Cell cell2 = row.createCell(2);
//						cell2.setCellStyle(cellStyle);
//					}
//					// 取员工信息 + 导入有效性校验
//					JSONObject jsonObject = HttpApiUtils.getHttpRequest(URL2 + row.getCell(2).toString()+"/"+userNo);
//					System.out.println("请求URL：" + URL2 + row.getCell(2).toString()+"/"+userNo);
//					System.out.println("返回结果：" + jsonObject);
//					if (jsonObject != null) {
//						if ("200".equals(jsonObject.get("code"))) {
//							JSONObject jdata = jsonObject.getJSONObject("data");
//							if (!jdata.containsKey("employee_number")) {
//								errorMsg = errorMsg + "员工编号不存在，";
//								Cell cell2 = row.getCell(2);
//								cell2.setCellStyle(cellStyle);
//							}else{
//								Map<String, Object> data = new HashMap<>();
//								Iterator it = JSONObject.fromObject(jsonObject.get("data")).keys();
//								while (it.hasNext()) {
//									String key = String.valueOf(it.next().toString());
//									String value = JSONObject.fromObject(jsonObject.get("data")).get(key).toString();
//									data.put(key, value);
//								}
//								Map<String, Object> map = new HashMap<>();
//								map.put("jobNumber", row.getCell(2).toString());
//								if (StringUtils.isNotEmpty(applyNo)) {
//									map.put("applyNo", applyNo);
//								}
//								int a = moreLeaveApplyMapper.selectLeaveEmployeeCount(map);
//								if (a > 0) {
//									errorMsg = errorMsg + "该员工已存在离职申请，";
//								}
//								uploadEmployeeInfoData.setName(data.get("last_name").toString());
//								uploadEmployeeInfoData.setGender(data.get("sex").toString());
//								uploadEmployeeInfoData.setBirthTime(
//										dateFormat.parse(data.get("date_of_birth").toString()));
//								uploadEmployeeInfoData.setEmployeeType(data.get("person_type_id").toString());
//								uploadEmployeeInfoData.setEmployeeTypeName(data.get("person_type").toString());
//								uploadEmployeeInfoData.setPersonnelRank(data.get("grade_name").toString());
//								uploadEmployeeInfoData.setUnitCode(data.get("orgCode").toString());
//								orgList.add(data.get("orgCode").toString());
//								uploadEmployeeInfoData.setUnit(data.get("orgName").toString());
//								uploadEmployeeInfoData.setDepartmentCode(data.get("deptCode").toString());
//								uploadEmployeeInfoData.setDepartment(data.get("deptName").toString());
//								uploadEmployeeInfoData.setPostCode(data.get("position_code").toString());
//								uploadEmployeeInfoData.setPost(data.get("position_name").toString());
//								if( !ObjectUtils.isEmpty(data.get("high_edu")) &&  !data.get("high_edu").toString().equals("null")){
//									uploadEmployeeInfoData.setEducationCode(data.get("high_edu").toString());
//									uploadEmployeeInfoData.setEducation(data.get("high_edu_name").toString());
//								}
//								uploadEmployeeInfoData.setOnboardTime(ObjectUtils.isEmpty(data.get("attribute28")) ? null : dateFormat.parse(data.get("attribute28").toString().replaceAll("/","-")));
//								uploadEmployeeInfoData.setIsBadJob(data.get("is_poison_pos").toString());
//								uploadEmployeeInfoData.setContactNumber(data.get("internal_location").toString());
//
//								if (!data.get("person_type_id").toString().equals("1144") && !data.get("person_type_id").toString().equals("1148")) {
//									errorMsg = errorMsg + "该人员类型不在共享离职流程范围内，";
//								}
//								Cell cell3 = row.getCell(3);
//								if (objectIsNotEmpty(cell3)) {
//									if (data.get("last_name").toString().equals(cell3.toString())) {
//										uploadEmployeeInfoData.setName(cell3.toString());
//									} else {
//										errorMsg = errorMsg + "人员姓名与编号不符，";
//										cell3 = row.getCell(3);
//										cell3.setCellStyle(cellStyle);
//									}
//								}
//								Cell cell4 = row.getCell(4);
//								if (objectIsNotEmpty(cell4) ) {
//									try {
//										if (HSSFDateUtil.isCellDateFormatted(cell4)) {
////                    Date centerFirstDisDate=parseExcelDate(row.getCell(4),DateHelper.YYYY_MM_DD);
//											if(leaveWay.equals("1")){
//												if (dateCompare(cell4.getDateCellValue())) {
//													uploadEmployeeInfoData.setLeaveDate(cell4.getDateCellValue());
//												} else {
//													errorMsg = errorMsg + "离职日期距离当前日期小于7个工作日，";
//													cell4.setCellStyle(cellStyle);
//													cell4.setCellStyle(hssfCellStyleDate);
//												}
//											}else{
//												uploadEmployeeInfoData.setLeaveDate(cell4.getDateCellValue());
//											}
//											if(!ObjectUtils.isEmpty(data.get("attribute28"))){
//												Date d =  dateFormat.parse(data.get("attribute28").toString().replaceAll("/","-"));
//												String wol = dayComparePrecise(d,cell4.getDateCellValue());
//												uploadEmployeeInfoData.setWorkingLife(wol);
//											}
//										} else {
//											errorMsg = errorMsg + "离职日期格式不正确，";
//											cell4.setCellStyle(cellStyle);
//										}
//									} catch (Exception e) {
//										errorMsg = errorMsg + "离职日期格式不正确，";
//										cell4.setCellStyle(cellStyle);
//									}
//								} else {
//									errorMsg = errorMsg + "离职日期不能为空，";
//									cell4 = row.createCell(4);
//									cell4.setCellStyle(cellStyle);
//								}
//								if (data.get("person_type_id").toString().equals("1144")) {
//									Cell cell5 = row.getCell(5);
//									if (objectIsNotEmpty(cell5)) {
//										// 离职类型（仅合同制员工填写）
//										LeaveType LeaveType =
//												leaveTypeMapper.selectLeaveTypeByTypeName(row.getCell(5).toString());
//										if (LeaveType != null) {
//											if(i == 1){
//												leaveClass = LeaveType.getTypeClass();
//											}else{
//												if(LeaveType.getTypeClass().equals(leaveClass)){
//												}else{
//													errorMsg = errorMsg + "人员离职类型不一致，";
//													cell5.setCellStyle(cellStyle);
//												}
//											}
//											uploadEmployeeInfoData.setLeaveType(LeaveType.getTypeCode());
//											// 解除终止合同依据
//											uploadEmployeeInfoData.setContractBasis(LeaveType.getTypeAgreement());
//										} else {
//											errorMsg = errorMsg + "离职类型填写错误，";
//											cell5.setCellStyle(cellStyle);
//										}
//									} else {
//										errorMsg = errorMsg + "合同制员工离职类型不能为空，";
//										cell5 = row.createCell(5);
//										cell5.setCellStyle(cellStyle);
//									}
//									Cell cell14 = row.getCell(14);
//									if (objectIsNotEmpty(cell14)) {
//										// 离职去向（仅合同制员工填写）
//										if (useLoop(row.getCell(14).toString().replaceAll("、", ""))) {
//											uploadEmployeeInfoData.setLeaveWhereabouts(row.getCell(14).toString());
//										} else {
//											errorMsg = errorMsg + "离职去向填写错误，";
//											cell14.setCellStyle(cellStyle);
//										}
//
//									} else {
//										errorMsg = errorMsg + "合同制员工离职去向不能为空，";
//										cell14 = row.createCell(14);
//										cell14.setCellStyle(cellStyle);
//									}
//									Cell cell15 = row.getCell(15);
//									if (objectIsNotEmpty(cell15)) {
//										if(cell15.toString().length()<31){
//											// 离职去向具体单位
//											uploadEmployeeInfoData.setLeaveWhereaboutsCompany(row.getCell(15).toString());
//										}else{
//											errorMsg = errorMsg + "合同制员工离职去向具体单位填写错误，";
//											cell15.setCellStyle(cellStyle);
//										}
//									} else {
//										errorMsg = errorMsg + "合同制员工离职去向具体单位不能为空，";
//										cell15 = row.createCell(15);
//										cell15.setCellStyle(cellStyle);
//									}
//								}
//								Cell cell6 = row.getCell(6);
//								if (objectIsNotEmpty(cell6)) {
//									// 是否支付经济补偿金
//									if ("是".equals(row.getCell(6).toString())) {
//										uploadEmployeeInfoData.setIsCompensation("1");
//										Cell cell8 = row.getCell(8);
//										if (objectIsNotEmpty(cell8)) {
//											// 单位支付经济补偿金金额
//											if (isNumber(row.getCell(8).toString())) {
//												uploadEmployeeInfoData.setCompensation(
//														new BigDecimal(row.getCell(8).toString()));
//											} else {
//												errorMsg = errorMsg + "单位支付经济补偿金金额填写错误，";
//												cell8.setCellStyle(cellStyle);
//											}
//										} else {
//											errorMsg = errorMsg + "支付经济补偿金时单位支付经济补偿金金额不能为空，";
//											cell8 = row.createCell(8);
//											cell8.setCellStyle(cellStyle);
//										}
//										if (objectIsNotEmpty(row.getCell(7))){
//											if ( isNumber(row.getCell(7).toString())) {
//												// 员工解除劳动合同前12个月平均工资
//												uploadEmployeeInfoData.setAverageWage(
//														new BigDecimal(row.getCell(7).toString()));
//											} else {
//												errorMsg = errorMsg + "员工解除劳动合同前12个月平均工资金额填写错误，";
//												row.getCell(7).setCellStyle(cellStyle);
//											}
//										}
//										if (objectIsNotEmpty(row.getCell(9))){
//											if (isNumber(row.getCell(9).toString())) {
//												// 单位支付其他赔偿总金额
//												uploadEmployeeInfoData.setOtherCompensation(
//														new BigDecimal(row.getCell(9).toString()));
//											} else {
//												errorMsg = errorMsg + "单位支付其他赔偿总金额填写错误，";
//												row.getCell(9).setCellStyle(cellStyle);
//											}
//										}
//										if (objectIsNotEmpty(row.getCell(10)) ){
//											if (isNumber(row.getCell(10).toString())) {
//												// 单位支付生活补助费总金额
//												uploadEmployeeInfoData.setLivingAllowance(
//														new BigDecimal(row.getCell(10).toString()));
//											} else {
//												errorMsg = errorMsg + "单位支付生活补助费总金额填写错误，";
//												row.getCell(10).setCellStyle(cellStyle);
//											}
//										}
//
//										if (objectIsNotEmpty(row.getCell(11))){
//											if (isNumber(row.getCell(11).toString())) {
//												// 单位支付医疗补助费总金额
//												uploadEmployeeInfoData.setMedicalAllowance(
//														new BigDecimal(row.getCell(11).toString()));
//											} else {
//												errorMsg = errorMsg + "单位支付医疗补助费总金额填写错误，";
//												row.getCell(11).setCellStyle(cellStyle);
//											}
//										}
//										if (objectIsNotEmpty(row.getCell(12))){
//											if ( isNumber(row.getCell(12).toString())) {
//												// 单位支付一次性工伤医疗和伤残就业补助金总金额
//												uploadEmployeeInfoData.setInjuryAllowance(
//														new BigDecimal(row.getCell(12).toString()));
//											} else {
//												errorMsg = errorMsg + "单位支付一次性工伤医疗和伤残就业补助金总金额填写错误，";
//												row.getCell(12).setCellStyle(cellStyle);
//											}
//										}
//
//
//										if (objectIsNotEmpty(row.getCell(13))){
//											if (isNumber(row.getCell(13).toString())) {
//												// 个人支付违约金金额
//												uploadEmployeeInfoData.setDefaultAmount(
//														new BigDecimal(row.getCell(13).toString()));
//											} else {
//												errorMsg = errorMsg + "个人支付违约金金额填写错误，";
//												row.getCell(13).setCellStyle(cellStyle);
//											}
//										}
//									} else if ("否".equals(row.getCell(6).toString())) {
//										uploadEmployeeInfoData.setIsCompensation("0");
//									} else {
//										errorMsg = errorMsg + "是否支付经济补偿金填写错误，";
//										cell6.setCellStyle(cellStyle);
//									}
//								} else {
//									errorMsg = errorMsg + "是否支付经济补偿金不能为空，";
//									cell6 = row.createCell(6);
//									cell6.setCellStyle(cellStyle);
//								}
//
//								uploadEmployeeInfoData.setLeaveWay(leaveWay);
//								uploadEmployeeInfoData.setCreateBy(userNo);
//								uploadEmployeeInfoData.setCreateTime(new Date());
//								uploadEmployeeInfoData.setUpdateBy(userNo);
//								uploadEmployeeInfoData.setUpdateTime(new Date());
//							}
//						} else  if ("108".equals(jsonObject.get("code"))) {
//							errorMsg = errorMsg + "员工不在权限下，";
//						} else {
//							errorMsg = errorMsg + "获取人员信息失败";
//						}
//					} else {
//						errorMsg = errorMsg + "获取人员信息失败";
//					}
//					Cell cell16 = row.createCell(16);
//					cell16.setCellValue(errorMsg);
//					cell16.setCellStyle(fontStyle);
//					if (StringUtils.isNotEmpty(errorMsg)) {
//						errorFlag = false;
//					}
//					list.add(uploadEmployeeInfoData);
//				}
//			}
//			// 将excel放入临时文件夹
//			FileOutputStream fos = new FileOutputStream(files.getPath());
//			workbook.write(fos);
//			fos.close();
//			if (errorFlag) {
//				if (list != null && list.size() > 0) {
//					Set o = new HashSet(orgList);
//					if(o.size() <= 1){
//						if(leaveWay.equals("1")){
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("userNo", userNo);
//							map.put("leaveWay", leaveWay);
//							moreLeaveUploadEmployeeInfoMapper.deleteAllUploadLeaveEmployee(map);
//							for (int i = 0; i < list.size(); i++) {
//								LeaveUploadEmployeeInfoData uploadEmployeeInfo = new LeaveUploadEmployeeInfoData();
//								uploadEmployeeInfo = list.get(i);
//								moreLeaveUploadEmployeeInfoMapper.insertLeaveUploadEmployeeInfo(uploadEmployeeInfo);
//							}
//							resp.setCode(Constant.SUCCESS_CODE);
//							resp.setMsg("成功导入" + list.size() + "条数据");
//							List<LeaveUploadEmployeeInfoData> lists = new ArrayList<LeaveUploadEmployeeInfoData>();
//							lists = moreLeaveUploadEmployeeInfoMapper.selectUploadEmployeeInfoList(map);
//							resp.setData(lists);
//						}else{
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("userNo", userNo);
//							map.put("leaveWay", leaveWay);
//							moreLeaveUploadEmployeeInfoMapper.deleteAllUploadLeaveEmployee(map);
//							for (int i = 0; i < list.size(); i++) {
//								LeaveUploadEmployeeInfoData uploadEmployeeInfo = new LeaveUploadEmployeeInfoData();
//								uploadEmployeeInfo = list.get(i);
//								moreLeaveUploadEmployeeInfoMapper.insertLeaveUploadEmployeeInfo(uploadEmployeeInfo);
//							}
//							resp.setCode(Constant.SUCCESS_CODE);
//							resp.setMsg("成功导入" + list.size() + "条数据");
//							List<LeaveUploadEmployeeInfoData> lists = new ArrayList<LeaveUploadEmployeeInfoData>();
//							lists = moreLeaveUploadEmployeeInfoMapper.selectUploadEmployeeInfoList(map);
//							resp.setData(lists);
//						}
//					}else{
//						resp.setCode("4001");
//						resp.setMsg("导入数据单位不同，请重新选择");
//					}
//				} else {
//					resp.setCode(Constant.SUCCESS_CODE);
//					resp.setMsg("0");
//				}
//			} else {
//				resp.setCode(Constant.ERROR_CODE);
//				resp.setMsg(tmpPath);
//			}
//
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		} finally {
//			if (inputStream != null) {
//				inputStream.close();
//			}
//		}
//		return resp;
//	}
//}
