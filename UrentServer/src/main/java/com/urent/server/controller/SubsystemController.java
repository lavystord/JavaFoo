package com.urent.server.controller;

import com.urent.server.domain.Subsystem;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/3.
 */
@RestController
public class SubsystemController {

    @RequestMapping(value = "/subsystem", method = RequestMethod.POST)
    public Subsystem addSubsystem(@Valid @RequestBody Subsystem subsystem) {
        return null;
    }


    @RequestMapping(value = "/subsystem", method = RequestMethod.GET)
    public Map<String, Object> getSubsystems(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                             @RequestParam(value = "filter", required = false)String filterText) {
        return null;
    }

    @RequestMapping(value = "/subsystem/{id}", method = RequestMethod.GET)
    public Subsystem getSubsystem(@PathVariable("id")Long id) {
        return null;
    }

    @RequestMapping(value = "/subsystem", method = RequestMethod.PUT)
    public Subsystem updateSubsystem(@Valid @RequestBody Subsystem subsystem) {
        return null;
    }

}
