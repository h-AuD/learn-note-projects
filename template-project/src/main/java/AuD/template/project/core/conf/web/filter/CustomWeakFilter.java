package AuD.template.project.core.conf.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: TODO
 *
 * @author AuD/胡钊
 * @ClassName CustomWeakFilter
 * @date 2021/7/13 9:43
 * @Version 1.0
 */
@Slf4j
@Component
@Order(value = Integer.MAX_VALUE-10)
public class CustomWeakFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("this filter class is {},and request-url:{}",this.getClass().getName(),request.getRequestURL());
        filterChain.doFilter(request,response);
    }


}
