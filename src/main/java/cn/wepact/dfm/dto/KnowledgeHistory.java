package cn.wepact.dfm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KnowledgeHistory {
	
	private Integer id;
	
	private String knowledgeTitle;
	
	private String versionNum;
	
	private String categoryName;
	
	private String keyword;
	
	private String submitter;
	
	private String submitTime;

}
