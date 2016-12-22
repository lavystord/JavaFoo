package com.urent.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lavystord on 2015/11/17.
 */
public class PatternCheckTool {

//todo 可能未考虑到170等通信小号的情况
    public static boolean isMobile(String data) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(data);
        return m.matches();
    }

    public static boolean isIdCard(String data) {
        Pattern p = Pattern.compile("(\\d{14}\\w)|\\d{17}\\w");
        Matcher m = p.matcher(data);
        return m.matches();
    }

}
