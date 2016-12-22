package com.urent.server.util;

import com.urent.server.domain.Key;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/26.
 */
public class SuccessNoDataResult {
    static final public Map<String, Boolean> getSuccessResult() {
        return Collections.singletonMap("success", true);
    }

}
