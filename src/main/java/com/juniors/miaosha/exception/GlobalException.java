package com.juniors.miaosha.exception;

import com.juniors.miaosha.result.CodeMsg;

/**
 * @author Juniors
 */
public class GlobalException extends RuntimeException{

    public  static final long SerialVersionID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg msg){
        super(msg.toString());
        this.cm = msg;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
