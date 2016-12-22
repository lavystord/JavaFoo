/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-18
 * Time: 下午5:25
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Console.controller.Logout', {
    extend: 'Ext.app.Controller' ,
    views: [
        'LoginWindow'
    ],

    refs: [
        {
            selector: 'app-main',
            ref: 'main'
        },
        {
            selector: 'viewport',
            ref: 'viewport'
        }
    ],

    init: function(){
        this.control({
            'app-main button[id=logout]' : {
                click: this.logout
            }
        });
        this.callParent(arguments);
    },


    logout: function(){
        Ext.Ajax.request({
            url: "logout",
            method: "POST",
            scope: this,
            jsonData: this.getApplication().user ,
            success: function(response, options) {
                // Ext.util.Cookies.clear(Console.config.Config.crowdTokeCookieName);
                this.getViewport().removeAll();
                this.getViewport().add(Ext.create(this.getLoginWindowView()));
            },
            failure: function(response) {
                console.error(response);
            }
        })
    }

});
