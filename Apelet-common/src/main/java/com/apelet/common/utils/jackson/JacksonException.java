package com.apelet.common.utils.jackson;

/**
 * @author xiaoyuan-zs
 */
public class JacksonException extends RuntimeException {

    public JacksonException(String message, Exception e) {
        super(message, e);
    }

}
