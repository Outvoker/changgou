package exception;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Xu Rui
 * @date 2021/2/20 18:03
 */
@ControllerAdvice
public class BaseExceptionHandler {
    /***
     * 异常处理
     * @param e exception
     * @return  Result
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<?> error(Exception e) {
        e.printStackTrace();
        return new Result<>(false, StatusCode.ERROR, e.getMessage());
    }
}
