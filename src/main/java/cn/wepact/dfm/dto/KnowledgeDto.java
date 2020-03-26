package cn.wepact.dfm.dto;

import java.util.List;

import cn.wepact.dfm.generator.entity.KnowledgeAttachment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeDto {
	
	/**
	 * 知识id
	 */
	private Integer id;
	
	/**
	 * 知识key:用来同意不同版本
	 */
	private String knowledgeCode;
	
	/**
	 * 知识标题
	 */
	private String knowledgeTitle;
	
	private Integer knowledgeStatus;
	
	private String rejectReason;
	
	/**
	 * 知识关联分类id
	 */
	private Integer categoryId;

	/**
	 * 知识关联城市code
	 */
	private String cityCode;
	
	private String cityName;
	/**
	 * 知识关联组织code
	 */
	private String orgCode;
	
	private String orgName;
	
	/**
	 * 有效期
	 */
	private String validTime;
	
	/**
	 * 可见范围
	 */
	private String visibleTo;
	
	/**
	 * 岗位序列
	 */
	private String jobposition;
	
	private String jobpositionName;
	
	/**
	 * 员工类型
	 */
	private String employeetype;
	
	private String employeetypeName;
	/**
	 * 产品外键
	 */
	private Integer productId;
	private String productName;
	/**
	 * 知识类别外键
	 */
	private Integer typeId;
	
	private String typeName;
	/**
	 * 关键字
	 */
	private String keyWord;
	
	/**
	 * 简介
	 */
	private String knowledgeDesc;
	
	/**
	 * 内容
	 */
	private String knowledgeContent;
	
	/**
	 * 上传文件
	 */
	private List<KnowledgeAttachment> fileList;
	
}
