package cn.wepact.dfm.dto;



import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class KnowledgeTableDto {

	/**
	 * 知识id
	 */
	private Integer id;

	private Integer countKonledge;

	/**
	 * 知识标题
	 */
	private String KnowledgeTitle;

	/**
	 * 知识分类
	 */
	private String categoryName;

	/**
	 * 关键字
	 */
	private String keyword;

	/**
	 * 组织
	 */
	private String orgName;

	/**
	 * 地域
	 */
	private String cityName;

	/**
	 * 知识类别
	 */
	private String typeName;

	/**
	 * 岗位序列
	 */
	private String jobPositionName;

	/**
	 * 人员类型
	 */
	private String employeetypeName;

	/**
	 * 产品
	 */
	private String productId;
	private String productName;


	/**
	 * 状态
	 */
	private String status;
	private String rejectReason;
	/**
	 * 有效期
	 */
	private String validTime;

	/**
	 * 是否有效
	 */
	private String validFlag;

	/**
	 *来源
	 */
	private String knowledgeFrom;

	/**
	 *修改人
	 */
	private String updateBy;
	private String updatorName;
	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 创建人
	 */
	private String createBy;
	private String creatorName;
	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 是否置顶
	 */
	private String knowledgeTop;
	/**
	 * 知识key
	 */
	private String knowledgeCode;

	private String categoryId;


	private String orgCode;

	private String cityCode;

	private String typeId;

	private String jobPositionCode;

	private String employeetypeId;


	private String knowledgeStatus;


	private Date endTime;

	private String knowledgeDesc;

	private String knowledgeContent;


}
