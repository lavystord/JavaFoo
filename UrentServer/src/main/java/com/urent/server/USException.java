package com.urent.server;

import com.urent.server.util.GlobalConstant;

/**
 * Created by Administrator on 2015/7/23.
 */
public class USException extends RuntimeException {
    public enum ErrorCode {
        RegisterCodeInvalid(1, "验证码未能通过"),
        DuplicateKey(2, "键值重复"),
        IllegalDataFormat(3, "数据格式不能识别"),
        NoSuchUser(4, "找不到相应的用户"),
        PasswordVerifyError(5, "密码验证失败"),
        UserNotLoggedIn(6, "用户没有登陆"),
        DataFieldValidateError(7, "提交数据有域不符合规范"),
        RequestUnauthorized(8, "请求了超越用户权限的数据"),
        NonexistentObjectId(9, "请求的对象不存在"),
        FileTypeNotAcceptable(10, "文件类型不能被识别或者不能被接受"),
        FileIdNotFound(11, "文件没有找到"),
        FileAccessException(12, "文件访问时发生错误"),
        DeleteReferentResource(13, "要求删除的资源被有效引用"),
        IllegalRequestParam(14, "请求的参数非法"),
        SystemRunEnvironmentError(15, "运行环境发生异常"),
        UserWithSameMobileExists(16, "已有相同手机号的用户存在，请确认是否已经注册，并在无法解决问题时联系我们"),
        NoSuchVersion(17,"不存在当前版本"),

        TooManyKeysToReturn(101, "当前钥匙数目超过" + GlobalConstant.maxActiveKeysPerUser + "，暂时不支持"),
        KeyIsOutdate(102, "钥匙已经失效"),
        ExceedSharedMaxCount(103, "分享钥匙数目超过当前系统上限" + GlobalConstant.maxSharedCountPerKey + "，暂时不支持"),
        //AddressAssociatedWithHouse(104,"地址和房屋存在关联"),
        // PrimaryKeyExistsOnSameLock(105, "锁上已经存在一把Primary的钥匙"),
        AvailableKeyExistsOnLock(106, "锁上存在活跃钥匙"),
        KeyExistsOnLock(107, "锁上存在钥匙"),
       // PrimaryKeyExistsOnLockForSameUser(108, "锁上存在同用户的主钥匙"),
        //PrimaryKeyBelongsToPreviousOwner(109, "前房主仍然拥有锁上的钥匙"),
        LockExistsOnHouse(110, "房屋上已存在锁"),
        OwnerExistsOnHouse(111, "房屋存在主人"),
        HouseWithSameAddressExists(112, "存在完全相同地址（小区、幢、单元、门牌号）的房屋"),
        KeyStatusUnsatisfied(113, "钥匙的状态与请求不相应"),
//        KeyNotBelongtoCurrentUser(113,"所请求钥匙不属于当前登录用户"), //归纳到RequestUnauthorized
//        LockWordisNull(114,"发送的锁原语为空"),  //归纳到DataFieldValidateError
//        KeyIdValueBeyondtheBoundary(115,"不存在所请求钥匙"), //归纳到KeyIsOutdate
//        KeyWordMethodAmbiguous(116,"请求钥匙原语方式存在歧义"),   //归纳到DataFieldValidateError
//        LockWordLengthIllegal(117,"锁原语长度非法"),   //归纳到DataFieldValidateError
        DataConsistencyError(118,"请求数据不一致"),    //防止恶意用户和脏数据
        ThisKeyCannotDoThisAction(119,"该类型钥匙不能执行此操作"), //防止恶意用户和脏数据
        SharedIdEqualsItself(120,"双方账户相同，请核对后重试"),
//        UserNotExist(121,"该账户未注册"),//同NoSuchUser
//        KeyCanntShare(122,"该类型钥匙不可被分享"), //同ThisKeyCannotDoThisAction
        ExceedExpiredDate(123,"分享钥匙的有效期超出了原有钥匙的有效期"),
        ActiveLockWithSameGapAddressExists(124, "有相同GAP地址的活跃的锁存在"),
        NameAndCardNotMatch(125,"姓名和身份证号不匹配"),
        DeviceIsAmbiguous(126,"与上次登录设备不一致");



        private final int code;
        private String defaultMessage;
        private String detailMessage;

        ErrorCode(int code, String message) {
            this.code = code;
            this.defaultMessage = message;
        }

        public int getCode() {
            return code;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }

    private  ErrorCode errorCode;

    public USException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public USException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
