package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_region`")
public class KnowledgeRegion {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 知识id
     */
    @Column(name = "`knowledge_id`")
    private Integer knowledgeId;

    /**
     * 城市code
     */
    @Column(name = "`city_code`")
    private String cityCode;

    /**
     * 城市名
     */
    @Column(name = "`city_name`")
    private String cityName;
}