package com.urent.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.util.QueryFilter;
import com.urent.server.domain.util.QuerySorter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/24.
 */
public class CommonDataFormatTool {

    private static final String sorterLabel = "sort";

    public static final Map<String, Object> formatQueryFilter(Integer start, Integer limit,
                                                              String filterText, String sortText,
                                                              ObjectMapper objectMapper) {
        Map<String, Object>  map = new HashMap<String, Object>();
        if(start != null) {
            map.put("start", start);
            map.put("limit", limit);
        }

        try {
            if (filterText != null) {
                List<QueryFilter> filters = (List<QueryFilter>) objectMapper.readValue(filterText, new TypeReference<List<QueryFilter>>() {
                });
                Iterator<QueryFilter> iterator = filters.iterator();
                while (iterator.hasNext()) {
                    QueryFilter filter = iterator.next();
                    map.put(filter.getProperty(), filter.getValue());
                }
            }

            if(sortText != null) {

                List<QuerySorter> list = (List<QuerySorter>) objectMapper.readValue(sortText, new TypeReference<List<QuerySorter>>() {
                        });
                assert list.size() == 1;
                map.put(sorterLabel, list.get(0));
            }
            else {
                // 对于大部分的表而言，访问应该都是根据id倒排
                QuerySorter sorter = new QuerySorter();
                sorter.setDirection("desc");
                sorter.setProperty("id");
                map.put(sorterLabel, sorter);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static final Map<String, Object> formatListResult(Long total, List<? extends Object> list){
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("total", total);
        map.put("list", list);

        return map;
    }
}
