package app.com.tw.monster.security;


import app.com.tw.monster.security.annotation.DevOnlyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DevOnlyInterceptor devOnlyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(devOnlyInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 如果不使用 SecurityConfig 的 CORS 配置，可以啟用這裡的設定
        registry.addMapping("/**")
                .allowedOrigins("*") // 測試環境允許所有來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}