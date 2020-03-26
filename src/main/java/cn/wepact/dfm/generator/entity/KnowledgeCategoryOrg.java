package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_category_org`")
public class KnowledgeCategoryOrg {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 分类表的Id外键
     */
    @Column(name = "`category_id`")
    private Integer categoryId;

    /**
     * 单位code
     */
    @Column(name = "`org_code`")
    private String orgCode;

    /**
     * 单位名称
     */
    @Column(name = "`org_name`")
    private String orgName;
}