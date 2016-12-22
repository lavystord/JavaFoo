package com.urent.server.util;

import com.urent.server.USException;
import com.urent.server.domain.Key;

import java.util.Date;

/**
 * Created by Administrator on 2015/10/22.
 */
public class KeyIsExpired {

    public static boolean checkKeyIsExpired(Key key) {
        //检查钥匙是否过期
        if (key.getExpiredDate()!=null) {
            Date currentDate = new Date();
            if (key.getType().equals(Key.typeTemp)){
             //tempKey
                switch (key.getStatus().intValue()) {
                    case Key.statusActive:
                        if (currentDate.after(key.getExpiredDate())) {
                            key.setStatus(Key.statusExpiredButNotUse);
                            key.setUpdateDate(currentDate);
                            return true;
                        }
                        else return false;

                    case Key.statusInUse:
                        if (currentDate.after(key.getExpiredDate())) {
                            key.setStatus(Key.statusInUseAndOverTime);
                            key.setUpdateDate(currentDate);
                            return true;
                        }
                        else return false;

                    case Key.statusInUseAndOverTime:
                        return true;
                    default:
                        return false;
                }
            }
            //slaveKey and primaryKey
            else {
                if (currentDate.after(key.getExpiredDate())) {
                    //expired
                    key.setStatus(Key.statusInactive);
                    key.setUpdateDate(currentDate);
                    return true;
                }
                else
                    return false;
            }



        }//end of expiredDate!=null

        else
            return false;
    }

}
