package com.urent.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.AppMenu;
import com.urent.server.domain.Menu;
import com.urent.server.domain.User;
import com.urent.server.service.AppMenuService;
import com.urent.server.util.CommonDataFormatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-11
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class AppMenuController {

    @Autowired
    AppMenuService appMenuService;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/appmenu", method = RequestMethod.GET)
    @ResponseBody
    public List<AppMenu> getAppMenu(HttpSession session) {
        User user = (User) session.getAttribute("user");

        return appMenuService.getAppMenu(user);
    }

    @RequestMapping(value = "/allappmenu", method = RequestMethod.GET)
    @ResponseBody
    public List<AppMenu> getAllAppMenu() {
        return appMenuService.getFullAppMenuWithoutAcl();
    }

    @RequestMapping(value = "/menuUnrelated", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUnrelatedMenus(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                        @RequestParam(value = "filter", required = true)String filterText,
                                        @RequestParam(value = "sort", required = false) String sortText) {

        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<Menu> list = appMenuService.getUnrelatedMenus(queryFilter);
        Long total = appMenuService.getUnrelatedMenuCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }
}
