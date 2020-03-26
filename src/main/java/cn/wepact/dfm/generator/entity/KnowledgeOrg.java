package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_org`")
public class KnowledgeOrg {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 知识id外键
     */
    @Column(name = "`knowledge_id`")
    private Integer knowledgeId;

    /**
     * 组织代码
     */
    @Column(name = "`org_code`")
    private String orgCode;
}