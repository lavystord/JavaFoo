/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when upgrading.
*/

Ext.Loader.loadScript({
    url: './packages/tips/examples.js'
});

Ext.Loader.syncRequire('Ext.util.CSS');

Ext.util.CSS.createStyleSheet('', 'tipsexample');
Ext.util.CSS.swapStyleSheet('tipsexample', 'packages/tips/example.css') ;


// hack 重写proxy.Ajax方法，对出错进行统一处理
Ext.Loader.syncRequire('Ext.data.proxy.Ajax');
//Ext.Loader.syncRequire('Console.config.Config');
Ext.override(Ext.data.proxy.Ajax, {
    doRequest: function (operation, callback, scope) {
        var writer = this.getWriter(),
            request = this.buildRequest(operation, callback, scope);

        if (operation.allowWrite()) {
            request = writer.write(request);
        }

        Ext.apply(request, {
            headers: this.headers,
            timeout: this.timeout,
            scope: this,
            callback: this.createRequestCallback(request, operation, callback, scope),
            method: this.getMethod(request),
            disableCaching: false,

            failure: function (resp) {
                Console.config.Config.ajaxFailure(resp)
            }
        });
        Ext.Ajax.request(request);
        return request;
    }
});

Ext.application({
    name: 'Console',

    extend: 'Console.Application',
    
    autoCreateViewport: true
});
