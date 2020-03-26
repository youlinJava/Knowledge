package cn.wepact.dfm.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Getter
@Setter
public class Pagination<T> {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    protected int pageNo = 1;
    protected int pageSize = 10;
    protected String orderBy = "id";
    protected String order = "desc";
    protected List<T> result = new ArrayList<>();
    protected long totalCount = -1L;
    @JsonIgnore
    private int start;
    public Map<String, Object> parameterMap = new HashMap<>();

    public int getStart() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public Pagination<T> setResult(List<T> result) {
        this.result = result;
        return this;
    }

    public Pagination<T> setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

}
