package cn.wepact.dfm.controller;

import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.common.util.Constant;
import cn.wepact.dfm.common.util.GeneralRespBean;
import cn.wepact.dfm.generator.entity.CallcenterWebCustomerSetting;
import cn.wepact.dfm.service.CallcenterWebCustomerSettingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/callcenterWebCustomerSetting")
public class CallcenterWebCustomerSettingController {

    @Resource
    CallcenterWebCustomerSettingService callcenterWebCustomerSettingService;

    /**
     * 获取渠道设置信息
     * @return
     */
    @ApiOperation(value = "获取渠道设置信息", notes = "")
    @GetMapping("/getCallcenterWebCustomerSetting")
    public GeneralRespBean<CallcenterWebCustomerSetting> getCallcenterWebCustomerSetting() {
        GeneralRespBean<CallcenterWebCustomerSetting> respBean = new GeneralRespBean<CallcenterWebCustomerSetting>();
        try {
            log.info("获取渠道设置信息");
            CallcenterWebCustomerSetting callcenterWebCustomerSetting = callcenterWebCustomerSettingService.getCallcenterWebCustomerSetting();
            respBean.setCode(Constant.SUCCESS_CODE);
            respBean.setMsg(Constant.SUCCESS_MSG);
            respBean.setData(callcenterWebCustomerSetting);
        } catch (Exception e) {
            e.printStackTrace();
            respBean.setCode(Constant.ERROR_CODE);
            respBean.setMsg(Constant.ERROR_MSG);
            log.error(e.getMessage());
        }
        log.info("获取渠道设置信息");
        return respBean;
    }
    /**
     * 修改渠道设置信息
     * @param callcenterWebCustomerSetting
     * @return
     */
    @ApiOperation(value = "修改渠道设置信息", notes = "")
    @RequestMapping("/updateCallcenterWebCustomerSetting")
    public BaseRespBean updateCallcenterWebCustomerSetting(@RequestBody CallcenterWebCustomerSetting callcenterWebCustomerSetting){
        BaseRespBean respBean = new BaseRespBean();
        try{
            log.info("修改渠道设置信息开始");
            respBean = callcenterWebCustomerSettingService.update(callcenterWebCustomerSetting);
            log.info("修改渠道设置信息结束");
        }catch (Exception e) {
            e.printStackTrace();
            respBean.setCode(Constant.ERROR_CODE);
            respBean.setMsg(Constant.ERROR_MSG);
            log.error(e.getMessage());
        }
        return respBean;
    }
}
