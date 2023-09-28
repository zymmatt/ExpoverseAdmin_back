package com.example.admin.entity.Response.Exveption;

import com.example.admin.entity.Response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ResponseObject handlerException(Exception e) {
        log.error(e.getMessage());
        return ResponseObject.fail("服务器出错！");
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public ResponseObject handleException(ServiceException e) {
        log.error(e.getMessage());
        return ResponseObject.fail(e.getMessage(), e.getCode());
    }


}
