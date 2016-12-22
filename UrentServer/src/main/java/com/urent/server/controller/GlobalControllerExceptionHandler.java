package com.urent.server.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.urent.server.USException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.urent.server.USException;
import com.urent.server.USException.ErrorCode;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-14
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);

    private static final String success = "success";
    private static final String errorCode = "errorCode";
    private static final String message = "message";
    private static final String description = "description";

    @Value("${returnDetailExceptionInfo}")
    Boolean returnDeatilExceptionInfo = false;


    /**
     *  将程序生成的错误转化成相应的json数据返回
     * @param response
     * @param e
     * @return
     */
    //@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)    // 402不会被劫持，500会……
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(USException.class)
    @ResponseBody
    public Map<String, Object> handleUSException(HttpServletResponse response, USException e) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        logger.error("[Get USException]", e);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(success, false);
        map.put(errorCode, e.getErrorCode().getCode());
        if(returnDeatilExceptionInfo) {
            map.put(message, e.getMessage());
        }
        else {
            map.put(message, e.getErrorCode().getDefaultMessage());
        }

        return map;
    }

    /**
     *  将程序逻辑之外产生的异常转化成相应的json数据返回
     * @param response
     * @param e
     * @return
     */
    //@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)    // 402不会被劫持，500会……
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleICVException(HttpServletResponse response, Exception e) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        logger.error("[Get handleICVException]", e);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put(success, false);
        if (e.getClass() == DuplicateKeyException.class) {
            DuplicateKeyException duplicateKeyException = (DuplicateKeyException)e;
            map.put(errorCode, ErrorCode.DuplicateKey.getCode());
            map.put(message, e.getMessage());
        }
        else if(e.getClass() == MethodArgumentNotValidException.class) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            map.put(errorCode, ErrorCode.DataFieldValidateError.getCode());
            String errorMessage = ErrorCode.DataFieldValidateError.getDefaultMessage();
            Iterator<FieldError> iterator = methodArgumentNotValidException.getBindingResult().getFieldErrors().iterator();
            while (iterator.hasNext()) {
                errorMessage += "[" + iterator.next().getDefaultMessage() + "]";
            }
            map.put(message, errorMessage);
        }

        return map;
    }
}
