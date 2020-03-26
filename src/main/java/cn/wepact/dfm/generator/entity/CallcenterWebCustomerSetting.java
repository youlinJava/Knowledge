package cn.wepact.dfm.generator.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "`t_callcenter_web_customer_setting`")
public class CallcenterWebCustomerSetting {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 网页客服开关 0关 1开
     */
    @Column(name = "`web_customer_flg`")
    private String webCustomerFlg;

    /**
     * 非即时咨询开关 0关 1开
     */
    @Column(name = "`non_instant_consultation`")
    private String nonInstantConsultation;

    /**
     * 排队提示语
     */
    @Column(name = "`queue_prompt`")
    private String queuePrompt;

    /**
     * 接通欢迎语
     */
    @Column(name = "`welcome_msg`")
    private String welcomeMsg;

    /**
     * 客服超时时间
     */
    @Column(name = "`service_timeout`")
    private Integer serviceTimeout;

    /**
     * 客服超时提示语
     */
    @Column(name = "`service_timeout_msg`")
    private String serviceTimeoutMsg;

    /**
     * 访客超时时间
     */
    @Column(name = "`visitor_timeout`")
    private Integer visitorTimeout;

    /**
     * 访客超时提示语
     */
    @Column(name = "`visitor_timeout_msg`")
    private String visitorTimeoutMsg;

    /**
     * 访客超时语重复次数
     */
    @Column(name = "`visitor_timeout_repeat`")
    private Integer visitorTimeoutRepeat;

    /**
     * 访客超时语重复间隔（秒）
     */
    @Column(name = "`visitor_timeout_repeat_interval`")
    private Integer visitorTimeoutRepeatInterval;

    /**
     * 转移对话提示语
     */
    @Column(name = "`conversation_transfer_prompt`")
    private String conversationTransferPrompt;

    /**
     * 接管对话提示语
     */
    @Column(name = "`conversation_accept_prompt`")
    private String conversationAcceptPrompt;

    /**
     * 客服断线提示语
     */
    @Column(name = "`service_disconnect_prompt`")
    private String serviceDisconnectPrompt;

    /**
     * 访客断线提示语
     */
    @Column(name = "`visitor_disconnect_prompt`")
    private String visitorDisconnectPrompt;

    /**
     * 客服关闭会话提示语
     */
    @Column(name = "`service_session_close_prompt`")
    private String serviceSessionClosePrompt;

    /**
     * 访客客服关闭会话提示语
     */
    @Column(name = "`visitor_session_close_prompt`")
    private String visitorSessionClosePrompt;

    /**
     * 系统关闭会话提示语
     */
    @Column(name = "`system_close_sesson_prompt`")
    private String systemCloseSessonPrompt;

    /**
     * 非工作时间提示语
     */
    @Column(name = "`non_worktime_prompt`")
    private String nonWorktimePrompt;

    /**
     * 敏感词提示语
     */
    @Column(name = "`sensitive_words_prompt`")
    private String sensitiveWordsPrompt;

    /**
     * 敏感词屏蔽方式 0关键词屏蔽 1整句屏蔽
     */
    @Column(name = "`sensitive_words_screening_way`")
    private String sensitiveWordsScreeningWay;
}