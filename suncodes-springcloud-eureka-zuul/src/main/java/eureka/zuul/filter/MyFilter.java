package eureka.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 测试：http://localhost:9527/scz/p/dept/get/1?token=111
 *
 * 可以通过 zuul.MyFilter.pre.disable=true 来 开启或关闭 过滤器
 */
@Slf4j
@Component
public class MyFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            log.warn("token is empty");
        }
        currentContext.setSendZuulResponse(false);
        currentContext.setResponseStatusCode(401);
        try {
            currentContext.getResponse().getWriter().write("token is empty");
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("ok");
        return null;
    }
}
