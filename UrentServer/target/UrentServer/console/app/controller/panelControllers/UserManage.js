Ext.define('Console.controller.panelControllers.UserManage', {
    extend: 'Ext.app.Controller',
    statics: {
        // 可能的子grid
        subGrids : ['userRoleManage']
    },

    refs : [
        {
            selector: 'viewport workspace usermanage',
            ref: 'userManage'
        }],

    config:{
        accessControl: [] ,
        subPanelAcls: null
    },

    requires: [
        'Ext.window.Window'
    ],

    constructor: function(acl, children) {
        this.accessControl = acl;
        this.subPanelAcls = children;

        this.callParent(arguments);
    }

});
