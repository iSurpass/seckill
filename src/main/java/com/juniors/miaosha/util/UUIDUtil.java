package com.juniors.miaosha.util;

import java.util.UUID;

/**
 * @author Juniors
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
