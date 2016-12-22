/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-3
 * Time: 上午9:28
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Console.controller.panelControllers.MenuManage', {
    extend: 'Ext.app.Controller',
    config:{
        accessControl: null ,
        subPanelAcls: null
    },

    refs: [
        {
            selector: 'viewport  workspace menumanage',
            ref: 'menuManage'
        }],

    requires: [
    ],

    constructor: function(acl, children) {
        this.accessControl = acl;
        this.subPanelAcls = children;

        this.callParent(arguments);
    },


    init: function() {
       this.listen({
            component: {
               'menumanage #refresh' : {
                    'click' : function() {
                        this.getMenuManage().down('#menutreepanel').getStore().load();
                    }
                },
                'menumanage #menutreepanel' : {
                    'itemclick': function(view, record) {
                        this.expandSubWindows(record);
                    }
                }
            }
        });
    },

    expandSubWindows: function(record) {
        this.getMenuManage().down('#subwindowpanel').store.removeAll();
        if(record.get('children')){
            this.getMenuManage().down('#subwindowpanel').store.add(record.get('children'));
        }
    }
});
