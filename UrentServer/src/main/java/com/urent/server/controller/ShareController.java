package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.User;
import com.urent.server.domain.View;
import com.urent.server.service.KeyAuthorityVerifyService;
import com.urent.server.service.MyKeyService;
import com.urent.server.service.ShareService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/31.
 */
@RestController
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KeyAuthorityVerifyService keyAuthorityVerifyService;

    @Autowired
    MyKeyService myKeyService;

    @JsonView(View.QueryName.class)
    @RequestMapping(value = "/share/queryName",method = RequestMethod.GET)
    public User getNameByMobile(@RequestParam(value = "mobile") String mobile,HttpSession session) {
    User user=(User)session.getAttribute(GlobalConstant.userConstant);
        return  shareService.getNameByMobile(mobile.trim(),user);
    }


    @JsonView(View.ShareDetail.class)
    @RequestMapping(value = "/share" ,method=RequestMethod.GET)
    public Map<String, Object> getMySharedKeys(@RequestParam(value = "id") Long id,@RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit,
                                         @RequestParam(value = "filter", required = false) String filterText,
                                         @RequestParam(value = "sort", required = false) String sortText, HttpSession session) {

        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        User loginUser=(User)session.getAttribute(GlobalConstant.userConstant);
        keyAuthorityVerifyService.authorityCheck(loginUser,id,Key.typePrimary);
        /*属于当前用户，身份一致*/
            queryFilter.put("primaryKeyId",id);
            List<Key> list = shareService.getMySharedKeys(queryFilter);
//            Long total = shareService.getMySharedKeyCount(queryFilter);
            Long total = shareService.getMySharedKeyCount(queryFilter);
        return CommonDataFormatTool.formatListResult(total, list);

    }

    @JsonView(View.ShareResult.class)
    @RequestMapping(value = "/share",method =RequestMethod.POST)
    public Key addMySharedKey( @RequestBody Key key,HttpSession session){
        User loginUser=(User)session.getAttribute(GlobalConstant.userConstant);
        Key primaryKey =keyAuthorityVerifyService.authorityCheck(loginUser,key.getId(),Key.typePrimary);
        /*属于当前用户，身份一致*/
           /*if (!primaryKey.getType().equals(Key.typePrimary)) {
            *//* not sharable*//*
               throw new  USException(USException.ErrorCode.KeyCanntShare);
           }*/
        if (key.getOwner().getId().longValue()==loginUser.getId().longValue())
            throw new USException(USException.ErrorCode.SharedIdEqualsItself);

            return  shareService.addMySharedKey(key,primaryKey);

    }

    @JsonView(View.ShareResult.class)
    @RequestMapping(value = "/share/{id}",method = RequestMethod.PUT) //id=slaveKeyId
    public Key updateMySharedKey(@PathVariable Long id, @RequestBody Key key,HttpSession session){
        User loginUser=(User)session.getAttribute(GlobalConstant.userConstant);
        if (id.longValue()!=key.getId().longValue())
            throw new USException(USException.ErrorCode.DataConsistencyError,"url和RequestBody中id不一致");
        Key slaveKey =keyAuthorityVerifyService.authorityCheck(loginUser,key.getId(),Key.typeSlave);

      return shareService.updateMySharedKey(key,slaveKey);
    }



    @RequestMapping(value = "/share/{id}",method =RequestMethod.DELETE)
    public Map<String,Boolean> deleteMySharedKey(@PathVariable Long id,HttpSession session){
        User loginUser=(User)session.getAttribute(GlobalConstant.userConstant);
       Key slaveKey= keyAuthorityVerifyService.authorityCheck(loginUser, id, Key.typeSlave);

       /* Key key=myKeyService.getMyKeyById(id);
        Key key1=myKeyService.getKeyInfoById(id);
        *//*type judge first,otherwise maybe encounter null Exception,because of the sharedFrom may not exist*//*
        if (key.getType().equals(Key.typePrimary))
            throw  new USException(USException.ErrorCode.ThisKeyCannotDoThisAction);
        *//*authority check*//*
        if ( user.getId().longValue()!=key.getSharedFrom().getOwner().getId().longValue() )  {
            throw new USException(USException.ErrorCode.RequestUnauthorized);
        }*/
        shareService.deleteMySharedKey(id);
        return  SuccessNoDataResult.getSuccessResult();
    }

}

