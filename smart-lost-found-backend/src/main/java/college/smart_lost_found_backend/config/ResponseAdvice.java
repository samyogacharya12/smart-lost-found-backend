package college.smart_lost_found_backend.config;

import college.smart_lost_found_backend.dto.RestResponse;
import college.smart_lost_found_backend.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.zalando.problem.DefaultProblem;

@Slf4j
@RestControllerAdvice(basePackages = "college.smart_lost_found_backend.api")
public class ResponseAdvice  implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public RestResponse beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof RestResponse)
            return (RestResponse) body;
        else if (body instanceof DefaultProblem)
            return ResponseUtil.getErrorResponse((DefaultProblem) body, ResponseUtil.getResponseMessage(response, false));
        else
            return ResponseUtil.getSuccessResponse(body, ResponseUtil.getResponseMessage(response, true));
    }
}
