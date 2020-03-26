package cn.wepact.dfm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class knowledgeListDTO {
    private Integer id;


    /**
     * 知识标题
     */
    private String knowledgeTitle;

    /**
     * 分类
     */
    private String categoryName;

    /**
     * 有效期
     */
    private Date validTime;

    /**
     * 关键字用分号分隔
     */
    private String keyword;

    /**
     * 简介
     */
    private String knowledgeDesc;

    /**
     * 状态 0已保存 1 待审核 2 已发布 3已驳回
     */
    private String knowledgeStatus;

    /**
     * 来源，0 知识库 1 工单
     */
    private String knowledgeFrom;

    /**
     * 置顶标识 0 非置顶 1 置顶
     */
    private String knowledgeTop;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 删除标识,0 未删除 1 删除
     */
    private String delFlag;

    /**
     * 内容
     */
    private String knowledgeContent;

    /**
     * 组织
     */
    private String orgName;

    /**
     * 城市
     */
    private String cityName;

    /**
     * 岗位序列
     */
    private String jobPositionCode;

    /**
     * 人员类型
     */
    private String employeetypeId;

    /**
     * 可见范围
     */
    private String visibleTo;

    /**
     * 产品
     */
    private String productName;

    /**
     * 知识类别
     */
    private String typeName;

    /**
     * 是否有效
     */
    private String validFlag;


}
