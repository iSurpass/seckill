package com.juniors.miaosha.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * @author Juniors
 */
public class VaildatorUtil {

    private static final Pattern mobile_patter = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = mobile_patter.matcher(src);
        return m.matches();
    }

}
