package cn.wepact.dfm.customize.mapper;

import cn.wepact.dfm.common.model.Pagination;
import cn.wepact.dfm.generator.entity.CallcenterCommonLanguage;

import java.util.List;
/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
public interface MoreCallcenterCommonLanguageMapper {
	List<CallcenterCommonLanguage> list(Pagination<CallcenterCommonLanguage> param);
	int totalCount(Pagination<CallcenterCommonLanguage> param);
}
