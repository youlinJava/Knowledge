package cn.wepact.dfm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeCategoryDTO {

    private Integer id;

    /**
     * 父级分类
     */
    private String parentName;

    /**
     * 分类名称
     */
    private String childrenName;

    /**
     * 单位权限
     */
    private String orgName;

    /**
     * 产品
     */
    private String productName;

    /**
     * 产品id
     */
    private String productId;
    
    /**
     * 权限code
     */
    private String orgCode;


}