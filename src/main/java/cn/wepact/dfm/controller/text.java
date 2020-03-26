//package cn.wepact.dfm.HRProcessmaster.OnBorad.leave.controller;
//
//import cn.wepact.dfm.account.entity.MoreUser;
//import io.swagger.annotations.ApiOperation;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.jboss.logging.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.wepact.dfm.HRProcessmaster.OnBorad.commonUtils.BaseRespBean;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.commonUtils.Constant;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.commonUtils.GeneralRespBean;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.leave.entity.more.LeaveApplyData;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.leave.entity.more.LeaveSpecialEmployeeInfoData;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.leave.entity.more.LeaveUploadEmployeeInfoData;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.leave.service.LeaveApplyService;
//import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.util.AuthorizationUtil;
//import cn.wepact.dfm.common.model.Pagination;
//import cn.wepact.dfm.common.util.BaseController;
//
//@RestController
//@RequestMapping("/leave/leaveApply")
//public class LeaveApplyController extends BaseController {
//    @Autowired
//    private LeaveApplyService leaveApplyService;
//    @javax.annotation.Resource
//    private AuthorizationUtil authorizationUtil;
//
//    private final Logger logger = Logger.getLogger(LeaveApplyController.class);
//
//    @ApiOperation(value = "保存或提交离职申请", notes = "")
//    @RequestMapping(value = "/saveLeaveApply", method = RequestMethod.POST)
//    public BaseRespBean saveLeaveApply(@RequestBody LeaveApplyData leaveApply){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        leaveApply.setCreateBy(user.getUserAccount());
//        leaveApply.setUpdateBy(user.getUserAccount());
//        leaveApply.setApplyUser(user.getUserAccount());
//        leaveApply.setApplyUserName(user.getUserName());
//        try{
//            leaveApplyService.saveLeaveApply(leaveApply);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "修改或修改提交离职申请", notes = "")
//    @RequestMapping(value = "/updateLeaveApply", method = RequestMethod.POST)
//    public BaseRespBean updateLeaveApply(@RequestBody LeaveApplyData leaveApply){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.updateLeaveApply(leaveApply,user);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "删除申请", notes = "")
//    @RequestMapping(value = "/deleteLeaveApply", method = RequestMethod.POST)
//    public BaseRespBean deleteLeaveApply(@RequestBody Map<String, String> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            String applyNo = map.get("applyNo");
//            leaveApplyService.deleteLeaveApply(applyNo);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "申请列表分页查询", notes = "")
//    @RequestMapping(value = "/selectLeaveApplyList",method = RequestMethod.POST)
//    public GeneralRespBean<Pagination<LeaveApplyData>> selectLeaveApplyList(@RequestBody Pagination<LeaveApplyData> pagination){
//        GeneralRespBean<Pagination<LeaveApplyData>> bean = new GeneralRespBean<Pagination<LeaveApplyData>>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            Pagination<LeaveApplyData> result = leaveApplyService.selectLeaveApplyList(pagination);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "申请查询", notes = "")
//    @RequestMapping(value = "/selectLeaveApplyByApplyNo",method = RequestMethod.POST)
//    public GeneralRespBean<LeaveApplyData> selectLeaveApplyByApplyNo(@RequestBody Map<String, Object> map){
//        GeneralRespBean<LeaveApplyData> bean = new GeneralRespBean<LeaveApplyData>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            LeaveApplyData result = leaveApplyService.selectLeaveApplyByApplyNo(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    //    @ApiOperation(value = "员工确认申请查询详细信息", notes = "")
////    @RequestMapping(value = "/selectLeaveApplyByJobNo",method = RequestMethod.POST)
////    public GeneralRespBean<LeaveApplyData> selectLeaveApplyByJobNo(@RequestBody Map<String, Object> map){
////        GeneralRespBean<LeaveApplyData> bean = new GeneralRespBean<LeaveApplyData>();
////        MoreUser user = authorizationUtil.getUser();
////        try{
////            String applyNo = leaveApplyService.selectApplyNoByApplyNo(map.get("jobNumber").toString());
////            map.put("applyNo",applyNo);
////            LeaveApplyData result = leaveApplyService.selectLeaveApplyByApplyNo(map);
////            bean.setCode(Constant.SUCCESS_CODE);
////            bean.setMsg(Constant.SUCCESS_MSG);
////            bean.setData(result);
////        }catch (Exception e){
////            e.printStackTrace();
////            bean.setCode(Constant.ERROR_CODE);
////            bean.setMsg(Constant.ERROR_MSG);
////        }
////        return bean;
////    }
//    @ApiOperation(value = "修改申请状态", notes = "")
//    @RequestMapping(value = "/uadateLeaveApplyStatus", method = RequestMethod.POST)
//    public BaseRespBean uadateLeaveApplyStatus(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.uadateLeaveApplyStatus(map);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "保存或提交特殊离职", notes = "")
//    @RequestMapping(value = "/saveSpecialLeave", method = RequestMethod.POST)
//    public BaseRespBean saveSpecialLeave(@RequestBody LeaveSpecialEmployeeInfoData specialEmployeeInfo){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.saveSpecialLeave(specialEmployeeInfo);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "修改或提交特殊离职", notes = "")
//    @RequestMapping(value = "/updateSpecialLeave", method = RequestMethod.POST)
//    public BaseRespBean updateSpecialLeave(@RequestBody LeaveSpecialEmployeeInfoData specialEmployeeInfo){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.updateSpecialLeave(specialEmployeeInfo);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "特殊申请分页查询", notes = "")
//    @RequestMapping(value = "/selectSpecialLeaveList",method = RequestMethod.POST)
//    public GeneralRespBean<Pagination<LeaveSpecialEmployeeInfoData>> selectSpecialLeaveList(@RequestBody Pagination<LeaveSpecialEmployeeInfoData> pagination){
//        GeneralRespBean<Pagination<LeaveSpecialEmployeeInfoData>> bean = new GeneralRespBean<Pagination<LeaveSpecialEmployeeInfoData>>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            Pagination<LeaveSpecialEmployeeInfoData> result = leaveApplyService.selectSpecialLeaveList(pagination);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "根据ID查询特殊申请", notes = "")
//    @RequestMapping(value = "/selectSpecialLeaveById",method = RequestMethod.POST)
//    public GeneralRespBean<LeaveSpecialEmployeeInfoData> selectSpecialLeaveById(@RequestBody Map<String, Object> map){
//        GeneralRespBean<LeaveSpecialEmployeeInfoData> bean = new GeneralRespBean<LeaveSpecialEmployeeInfoData>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            LeaveSpecialEmployeeInfoData result = leaveApplyService.selectSpecialLeaveById(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "api-导入离职申请", notes = "")
//    @RequestMapping(value = "/uploadEmployeeInfoFile", method = RequestMethod.POST)
//    public GeneralRespBean<List<LeaveUploadEmployeeInfoData>> uploadEmployeeInfoFile(HttpServletRequest request) throws Exception {
//        GeneralRespBean<List<LeaveUploadEmployeeInfoData>> resp = new GeneralRespBean<List<LeaveUploadEmployeeInfoData>>();
//        try {
//            String leaveWay = request.getParameter("leaveWay");
//            String userNo= request.getParameter("userNo");
//            String applyNo= request.getParameter("applyNo");
//            resp = leaveApplyService.uploadEmployeeInfoFile(request,userNo,leaveWay,applyNo);
//        } catch (Exception e) {
//            resp.setCode(Constant.SYSTEM_ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "查看导入离职详细信息", notes = "")
//    @RequestMapping(value = "/selectUploadEmployeeInfo",method = RequestMethod.POST)
//    public GeneralRespBean<LeaveUploadEmployeeInfoData> selectUploadEmployeeInfo(@RequestBody Map<String, Object> map){
//        GeneralRespBean<LeaveUploadEmployeeInfoData> bean = new GeneralRespBean<LeaveUploadEmployeeInfoData>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            LeaveUploadEmployeeInfoData result = leaveApplyService.selectUploadEmployeeInfo(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "查看导入离职详细信息列表", notes = "")
//    @RequestMapping(value = "/selectUploadEmployeeInfoList",method = RequestMethod.POST)
//    public GeneralRespBean<List<LeaveUploadEmployeeInfoData>> selectUploadEmployeeInfoList(@RequestBody Map<String, Object> map){
//        GeneralRespBean<List<LeaveUploadEmployeeInfoData>> bean = new GeneralRespBean<List<LeaveUploadEmployeeInfoData>>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            List<LeaveUploadEmployeeInfoData> result = leaveApplyService.selectUploadEmployeeInfoList(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "删除导入离职信息", notes = "")
//    @RequestMapping(value = "/deleteUploadLeaveEmployee", method = RequestMethod.POST)
//    public BaseRespBean deleteUploadLeaveEmployee(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.deleteUploadLeaveEmployee(map);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "取消导入离职信息", notes = "")
//    @RequestMapping(value = "/deleteAllUploadLeaveEmployee", method = RequestMethod.POST)
//    public BaseRespBean deleteAllUploadLeaveEmployee(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.deleteAllUploadLeaveEmployee(map);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "提交批量离职", notes = "")
//    @RequestMapping(value = "/submitUploadLeaveEmployee", method = RequestMethod.POST)
//    public BaseRespBean submitUploadLeaveEmployee(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            String setCode = leaveApplyService.submitUploadLeaveEmployee(map);
//            resp.setCode(setCode);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    @ApiOperation(value = "查询在离职申请中的员工数", notes = "")
//    @RequestMapping(value = "/selectLeaveEmployeeCount",method = RequestMethod.POST)
//    public GeneralRespBean<Integer> selectLeaveEmployeeCount(@RequestBody Map<String, Object> map){
//        GeneralRespBean<Integer> bean = new GeneralRespBean<Integer>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            Integer result = leaveApplyService.selectLeaveEmployeeCount(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "查看批量申请", notes = "")
//    @RequestMapping(value = "/selectBatchLeaveApply",method = RequestMethod.POST)
//    public GeneralRespBean<LeaveApplyData> selectBatchLeaveApply(@RequestBody Map<String, Object> map){
//        GeneralRespBean<LeaveApplyData> bean = new GeneralRespBean<LeaveApplyData>();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            LeaveApplyData result = leaveApplyService.selectBatchLeaveApply(map);
//            bean.setCode(Constant.SUCCESS_CODE);
//            bean.setMsg(Constant.SUCCESS_MSG);
//            bean.setData(result);
//        }catch (Exception e){
//            e.printStackTrace();
//            bean.setCode(Constant.ERROR_CODE);
//            bean.setMsg(Constant.ERROR_MSG);
//        }
//        return bean;
//    }
//
//    @ApiOperation(value = "编辑批量离职", notes = "")
//    @RequestMapping(value = "/editBatchLeaveApply", method = RequestMethod.POST)
//    public BaseRespBean editBatchLeaveApply(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.editBatchLeaveApply(map);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//
//    @ApiOperation(value = "员工取消离职", notes = "")
//    @RequestMapping(value = "/employeeCancelLeave", method = RequestMethod.POST)
//    public BaseRespBean employeeConfirmLeave(@RequestBody Map<String, Object> map){
//        BaseRespBean resp = new BaseRespBean();
//        MoreUser user = authorizationUtil.getUser();
//        try{
//            leaveApplyService.employeeCancelLeave(map);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//    /**
//     * 下载离职申请模版
//     */
//    @GetMapping("/downloadTemplate")
//    @ApiOperation("下载模版")
//    public ResponseEntity<Resource> downloadTemplate() throws IOException {
//        return leaveApplyService.downloadTemplate();
//    }
//    @ApiOperation("下载错误Excel")
//    @GetMapping("/downloadErrorExcel/{errorId}")
//    public ResponseEntity downloadErrorExcel(@PathVariable String errorId) throws IOException {
//        return leaveApplyService.downloadErrorExcel(errorId);
//    }
//
//
//    @ApiOperation(value = "获取该员工是否存在离职待办事项", notes = "")
//    @RequestMapping(value = "/employeeLeaveMatter/{employeeNo}", method = RequestMethod.GET)
//    public GeneralRespBean<List<Map<String,String>>> employeeLeaveMatter(@PathVariable String employeeNo){
//        GeneralRespBean<List<Map<String,String>>>  resp = new GeneralRespBean<List<Map<String,String>>> ();
//        try{
//            List<Map<String,String>> list = leaveApplyService.employeeLeaveMatter(employeeNo);
//            resp.setData(list);
//            resp.setCode(Constant.SUCCESS_CODE);
//            resp.setMsg(Constant.SUCCESS_MSG);
//        }catch (Exception e){
//            e.printStackTrace();
//            resp.setCode(Constant.ERROR_CODE);
//            resp.setMsg(Constant.ERROR_MSG);
//            logger.error(e.getMessage());
//        }
//        return resp;
//    }
//
//}