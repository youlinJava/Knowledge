package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_employeetype`")
public class KnowledgeEmployeetype {
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
     * 员工类型id
     */
    @Column(name = "`employeetype_id`")
    private String employeetypeId;

    /**
     * 员工类型名
     */
    @Column(name = "`employeetype_name`")
    private String employeetypeName;
}