package cn.wepact.dfm.generator.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge`")
public class Knowledge {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 知识key，用来统一不同版本，采用毫秒级时间戳+2位随机数
     */
    @Column(name = "`knowledge_code`")
    private String knowledgeCode;

    /**
     * 版本号
     */
    @Column(name = "`version_num`")
    private String versionNum;

    /**
     * 知识分类外键
     */
    @Column(name = "`category_id`")
    private Integer categoryId;

    /**
     * 知识标题
     */
    @Column(name = "`knowledge_title`")
    private String knowledgeTitle;

    /**
     * 有效期
     */
    @Column(name = "`valid_time`")
    private Date validTime;

    /**
     * 可见范围 普通员工，专业用户，呼叫中心 非共享员工，按位与1111
     */
    @Column(name = "`visible_to`")
    private String visibleTo;

    /**
     * 产品外键
     */
    @Column(name = "`product_id`")
    private Integer productId;

    /**
     * 知识类别外键
     */
    @Column(name = "`type_id`")
    private Integer typeId;

    /**
     * 关键字用分号分隔
     */
    @Column(name = "`keyword`")
    private String keyword;

    /**
     * 简介
     */
    @Column(name = "`knowledge_desc`")
    private String knowledgeDesc;

    /**
     * 状态 0已保存 1 待审核 2 已发布 3已驳回
     */
    @Column(name = "`knowledge_status`")
    private String knowledgeStatus;

    /**
     * 来源，0 知识库 1 工单
     */
    @Column(name = "`knowledge_from`")
    private String knowledgeFrom;

    /**
     * 置顶标识 0 非置顶 1 置顶
     */
    @Column(name = "`knowledge_top`")
    private String knowledgeTop;

    /**
     * 驳回原因
     */
    @Column(name = "`reject_reason`")
    private String rejectReason;

    /**
     * 创建人
     */
    @Column(name = "`create_by`")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "`update_by`")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 创建人名
     */
    @Column(name = "`creator_name`")
    private String creatorName;

    /**
     * 产品名
     */
    @Column(name = "`product_name`")
    private String productName;

    /**
     * 删除标识,0 未删除 1 删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

    /**
     * 内容
     */
    @Column(name = "`knowledge_content`")
    private String knowledgeContent;

    /**
     * 更新人名
     */
    @Column(name = "`updator_name`")
    private String updatorName;
}