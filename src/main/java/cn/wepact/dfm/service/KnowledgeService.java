package cn.wepact.dfm.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.common.util.BaseRespBean;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeAttachmentMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeEmployeetypeMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeJobpositionMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeOrgMapper;
import cn.wepact.dfm.customize.mapper.MoreKnowledgeRegionMapper;
import cn.wepact.dfm.dto.KnowledgeDto;
import cn.wepact.dfm.dto.KnowledgeHistory;
import cn.wepact.dfm.dto.KnowledgeTableDto;
import cn.wepact.dfm.generator.entity.Knowledge;
import cn.wepact.dfm.generator.entity.KnowledgeAttachment;
import cn.wepact.dfm.generator.entity.KnowledgeEmployeetype;
import cn.wepact.dfm.generator.entity.KnowledgeJobposition;
import cn.wepact.dfm.generator.entity.KnowledgeOrg;
import cn.wepact.dfm.generator.entity.KnowledgeRegion;
import cn.wepact.dfm.generator.mapper.KnowledgeAttachmentMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeEmployeetypeMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeJobpositionMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeOrgMapper;
import cn.wepact.dfm.generator.mapper.KnowledgeRegionMapper;
import cn.wepact.dfm.vo.KnowledgeVo;
import tk.mybatis.mapper.util.StringUtil;

@Service
public class KnowledgeService {
	
	@Autowired
	KnowledgeMapper knowledgeMapper;
	
	@Autowired
	MoreKnowledgeMapper moreKnowledgeMapper;
	
	@Autowired
	MoreKnowledgeRegionMapper moreKnowledgeRegionMapper;
	
	@Autowired
	KnowledgeRegionMapper knowledgeRegionMapper;
	
	@Autowired
	MoreKnowledgeOrgMapper moreKnowledgeOrgMapper;
	
	@Autowired
	KnowledgeOrgMapper knowledgeOrgMapper;
	
	@Autowired
	KnowledgeJobpositionMapper knowledgejobpositionMapper;
	
	@Autowired
	MoreKnowledgeJobpositionMapper moreKnowledgeJobpositionMapper;
	
	@Autowired
	KnowledgeEmployeetypeMapper knowledgeEmployeetypeMapper;
	
	@Autowired
	MoreKnowledgeEmployeetypeMapper moreKnowledgeEmployeetypeMapper;
	
	@Autowired
	MoreKnowledgeAttachmentMapper moreKnowledgeAttachmentMapper;
	
	@Autowired
	KnowledgeAttachmentMapper knowledgeAttachmentMapper;

	public Knowledge getOneByConditions(Knowledge paramObj) {
		return knowledgeMapper.selectOne(paramObj);
	}
	/**
     * 新增知识
	* @param advertising
	* @return
	*/
	@Transactional
	public BaseRespBean insert(KnowledgeVo knowledgeVo) {
		BaseRespBean respBean=new BaseRespBean();
		Knowledge kn = knowledgeVo.getKnowledge();
		String versionNum = getVersionNum();
		kn.setVersionNum(versionNum);
		if(StringUtil.isEmpty(kn.getKnowledgeCode())) {
			String knowledgeCode = getKnowledgeCode();
			kn.setKnowledgeCode(knowledgeCode);
		}
		kn.setCreateTime(new Date());
		kn.setUpdateTime(new Date());
		knowledgeMapper.insertSelective(kn);
		//添加知识地域关联
		insertCities(kn,knowledgeVo);
		//添加知识组织代码关联
		insertOrg(kn,knowledgeVo);
		//添加知识岗位序列关联
		insertJobposition(kn,knowledgeVo);
		//添加知识员工类型关联
		insertEmployeetype(kn,knowledgeVo);
		//添加知识附件
		insertAttachment(kn,knowledgeVo);
		respBean.setSuccessMsg();
    	return respBean;
	}
	/**
	 * 生成知识版本号
	 * @return
	 */
	private String getVersionNum() {
		return moreKnowledgeMapper.getVersionNum();
	}
	/**
	 * 生成知识key
	 * @return
	 */
	private String getKnowledgeCode() {
		String knowledgeCode = String.valueOf(new Date().getTime());
		int num = new Random().nextInt(100);
		knowledgeCode +=num;
		return knowledgeCode;
	}
	
	/**
	 * 知识和城市关联
	 * @param kn
	 * @param knowledgeVo
	 */
	private void insertCities(Knowledge kn,KnowledgeVo knowledgeVo) {
		moreKnowledgeRegionMapper.delectByKnowledgeId(kn.getId());
		for (KnowledgeRegion region : knowledgeVo.getKnowledgeRegionList()) {
			region.setKnowledgeId(kn.getId());
			knowledgeRegionMapper.insertSelective(region);
		}
	}
	/**
	 * 知识和组织代码关联
	 */
	private void insertOrg(Knowledge kn,KnowledgeVo knowledgeVo) {
		moreKnowledgeOrgMapper.delectByKnowledgeId(kn.getId());
		for (KnowledgeOrg org : knowledgeVo.getKnowledgeOrgList()) {
			org.setKnowledgeId(kn.getId());
			knowledgeOrgMapper.insertSelective(org);
		}
	}
	/**
	 * 知识和上传文件关联
	 */
	private void insertAttachment(Knowledge kn , KnowledgeVo vo) {
		moreKnowledgeAttachmentMapper.delectByKnowledgeId(kn.getId());
		for(KnowledgeAttachment att : vo.getAttachmentList()) {
			att.setKnowledgeId(kn.getId());
			knowledgeAttachmentMapper.insertSelective(att);
		}
	}
	
	
	/**
	 * 知识和岗位关联
	 * @param kn
	 * @param knowledgeVo
	 */
	private void insertJobposition(Knowledge kn,KnowledgeVo knowledgeVo) {
		moreKnowledgeJobpositionMapper.delectByKnowledgeId(kn.getId());
		for (KnowledgeJobposition jobposition : knowledgeVo.getJobpositionList()) {
			jobposition.setKnowledgeId(kn.getId());
			knowledgejobpositionMapper.insertSelective(jobposition);
		}
	}
	/**
	 * 知识和员工类型关联
	 * @param kn
	 * @param knowledgeVo
	 */
	private void insertEmployeetype(Knowledge kn,KnowledgeVo knowledgeVo) {
		moreKnowledgeEmployeetypeMapper.delectByKnowledgeId(kn.getId());
		for (KnowledgeEmployeetype employeetype : knowledgeVo.getEmployeetypeList()) {
			employeetype.setKnowledgeId(kn.getId());
			knowledgeEmployeetypeMapper.insertSelective(employeetype);
		}
	}
	
	/**
     * 修改知识
	* @param advertising
	* @return
	*/
	@Transactional
	public BaseRespBean update(KnowledgeVo knowledgeVo) {
		BaseRespBean respBean=new BaseRespBean();
		Knowledge kn = knowledgeVo.getKnowledge();
		kn.setUpdateTime(new Date());
		knowledgeMapper.updateByPrimaryKeySelective(kn);
		//添加知识地域关联
		insertCities(kn,knowledgeVo);
		//添加知识组织代码关联
		insertOrg(kn,knowledgeVo);
		//添加知识岗位序列关联
		insertJobposition(kn,knowledgeVo);
		//添加知识员工类型关联
		insertEmployeetype(kn,knowledgeVo);
		//添加知识附件
		insertAttachment(kn,knowledgeVo);
		respBean.setSuccessMsg();
    	return respBean;
	}

	/**
	 * 获取一条知识数据
	 */
	public KnowledgeDto getOne(Integer id) {
		KnowledgeDto dto = moreKnowledgeMapper.getOne(id);
		return dto;
	}
	
	/**
	 * 获取知识分页
	 */
	public Pagination<KnowledgeTableDto> listPaging(Pagination<KnowledgeTableDto> pagination) {
		List<KnowledgeTableDto> lst = moreKnowledgeMapper.list(pagination);
		int totalCount = moreKnowledgeMapper.totalCount(pagination);
		return pagination.setResult(lst).setTotalCount(totalCount);
	}
	
	
	/**
	 * 获取知识分页
	 */
	public Pagination<KnowledgeHistory> historyListPaging(Pagination<KnowledgeHistory> pagination) {
		List<KnowledgeHistory> lst = moreKnowledgeMapper.historylist(pagination);
		int totalCount = moreKnowledgeMapper.historytotalCount(pagination);
		return pagination.setResult(lst).setTotalCount(totalCount);
	}
	/**
	 * 修改知识状态
	 * @param knowledge
	 * @return
	 */
	public BaseRespBean updateKnowledgeStatus(Knowledge knowledge) {
		BaseRespBean respBean=new BaseRespBean();
		knowledge.setUpdateTime(new Date());
		knowledgeMapper.updateByPrimaryKeySelective(knowledge);
		respBean.setSuccessMsg();
    	return respBean;
	}
}
