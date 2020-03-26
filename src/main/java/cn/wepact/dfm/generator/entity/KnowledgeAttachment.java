package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_attachment`")
public class KnowledgeAttachment {
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
     * 文件路径
     */
    @Column(name = "`url`")
    private String url;

    /**
     * 文件名
     */
    @Column(name = "`name`")
    private String name;
}