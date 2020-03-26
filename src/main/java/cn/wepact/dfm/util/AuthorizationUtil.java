package cn.wepact.dfm.util;

import cn.wepact.dfm.HRProcessmaster.OnBorad.onboard.exception.AuthException;
import cn.wepact.dfm.account.client.OrgFeignClient;
import cn.wepact.dfm.account.entity.MoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangbin
 */
@Component
public class AuthorizationUtil {

    @Autowired
    private OrgFeignClient orgFeignClient;

    private ServletRequestAttributes servletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public HttpServletRequest getRequest() {
        return servletRequestAttributes().getRequest();
    }

    public String getToken() {
        return getRequest().getHeader("token");
    }

    public MoreUser getUser() {
        MoreUser user = orgFeignClient.getUserByToken(getToken()).getData();
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthException();
        }
        return user;
    }

    public String getUserNo() {
        MoreUser user = getUser();
        if (ObjectUtils.isEmpty(user)) {
            throw new AuthException();
        }
        return user.getUserAccount();
    }
}
