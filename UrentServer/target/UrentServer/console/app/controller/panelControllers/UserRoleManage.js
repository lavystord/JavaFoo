Ext.define('Console.controller.panelControllers.UserRoleManage', {
    extend: 'Ext.app.Controller',
    statics: {
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
