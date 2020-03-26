package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_jobposition`")
public class KnowledgeJobposition {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 知识Id外键
     */
    @Column(name = "`knowledge_id`")
    private Integer knowledgeId;

    /**
     * 岗位序列code
     */
    @Column(name = "`job_position_code`")
    private String jobPositionCode;

    /**
     * 岗位序列名字
     */
    @Column(name = "`job_position_name`")
    private String jobPositionName;
}