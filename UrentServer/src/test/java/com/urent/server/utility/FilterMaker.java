package com.urent.server.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.util.QueryFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 */
public class FilterMaker {

    public static String makeFilter(ObjectMapper objectMapper, List<QueryFilter> list) throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }


/*    public static void main(String[]args) throws JsonProcessingException {
        List<QueryFilter>list = new ArrayList<QueryFilter>();

        QueryFilter filter = new QueryFilter();
        filter.setProperty("parentId");
        filter.setValue(25);
        list.add(filter);

        filter = new QueryFilter();
        filter.setProperty("level");
        filter.setValue("pig");
        list.add(filter);

        System.out.println(makeFilter(new ObjectMapper(), list));

    }*/
}
