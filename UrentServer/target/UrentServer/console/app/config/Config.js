Ext.define('Console.config.Config', {
    statics : {
        fileUrlPrefix: '/file?id=',

        keyTypePrimary: 'primary',
        keyTypeTemp: 'temp',
        keyTypeSlave: 'slave',

        versionTypeLockStructure: 1,
        versionTypeLockFirmware: 2,
        versionTypeAndroidApp: 101,
        versionTypeIosApp: 201,

        keyStatusActive: 1,
        keyStatusInUse: 2,
        keyStatusInUseAndOverTime: 3,
        keyStatusLent: 4,
        keyStatusInactive: 0,
        keyStatusExpiredButNotUse: -1,
        keyStatusExpiredAnUserCheck: -2,
        keyStatusExpiredAndAdminCheck: -3,

        ajaxFailure: function(resp) {
            if (resp.status == 401) {
                // 刷新页面
                window.location.reload();
            }
            else {
                var message = "";
                if(resp.status == 500) {
                    var error = Ext.JSON.decode(resp.responseText);
                    if(error.errorCode != null && error.errorCode != undefined){
                        message += "出错代码是" + error.errorCode + ",";
                    }

                    if(error.message != null && error.message != undefined) {
                        message += "出错信息是["+ error.message + "]。";
                    }

                    if(message == "") {
                        message = "出错信息不明";
                    }
                }
                else {
                    message += "http请求返回的是" + resp.status;
                }
                Ext.example.msg("error", "向后台请求出错，"+ message);
            }
        }
    }


});