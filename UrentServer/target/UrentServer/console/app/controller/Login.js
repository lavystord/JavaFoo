Ext.define('Console.controller.Login', {
    extend: 'Ext.app.Controller',
    views: [
        'LoginWindow',
        'Main'
    ],

    requires: [
        Ext.util.Cookies
    ],

    refs: [
        {
            selector: 'loginwindow',
            ref: 'loginWindow'
        },
        {
            selector: 'viewport',
            ref: 'viewport'
        }
    ],

    init: function () {
        this.control({
            'loginwindow button[action=login]': {
                click: this.login
            },
            'loginwindow textfield[name=password]': {
                specialkey: function (field, e) {
                    if (e.getKey() == Ext.EventObject.ENTER) {
                        this.login();
                    }
                }
            },
            'loginwindow': {
                render: this.tryAutoLogin
            }
        });
        this.callParent(arguments);
    },


    login: function () {
        var username = this.getLoginWindow().down('textfield[name=username]').getValue();
        var password = this.getLoginWindow().down('textfield[name=password]').getValue();
        var deviceInfo = this.getBrowserInfo().name + ' ' + this.getBrowserInfo().version;

        Ext.Ajax.request({
            url: "login",
            method: "POST",
            scope: this,
            jsonData: {
                "username": username,
                "password": password,
                "device": deviceInfo
            },
            success: function (response, options) {
                var user = Ext.JSON.decode(response.responseText);
                // console.log(user);
                this.getViewport().remove(this.getLoginWindow());
                this.getViewport().add(Ext.create(this.getMainView(),
                    {
                        user: user
                    }
                ));

                // 将得到的用户信息存放在application上
                this.getApplication().user = user;
                Ext.util.Cookies.set('username', username);
            },
            failure: function (response) {
                console.error(response);
                Ext.MessageBox.alert("error", Ext.JSON.decode(response.responseText).message);
            }
        })
    },

    tryAutoLogin: function () {
        Ext.Loader.syncRequire('Ext.util.Cookies');
        var usernameField = this.getLoginWindow().down('textfield[name=username]');
        var passwordField = this.getLoginWindow().down('textfield[name=password]');
        /*
         尝试session级重登录
         */
        Ext.Ajax.request({
            url: "tryautologin",
            method: "POST",
            scope: this,
            success: function (response, options) {
                var user = Ext.JSON.decode(response.responseText);
                this.getViewport().remove(this.getLoginWindow());
                this.getViewport().add(Ext.create(this.getMainView(), {user: user}));
                this.getApplication().user = user;
                console.log("自动登录成功");
            },
            failure: function (response) {
                console.log("自动登录失败");
                console.log(response);

                var lastUsername = Ext.util.Cookies.get('username');
                if(lastUsername != null && lastUsername != undefined) {
                    usernameField.setValue(lastUsername);
                    passwordField.focus(false, 200);
                }
                else {
                    usernameField.focus(false, 200);
                }
            }
        })
    },

    getBrowserInfo: function () {
        var ua = navigator.userAgent, tem, M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
        if (/trident/i.test(M[1])) {
            tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
            return {name: 'IE', version: (tem[1] || '')};
        }
        if (M[1] === 'Chrome') {
            tem = ua.match(/\bOPR\/(\d+)/)
            if (tem != null) {
                return {name: 'Opera', version: tem[1]};
            }
        }
        M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
        if ((tem = ua.match(/version\/(\d+)/i)) != null) {
            M.splice(1, 1, tem[1]);
        }
        return {
            name: M[0],
            version: M[1]
        };
    }

})