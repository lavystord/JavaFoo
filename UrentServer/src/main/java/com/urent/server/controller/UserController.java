package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.*;
import com.urent.server.domain.util.VerifyCodeInfo;
import com.urent.server.service.FileStorageService;
import com.urent.server.service.RegisterService;
import com.urent.server.service.SecurityService;
import com.urent.server.service.UserService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.RealnameVerify;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/23.
 */
@RestController
public class UserController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    RegisterService registerService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    SecurityService securityService;

    @Value("${supportedImageFileTypes}")
    String supportedImageFileType;

    String [] supportedImageFileTypes;
    /**
     * 用户获得注册码
     * @param getRegisterCodeInfo
     * @return
     */
    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    public Map<String, Boolean> generateRegisterCode(@RequestBody @Valid GetRegisterCodeInfo getRegisterCodeInfo) {
        if(getRegisterCodeInfo.getCheckUnique() == true) {
            User user = userService.getUserByMobile(getRegisterCodeInfo.getMobile());
            if(user != null) {
                throw new USException(USException.ErrorCode.UserWithSameMobileExists);
            }
        }
        registerService.sendRegisterCode(getRegisterCodeInfo.getMobile());

        return SuccessNoDataResult.getSuccessResult();
    }


    /**
     * 用户注册入口
     * @param registerInfo 用户注册信息
     * @return 所注册用户
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @JsonView(View.Detail.class)
    public User registerUser2(@Valid @RequestPart(value = "registerInfo")RegisterInfo registerInfo,
                              @RequestPart(value = "file", required = false)MultipartFile headImageFile, HttpSession session) throws Exception {
        if(registerService.verifyRegisterCode(registerInfo.getUser().getMobile(),
                registerInfo.getRegisterCode(), true) == false){
            throw new USException(USException.ErrorCode.RegisterCodeInvalid);
        }
        else {
            String id = null;
            if(headImageFile != null) {
               id = fileStorageService.saveFile(headImageFile);
            }

            registerInfo.getUser().setHeaderImageId(id);
            registerInfo.getUser().setLastLoginDevice(registerInfo.getDevice());
            User user;
            try {
                user = userService.addUser(registerInfo.getUser());
            }catch (Exception e) {
                if(id != null)
                    fileStorageService.removeFile(id);
                throw e;
            }
            session.setAttribute(GlobalConstant.userConstant, user);
            return user;
        }
    }

    /**
     * 用户登陆入口
     * @param session 当前会话
     * @param loginInfo 登陆信息
     */
    @JsonView({View.Detail.class})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(HttpSession session, @Valid @RequestBody LoginInfo loginInfo) {
       User user;
        try {
           user = userService.checkLogin(loginInfo);
       }
        catch (USException e) {
            if (e.getErrorCode().getCode()==USException.ErrorCode.DeviceIsAmbiguous.getCode())
                securityService.resetConstantKeyWord(loginInfo.getUsername().trim());
            throw e;
        }

        session.setAttribute(GlobalConstant.userConstant, user);
        return user;
    }

    @JsonView({View.Detail.class})
    @RequestMapping(value = "/tryautologin", method = RequestMethod.POST)
    @ResponseBody
    User tryAutoLogin(HttpSession session) {
        if(session.getAttribute("user") != null){
            return (User) session.getAttribute("user");
        }
        throw  new USException(USException.ErrorCode.UserNotLoggedIn);
    }

    /**
     * 用户注销入口
     * @param session 当前会话
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Map<String, Boolean> logout(HttpSession session) {
        if(session.getAttribute(GlobalConstant.userConstant) != null) {
            session.removeAttribute(GlobalConstant.userConstant);
            return SuccessNoDataResult.getSuccessResult();
        }
        else
            throw new USException(USException.ErrorCode.UserNotLoggedIn);
    }

    /**
     * 得到用户列表
     * @param start 开始位置
     * @param limit 查询最大数目
     * @param filterText 过滤条件（暂时不支持）
     * @return 查询结果
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @JsonView(View.Summary.class)
    public Map<String,Object> getUserList(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                           @RequestParam(value = "filter", required = false)String filterText,
                                           @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<User> list = userService.getUserList(queryFilter);
        Long total = userService.getUserCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }

    /**
     * 得到某用户的详细信息
     * @param id 用户id
     * @return 用户详细信息
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("id")Long id) {
        return userService.getUserById(id);
    }


    /**
     *  更新一个用户的信息
     * @param id
     * @param user
     * @return
     * @attention: 这个接口是管理员调用的，用户只能调用单独的更新接口
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public User updateUser(@PathVariable("id")Long id, @RequestBody User user) {
        assert user.getId().equals(id);
        return userService.updateUser(user);
    }


    /**
     * 更新用户密码  untested!!
     * @param updatePasswordInfo
     * @param session
     * @return
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public User updatePassword(@Valid @RequestBody UpdatePasswordInfo updatePasswordInfo, HttpSession session) {
        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        if(registerService.verifyRegisterCode(user.getMobile(),
                updatePasswordInfo.getVerificationCode(),
                true) == false){
            throw new USException(USException.ErrorCode.RegisterCodeInvalid);
        }

        userService.updatePassword(user, updatePasswordInfo);

        return user;
    }

    /**
     * 更新用户手机号  untested!!
     * @param updateMobileInfo
     * @param session
     * @return
     */
    @RequestMapping(value = "/mobile", method = RequestMethod.POST)
    public User updateMobile(@Valid @RequestBody UpdateMobileInfo updateMobileInfo, HttpSession session) {
        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        if(registerService.verifyRegisterCode(updateMobileInfo.getMobile(), updateMobileInfo.getVerificationCode(),
                true) == false){
            throw new USException(USException.ErrorCode.RegisterCodeInvalid);
        }

        userService.updateMobile(user, updateMobileInfo);
        user.setMobile(updateMobileInfo.getMobile());
        session.setAttribute(GlobalConstant.userConstant, user);

        return user;
    }

    /**
     * 更新头像文件   untested!
     * @param session
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/headerImageFile", method = RequestMethod.POST)
    public User updateHeaderImageFile(HttpSession session, @RequestParam MultipartFile file) throws IOException {
        if(isLegalImageFileType(file.getOriginalFilename())){
            User user = (User) session.getAttribute(GlobalConstant.userConstant);

            String id = fileStorageService.saveFile(file);
            String oldId = user.getHeaderImageId();
            User user1 = new User();
            user1.setId(user.getId());
            user1.setHeaderImageId(id);
            userService.updateUser(user1);
            fileStorageService.removeFile(oldId);

            user.setHeaderImageId(id);
            session.setAttribute(GlobalConstant.userConstant, user);

            return user;
        }
        else
            throw new USException(USException.ErrorCode.FileTypeNotAcceptable);
    }

    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public User updateMyInfo(HttpSession session, @RequestBody User user) throws IOException {
        User me = (User) session.getAttribute(GlobalConstant.userConstant);
        if(me.getId() != user.getId()) {
            throw new USException(USException.ErrorCode.RequestUnauthorized, "更新提交的用户ID只能是自己");
        }

        userService.updateUser(user);

        me = userService.getUserById(me.getId());
        session.setAttribute(GlobalConstant.userConstant, me);
        return me;
    }
    /**
     * 访问一个文件
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/headerImageFile", method = RequestMethod.GET)
    public void getFile(@RequestParam(value = "id") String id, HttpServletResponse response) throws IOException {
        byte [] content = fileStorageService.readFile(id);
        response.setContentType(fileStorageService.getContentTypeById(id));
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public Map<String, Boolean> verifyCode(@RequestBody VerifyCodeInfo verifyCodeInfo) {
        if(registerService.verifyRegisterCode(verifyCodeInfo.getMobile(), verifyCodeInfo.getVerificationCode(),
                false) == false){
            throw new USException(USException.ErrorCode.RegisterCodeInvalid);
        }
        return SuccessNoDataResult.getSuccessResult();
    }

    /**
     * 确定文件类型是否支持的图像文件
     * @param fileName
     * @return
     */
    private boolean isLegalImageFileType(String fileName) {
        if(supportedImageFileTypes == null) {
            supportedImageFileTypes = supportedImageFileType.split(",");
        }
        for(String type: supportedImageFileTypes) {
            if(fileName.toLowerCase().endsWith(type)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 根据手机号码定位用户
     * @param mobile 手机号，可以是不完整的，但必须是前缀
     * @return 查询结果
     */
    @RequestMapping(value = "/locateUser", method = RequestMethod.GET)
    @JsonView(View.MyKeyDetail.class)
    public Map<String,Object> locateUser(@RequestParam(value = "mobile", required = true) String mobile) {
        List<User> list = userService.locateUserByMobile(mobile);
        Long total = Long.valueOf(list.size());

        return CommonDataFormatTool.formatListResult(total, list);
    }


    @RequestMapping(value = "/certify",method = RequestMethod.POST)
    public Map<String,Boolean> verifyRealName(@RequestBody User info, HttpSession session) {
        User user=(User)session.getAttribute(GlobalConstant.userConstant);
        if (user.getId().longValue()!=info.getId().longValue())
            throw new USException(USException.ErrorCode.RequestUnauthorized,"您与当前登录用户不一致");
        if (info.getName()!=null && info.getIdCardNumber()!=null) {
            if (RealnameVerify.check(info.getName(),info.getIdCardNumber())){
                userService.updateIdInfo(info);
            }
            else throw new USException(USException.ErrorCode.NameAndCardNotMatch);
        }
        else
            throw new USException(USException.ErrorCode.DataFieldValidateError);
        return SuccessNoDataResult.getSuccessResult();
    }

}
