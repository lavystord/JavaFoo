/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-18
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
Ext.define("Console.view.Header", {
    extend: 'Ext.panel.Header',
    requires: [
    ],
    alias: "widget.app-header",
    id: 'app-header',
    height: 25,
    user: null,
    style:'margin-right:100px;color:black;font-size:12px;font-family:"Helvetica Neue"',

    initComponent: function(){
        // Console.log(this.user);
        this.html =   '欢迎来到MURIOA管理系统' +  this.user.display-name;
        this.items =  [
            {
                xtype: 'button',
                id: 'logout',
                text: '退出'
            }
        ];

        this.callParent(arguments);
    }
});