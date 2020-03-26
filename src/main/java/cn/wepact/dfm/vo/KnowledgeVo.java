package cn.wepact.dfm.vo;

import java.util.List;

import cn.wepact.dfm.generator.entity.Knowledge;
import cn.wepact.dfm.generator.entity.KnowledgeAttachment;
import cn.wepact.dfm.generator.entity.KnowledgeEmployeetype;
import cn.wepact.dfm.generator.entity.KnowledgeJobposition;
import cn.wepact.dfm.generator.entity.KnowledgeOrg;
import cn.wepact.dfm.generator.entity.KnowledgeRegion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeVo {
	
	private Knowledge knowledge;

	private List<KnowledgeRegion> knowledgeRegionList;
	
	private List<KnowledgeOrg> knowledgeOrgList;
	
	private List<KnowledgeJobposition> jobpositionList;
	
	private List<KnowledgeEmployeetype> employeetypeList;
	
	private List<KnowledgeAttachment> attachmentList;
}
