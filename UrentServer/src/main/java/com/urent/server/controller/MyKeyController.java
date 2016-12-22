package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.*;
import com.urent.server.service.FileStorageService;
import com.urent.server.service.KeyActionLogService;
import com.urent.server.service.KeyAuthorityVerifyService;
import com.urent.server.service.MyKeyService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by Dell on 2015/8/7.
 */
@RestController
public class MyKeyController {

    @Autowired
    MyKeyService myKeyService;

    @Autowired
    KeyAuthorityVerifyService keyAuthorityVerifyService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    KeyActionLogService keyActionLogService;

    @RequestMapping(value="/myKey/{id}", method= RequestMethod.GET)
    @JsonView(View.MyKeyDetail.class)
    public Key getMyKeyById(@PathVariable("id") Long id, HttpSession session) {
//        boolean hasAKey=false;
        User user=(User)session.getAttribute(GlobalConstant.userConstant);
        keyAuthorityVerifyService.authorityCheck(user,id,null);
        return myKeyService.getMyKeyById(id);
//        Map<String,Object> queryFilter=new HashMap<String, Object>();
//        queryFilter.put("userId",user.getId());
//        List<Key> list = myKeyService.getMyKeys(queryFilter);
////        Long total = myKeyService.getMyKeyCount(queryFilter);
//      for (Iterator<Key> it=list.iterator();it.hasNext();){
//          if (id.longValue()==it.next().getId().longValue()) {
//              hasAKey=true;
//              break;
//          }
//
//      }
//        if (hasAKey) {
//            return myKeyService.getMyKeyById(id);
//        }
//        else throw new USException(USException.ErrorCode.KeyIsOutdate,"123");
        }


    @JsonView({View.MyKeyList.class})
    @RequestMapping(value = "/myKey", method = RequestMethod.GET)
    public Map<String, Object> getMyKeys(@RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit,
                                       @RequestParam(value = "filter", required = false) String filterText,
                                       @RequestParam(value = "sort", required = false) String sortText, HttpSession session) {

        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        User user=(User)session.getAttribute(GlobalConstant.userConstant);
        queryFilter.put("userId",user.getId());
        List<Key> list = myKeyService.getMyKeys(queryFilter);
        Long total = myKeyService.getMyKeyCount(queryFilter);
        /*必须先返回所有钥匙，才能调用count函数，否则count会多计算已过期的钥匙*/
        if(total.intValue()>GlobalConstant.maxActiveKeysPerUser)
            throw  new USException(USException.ErrorCode.TooManyKeysToReturn,"您返回的钥匙数量过多，超过系统支持上限");

        return CommonDataFormatTool.formatListResult(total, list);
    }

    /*todo 未防止重放攻击*/
    @JsonView({View.MyKeyDetail.class})
    @RequestMapping(value ="/myKey/keyWord",method = RequestMethod.POST)
    public WordInfo getKeyWord(@Valid @RequestBody WordInfo wordInfo,HttpSession session) {

        User user=(User)session.getAttribute(GlobalConstant.userConstant);
        Key key=keyAuthorityVerifyService.authorityCheck(user, wordInfo.getKeyId(),null);
             /*若匹配，则根据锁原语查询相应钥匙原语*/
            WordInfo newWordInfo=myKeyService.getKeyWord(wordInfo,key.getLock(),key, user);
            return newWordInfo;
    }


    @RequestMapping(value ="/myKey/keyUsed",method =RequestMethod.POST)
    public Map<String, Boolean> unlockTheLock(@Valid @RequestBody List<UsedInfo> usedInfos, HttpSession session){
        User user = (User) session.getAttribute(GlobalConstant.userConstant);
         myKeyService.unlockTheLockOnce(usedInfos, user);
        return  SuccessNoDataResult.getSuccessResult();
    }


    @RequestMapping(value ="/myKey/{id}",method = RequestMethod.PUT)
    public Key updateMyKey( @RequestBody Key key ,@PathVariable Long id,HttpSession session) {
        if (key.getId()!=null) {
            if (key.getId().longValue()!=id.longValue())
                throw  new USException(USException.ErrorCode.DataConsistencyError,"url和requestBody中id不一致");
        }
        else
            throw new USException(USException.ErrorCode.DataFieldValidateError);

           User user=(User)session.getAttribute(GlobalConstant.userConstant);
           keyAuthorityVerifyService.authorityCheck(user,id,null);
           myKeyService.updateMyKey(key);
           return  key;
    }

    /* todo 增加相应的日志记录*/
    @RequestMapping(value = "/myKey/{id}",method =RequestMethod.DELETE) //id=slaveKeyId
    public Map<String,Boolean> deleteMyKey(@PathVariable Long id,HttpSession session){
        User user=(User)session.getAttribute(GlobalConstant.userConstant);
        keyAuthorityVerifyService.authorityCheck(user,id,Key.typeSlave);
//        if (key.getType().equals(Key.typePrimary))
//            throw  new USException(USException.ErrorCode.ThisKeyCannotDoThisAction);
        myKeyService.deleteMyKey(id);
        return  SuccessNoDataResult.getSuccessResult();
    }

    /*
     * 临时钥匙确认离开接口
     */
    /*@RequestMapping(value = "/myKey/leave", method = RequestMethod.POST)
    public Map<String,Boolean> leaveHouse(@Valid @RequestBody Key key) {
        myKeyService.leaveHouse(key);
        return SuccessNoDataResult.getSuccessResult();
    }*/


    /**
     * 访问一个文件
     * @param id
     * @param response
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/myKey/dfu", method = RequestMethod.GET)
    public void downloadFirmware(@RequestParam(value = "versionId") Long id, HttpServletResponse response) throws IOException {
        String filename = myKeyService.getDfu(id);
        byte[] content = fileStorageService.readFile(filename);
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType(fileStorageService.getContentTypeById(filename));
        response.getOutputStream().write(content);
        response.getOutputStream().flush();

    }

    @RequestMapping(value = "/myKey/dfu", method = RequestMethod.POST)
    public Map<String, Boolean> reportDfu(@Valid @RequestBody DfuInfo dfuInfo,HttpSession session) {
        User user=(User)session.getAttribute(GlobalConstant.userConstant);
       Key oneKey= keyAuthorityVerifyService.authorityCheck(user, dfuInfo.getKeyId(), null);
        myKeyService.reportDfu(dfuInfo,oneKey, user);
        return SuccessNoDataResult.getSuccessResult();
    }


    @JsonView({View.KeyLogResult.class})
    @RequestMapping(value = "/myKey/log", method = RequestMethod.GET)
    public Map<String, Object> getKeyUsedLogs(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                              @RequestParam(value = "keyId")Long keyId,
                                              @RequestParam(value = "filter", required = false) String filterText,
                                              @RequestParam(value = "sort", required = false) String sortText,
                                              HttpSession session) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        List<KeyActionLog> list = keyActionLogService.getKeyUsedLogs(keyId, user, queryFilter);
        Long total = 0L;            // 这个查询暂时不支持total


        return CommonDataFormatTool.formatListResult(total, list);
    }
}
