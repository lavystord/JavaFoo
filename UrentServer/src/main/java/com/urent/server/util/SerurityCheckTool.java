package com.urent.server.util;

/**
 * Created by Dell on 2015/9/11.
 */
public class SerurityCheckTool {

    public static boolean sql_inj(String str)
    {
        String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
        String inj_stra[] = inj_str.split("\\|");
        for (int i=0 ; i<inj_stra.length ; i++ )
        {
            if (str.indexOf(inj_stra[i])!=-1)
            {
                return true;
            }
        }
        return false;
    }
}
