package club.simplecreate.interceptor;

import club.simplecreate.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    TokenCache tokenCache;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        //利用该token从redis中判断是否存在
        String openid=tokenCache.getToken(token);
        //不存在则失败
        if(openid==null) {
            //返回错误信息
            PrintWriter pw = httpServletResponse.getWriter();
            pw.write("{status:100}");
            return false;
        }
        //存在则通过,并且将openid加入request
        else {
            httpServletRequest.setAttribute("userId", openid);
            return true;
        }
    }
}
