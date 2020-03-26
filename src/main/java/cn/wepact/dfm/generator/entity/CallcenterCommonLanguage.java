package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_callcenter_common_language`")
public class CallcenterCommonLanguage {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 分类
     */
    @Column(name = "`category`")
    private String category;

    /**
     * 内容
     */
    @Column(name = "`content`")
    private String content;
}