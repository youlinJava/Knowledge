package cn.wepact.dfm.generator.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_callcenter_sensitive_word`")
public class CallcenterSensitiveWord {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 关键字
     */
    @Column(name = "`key_word`")
    private String keyWord;

    /**
     * 描述
     */
    @Column(name = "`word_desc`")
    private String wordDesc;

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
     * 创建人姓名
     */
    @Column(name = "`creator_name`")
    private String creatorName;

    /**
     * 修改人姓名
     */
    @Column(name = "`updator_name`")
    private String updatorName;

    /**
     * 删除标识,0 未删除 1 删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;
}