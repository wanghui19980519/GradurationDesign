package club.simplecreate.config;

import club.simplecreate.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    /**
     * 注入为null的时候，是通过new的方式创建的拦截器，
     * 通过new出来的实例是没有交给spring进行管理的，
     * 没有被spring管理的实例，spring是无法自动注入bean的，所以为null
     */
    @Autowired
    public LoginInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器，拦截除登录，查看文章详情，....请求
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/wxLogin","/user/checkLogin",
                        "/article/selectArticleById", "/article/getCommentList",
                        "/comment/getReplyList","/type/getTypeList",
                        "/article/getTopNList","/article/getNewNList");
    }
}
