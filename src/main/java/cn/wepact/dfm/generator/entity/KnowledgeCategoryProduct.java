package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_knowledge_category_product`")
public class KnowledgeCategoryProduct {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 分类Id,外键
     */
    @Column(name = "`category_id`")
    private Integer categoryId;

    /**
     * 关联产品Id,外键
     */
    @Column(name = "`product_id`")
    private String productId;

    /**
     * 关联的产品名字
     */
    @Column(name = "`product_name`")
    private String productName;
}