package app.com.tw.monster.security.annotation;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DevOnlyInterceptor implements HandlerInterceptor {

    @Value("${dev.tokens}")
    private String devTokensRaw;

    private Set<String> DEV_TOKENS;

    @PostConstruct
    public void init() {
        DEV_TOKENS = Arrays.stream(devTokensRaw.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod method && method.hasMethodAnnotation(DevOnly.class)) {
            String token = request.getHeader("X-Dev-Token");
            if (!DEV_TOKENS.contains(token)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":false,\"errorCode\":403,\"message\":\"無權限存取此 API\"}");
                return false;
            }
        }
        return true;
    }
}

