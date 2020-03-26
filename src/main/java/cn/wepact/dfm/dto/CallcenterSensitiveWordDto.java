package cn.wepact.dfm.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallcenterSensitiveWordDto {

	private Integer id;

    /**
     * 关键字
     */
    private String keyWord;

    /**
     * 描述
     */
    private String wordDesc;
    
    /**
     * 屏蔽方式
     */
    private String sensitiveWordsScreeningWay;
    
}
