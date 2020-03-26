package cn.wepact.dfm.service;

import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.generator.entity.CallcenterWebCustomerSetting;
import cn.wepact.dfm.generator.mapper.CallcenterWebCustomerSettingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
@Slf4j
public class CallcenterWebCustomerSettingService {
    @Resource
    CallcenterWebCustomerSettingMapper callcenterWebCustomerSettingMapper;

    /**
     * 获取渠道设置信息
     * @return
     */
    public CallcenterWebCustomerSetting getCallcenterWebCustomerSetting(){
        return callcenterWebCustomerSettingMapper.selectByPrimaryKey(1);
    }
    /**
     * 修改渠道设置信息
     * @param callcenterWebCustomerSetting
     * @return
     */
    @Transactional
    public BaseRespBean update(CallcenterWebCustomerSetting callcenterWebCustomerSetting) {
        BaseRespBean respBean=new BaseRespBean();
        //修改渠道设置信息
        callcenterWebCustomerSetting.setId(1);
        callcenterWebCustomerSettingMapper.updateByPrimaryKeySelective(callcenterWebCustomerSetting);
        respBean.setSuccessMsg();
        return respBean;
    }


}
