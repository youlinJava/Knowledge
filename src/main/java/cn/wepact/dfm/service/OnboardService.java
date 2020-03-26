//package cn.wepact.dfm.service;
//
//import cn.wepact.dfm.DTO.MailDataDTO;
//import cn.wepact.dfm.DTO.SmsDTO;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.entity.*;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.mapper.*;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.dto.*;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.excel.*;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.excel.util.util.ExcelImportUtil;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.mapper.MoreTOnboardMapper;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.util.*;
//import cn.wepact.dfm.account.client.OrgFeignClient;
//import cn.wepact.dfm.account.entity.MoreUser;
//import cn.wepact.dfm.common.model.Pagination;
//import cn.wepact.dfm.common.util.BeanCopyUtils;
//import cn.wepact.dfm.common.util.GeneralRespBean;
//import cn.wepact.dfm.common.util.JsonUtil;
//import cn.wepact.dfm.common.util.StringUtils;
//import cn.wepact.dfm.feign.NotificationServiceFeignClient;
//import cn.wepact.dfm.org.entity.Dictionary;
//import cn.wepact.dfm.org.entity.*;
//import cn.wepact.dfm.org.feign.OrgClient;
//import cn.wepact.dfm.ticket.api.DTO.TicketDTO;
//import cn.wepact.dfm.workflow.api.entity.WorkflowProcessInstanceDTO;
//import cn.wepact.dfm.workflow.api.entity.WorkflowTaskDTO;
//import cn.wepact.dfm.workflow.api.entity.WorkflowTaskDefinitionDTO;
//import cn.wepact.dfm.workflow.api.feign.WorkflowFeignClient;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.multipart.MultipartFile;
//import tk.mybatis.mapper.entity.Example;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.constraints.NotNull;
//import java.io.*;
//import java.lang.reflect.InvocationTargetException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.nio.file.Files;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import static cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.util.DictionaryUtil.*;
//import static cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.util.TicketMongo.TICKET_COLLECTION_NAME;
//import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
//
///**
// * @author wangbin
// */
//@Service
//@Slf4j
//public class OnboardService {
//
//    @Resource
//    private RedisTemplate<String, Integer> redisTemplate;
//
//    @Resource
//    private TOnboardBaseMapper baseMapper;
//    @Resource
//    private TOnboardEmployeeInfoMapper employeeInfoMapper;
//    @Resource
//    private TOnboardJobInfoMapper jobInfoMapper;
//    @Resource
//    private TOnboardHireInfoMapper hireInfoMapper;
//    @Resource
//    private TOnboardLaborContractMapper laborContractMapper;
//
//
//    @Resource
//    private MoreTOnboardMapper moreOnboardMapper;
//    @Resource
//    private TOnboardApplyMapper applyMapper;
//
//    @Resource
//    private WorkflowFeignClient workflowFeignClient;
//
//    @Resource
//    private OnboardTicketService onboardTicketService;
//    @Resource
//    private OrgFeignClient orgFeignClient;
//    @Resource
//    private OrgClient orgClient;
//
//    @Resource
//    private AuthorizationUtil authorizationUtil;
//    @Resource
//    private NotificationServiceFeignClient notificationServiceFeignClient;
//
//    @Resource
//    private OnboardMailService onboardMailService;
//    @Value("${root.url}")
//    private String rootUrl;
//    @Resource
//    private OnboardWorkflowService onboardWorkflowService;
//    @Resource
//    private ExcelService excelService;
//    @Resource
//    private TicketMongo ticketMongo;
//    @Resource
//    private MongoTemplate mongotemplate;
//    @Resource
//    private FileService fileService;
//
//    /**
//     * 新建入职流程
//     *
//     * @param dto 入职信息实体
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public OnboardApplyDTO newOnboardApply(OnboardDTO dto) throws JsonProcessingException, InterruptedException {
//        log.info("newOnboardApply-新建入职流程-start");
//        log.info("newOnboardApply-dto json: {}", JsonUtil.toJson(dto));
//        String departmentCode = dto.getJobInfo().getPostCode();
//        log.info("newOnboardApply-岗位code: {}", departmentCode);
//        MoreUser user = authorizationUtil.getUser();
//        log.info("newOnboardApply-当前请求用户信息:{}", JsonUtil.toJson(user));
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//        log.info("newOnboardApply-getOrgName: {}", orgOrg.getOrgName());
//        log.info("newOnboardApply-getOrgCode: {}", orgOrg.getOrgCode());
//        log.info("newOnboardApply-getPositionName: {}", deptOrg.getPositionName());
//        log.info("newOnboardApply-getPositionId: {}", deptOrg.getPositionId());
//        Boolean isModeDept = orgFeignClient.isModeDept(orgOrg.getOrgCode()).getData();
//        log.info("newOnboardApply-是否是板块单位:{}", isModeDept);
//        if (isModeDept == null) {
//            throw new RuntimeException("找不到板块信息");
//        }
//        // 调用工作流模块，新建流程
//        WorkflowProcessInstanceDTO processInstance = onboardWorkflowService.callWorkflowNewProcess(isModeDept, orgOrg.getOrgCode(), user.getUserAccount());
//
//        // 保存入职申请
//        TOnboardApply apply = saveApply(dto, orgOrg, user.getUserName(), user.getUserAccount(), processInstance.getId());
//        // 查询保存后的申请
//        apply = applyMapper.selectByPrimaryKey(apply.getId());
//        // 保存员工详细信息
//        saveEmployeeInfo(dto, apply.getId());
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "通过");
//        // 完成当前节点
//        onboardWorkflowService.completeCurrentTask(user.getUserAccount(), processInstance.getId(), null);
//        // 自动审批节点
//        autoComplete(user.getUserAccount(), processInstance.getId(), param, user.getUserAccount());
//        // 保存节点状态并发送提醒
//        completeOther(apply, processInstance.getId(), orgOrg);
//
//        log.info("newOnboardApply-新建入职流程-end");
//        return BeanCopyUtils.copy(apply, OnboardApplyDTO.class);
//    }
//
//    public OnboardApplyDTO submit(OnboardDTO dto) throws JsonProcessingException, InterruptedException {
//        log.info("submit-新建入职流程-start");
//        log.info("submit-dto json: {}", JsonUtil.toJson(dto));
//        String departmentCode = null;
//        if (!ObjectUtils.isEmpty(dto.getJobInfo())) {
//            departmentCode = dto.getJobInfo().getPostCode();
//
//        }
//        log.info("submit-岗位code: {}", departmentCode);
//        MoreUser user = authorizationUtil.getUser();
//        log.info("submit-当前请求用户信息:{}", JsonUtil.toJson(user));
//        Org orgOrg = null;
//        Org deptOrg = null;
//        if (!ObjectUtils.isEmpty(departmentCode)) {
//            orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//            if (!ObjectUtils.isEmpty(orgOrg)) {
//                log.info("submit-getOrgName: {}", orgOrg.getOrgName());
//                log.info("submit-getOrgCode: {}", orgOrg.getOrgCode());
//            }
//            deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//            if (!ObjectUtils.isEmpty(deptOrg)) {
//                log.info("submit-getPositionName: {}", deptOrg.getPositionName());
//                log.info("submit-getPositionId: {}", deptOrg.getPositionId());
//            }
//        }
//        // 保存入职申请
//        TOnboardApply apply = saveApply(dto, orgOrg, user.getUserName(), user.getUserAccount(), null);
//        // 查询保存后的申请
//        apply = applyMapper.selectByPrimaryKey(apply.getId());
//        moreOnboardMapper.changeApplyStatusById(DICTIONARY_VALUE_APPLY_STATUS_6, apply.getId());
//        // 保存时没有提交日期
//        moreOnboardMapper.removeApplyTime(apply.getId());
//        // 保存员工详细信息
//        saveEmployeeInfo(dto, apply.getId());
//        return BeanCopyUtils.copy(apply, OnboardApplyDTO.class);
//    }
//
//    void completeOther(TOnboardApply apply, String processInstanceId, Org orgOrg) throws JsonProcessingException {
//        // 获取下一节点审批人
//        List<MoreUser> userList = onboardWorkflowService.getNextNodeUserList(processInstanceId);
//        // 获取当前处理人
//        String currentUser = getCurrentUser(userList);
//
//        // 修改当前处理人
//        this.changeCurrentUser(processInstanceId, currentUser);
//        List<Dictionary> dictionaries = orgClient.getDictionaryAll();
//        // 发送审批通知
//        this.sendMessage(userList, currentUser, apply, orgOrg, dictionaries);
//        // 修改申请的更新时间
//        moreOnboardMapper.changeUpdateTimeByInstanceId(processInstanceId);
//    }
//
//    /**
//     * 保存入职申请（单条）
//     */
//    private TOnboardApply saveApply(OnboardDTO dto, Org orgOrg, String applyUser, String createUser, String instanceId) throws JsonProcessingException, InterruptedException {
//        log.info("saveApply-保存入职申请（单条）-start");
//        TOnboardApply apply = new TOnboardApply();
//        if (!ObjectUtils.isEmpty(dto) && !ObjectUtils.isEmpty(dto.getEmployeeInfo())) {
//            apply.setApplyName(dto.getEmployeeInfo().getName() + "的入职申请");
//            apply.setDelegateName(dto.getEmployeeInfo().getName());
//        }
//        apply.setPersonNumber(1);
//        insertApply(apply, orgOrg, applyUser, createUser, instanceId);
//        log.info("saveApply-保存入职申请（单条）-end");
//        return apply;
//    }
//
//    private void insertApply(TOnboardApply apply, Org orgOrg, String applyUser, String createUser, String instanceId) throws JsonProcessingException, InterruptedException {
//        apply.setCreateBy(createUser);
//        if (!ObjectUtils.isEmpty(orgOrg)) {
//            apply.setUnit(orgOrg.getOrgName());
//            apply.setUnitCode(orgOrg.getOrgCode());
//        } else {
//            apply.setUnit(null);
//            apply.setUnitCode(null);
//        }
//        apply.setApplyTime(new Date());
//        apply.setApplyUser(applyUser);
//        apply.setWorkflowInstanceId(instanceId);
//        apply.setApplyStatus(DICTIONARY_VALUE_APPLY_STATUS_1);
//        apply = updateApplyNumber(apply);
//        log.info("即将提交apply:{}", JsonUtil.toJson(apply));
//        applyMapper.insertSelective(apply);
//    }
//
//    private void saveEmployeeInfo(OnboardDTO dto, Integer applyId) throws JsonProcessingException {
//        TOnboardBase basic = dto.getBasic();
//        if (ObjectUtils.isEmpty(basic)) {
//            basic = new TOnboardBase();
//        }
//        basic.setApplyId(applyId);
//        log.info("即将提交用户基础数据base:{}", JsonUtil.toJson(basic));
//        baseMapper.insertSelective(basic);
//        TOnboardEmployeeInfo employeeInfo = dto.getEmployeeInfo();
//        if (!ObjectUtils.isEmpty(employeeInfo)) {
//            employeeInfo.setBaseId(basic.getId());
//            GeneralRespBean<EmpLeaveInfo> grb = orgClient.getEmpByIdCard(employeeInfo.getIdNumber());
//            EmpLeaveInfo data = grb.getData();
//            if (!ObjectUtils.isEmpty(data)) {
//                log.info("saveEmployeeInfo-找到员工工号,身份证:{}", employeeInfo.getIdNumber());
//                log.info("saveEmployeeInfo-找到员工工号,工号:{}", data.getEmployee_number());
//                employeeInfo.setJobNumber(data.getEmployee_number());
//            } else {
//                log.info("saveEmployeeInfo-没找到员工工号,身份证:{}", employeeInfo.getIdNumber());
//            }
//            log.info("即将提交用户信息数据:{}", JsonUtil.toJson(employeeInfo));
//            employeeInfoMapper.insertSelective(employeeInfo);
//        }
//        TOnboardJobInfo jobInfo = dto.getJobInfo();
//        String departmentCode = dto.getJobInfo().getPostCode();
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//        if (!ObjectUtils.isEmpty(jobInfo)) {
//            MorePosition position = orgClient.getPositionByCode(String.valueOf(deptOrg.getPositionId())).getData();
//            jobInfo.setJobName(position.getJob_name());
//            if (!ObjectUtils.isEmpty(deptOrg)) {
//                jobInfo.setPostCode(String.valueOf(deptOrg.getPositionId()));
//                jobInfo.setPost(deptOrg.getPositionName());
//            } else {
//                jobInfo.setPostCode(null);
//                jobInfo.setPost(null);
//            }
//            if (!ObjectUtils.isEmpty(orgOrg)) {
//                jobInfo.setUnit(orgOrg.getOrgName());
//                jobInfo.setUnitCode(orgOrg.getOrgCode());
//            } else {
//                jobInfo.setUnit(null);
//                jobInfo.setUnitCode(null);
//            }
//            jobInfo.setBaseId(basic.getId());
//            if (!ObjectUtils.isEmpty(orgOrg)) {
//                jobInfo.setOrganizationId(orgOrg.getOrganizationId());
//            }
//            log.info("即将提交岗位信息数据:{}", JsonUtil.toJson(jobInfo));
//            jobInfoMapper.insertSelective(jobInfo);
//        }
//        TOnboardHireInfo hireInfo = dto.getHireInfo();
//        if (!ObjectUtils.isEmpty(hireInfo)) {
//            hireInfo.setBaseId(basic.getId());
//            log.info("即将提交协议信息数据:{}", JsonUtil.toJson(hireInfo));
//            hireInfoMapper.insertSelective(hireInfo);
//        }
//        TOnboardLaborContract laborContract = dto.getLaborContract();
//        if (!ObjectUtils.isEmpty(laborContract)) {
//            laborContract.setBaseId(basic.getId());
//            log.info("即将提交合同信息数据:{}", JsonUtil.toJson(laborContract));
//            laborContractMapper.insertSelective(laborContract);
//        }
//    }
//
//    Pagination<OnboardApplyDTO> getOnboardApplyList(Pagination<OnboardApplyDTO> pagination) {
//        log.info("申请列表-getOnboardApplyList-start");
//        String userNo = authorizationUtil.getUserNo();
//        log.info("用户account:{}", userNo);
//        pagination.getParameterMap().put("createBy", userNo);
//        List<OnboardApplyDTO> onboardInfoList = moreOnboardMapper.getOnboardApplyList(pagination);
//        for (OnboardApplyDTO onboardApply : onboardInfoList) {
//            if (DICTIONARY_VALUE_APPLY_STATUS_2.equals(onboardApply.getApplyStatus())) {
//                onboardApply.setRelatedWorkOrderStatus(onboardTicketService.checkTicketStausByApply(onboardApply.getId()));
//            }
//        }
//        long onboardInfoCount = moreOnboardMapper.getOnboardApplyCount(pagination);
//        pagination.getParameterMap().remove("createBy");
//        log.info("申请列表-getOnboardApplyList-end");
//        return pagination.setResult(onboardInfoList).setTotalCount(onboardInfoCount);
//    }
//
//    /**
//     * 下载批量导入模版
//     */
//    ResponseEntity<org.springframework.core.io.Resource> downloadTemplate() throws IOException {
//        String fileName = "excel/template.zip";
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("入职信息导入模板.zip", "utf-8"));
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        org.springframework.core.io.Resource resource = new ClassPathResource(fileName);
//        InputStream is = resource.getInputStream();
//        InputStreamResource isr = new InputStreamResource(is);
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(isr);
//    }
//
//    /**
//     * 批量导入
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public ExcelResultDTO uploadTemplate(MultipartFile file, String applyId) throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, ParseException {
//        log.info("批量入职申请-uploadTemplate-start");
//        List<ExcelError> errorList = new ArrayList<>();
//        List list = ExcelImportUtil.importExcel(file, errorList);
//        // 校验excel
//        String errorId = excelService.checkAll(list, applyId, errorList);
//        log.info("Excel校验结束，校验结果:{}", ObjectUtils.isEmpty(errorId));
//        if (ObjectUtils.isEmpty(errorId)) {
//            // 替换特殊字段（比如数据字典字段，需要关联查询的字段等）
//            List<Object> objects = this.replenishMissingFields(list);
//            log.info("批量入职申请-uploadTemplate-end");
//            return new ExcelResultDTO().success(objects);
//        } else {
//            log.info("批量入职申请-uploadTemplate-end");
//            return new ExcelResultDTO().error(errorId);
//        }
//    }
//
//    private List<Object> replenishMissingFields(List<Object> objects) {
//        log.info("replenishMissingFields-将excel中的汉字转换成字典码");
//        List<Dictionary> dictionaries = orgClient.getDictionaryAll();
//        if (ObjectUtils.isEmpty(objects)) {
//            return new ArrayList<>();
//        }
//
//        return objects.stream().peek(s -> {
//            ExcelDTO dto = (ExcelDTO) s;
//            String departmentCode = dto.getPost();
//            Org orgOrg = orgClient.getOrgByPositionCode2(departmentCode).getData();
//            Org deptOrg = orgClient.getDeptByPositionCode2(departmentCode).getData();
//            MorePosition position = orgClient.getPositionByCode(String.valueOf(deptOrg.getPositionId())).getData();
//            String joinWay = this.getIdFromDictionary(dictionaries, dto.getJoinWay(), TYPE_NAME_EMPLOYEE_CATG);
//            dto.setJoinWay(joinWay);
//            dto.setIsBadJob(DictionaryUtil.getTrueOrFalseId(dto.getIsBadJob()));
//            dto.setUnit(orgOrg.getOrgName());
//            dto.setUnitCode(orgOrg.getOrgCode());
//            dto.setPost(deptOrg.getPositionName());
//            dto.setPostCode(String.valueOf(deptOrg.getPositionId()));
//            dto.setDepartment(deptOrg.getOrgName());
//            dto.setDepartmentCode(deptOrg.getOrgCode());
//            dto.setOrganizationId(orgOrg.getOrganizationId());
//            dto.setEbsCallbackTime(((ExcelDTO) s).getEbsCallbackTime());
//
//            String directLeaderName = dto.getDirectLeaderName();
//            if (ObjectUtils.isEmpty(directLeaderName)) {
//                GeneralRespBean<EmpInfo> employee = orgClient.getEmpByEmployeeNumber(dto.getDirectLeaderNumber());
//                EmpInfo data = employee.getData();
//                if (ObjectUtils.isEmpty(data)) {
//                    log.info("replenishMissingFields-EmpInfo is empty");
//                } else if (ObjectUtils.isEmpty(data.getLast_name())) {
//                    log.info("replenishMissingFields-EmpInfo getLast_name is empty");
//                } else {
//                    log.info("replenishMissingFields-直接领导姓名为空，读取getEmpByEmployeeNumber获得");
//                    dto.setDirectLeaderName(data.getLast_name());
//                }
//            }
//
//            if (!ObjectUtils.isEmpty(position)) {
//                dto.setPersonnelRank(position.getGrade_name());
//                dto.setPosNum(position.getPos_num());
//                dto.setOnJobNum(String.valueOf(position.getOn_jod_num()));
//            }
//
//            if (s instanceof FinishedTalent) {
//                FinishedTalent talent = (FinishedTalent) s;
//                talent.setEmployeeType("1");
//                String employeeForm = this.getIdFromDictionary(dictionaries, talent.getEmployeeForm(), TYPE_NAME_CUX_DFM_HIRE_WAY);
//                talent.setEmployeeForm(employeeForm);
//                talent.setGender(DictionaryUtil.getGenderId(talent.getGender()));
//                talent.setIsChineseExperts(DictionaryUtil.getTrueOrFalseId(talent.getIsChineseExperts()));
//                talent.setIsOverseasTalents(DictionaryUtil.getTrueOrFalseId(talent.getIsOverseasTalents()));
//                String contractType = this.getIdFromDictionary(dictionaries, talent.getContractType(), "CONTRACT_TYPE");
//                talent.setContractType(contractType);
//                String academicDegree = this.getIdFromDictionary(dictionaries, talent.getAcademicDegree(), TYPE_NAME_DFL_DEGREE);
//                talent.setAcademicDegree(academicDegree);
//                String education = this.getIdFromDictionary(dictionaries, talent.getEducation(), TYPE_NAME_DFL_HIGH_EDU_LEVEL);
//                talent.setEducation(education);
//            }
//            if (s instanceof GraduatedStudent) {
//                GraduatedStudent talent = (GraduatedStudent) s;
//                talent.setEmployeeType("2");
//                String academicDegree = this.getIdFromDictionary(dictionaries, talent.getAcademicDegree(), TYPE_NAME_DFL_DEGREE);
//                talent.setAcademicDegree(academicDegree);
//                String education = this.getIdFromDictionary(dictionaries, talent.getEducation(), TYPE_NAME_DFL_HIGH_EDU_LEVEL);
//                talent.setEducation(education);
//                String contractType = this.getIdFromDictionary(dictionaries, talent.getContractType(), "CONTRACT_TYPE");
//                talent.setContractType(contractType);
//                talent.setGender(DictionaryUtil.getGenderId(talent.getGender()));
//            }
//            if (s instanceof RehireTalent) {
//                RehireTalent talent = (RehireTalent) s;
//                talent.setEmployeeType("3");
//                String rehireType = this.getIdFromDictionary(dictionaries, talent.getRehireType(), "CUX_DFMHQ_WF_REHTYPE");
//                talent.setRehireType(rehireType);
//                talent.setGender(DictionaryUtil.getGenderId(talent.getGender()));
//                talent.setIsRehire(DictionaryUtil.getTrueOrFalseId(talent.getIsRehire()));
//                String employeeTypeDesc = this.getIdFromDictionary(dictionaries, talent.getEmployeeTypeDesc(), "CUX_PERSON_TYPE_EXP");
//                talent.setEmployeeTypeDesc(employeeTypeDesc);
//            }
//            if (s instanceof ForeignTalent) {
//                ForeignTalent talent = (ForeignTalent) s;
//                talent.setEmployeeType("4");
//                String employeeTypeDesc = this.getIdFromDictionary(dictionaries, talent.getEmployeeTypeDesc(), "CUX_PERSON_TYPE_EXP");
//                talent.setEmployeeTypeDesc(employeeTypeDesc);
//                talent.setGender(DictionaryUtil.getGenderId(talent.getGender()));
//            }
//        }).collect(Collectors.toList());
//    }
//
//    public boolean submitEdit(OnboardDetailDTO detail) throws JsonProcessingException {
//        log.info("submitEdit-编辑申请中的保存按钮-start");
//        log.info("参数:{}", JsonUtil.toJson(detail));
//        TOnboardApply apply = detail.getApply();
//        OnboardDTO mainDto = detail.getList().get(0);
//
//        OnboardDTO onboardDTO = detail.getList().get(0);
//        String departmentCode;
//        Org orgOrg = null;
//        Org deptOrg = null;
//        if (!ObjectUtils.isEmpty(onboardDTO) && !ObjectUtils.isEmpty(onboardDTO.getJobInfo()) && !ObjectUtils.isEmpty(onboardDTO.getJobInfo().getPostCode())) {
//            departmentCode = onboardDTO.getJobInfo().getPostCode();
//            log.info("newOnboardApply-岗位code: {}", departmentCode);
//            orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//            if (!ObjectUtils.isEmpty(orgOrg)) {
//                log.info("newOnboardApply-getOrgName: {}", orgOrg.getOrgName());
//                log.info("newOnboardApply-getOrgCode: {}", orgOrg.getOrgCode());
//            }
//            deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//            if (!ObjectUtils.isEmpty(deptOrg)) {
//                log.info("newOnboardApply-getPositionName: {}", deptOrg.getPositionName());
//                log.info("newOnboardApply-getPositionCode: {}", deptOrg.getPositionId());
//            }
//        }
//        // 保存入职申请
//        updateApply(apply, mainDto, apply.getWorkflowInstanceId(), null, orgOrg);
//        moreOnboardMapper.changeApplyStatusById(DICTIONARY_VALUE_APPLY_STATUS_6, apply.getId());
//        // 保存详细数据
//        saveEmployeeInfoList(detail.getList(), orgOrg, deptOrg);
//        log.info("submitEdit-编辑申请中的保存按钮-end");
//        return true;
//    }
//
//    /**
//     * 批量导入
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public OnboardApplyDTO submitExcel(List<StandardDTO> excelData) throws JsonProcessingException, InterruptedException {
//        log.info("submitExcel-批量提交入职申请-submitExcel-start");
//        StandardDTO standardDTO = excelData.get(0);
//
//        String departmentCode = standardDTO.getPostCode();
//        log.info("submitExcel-岗位code:{}", departmentCode);
//        MoreUser user = authorizationUtil.getUser();
//        log.info("当前请求用户信息:{}", JsonUtil.toJson(user));
//
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//        log.info("submitExcel-getOrgCode:{}", orgOrg.getOrgCode());
//        log.info("submitExcel-getOrgName:{}", orgOrg.getOrgName());
//        log.info("submitExcel-getPositionId:{}", orgOrg.getPositionId());
//        log.info("submitExcel-getPositionName:{}", orgOrg.getPositionName());
//        Boolean isModeDept = orgFeignClient.isModeDept(orgOrg.getOrgCode()).getData();
//        log.info("submitExcel-是否是板块单位:{}", isModeDept);
//        if (isModeDept == null) {
//            throw new RuntimeException("找不到板块信息");
//        }
//        // 调用工作流模块，新建流程
//        WorkflowProcessInstanceDTO processInstance = onboardWorkflowService.callWorkflowNewProcess(isModeDept, orgOrg.getOrgCode(), user.getUserAccount());
//
//        // 保存入职申请
//        TOnboardApply apply = saveApplyList(excelData, orgOrg, user.getUserName(), user.getUserAccount(), processInstance.getId());
//        // 查询保存后的申请
//        apply = applyMapper.selectByPrimaryKey(apply.getId());
//        // 保存员工详细信息
//        saveEmployeeInfoList(excelData, apply.getId());
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "通过");
//        // 完成当前节点
//        onboardWorkflowService.completeCurrentTask(user.getUserAccount(), processInstance.getId(), null);
//        // 自动审批节点
//        autoComplete(user.getUserAccount(), processInstance.getId(), param, user.getUserAccount());
//        // 保存节点状态并发送提醒
//        completeOther(apply, processInstance.getId(), orgOrg);
//
//        log.info("submitExcel-批量提交入职申请-submitExcel-end");
//        return BeanCopyUtils.copy(apply, OnboardApplyDTO.class);
//    }
//
//    /**
//     * 批量修改
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public boolean editTemplate(String applyId, List<StandardDTO> excelData) throws JsonProcessingException {
//        log.info("editTemplate-批量修改-editTemplate-start");
//        log.info("editTemplate-applyId:{}", applyId);
//        TOnboardApply apply = applyMapper.selectByPrimaryKey(applyId);
//        StandardDTO standardDTO = excelData.get(0);
//
//        String departmentCode = standardDTO.getPostCode();
//        log.info("submitExcel-岗位code:{}", departmentCode);
//        MoreUser user = authorizationUtil.getUser();
//        log.info("当前请求用户信息:{}", JsonUtil.toJson(user));
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        log.info("submitExcel-getOrgCode:{}", orgOrg.getOrgCode());
//        log.info("submitExcel-getOrgName:{}", orgOrg.getOrgName());
//        log.info("submitExcel-getPositionId:{}", orgOrg.getPositionId());
//        log.info("submitExcel-getPositionName:{}", orgOrg.getPositionName());
//        Boolean isModeDept = orgFeignClient.isModeDept(orgOrg.getOrgCode()).getData();
//        log.info("newOnboardApply-是否是板块单位:{}", isModeDept);
//        if (isModeDept == null) {
//            throw new RuntimeException("找不到板块信息");
//        }
//
//        String processInstanceId = apply.getWorkflowInstanceId();
//        if (ObjectUtils.isEmpty(processInstanceId)) {
//            WorkflowProcessInstanceDTO processInstance = onboardWorkflowService.callWorkflowNewProcess(isModeDept, orgOrg.getOrgCode(), user.getUserAccount());
//            processInstanceId = processInstance.getId();
//        }
//
//        // 保存入职申请
//        updateApply(excelData, apply, processInstanceId, orgOrg);
//        // 查询保存后的申请
//        apply = applyMapper.selectByPrimaryKey(apply.getId());
//        // 保存详细数据
//        saveEmployeeInfoList(excelData, apply.getId());
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "通过");
//        // 完成当前节点
//        onboardWorkflowService.completeCurrentTask(user.getUserAccount(), processInstanceId, null);
//        // 自动审批节点
//        autoComplete(user.getUserAccount(), processInstanceId, param, user.getUserAccount());
//        // 保存节点状态并发送提醒
//        completeOther(apply, processInstanceId, orgOrg);
//
//        log.info("editTemplate-批量修改-editTemplate-start");
//        return true;
//    }
//
//    private void updateApply(List<StandardDTO> excelData, TOnboardApply apply, String processInstanceId, Org orgOrg) throws JsonProcessingException {
//        log.info("更新入职申请信息-updateEmployee-start");
//        if (!ObjectUtils.isEmpty(apply.getId())) {
//            log.info("通过applyId({})删除Employee,Job,Hire,Base", apply.getId());
//            moreOnboardMapper.deleteEmployeeByApplyId(apply.getId());
//            moreOnboardMapper.deleteJobByApplyId(apply.getId());
//            moreOnboardMapper.deleteHireByApplyId(apply.getId());
//            moreOnboardMapper.deleteLaborContractByApplyId(apply.getId());
//            moreOnboardMapper.deleteBaseByApplyId(apply.getId());
//        }
//        StandardDTO mainDto = excelData.get(0);
//        if (excelData.size() > 1) {
//            apply.setApplyName(mainDto.getName() + "等" + excelData.size() + "人的入职申请");
//        }
//        if (excelData.size() == 1) {
//            apply.setApplyName(mainDto.getName() + "的入职申请");
//        }
//        apply.setDelegateName(mainDto.getName());
//        apply.setPersonNumber(excelData.size());
//
//        apply.setApplyStatus(DICTIONARY_VALUE_APPLY_STATUS_1);
//        apply.setUnit(orgOrg.getOrgName());
//        apply.setUnitCode(orgOrg.getOrgCode());
//        apply.setApplyTime(new Date());
//        if (!ObjectUtils.isEmpty(processInstanceId)) {
//            apply.setWorkflowInstanceId(processInstanceId);
//        }
//        log.info("即将更新apply表:{}", JsonUtil.toJson(apply));
//        applyMapper.updateByPrimaryKeySelective(apply);
//
//        log.info("更新入职申请信息-updateEmployee-end");
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean editEmployee(OnboardDetailDTO detail) throws JsonProcessingException {
//        log.info("编辑申请（单条）-editEmployee-start");
//        log.info("参数:{}", JsonUtil.toJson(detail));
//        TOnboardApply apply = detail.getApply();
//        OnboardDTO mainDto = detail.getList().get(0);
//        String departmentCode = mainDto.getJobInfo().getPostCode();
//        log.info("submitExcel-岗位code:{}", departmentCode);
//        MoreUser user = authorizationUtil.getUser();
//        log.info("当前请求用户信息:{}", JsonUtil.toJson(user));
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//        log.info("newOnboardApply-getOrgName: {}", orgOrg.getOrgName());
//        log.info("newOnboardApply-getOrgCode: {}", orgOrg.getOrgCode());
//        log.info("newOnboardApply-getPositionName: {}", deptOrg.getPositionName());
//        log.info("newOnboardApply-getPositionId: {}", deptOrg.getPositionId());
//        Boolean isModeDept = orgFeignClient.isModeDept(orgOrg.getOrgCode()).getData();
//        log.info("newOnboardApply-是否是板块单位:{}", isModeDept);
//        if (isModeDept == null) {
//            throw new RuntimeException("找不到板块信息");
//        }
//
//        String processInstanceId = apply.getWorkflowInstanceId();
//        if (ObjectUtils.isEmpty(processInstanceId)) {
//            WorkflowProcessInstanceDTO processInstance = onboardWorkflowService.callWorkflowNewProcess(isModeDept, orgOrg.getOrgCode(), user.getUserAccount());
//            processInstanceId = processInstance.getId();
//        }
//
//        // 保存入职申请
//        updateApply(apply, mainDto, processInstanceId, new Date(), orgOrg);
//        // 保存详细数据
//        saveEmployeeInfoList(detail.getList(), orgOrg, deptOrg);
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "通过");
//        // 完成当前节点
//        onboardWorkflowService.completeCurrentTask(user.getUserAccount(), processInstanceId, null);
//        // 自动审批节点
//        autoComplete(user.getUserAccount(), processInstanceId, param, user.getUserAccount());
//        // 保存节点状态并发送提醒
//        completeOther(apply, processInstanceId, orgOrg);
//
//        log.info("编辑申请（单条）-editEmployee-end");
//        return true;
//    }
//
//    private void saveEmployeeInfoList(List<OnboardDTO> list, Org orgOrg, Org deptOrg) throws JsonProcessingException {
//        MorePosition position = orgClient.getPositionByCode(String.valueOf(deptOrg.getPositionId())).getData();
//        for (OnboardDTO onboardDTO : list) {
//            TOnboardBase basic = onboardDTO.getBasic();
//            log.info("saveEmployeeInfoList-即将插入base表:{}", JsonUtil.toJson(basic));
//            baseMapper.updateByPrimaryKey(basic);
//            TOnboardEmployeeInfo employeeInfo = onboardDTO.getEmployeeInfo();
//            log.info("saveEmployeeInfoList-即将插入employeeInfo表:{}", JsonUtil.toJson(employeeInfo));
//            employeeInfoMapper.updateByPrimaryKey(employeeInfo);
//            TOnboardJobInfo jobInfo = onboardDTO.getJobInfo();
//            jobInfo.setJobName(position.getJob_name());
//            if (!ObjectUtils.isEmpty(deptOrg)) {
//                jobInfo.setPostCode(String.valueOf(deptOrg.getPositionId()));
//                jobInfo.setPost(deptOrg.getPositionName());
//            } else {
//                jobInfo.setPostCode(null);
//                jobInfo.setPost(null);
//            }
//            if (!ObjectUtils.isEmpty(orgOrg)) {
//                jobInfo.setUnit(orgOrg.getOrgName());
//                jobInfo.setUnitCode(orgOrg.getOrgCode());
//            } else {
//                jobInfo.setUnit(null);
//                jobInfo.setUnitCode(null);
//            }
//            log.info("saveEmployeeInfoList-即将插入jobInfo表:{}", JsonUtil.toJson(jobInfo));
//            jobInfoMapper.updateByPrimaryKey(jobInfo);
//            TOnboardHireInfo hireInfo = onboardDTO.getHireInfo();
//            hireInfo.setBaseId(basic.getId());
//            log.info("saveEmployeeInfoList-即将插入hireInfo表:{}", JsonUtil.toJson(hireInfo));
//            hireInfoMapper.updateByPrimaryKey(hireInfo);
//            TOnboardLaborContract laborContract = onboardDTO.getLaborContract();
//            log.info("saveEmployeeInfoList-即将插入laborContract表:{}", JsonUtil.toJson(laborContract));
//            laborContractMapper.updateByPrimaryKey(laborContract);
//        }
//    }
//
//    private void updateApply(TOnboardApply apply, OnboardDTO mainDto, String processInstanceId, Date applyTime, Org orgOrg) throws JsonProcessingException {
//        apply.setApplyName(mainDto.getEmployeeInfo().getName() + "的入职申请");
//        apply.setDelegateName(mainDto.getEmployeeInfo().getName());
//        apply.setPersonNumber(1);
//        if (!ObjectUtils.isEmpty(orgOrg)) {
//            apply.setUnit(orgOrg.getOrgName());
//            apply.setUnitCode(orgOrg.getOrgCode());
//        } else {
//            apply.setUnit(null);
//            apply.setUnitCode(null);
//        }
//        apply.setApplyStatus(DICTIONARY_VALUE_APPLY_STATUS_1);
//        if (!ObjectUtils.isEmpty(applyTime)) {
//            apply.setApplyTime(applyTime);
//        }
//        if (!ObjectUtils.isEmpty(processInstanceId)) {
//            apply.setWorkflowInstanceId(processInstanceId);
//        }
//        log.info("即将插入apply表:{}", JsonUtil.toJson(apply));
//        applyMapper.updateByPrimaryKeySelective(apply);
//    }
//
//    /**
//     * 修改当前审批人
//     */
//    private void changeCurrentUser(String processInstanceId, String currentUser) throws JsonProcessingException {
//        log.info("changeCurrentUser-start");
//        // 修改当前处理人
//        Map<String, Object> param = new HashMap<>();
//        param.put("processInstanceId", processInstanceId);
//        param.put("currentUser", currentUser);
//        moreOnboardMapper.changeCurrentUser(param);
//        if (ObjectUtils.isEmpty(currentUser)) {
//            onboardWorkflowService.processHasBeenFinished(processInstanceId);
//        }
//        log.info("changeCurrentUser-end");
//    }
//
//    /**
//     * 查询下一节点审批人
//     */
//    public List<MoreUser> getNextCandidateUsers(String processInstanceId) throws JsonProcessingException {
//        log.info("getNextCandidateUsers-start");
//        List<String> userNames = workflowFeignClient.getNextCandidateUsers(processInstanceId);
//        log.info("userNames:{}", userNames);
//        List<MoreUser> userList = new ArrayList<>();
//        if (!ObjectUtils.isEmpty(userNames)) {
//            for (String userName : userNames) {
//                MoreUser user = orgFeignClient.getOrgRoleUserInfo(userName).getData();
//                userList.add(user);
//            }
//        }
//        log.info("userList:{}", JsonUtil.toJson(userList));
//        log.info("getNextCandidateUsers-end");
//        return userList;
//    }
//
//    /**
//     * 获取当前审批人
//     */
//    public String getCurrentUser(List<MoreUser> userList) throws JsonProcessingException {
//        log.info("getCurrentUser-start");
//        log.info("userList:{}", JsonUtil.toJson(userList));
//        String currentUser = null;
//        if (!ObjectUtils.isEmpty(userList)) {
//            currentUser = "";
//            for (MoreUser user : userList) {
//                String userName1 = user.getUserName();
//                currentUser += userName1 + ",";
//            }
//            currentUser = currentUser.substring(0, currentUser.length() - 1);
//        }
//        log.info("currentUser:{}", currentUser);
//        log.info("getCurrentUser-end");
//        return currentUser;
//    }
//
//    /**
//     * 发送通知邮件和短信
//     */
//    public void sendMessage(List<MoreUser> userList, String currentUser, TOnboardApply apply, Org orgOrg, List<Dictionary> dictionaries) {
//        log.info("sendMessage-发送通知消息-sendMessage-start");
//        // 获取当前审批人
//        for (MoreUser user : userList) {
//            //发送审批通知邮件
//            this.sendMail(apply, currentUser, dictionaries, user, orgOrg);
//            //发送审批通知短信
//            this.sendSms(apply, user);
//        }
//        log.info("sendMessage-发送通知消息-sendMessage-end");
//    }
//
//    /**
//     * 发送审批通知短信
//     */
//    private void sendSms(TOnboardApply apply, MoreUser user) {
//        try {
//            log.info("发送审批通知短信-sendSms-start");
//            if (ObjectUtils.isEmpty(user.getMobilePhone())) {
//                return;
//            }
//            SmsDTO smsDTO = new SmsDTO();
//            smsDTO.setSmsType("1");
//            smsDTO.setApprovalName(user.getUserName());
//            smsDTO.setApplyType("入职");
//            smsDTO.setApplyNumber(apply.getApplyNumber());
//            smsDTO.setApplyName(apply.getApplyName());
//            smsDTO.setPhone(user.getMobilePhone());
//            log.info("即将发送审批通知短信:{}", JsonUtil.toJson(smsDTO));
//            String result = notificationServiceFeignClient.sendSms(smsDTO);
//            log.info("短信发送结果:{}", result);
//            log.info("发送审批通知短信-sendSms-end");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            log.info("发送审批通知短信-sendSms-error");
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 发送审批通知邮件
//     */
//    private void sendMail(TOnboardApply apply, String currentUser, List<Dictionary> dictionaries, MoreUser user, Org orgOrg) {
//        try {
//            log.info("sendMail-发送审批通知邮件-sendMail-start");
//            log.info("sendMail-currentUser:{}", currentUser);
//            log.info("sendMail-apply:{}", JsonUtil.toJson(apply));
//            log.info("sendMail-user:{}", JsonUtil.toJson(user));
//            log.info("sendMail-getOrgCode:{}", orgOrg.getOrgCode());
//            log.info("sendMail-getOrgName:{}", orgOrg.getOrgName());
//            if (ObjectUtils.isEmpty(user.getEmail())) {
//                return;
//            }
//            MailDataDTO mailDataDTO = new MailDataDTO();
//            mailDataDTO.setMailType("1");
//            mailDataDTO.setTargetMailbox(Collections.singletonList(user.getEmail()));
//            mailDataDTO.setNo("1");
//            mailDataDTO.setAddressee(user.getUserName());
//            mailDataDTO.setApplyName(apply.getApplyName());
//            mailDataDTO.setCompany(apply.getUnit());
//            mailDataDTO.setApplyMan(apply.getApplyUser());
//            mailDataDTO.setApplyNumber(apply.getApplyNumber());
//            String applyState = this.getNameFromDictionary(dictionaries, apply.getApplyStatus());
//            mailDataDTO.setApplyState(applyState);
//            mailDataDTO.setCurrentHandleMan(currentUser);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
//            String submitTime = sdf.format(apply.getUpdateTime());
//            mailDataDTO.setSubmitTime(submitTime);
//            String uuid = onboardMailService.generateUuid(apply.getWorkflowInstanceId(), user.getUserAccount());
//            log.info("sendMail-生成redis uuid key:{}", uuid);
//            mailDataDTO.setUnit(orgOrg.getOrgName());
//            mailDataDTO.setPass(rootUrl + "/api/hrprocess-master/approval/" + uuid);
//            mailDataDTO.setFail(rootUrl + "/api/hrprocess-master/reject/" + uuid);
//            log.info("sendMail-即将发送邮件:{}", JsonUtil.toJson(mailDataDTO));
//            //发送邮件
//            String result = notificationServiceFeignClient.sendEmail(mailDataDTO);
//            log.info("sendMail-邮件发送结果:{}", result);
//            log.info("sendMail-发送审批通知邮件-sendMail-end");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            log.info("sendMail-发送审批通知邮件-sendMail-error");
//            e.printStackTrace();
//        }
//    }
//
//    private TOnboardApply saveApplyList(List<StandardDTO> excelData, Org orgOrg, String applyUser, String createUser, String instanceId) throws JsonProcessingException, InterruptedException {
//        log.info("saveApplyList-通过excel数据批量保存-start");
//        // 提交申请
//        TOnboardApply apply = new TOnboardApply();
//        StandardDTO mainDto = excelData.get(0);
//        if (excelData.size() > 1) {
//            apply.setApplyName(mainDto.getName() + "等" + excelData.size() + "人的入职申请");
//        }
//        if (excelData.size() == 1) {
//            apply.setApplyName(mainDto.getName() + "的入职申请");
//        }
//        apply.setDelegateName(mainDto.getName());
//        apply.setPersonNumber(excelData.size());
//        insertApply(apply, orgOrg, applyUser, createUser, instanceId);
//        log.info("saveApplyList-通过excel数据批量保存-end");
//        return apply;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public TOnboardApply updateApplyNumber(TOnboardApply apply) throws InterruptedException {
//        log.info("更新申请编号-updateApplyNumber-start");
//        /*
//        申请编号规则：
//        业务模块首字母缩写4位+业务类型首字母缩写2位+1位数字（申请为1）+8位日期（YYYYMMDD）+4位流水号
//        流水号是根据日期重置
//        例：RYGLRZ1201909230001(人员管理入职申请201909230001)
//         */
//        String buffer = "RYGLRZ1" + new SimpleDateFormat("yyyyMMdd").format(new Date()) +
//                String.format("%04d", getNumberFromRedis());
//        apply.setApplyNumber(buffer);
//        log.info("编号为:{}", apply.getApplyNumber());
//        applyMapper.updateByPrimaryKeySelective(apply);
//        log.info("更新申请编号-updateApplyNumber-end");
//        return apply;
//    }
//
//    private Integer getNumberFromRedis() throws InterruptedException {
//        String TEST_APPLY_NUMBER = "DFMC_RYGLRZ1_APPLY_NUMBER";
//        String TEST_APPLY_NUMBER_LOCK = TEST_APPLY_NUMBER + "_LOCK";
//        String dateString = new SimpleDateFormat("yyyyMMdd").format(new Date());
//        ValueOperations<String, Integer> operations = redisTemplate.opsForValue();
//        Integer number;
//        while (true) {
//            // 锁时间10秒上限
//            Boolean flag = operations.setIfAbsent(TEST_APPLY_NUMBER_LOCK, 1, 10, TimeUnit.SECONDS);
//            if (flag == null) {
//                throw new RuntimeException();
//            }
//            if (flag) {
//                //do something
//                number = operations.get(TEST_APPLY_NUMBER + dateString);
//                if (number == null) {
//                    number = 1;
//                } else {
//                    number += 1;
//                }
//                // 值时间24小时上限
//                operations.set(TEST_APPLY_NUMBER + dateString, number, 24, TimeUnit.HOURS);
//                //do something
//                redisTemplate.delete(TEST_APPLY_NUMBER_LOCK);
//                break;
//            }
//            Thread.sleep(500);
//        }
//        return number;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void saveEmployeeInfoList(List<StandardDTO> excelData, Integer applyId) throws JsonProcessingException {
//        log.info("保存入职申请详细信息-saveEmployeeInfoData-start");
//        for (StandardDTO dto : excelData) {
//            String departmentCode = dto.getPostCode();
//            Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//            Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//            TOnboardBase base = new TOnboardBase();
//            base.setEmployeeType(dto.getEmployeeType());
//            base.setJoinWay(dto.getJoinWay());
//            // 入职时间
//            base.setOnboardTime(dto.getOnboardTime());
//            // 录用形式
//            base.setEmployeeForm(dto.getEmployeeForm());
//            base.setApplyId(applyId);
//            base.setRehireType(dto.getRehireType());
//            base.setEmployeeTypeDesc(dto.getEmployeeTypeDesc());
//            base.setEbsCallbackTime(dto.getEbsCallbackTime());
//            log.info("即将保存base表:{}", JsonUtil.toJson(base));
//            baseMapper.insertSelective(base);
//            /* 入职人员信息表*/
//            TOnboardEmployeeInfo employeeInfo = BeanCopyUtils.copy(dto, TOnboardEmployeeInfo.class);
//            employeeInfo.setIdType(DICTIONARY_VALUE_ID_TYPE_1);
//            employeeInfo.setBaseId(base.getId());
//            if ("4".equals(dto.getEmployeeType())) {
//                employeeInfo.setIdType(DICTIONARY_VALUE_ID_TYPE_2);
//            }
//            employeeInfo.setRetireContinuousTime(dto.getRetireContinuousTime());
//            GeneralRespBean<EmpLeaveInfo> grb = orgClient.getEmpByIdCard(employeeInfo.getIdNumber());
//            EmpLeaveInfo data = grb.getData();
//            if (!ObjectUtils.isEmpty(data)) {
//                log.info("saveEmployeeInfoList-找到员工工号,身份证:{}", employeeInfo.getIdNumber());
//                log.info("saveEmployeeInfoList-找到员工工号,工号:{}", data.getEmployee_number());
//                employeeInfo.setJobNumber(data.getEmployee_number());
//            } else {
//                log.info("saveEmployeeInfoList-没找到员工工号,身份证:{}", employeeInfo.getIdNumber());
//            }
//            log.info("即将保存employeeInfo表:{}", JsonUtil.toJson(employeeInfo));
//            employeeInfoMapper.insertSelective(employeeInfo);
//            /* 入职岗位信息表 */
//            TOnboardJobInfo jobInfo = BeanCopyUtils.copy(dto, TOnboardJobInfo.class);
//            MorePosition position = orgClient.getPositionByCode(String.valueOf(deptOrg.getPositionId())).getData();
//            jobInfo.setJobName(position.getJob_name());
//            jobInfo.setPersonnelRank(dto.getPersonnelRank());
//            jobInfo.setPostCode(String.valueOf(deptOrg.getPositionId()));
//            jobInfo.setPost(deptOrg.getPositionName());
//            jobInfo.setDepartment(dto.getDepartment());
//            jobInfo.setDepartmentCode(dto.getDepartmentCode());
//            jobInfo.setUnit(orgOrg.getOrgName());
//            jobInfo.setUnitCode(orgOrg.getOrgCode());
//            jobInfo.setBaseId(base.getId());
//            jobInfo.setPosNum(Integer.valueOf(dto.getPosNum()));
//            jobInfo.setOnJodNum(Integer.valueOf(dto.getOnJobNum()));
//            jobInfo.setOrganizationId(orgOrg.getOrganizationId());
//            log.info("即将保存jobInfo表:{}", JsonUtil.toJson(jobInfo));
//            jobInfoMapper.insertSelective(jobInfo);
//            /* 入职聘用(协议)信息表 */
//            TOnboardHireInfo hireInfo = BeanCopyUtils.copy(dto, TOnboardHireInfo.class);
//            if (ObjectUtils.isEmpty(hireInfo.getStateReason())) {
//                hireInfo.setStateReason("订立-新签");
//            }
//            hireInfo.setBaseId(base.getId());
//            log.info("即将保存hireInfo表:{}", JsonUtil.toJson(hireInfo));
//            hireInfoMapper.insert(hireInfo);
//            /* 入职劳动合同信息表 */
//            TOnboardLaborContract laborContract = BeanCopyUtils.copy(dto, TOnboardLaborContract.class);
//            laborContract.setContractPeriodYear(StringUtils.toInt(dto.getContractPeriodYear()));
//            laborContract.setContractPeriodMonth(StringUtils.toInt(dto.getContractPeriodMonth()));
//            laborContract.setContractPeriodDay(StringUtils.toInt(dto.getContractPeriodDay()));
//            laborContract.setProbationPeriodMonth(StringUtils.toInt(dto.getProbationPeriodMonth()));
//            laborContract.setBaseId(base.getId());
//            if (ObjectUtils.isEmpty(laborContract.getStateReason())) {
//                laborContract.setStateReason("订立-新签");
//            }
//            if (ObjectUtils.isEmpty(laborContract.getContractPeriodYear())) {
//                laborContract.setContractPeriodYear(0);
//            }
//            if (ObjectUtils.isEmpty(laborContract.getContractPeriodMonth())) {
//                laborContract.setContractPeriodMonth(0);
//            }
//            if (ObjectUtils.isEmpty(laborContract.getContractPeriodDay())) {
//                laborContract.setContractPeriodDay(0);
//            }
//            if (ObjectUtils.isEmpty(laborContract.getProbationPeriodMonth())) {
//                laborContract.setProbationPeriodMonth(0);
//            }
//            log.info("即将保存laborContract表:{}", JsonUtil.toJson(laborContract));
//            laborContractMapper.insertSelective(laborContract);
//        }
//        log.info("保存入职申请详细信息-saveEmployeeInfoData-end");
//    }
//
//    private String getIdFromDictionary(List<Dictionary> dictionaries, String name, String type) {
//        if (StringUtils.isEmpty(name)) {
//            return null;
//        }
//        for (Dictionary dictionary : dictionaries) {
//            if (dictionary.getName().equals(name) && dictionary.getType().equals(type)) {
//                return dictionary.getCode();
//            }
//        }
//        return null;
//    }
//
//    private String getNameFromDictionary(List<Dictionary> dictionaries, String id) {
//        if (StringUtils.isEmpty(id)) {
//            return null;
//        }
//        for (Dictionary dictionary : dictionaries) {
//            if (dictionary.getCode().equals(id)) {
//                return dictionary.getName();
//            }
//        }
//        return null;
//    }
//
//    private String getNameFromDictionary(List<Dictionary> dictionaries, String id, String type) {
//        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(type)) {
//            return null;
//        }
//        for (Dictionary dictionary : dictionaries) {
//            if (id.equals(dictionary.getCode()) && type.equals(dictionary.getType())) {
//                return dictionary.getName();
//            }
//        }
//        return null;
//    }
//
//    OnboardDetailDTO detail(String applyId) throws JsonProcessingException {
//        log.info("入职申请详情-detail-start");
//        List<OnboardDTO> detail = moreOnboardMapper.detail(applyId);
//        if (!CollectionUtils.isEmpty(detail)) {
//            //循环加入工单信息
//            for (OnboardDTO onboard : detail) {
//                HashMap ticketInfo = onboardTicketService.loadTicketOnboardId(onboard.getId());
//                onboard.setTicketInfo(ticketInfo);
//            }
//        }
//        TOnboardApply apply = applyMapper.selectByPrimaryKey(applyId);
//        List<WorkflowTaskDefinitionDTO> definitionList = null;
//        List<WorkflowTaskDTO> taskList;
//        List<OnboardTaskDTO> collect = null;
//        if (!ObjectUtils.isEmpty(apply.getWorkflowInstanceId())) {
//            definitionList = workflowFeignClient.getProcessDefinitionDetailByProcessInstanceId(apply.getWorkflowInstanceId());
//            if (ObjectUtils.isEmpty(definitionList)) {
//                definitionList = new ArrayList<>();
//            }
//            log.info("definitionList:{}", JsonUtil.toJson(definitionList));
//            taskList = workflowFeignClient.getHistoryByProcessInstanceId(apply.getWorkflowInstanceId());
//            if (ObjectUtils.isEmpty(taskList)) {
//                taskList = new ArrayList<>();
//            }
//            log.info("taskList:{}", JsonUtil.toJson(taskList));
//            collect = taskList.stream().map(s -> {
//                OnboardTaskDTO task = new OnboardTaskDTO(s);
//                if (!ObjectUtils.isEmpty(s.getAssignee())) {
//                    MoreUser user = orgFeignClient.getOrgRoleUserInfo(s.getAssignee()).getData();
//                    if (!ObjectUtils.isEmpty(user)) {
//                        task.setUpdateTime(s.getEndTime());
//                        task.setUpdateUser(user.getUserName());
//                        String roleName = task.getName().replace("发起", "").replace("审批", "");
//                        task.setRoleName(roleName);
//                        task.setJobNumber(user.getUserAccount());
//                    }
//                    if (!ObjectUtils.isEmpty(s.getTaskLocalVariables())) {
//                        Map<String, Object> taskLocalVariables = s.getTaskLocalVariables();
//                        task.setFormValue((String) taskLocalVariables.get("approved"));
//                        task.setRejectReason((String) taskLocalVariables.get("reason"));
//                    }
//                }
//                return task;
//            }).collect(Collectors.toList());
//        }
//        log.info("入职申请详情-detail-end");
//        return new OnboardDetailDTO(apply, detail, definitionList, collect);
//    }
//
//    public boolean checkIdNumber(String idNumber, String applyId) {
//        Long count;
//        if (ObjectUtils.isEmpty(applyId)) {
//            count = moreOnboardMapper.checkIdNumber(idNumber);
//        } else {
//            count = moreOnboardMapper.checkIdNumberAndApplyId(idNumber, applyId);
//        }
//        log.info("checkIdNumber,idNumber:{},count:{}", idNumber, count);
//        return count == 0;
//    }
//
//
//    Pagination<OnboardApplyDTO> getTaskList(Pagination<OnboardApplyDTO> pagination, String flag) {
//        log.info("审批列表-getTaskList-start");
//        String userNo = authorizationUtil.getUserNo();
//        List<String> workflowInstanceIds = null;
//        if ("2".equals(flag)) {
//            // 已审批
//            pagination.getParameterMap().put("flag", "2");
//            workflowInstanceIds = workflowFeignClient.getApproved(userNo);
//        } else if ("0".equals(flag)) {
//            // 待审批
//            pagination.getParameterMap().put("flag", "0");
//            workflowInstanceIds = workflowFeignClient.getApprovalPending(userNo);
//        }
//        log.info("workflowInstanceIds:{}", workflowInstanceIds);
//        if (!CollectionUtils.isEmpty(workflowInstanceIds)) {
//            StringBuilder buffer = new StringBuilder();
//            buffer.append("(");
//            for (String instanceId : workflowInstanceIds) {
//                buffer.append("'");
//                buffer.append(instanceId);
//                buffer.append("'");
//                buffer.append(",");
//            }
//            buffer.append(")");
//            String instanceIds = buffer.toString().replace(",)", ")");
//            log.info("instanceIds:{}", instanceIds);
//            pagination.getParameterMap().put("instanceIds", instanceIds);
//        } else {
//            log.info("instanceIds为空");
//            pagination.getParameterMap().put("instanceIds", "('')");
//        }
//
//        String dataRange = orgFeignClient.getUserDataRange(userNo).getData();
//        log.info("用户dataRange:{}", dataRange);
//        if (!ObjectUtils.isEmpty(dataRange)) {
//            String[] split = dataRange.split(",");
//            String newDataRange = "";
//            for (String s : split) {
//                newDataRange = newDataRange + "'" + s + "'" + ",";
//            }
//            newDataRange = newDataRange.substring(0, newDataRange.length() - 1);
//            log.info("newDataRange:{}", newDataRange);
//            pagination.getParameterMap().put("dataRange", newDataRange);
//        }
//        List<OnboardApplyDTO> onboardInfoList = moreOnboardMapper.getOnboardApplyList(pagination);
//        long onboardInfoCount = moreOnboardMapper.getOnboardApplyCount(pagination);
//        pagination.getParameterMap().remove("instanceIds");
//        pagination.getParameterMap().remove("flag");
//        if (!ObjectUtils.isEmpty(dataRange)) {
//            pagination.getParameterMap().remove("dataRange");
//        }
//        pagination.setResult(onboardInfoList).setTotalCount(onboardInfoCount);
//        log.info("审批列表-getTaskList-end");
//        return pagination;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean approval(ApprovalDTO dto, String userNo) throws JsonProcessingException {
//        log.info("approval-审批通过-approval-start");
//        Example example = new Example(TOnboardApply.class);
//        example.createCriteria().andEqualTo("workflowInstanceId", dto.getProcessInstanceId());
//        TOnboardApply apply = applyMapper.selectOneByExample(example);
//        // 岗位code
//        String departmentCode = moreOnboardMapper.selectPostByApplyId(apply.getId());
//        if (ObjectUtils.isEmpty(departmentCode)) {
//            throw new RuntimeException("岗位不存在");
//        }
//        log.info("approval-岗位code: {}", departmentCode);
//        Org orgOrg = orgClient.getOrgByPositionCode(departmentCode).getData();
//        Org deptOrg = orgClient.getDeptByPositionCode(departmentCode).getData();
//        log.info("approval-getOrgName: {}", orgOrg.getOrgName());
//        log.info("approval-getOrgCode: {}", orgOrg.getOrgCode());
//        log.info("approval-getPositionName: {}", deptOrg.getPositionName());
//        log.info("approval-getPositionId: {}", deptOrg.getPositionId());
//
//        String userId;
//        if (!ObjectUtils.isEmpty(userNo)) {
//            userId = userNo;
//        } else {
//            userId = authorizationUtil.getUserNo();
//        }
//        log.info("approval-当前用户account:{}", userId);
//
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "通过");
//
//        // 标记状态为审批中
//        moreOnboardMapper.changeApplyStatus(DICTIONARY_VALUE_APPLY_STATUS_5, dto.getProcessInstanceId());
//        // 完成当前节点
//        onboardWorkflowService.completeCurrentTask(userId, dto.getProcessInstanceId(), param);
//        // 自动审批节点
//        autoComplete(userId, dto.getProcessInstanceId(), param, userId);
//        // 保存节点状态并发送提醒
//        completeOther(apply, dto.getProcessInstanceId(), orgOrg);
//
//
//        // 标记已审批过
//        processHasBeenDirty(dto.getProcessInstanceId());
//        // 重置邮件标识位
//        moreOnboardMapper.processMailRemoveDirty(apply.getId());
//
//        log.info("approval-审批通过-approval-end");
//        return true;
//    }
//
//    /**
//     * 自动审批
//     * 如果下一个节点和当前节点审批人相同，则自动通过
//     */
//    void autoComplete(final String userId, String processInstanceId, @NotNull Map<String, Object> param, final String currentUser) {
//        List<MoreUser> userList = onboardWorkflowService.getNextNodeUserList(processInstanceId);
//        int count = 0;
//        while (!ObjectUtils.isEmpty(userList) && !ObjectUtils.isEmpty(currentUser) && count < 3) {
//            MoreUser user = userList.get(0);
//            if (!currentUser.equals(user.getUserAccount())) {
//                break;
//            }
//            onboardWorkflowService.completeCurrentTask(userId, processInstanceId, param);
//            userList = onboardWorkflowService.getNextNodeUserList(processInstanceId);
//            count++;
//        }
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public boolean reject(ApprovalDTO dto) throws JsonProcessingException {
//        log.info("reject-申请驳回操作-start");
//        String userId = authorizationUtil.getUserNo();
//        log.info("reject-当前用户account:{}", userId);
//
//        WorkflowTaskDTO task = workflowFeignClient.getTaskByUserIdAndProcessInstanceId(userId, dto.getProcessInstanceId());
//        log.info("reject-taskId:{}", task.getId());
//        Map<String, Object> param = new HashMap<>();
//        param.put("approved", "拒绝");
//        param.put("reason", dto.getRejectReason());
//        workflowFeignClient.claim(task.getId(), userId);
//        workflowFeignClient.complete(task.getId(), param);
//        TOnboardApply apply = getApplyByInstanceId(dto.getProcessInstanceId());
//        // 发送驳回邮件
//        onboardMailService.sendRejectMail(apply, dto.getRejectReason());
//        // 给之前的审批人发送邮件
//        onboardMailService.sendRejectMail(apply, dto.getRejectReason(), dto.getProcessInstanceId());
//        // 删除当前处理人
//        moreOnboardMapper.changeApplyStatus(DICTIONARY_VALUE_APPLY_STATUS_4, task.getProcessInstanceId());
//        moreOnboardMapper.deleteCurrentUserByProcessInstanceId(task.getProcessInstanceId());
//        moreOnboardMapper.processMailRemoveDirty(apply.getId());
//        moreOnboardMapper.changeUpdateTimeByInstanceId(task.getProcessInstanceId());
//        moreOnboardMapper.removeDirtyFlag(apply.getId());
//
//        log.info("reject-申请驳回操作-end");
//        return true;
//    }
//
//    TOnboardApply getApplyByInstanceId(String processInstanceId) {
//        Example example = new Example(TOnboardApply.class);
//        example.createCriteria().andEqualTo("workflowInstanceId", processInstanceId);
//        return applyMapper.selectOneByExample(example);
//    }
//
//    boolean processHasBeenDirty(String processInstanceId) {
//        log.info("标记该申请已经审批过，processInstanceId:{}", processInstanceId);
//        moreOnboardMapper.processHasBeenDirty(processInstanceId);
//        return true;
//    }
//
//    ResponseEntity downloadErrorExcel(String errorId) throws IOException {
//        errorId = URLDecoder.decode(errorId.replace("%3F", "%2F"), "utf-8");
//        log.info("下载错误excel");
//        log.info("errorId:{}", errorId);
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + System.currentTimeMillis() + ".xlsx");
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        String errorExcelPath = errorId + File.separator + "error.xlsx";
//        log.info("errorExcelPath:{}", errorExcelPath);
//        FileInputStream fis = new FileInputStream(new File(errorExcelPath));
//
//        InputStreamResource isr = new InputStreamResource(fis);
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(isr);
//    }
//
//    public void updateJobNumberById(Integer id, String jobNumber) {
//        TOnboardEmployeeInfo employeeInfo = new TOnboardEmployeeInfo();
//        employeeInfo.setId(id);
//        employeeInfo.setJobNumber(jobNumber);
//        employeeInfoMapper.updateByPrimaryKeySelective(employeeInfo);
//    }
//
//    public Date getWorkingDayAfterNow(Integer day) throws ParseException {
//        List<String> workingDays = moreOnboardMapper.getWorkingDays("asc");
//        return WorkingDayUtil.getWorkingDayAfterNow(workingDays, day);
//    }
//
//    public Date getWorkingDayBeforeNow(Integer day) {
//        List<String> workingDays = moreOnboardMapper.getWorkingDays("desc");
//        try {
//            return WorkingDayUtil.getWorkingDayBeforeNow(workingDays, day);
//        } catch (Exception e) {
//            Calendar calendar = new GregorianCalendar();
//            calendar.setTime(new Date());
//            calendar.add(calendar.DATE, -1 * day);
//            return calendar.getTime();
//        }
//    }
//
//    public Pagination<PersonnelInformationDTO> personnelInformationList(Pagination<PersonnelInformationDTO> pagination) {
//        String userNo = authorizationUtil.getUserNo();
//        String unitCode = orgFeignClient.getUserDataRange(userNo).getData();
//        pagination.getParameterMap().put("unitCode", unitCode);
//        List<PersonnelInformationDTO> list = moreOnboardMapper.selectPersonnelInformationList(pagination);
//        Long count = moreOnboardMapper.selectPersonnelInformationCount(pagination);
//        return pagination.setResult(list).setTotalCount(count);
//    }
//
//    /**
//     * 导出人员信息
//     */
//    public ResponseEntity<org.springframework.core.io.Resource> personnelInformationExport(String flag, Pagination<PersonnelInformationDTO> pagination) throws IOException {
//        log.info("导出人员信息Excel personnelInformationExport start");
//        log.info("导出人员信息Excel personnelInformationExport flag:{}", flag);
//        String fileName;
//        if ("1".equals(flag)) {
//            // 导出全部信息
//            fileName = "excel/personnel_1.xlsx";
//        } else if ("2".equals(flag)) {
//            // 导出基本信息
//            fileName = "excel/personnel_2.xlsx";
//        } else {
//            throw new RuntimeException("工单类型为空");
//        }
//        String exportPath = Files.createTempDirectory("excel-personnelInformationExport-").toString();
//        log.info("导出人员信息Excel personnelInformationExport exportPath:{}", exportPath);
//
//        String exportFileName = System.currentTimeMillis() + ".xlsx";
//        log.info("导出人员信息Excel personnelInformationExport exportFileName:{}", exportFileName);
//
//        /* 读取模板 */
//        org.springframework.core.io.Resource resource = new ClassPathResource(fileName);
//        /* 赋值后生成新的文件 */
//        Workbook workbook = new XSSFWorkbook(resource.getInputStream());
//        String userNo = authorizationUtil.getUserNo();
//        String unitCode = orgFeignClient.getUserDataRange(userNo).getData();
//        pagination.setPageNo(1);
//        pagination.setPageSize(9999999);
//        pagination.getParameterMap().put("unitCode", unitCode);
//        if ("1".equals(flag)) {
//            // 导出全部信息
//            List<PersonnelInformationDTO> personnelInformationAll = moreOnboardMapper.selectPersonnelInformationAll(pagination);
//            fillPersonnelInformationExcelDataAll(workbook, personnelInformationAll);
//            log.info("导出人员信息Excel personnelInformationExport personnelInformationAll size:{}", personnelInformationAll.size());
//        } else if ("2".equals(flag)) {
//            // 导出基本信息
//            List<PersonnelInformationDTO> personnelInformationBase = moreOnboardMapper.selectPersonnelInformationBase(pagination);
//            fillPersonnelInformationExcelDataBase(workbook, personnelInformationBase);
//            log.info("导出人员信息Excel personnelInformationExport personnelInformationBase size:{}", personnelInformationBase.size());
//        }
//        workbook.write(new FileOutputStream(new File(exportPath + File.separator + exportFileName).getPath()));
//        /* 下载新文件 */
//        FileInputStream fis = new FileInputStream(new File(exportPath + File.separator + exportFileName));
//        InputStreamResource isr = new InputStreamResource(fis);
//
//        HttpHeaders header = new HttpHeaders();
//        String exportName = null;
//        if ("1".equals(flag)) {
//            // 导出全部信息
//            exportName = "人员信息导出（全部信息）-";
//        } else if ("2".equals(flag)) {
//            // 导出基本信息
//            exportName = "人员信息导出（基本信息）-";
//        }
//        exportName += new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";
//        log.info("导出人员信息Excel personnelInformationExport exportName:{}", exportName);
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(exportName, "utf-8"));
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        log.info("导出人员信息Excel personnelInformationExport end");
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(isr);
//    }
//
//    /**
//     * 填充人员信息（基本信息）Excel数据
//     */
//    private void fillPersonnelInformationExcelDataBase(Workbook workbook, List<PersonnelInformationDTO> personnelInformationAll) {
//        for (int i = 0; i < personnelInformationAll.size(); i++) {
//            PersonnelInformationDTO dto = personnelInformationAll.get(i);
//            int rowNumber = i + 1;
//            setPersonnelInformationExportValue(rowNumber, 0, workbook, rowNumber);
//            setPersonnelInformationExportValue(rowNumber, 1, workbook, dto.getPersonNumber());//人员编号
//            setPersonnelInformationExportValue(rowNumber, 2, workbook, dto.getName());//员工姓名
//            setPersonnelInformationExportValue(rowNumber, 3, workbook, dto.getJoinLocalUnitDate() == null ? null : new SimpleDateFormat("yyyy-M-d").format(dto.getJoinLocalUnitDate()));
//            setPersonnelInformationExportValue(rowNumber, 4, workbook, dto.getDepartment());//部门名称
//            setPersonnelInformationExportValue(rowNumber, 5, workbook, dto.getPost());//岗位
//            String gender = "";
//            if ("F".equals(dto.getGender())) {
//                gender = "女";
//            } else if ("M".equals(dto.getGender())) {
//                gender = "男";
//            }
//            setPersonnelInformationExportValue(rowNumber, 6, workbook, gender);//性别
//            setPersonnelInformationExportValue(rowNumber, 7, workbook, dto.getIdCardNumber());//身份证号
//            setPersonnelInformationExportValue(rowNumber, 8, workbook, dto.getBankAccountNumber());
//            setPersonnelInformationExportValue(rowNumber, 9, workbook, dto.getBankName());
//        }
//    }
//
//    private void setTicketExportValue(int rowNumber, int cellNumber, Workbook workbook, Object value) {
//        Sheet sheet = workbook.getSheetAt(0);
//        Row row = sheet.getRow(rowNumber) == null ? sheet.createRow(rowNumber) : sheet.getRow(rowNumber);
//        Cell cell = row.getCell(cellNumber, CREATE_NULL_AS_BLANK);
//        if (ObjectUtils.isEmpty(value)) {
//            cell.setCellValue("");
//        } else if (value instanceof Double) {
//            Double convert = (Double) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof Integer) {
//            Integer convert = (Integer) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof Date) {
//            Date convert = (Date) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof String) {
//            String convert = (String) value;
//            cell.setCellValue(convert);
//        }
//    }
//
//    private void setPersonnelInformationExportValue(int rowNumber, int cellNumber, Workbook workbook, Object value) {
//        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setFillPattern(FillPatternType.NO_FILL);
//        cellStyle.setLocked(false);
//
//        BorderStyle borderStyle = BorderStyle.THIN;
//        cellStyle.setBorderTop(borderStyle);
//        cellStyle.setBorderBottom(borderStyle);
//        cellStyle.setBorderLeft(borderStyle);
//        cellStyle.setBorderRight(borderStyle);
//
//        short borderColor = IndexedColors.BLACK.getIndex();
//        cellStyle.setTopBorderColor(borderColor);
//        cellStyle.setBottomBorderColor(borderColor);
//        cellStyle.setLeftBorderColor(borderColor);
//        cellStyle.setRightBorderColor(borderColor);
//
//        Sheet sheet = workbook.getSheetAt(0);
//        Row row = sheet.getRow(rowNumber) == null ? sheet.createRow(rowNumber) : sheet.getRow(rowNumber);
//        Cell cell = row.getCell(cellNumber, CREATE_NULL_AS_BLANK);
//        cell.setCellStyle(cellStyle);
//        if (ObjectUtils.isEmpty(value)) {
//            cell.setCellValue("");
//        } else if (value instanceof Double) {
//            Double convert = (Double) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof Integer) {
//            Integer convert = (Integer) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof Date) {
//            Date convert = (Date) value;
//            cell.setCellValue(convert);
//        } else if (value instanceof String) {
//            String convert = (String) value;
//            cell.setCellValue(convert);
//        }
//    }
//
//    /**
//     * 填充人员信息（全部信息）Excel数据
//     */
//    private void fillPersonnelInformationExcelDataAll(Workbook workbook, List<PersonnelInformationDTO> personnelInformationAll) {
//        List<Dictionary> dictionaries = orgClient.getDictionaryAll();
//
//        for (int i = 0; i < personnelInformationAll.size(); i++) {
//            PersonnelInformationDTO dto = personnelInformationAll.get(i);
//            int rowNumber = i + 1;
//            setPersonnelInformationExportValue(rowNumber, 0, workbook, "");//板块或事业部 todo
//            setPersonnelInformationExportValue(rowNumber, 1, workbook, dto.getCompany());//单位名称
//            setPersonnelInformationExportValue(rowNumber, 2, workbook, dto.getDepartment());//部门名称
//            setPersonnelInformationExportValue(rowNumber, 3, workbook, dto.getPersonNumber());//人员编号
//            setPersonnelInformationExportValue(rowNumber, 4, workbook, dto.getName());//姓名
//
//            String gender = "";
//            if ("F".equals(dto.getGender())) {
//                gender = "女";
//            } else if ("M".equals(dto.getGender())) {
//                gender = "男";
//            }
//            setPersonnelInformationExportValue(rowNumber, 5, workbook, gender);//性别
//            setPersonnelInformationExportValue(rowNumber, 6, workbook, dto.getBirthday() == null ? null : new SimpleDateFormat("yyyy-M-d").format(dto.getBirthday()));//出生日期
//            setPersonnelInformationExportValue(rowNumber, 7, workbook, dto.getAge());//年龄
//            setPersonnelInformationExportValue(rowNumber, 8, workbook, dto.getIdCardNumber());//身份证号
//            setPersonnelInformationExportValue(rowNumber, 9, workbook, dto.getPhoneNumber());
//            setPersonnelInformationExportValue(rowNumber, 10, workbook, dto.getEmail());
//            setPersonnelInformationExportValue(rowNumber, 11, workbook, getNameFromDictionary(dictionaries, dto.getNation(), "CN_RACE"));
//            setPersonnelInformationExportValue(rowNumber, 12, workbook, getNameFromDictionary(dictionaries, dto.getNationality(), "NATIONALITY"));
//            setPersonnelInformationExportValue(rowNumber, 13, workbook, getNameFromDictionary(dictionaries, dto.getNativePlace(), "DFL_CITY"));
//            String personType = "";
//            if ("1".equals(dto.getPersonType())) {
//                personType = "成品人才";
//            } else if ("2".equals(dto.getPersonType())) {
//                personType = "应届毕业生";
//            } else if ("3".equals(dto.getPersonType())) {
//                personType = "返聘人员";
//            } else if ("4".equals(dto.getPersonType())) {
//                personType = "外籍人员";
//            }
//            setPersonnelInformationExportValue(rowNumber, 14, workbook, personType);//人员类型
//            setPersonnelInformationExportValue(rowNumber, 15, workbook, getNameFromDictionary(dictionaries, dto.getPoliticalStatus(), "CN_PARTY_TYPE"));//政治面貌
//            String education = "";
//            if (!ObjectUtils.isEmpty(dto.getEducation())) {
//                education = getNameFromDictionary(dictionaries, dto.getEducation(), TYPE_NAME_DFL_HIGH_EDU_LEVEL);
//            }
//            String degree = "";
//            if (!ObjectUtils.isEmpty(dto.getDegree())) {
//                degree = getNameFromDictionary(dictionaries, dto.getDegree(), TYPE_NAME_DFL_DEGREE);
//
//            }
//            setPersonnelInformationExportValue(rowNumber, 16, workbook, education);//最高学历
//            setPersonnelInformationExportValue(rowNumber, 17, workbook, degree);//最高学位
//            setPersonnelInformationExportValue(rowNumber, 18, workbook, education);//第一学历
//            setPersonnelInformationExportValue(rowNumber, 19, workbook, getNameFromDictionary(dictionaries, dto.getResidenceType(), "CN_HUKOU_TYPE"));//户口类型
//            setPersonnelInformationExportValue(rowNumber, 20, workbook, degree);//第一学位
//            setPersonnelInformationExportValue(rowNumber, 21, workbook, "");//最高职称 todo
//            setPersonnelInformationExportValue(rowNumber, 22, workbook, "");//最高职称级别 todo
//            setPersonnelInformationExportValue(rowNumber, 23, workbook, dto.getPost());//岗位名称
//            setPersonnelInformationExportValue(rowNumber, 24, workbook, dto.getJobSequence());//岗位序列
//            setPersonnelInformationExportValue(rowNumber, 25, workbook, dto.getAddDfmDate() == null ? null : new SimpleDateFormat("yyyy/MM/dd").format(dto.getAddDfmDate()));//加入东风日期
//            setPersonnelInformationExportValue(rowNumber, 26, workbook, dto.getPersonLevel());
//            setPersonnelInformationExportValue(rowNumber, 27, workbook, getNameFromDictionary(dictionaries, dto.getJoinCompanyWay(), "EMPLOYEE_CATG"));
//            setPersonnelInformationExportValue(rowNumber, 28, workbook, dto.getJoinLocalUnitDate() == null ? null : new SimpleDateFormat("yyyy/MM/dd").format(dto.getJoinLocalUnitDate()));
//            setPersonnelInformationExportValue(rowNumber, 29, workbook, dto.getAttendWorkDate() == null ? null : new SimpleDateFormat("yyyy/MM/dd").format(dto.getAttendWorkDate()));
//            setPersonnelInformationExportValue(rowNumber, 30, workbook, getNameFromDictionary(dictionaries, dto.getContractType(), "CONTRACT_TYPE"));
//            setPersonnelInformationExportValue(rowNumber, 31, workbook, dto.getStateReason());
//            String contractPeriod = dto.getContractPeriodYear() == null ? "" : dto.getContractPeriodYear() + "年";
//            if (dto.getContractPeriodMonth() != null && dto.getContractPeriodMonth() != 0) {
//                contractPeriod += dto.getContractPeriodMonth() + "个月";
//            }
//            if (dto.getContractPeriodDay() != null && dto.getContractPeriodDay() != 0) {
//                contractPeriod += dto.getContractPeriodDay() + "天";
//            }
//            if ((dto.getContractPeriodYear() == null || dto.getContractPeriodYear() == 0)
//                    && (dto.getContractPeriodMonth() == null || dto.getContractPeriodMonth() == 0)
//                    && (dto.getContractPeriodDay() == null || dto.getContractPeriodDay() == 0)) {
//                contractPeriod = "";
//            }
//            setPersonnelInformationExportValue(rowNumber, 32, workbook, contractPeriod);
//            setPersonnelInformationExportValue(rowNumber, 33, workbook, dto.getStartDate() == null ? null : new SimpleDateFormat("yyyy/M/d").format(dto.getStartDate()));//起始日期
//            setPersonnelInformationExportValue(rowNumber, 34, workbook, dto.getEndDate() == null ? null : new SimpleDateFormat("yyyy/M/d").format(dto.getEndDate()));//起始日期
//            setPersonnelInformationExportValue(rowNumber, 35, workbook, dto.getBankAccountNumber());//起始日期
//            setPersonnelInformationExportValue(rowNumber, 36, workbook, dto.getBankName());//起始日期
//
//        }
//    }
//
//    public ResponseEntity<org.springframework.core.io.Resource> ticketExport(String flag, Pagination<HashMap> pagination, HttpServletRequest req) throws IOException {
//        log.info("导出工单Excel ticketExport start");
//        log.info("导出工单Excel ticketExport flag:{}", flag);
//        String fileName;
//        if ("11".equals(flag)) {
//            // 待处理工单导出_入职办理环节
//            fileName = "excel/ticket_1.xlsx";
//        } else if ("12".equals(flag)) {
//            // 待处理工单导出_后续跟踪环节
//            fileName = "excel/ticket_2.xlsx";
//        } else if ("13".equals(flag)) {
//            // 待处理工单导出_采集数据环节
//            fileName = "excel/ticket_3.xlsx";
//        } else if ("2".equals(flag)) {
//            // 待认领工单
//            fileName = "excel/ticket_4.xlsx";
//        } else if ("3".equals(flag)) {
//            // 工单池
//            fileName = "excel/ticket_5.xlsx";
//        } else if ("4".equals(flag)) {
//            // 已处理工单
//            fileName = "excel/ticket_6.xlsx";
//        } else {
//            throw new RuntimeException("工单类型为空");
//        }
//        String exportPath = Files.createTempDirectory("excel-ticket-").toString();
//        log.info("导出工单Excel ticketExport exportPath:{}", exportPath);
//
//        String exportFileName = System.currentTimeMillis() + ".xlsx";
//        log.info("导出工单Excel ticketExport exportFileName:{}", exportFileName);
//
//        /* 读取模板 */
//        org.springframework.core.io.Resource resource = new ClassPathResource(fileName);
//        /* 赋值后生成新的文件 */
//        Workbook workbook = new XSSFWorkbook(resource.getInputStream());
//        List<HashMap> dataList = getTicketExportData(flag, pagination, req);
//        fillTicketExcelData(workbook, flag, dataList);
//        workbook.write(new FileOutputStream(new File(exportPath + File.separator + exportFileName).getPath()));
//        /* 下载新文件 */
//        FileInputStream fis = new FileInputStream(new File(exportPath + File.separator + exportFileName));
//        InputStreamResource isr = new InputStreamResource(fis);
//
//        HttpHeaders header = new HttpHeaders();
//        String exportName = "列表导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";
//        log.info("导出工单Excel ticketExport exportName:{}", exportName);
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(exportName, "utf-8"));
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        log.info("导出工单Excel ticketExport end");
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(isr);
//    }
//
//    private void fillTicketExcelData(Workbook workbook, String flag, List<HashMap> dataList) {
//        List<Dictionary> dictionaries = orgClient.getDictionaryAll();
//
//        for (int i = 0; i < dataList.size(); i++) {
//            HashMap map = dataList.get(i);
//            List<String> titleList = new ArrayList<>();
//            if ("11".equals(flag)) {
//                // 待处理工单导出_入职办理环节
//                titleList = Arrays.asList("序号", "标题", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期", "入职进度", "信息确认进度", "操作");
//            } else if ("12".equals(flag)) {
//                // 待处理工单导出_后续跟踪环节
//                titleList = Arrays.asList("序号", "标题", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期", "入职进度", "信息确认进度");
//            } else if ("13".equals(flag)) {
//                // 待处理工单导出_采集数据环节
//                titleList = Arrays.asList("序号", "标题", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期", "入职进度", "信息确认进度", "操作");
//            } else if ("2".equals(flag)) {
//                // 待认领工单
//                titleList = Arrays.asList("序号", "标题", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期");
//            } else if ("3".equals(flag)) {
//                // 工单池
//                titleList = Arrays.asList("序号", "标题", "工单类型", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期", "入职进度", "信息确认进度", "状态", "操作人");
//            } else if ("4".equals(flag)) {
//                // 已处理工单
//                titleList = Arrays.asList("序号", "标题", "工单类型", "申请日期", "申请名字", "单位", "人员类型", "加入途径", "人员编号", "员工姓名", "性别", "电话号码", "个人邮箱", "拟入职日期", "入职进度", "信息确认进度");
//            }
//            TicketDTO ticket = null;
//            LinkedHashMap ticketMap = null;
//            try {
//                ticket = (TicketDTO) map.get("ticketInfo");
//            } catch (ClassCastException e) {
//                ticketMap = (LinkedHashMap) map.get("ticketInfo");
//            }
//            TOnboardApply apply = (TOnboardApply) map.get("applyInfo");
//            OnboardDTO onboard = (OnboardDTO) map.get("onboardInfo");
//            LinkedHashMap<String, Object> time = (LinkedHashMap<String, Object>) map.get("time");
//            for (int j = 0; j < titleList.size(); j++) {
//                Object value = null;
//                switch (titleList.get(j)) {
//                    case "状态": {
//                        LinkedHashMap status = (LinkedHashMap) map.get("status");
//                        if (status == null) break;
//                        String ticketStatus = (String) status.get("ticketStatus");
//                        if ("new".equals(ticketStatus)) {
//                            value = "待认领";
//                        } else if ("assigned".equals(ticketStatus)) {
//                            value = "待处理";
//                        } else if ("close".equals(ticketStatus)) {
//                            value = "已关闭";
//                        }
//                        break;
//                    }
//                    case "操作人":
//                        value = map.get("currentAssigneeName");
//                        break;
//                    case "工单类型": {
//                        Integer typeId = (Integer) map.get("typeId");
//                        if (ObjectUtils.isEmpty(typeId)) {
//                            break;
//                        } else if (1 == typeId) {
//                            value = "入职工单";
//                        } else if (3 == typeId) {
//                            value = "入职未到岗工单";
//                        }
//                        break;
//                    }
//                    case "序号":
//                        value = i + 1;
//                        break;
//                    case "标题":
//                        value = ticketMap != null ? ticketMap.get("title") : ticket.getTitle();
//                        break;
//                    case "申请日期":
//                        value = apply.getApplyTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(apply.getApplyTime());
//                        break;
//                    case "申请名字":
//                        value = apply.getApplyName();
//                        break;
//                    case "单位":
//                        value = apply.getUnit();
//                        break;
//                    case "人员类型":
//                        value = getNameFromDictionary(dictionaries, onboard.getBasic().getEmployeeType(), "PERSON_TYPE");
//                        break;
//                    case "加入途径":
//                        value = getNameFromDictionary(dictionaries, onboard.getBasic().getJoinWay(), "EMPLOYEE_CATG");
//                        break;
//                    case "人员编号":
//                        value = onboard.getEmployeeInfo().getJobNumber();
//                        break;
//                    case "员工姓名":
//                        value = onboard.getEmployeeInfo().getName();
//                        break;
//                    case "性别":
//                        value = "M".equals(onboard.getEmployeeInfo().getGender()) ? "男" : "女";
//                        break;
//                    case "电话号码":
//                        value = onboard.getEmployeeInfo().getContactNumber();
//                        break;
//                    case "个人邮箱":
//                        value = onboard.getEmployeeInfo().getEmail();
//                        break;
//                    case "拟入职日期":
//                        value = onboard.getBasic().getOnboardTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(onboard.getBasic().getOnboardTime());
//                        break;
//                    case "入职进度": {
//                        String processStatus = (String) map.get("processStatus");
//                        if (ObjectUtils.isEmpty(processStatus)) {
//                            break;
//                        }
//                        switch (processStatus) {
//                            case "new":
//                                value = "待提交信息";
//                                break;
//                            case "posted":
//                                value = "已提交，待审核";
//                                break;
//                            case "approved":
//                                value = "已审核，待入职";
//                                break;
//                            case "rejected":
//                                value = "已驳回，待重新提交";
//                                break;
//                            case "wait_confirm_onboard":
//                                value = "待确认到岗";
//                                break;
//                            case "confirm_onboard":
//                                value = "已到岗";
//                                break;
//                            case "cancel_onboard":
//                                value = "取消入职";
//                                break;
//                            case "not_onboard":
//                                value = "未到岗";
//                                break;
//                        }
//                        break;
//                    }
//                    case "信息确认进度": {
//                        String informationStatus = (String) map.get("informationStatus");
//                        if (ObjectUtils.isEmpty(informationStatus)) {
//                            break;
//                        }
//                        switch (informationStatus) {
//                            case "wait_confirm":
//                                value = "待确认信息";
//                                break;
//                            case "confirmed":
//                                value = "已确认";
//                                break;
//                            case "rejected":
//                                value = "已拒绝";
//                                break;
//                        }
//                        break;
//                    }
//                    default:
//                        value = "";
//                }
//                setTicketExportValue(i + 1, j, workbook, value);
//            }
//        }
//
//    }
//
//    private List<HashMap> getTicketExportData(String flag, Pagination<HashMap> pagination, HttpServletRequest req) {
//        List<HashMap> result;
//        pagination.setPageNo(1);
//        pagination.setPageSize(9999999);
//        if ("11".equals(flag) || "12".equals(flag) || "13".equals(flag) || "4".equals(flag)) {
//            // 11待处理工单导出_入职办理环节
//            // 12待处理工单导出_后续跟踪环节
//            // 13待处理工单导出_采集数据环节
//            // 4已处理工单
//            // mine
//            result = onboardTicketService.loadUserTickets(pagination, Integer.parseInt(req.getHeader("userId"))).getResult();
//        } else if ("2".equals(flag) || "3".equals(flag)) {
//            // 2待认领工单
//            // 3工单池
//            // list
//            result = onboardTicketService.loadTickets(pagination, false).getResult();
//        } else {
//            throw new RuntimeException("工单类型为空");
//        }
//        return result;
//    }
//
//    public ResponseEntity<org.springframework.core.io.Resource> personnelInformationPictureExport(Pagination<PersonnelInformationDTO> pagination) throws IOException {
//        String userNo = authorizationUtil.getUserNo();
//        String unitCode = orgFeignClient.getUserDataRange(userNo).getData();
//        pagination.getParameterMap().put("unitCode", unitCode);
//        List<PersonnelInformationPictureDTO> urlList = moreOnboardMapper.selectPersonnelInformationPictureList(pagination);
//        if (ObjectUtils.isEmpty(urlList)) {
//            return null;
//        }
//        ZipOutputStream zos = null;
//        OutputStream outStream = null;
////        照片导出20200227.zip
////        ④导出照片：点击后，将列表中所有人员在预入职信息采集系统中上传的‘个人证件照’导出至压缩包中，
////        照片命名规则为 员工姓名+人员编号，如同一人员存在两张照片，则命名中增加-数字区分（如张三8111811-2.jpg）
//
//
//        String zipName = "照片导出" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
//        String exportPath = Files.createTempDirectory("personnelInformationPictureExport-").toString();
//        log.info("personnelInformationPictureExport exportPath:{}", exportPath);
//
//        String exportFileName = System.currentTimeMillis() + ".zip";
//        log.info("personnelInformationPictureExport exportFileName:{}", exportFileName);
//        try {
//
//
//            outStream = new FileOutputStream(exportPath + File.separator + exportFileName);
//            zos = new ZipOutputStream(outStream);
//            //循环下载所有图片
//            for (PersonnelInformationPictureDTO dto : urlList) {
//                if (dto.getUrlList().size() > 0) {
//                    List<String> resultList = new ArrayList<>();
//
//                    for (int i = 0; i < dto.getUrlList().size(); i++) {
//                        String url = dto.getUrlList().get(i);
//                        String[] split = url.split(",");
//                        for (String s : split) {
//                            if (s.startsWith("http")) {
//                                resultList.add(s);
//                            }
//                        }
//                    }
//                    if (resultList.size() > 0) {
//                        for (int k = 0; k < resultList.size(); k++) {
//                            String url = resultList.get(k);
//                            String extension = url.split("\\.")[url.split("\\.").length - 1];
//                            try {
//                                byte[] fileData = fileService.downloadFile(url);
//                                //（如张三8111811-2.jpg）
//                                ZipEntry zipEntry = new ZipEntry(dto.getName() + dto.getCreatorId() + (k == 0 ? "" : "-" + k) + "." + extension);
//                                zos.putNextEntry(zipEntry);
//                                zos.write(fileData);
//                            } catch (IOException e) {
//                                log.error("personnelInformationPictureExport 下载出错，url:{}", url);
//                            }
//                        }
//                    }
//                }
//            }
//            zos.closeEntry();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (zos != null) {
//                try {
//                    zos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (outStream != null) {
//                try {
//                    outStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//
//        //改名后重新放到压缩包内
////        return null;
//        HttpHeaders header = new HttpHeaders();
//
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(zipName, "utf-8"));
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//
//        FileInputStream fis = new FileInputStream(new File(exportPath + File.separator + exportFileName));
//        InputStreamResource isr = new InputStreamResource(fis);
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(isr);
//    }
//}
