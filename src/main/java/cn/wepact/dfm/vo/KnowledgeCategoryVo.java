package cn.wepact.dfm.vo;

import java.util.List;

import cn.wepact.dfm.generator.entity.KnowledgeCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeCategoryVo {
	
	private KnowledgeCategory category;
	
	private List<product> productList;
	
	private List<org> orgList;
	
	@Getter
	@Setter
	public static class org{
		private String orgCode;
		
		private String orgName;
	}
	
	@Getter
	@Setter
	public static class product{
		private Integer code;
		
		private String label;
	}
}
