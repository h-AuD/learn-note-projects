package AuD.template.project.core.conf.web.filter;

import AuD.component.common.utils.IpUtils;
import AuD.template.project.core.constant.HttpSignatureConstant;
import AuD.template.project.core.exception.SignatureFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


/**
 * Description: 使用{@link OncePerRequestFilter} 作用拦截器
 * {@link OncePerRequestFilter} 支持顺序,即注解{@link Order} 是有效的
 *
 * @author AuD/胡钊
 * @ClassName SignatureFilter
 * @date 2021/4/27 16:59
 * @Version 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class SignatureFilter extends OncePerRequestFilter {

    /**
     *
     * @param request
     * @return true:不拦截请求,false:拦截请求
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String signature = request.getHeader(HttpSignatureConstant.SIGNATURE_PARAM_NAME);
        signature = Optional.ofNullable(signature).orElse("no value");
        if("123".equalsIgnoreCase(signature)){
            filterChain.doFilter(request, response);
        }else {
            log.warn("request signature fail,error signature info is:{},and request IP from:====>{}",signature, IpUtils.getIpAddr(request));

            /** ============================================================================================
             * 签名失败,响应数据处理方式.
             * 1.将该request转发到{@link ExceptionController}上(专门处理error/exception的前端控制器).
             * 并且在request中携带相关异常参数{@link SignatureFailException},异常交由ExceptionController处理.
             * - 或者使用restTemplate来做请求跳转
             * - 上述方式存在缺陷:
             *   需要额外维护一个controller & 常量配置
             *   如何保证this controller仅仅只能被自身调用,而不会被外部(恶意)使用.
             *
             * 2.直接使用response响应(输出)结果,如下:
             * // 设置响应类型为json,输出json数据
             * response.setContentType("application/json; charset=utf-8");
             * response.getWriter().write(JSONObject.toJSONString(ControllerResultInfo.of(SIGNATURE_FAIL.getCode(),SIGNATURE_FAIL.getMessage())));
             * - 不要使用response.setError这种做法,对响应不友好,相当于直接响应抛异常(类似于页面响应500这种).
             *
             * 综上:推荐使用方式二.
             * =================================================================================================*/
            request.setAttribute(HttpSignatureConstant.REQUEST_ATTRIBUTE_NAME_SIGNATURE_FAIL, SignatureFailException.SIGNATURE_FAIL_EXCEPTION);
            request.getRequestDispatcher(HttpSignatureConstant.DISPATCHER_PATH_SIGNATURE_FAIL).forward(request,response);
        }
    }
}

