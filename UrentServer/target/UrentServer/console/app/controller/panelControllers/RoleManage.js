Ext.define('Console.controller.panelControllers.RoleManage', {
    extend: 'Ext.app.Controller',
    statics: {
        // 可能的子grid
        subGrids : ['userRoleManage']
    },

    refs : [
        {
            selector: 'viewport workspace rolemanage',
            ref: 'roleManage'
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
