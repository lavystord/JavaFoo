Ext.define('Console.controller.panelControllers.KeyActionLogManage', {
    extend: 'Ext.app.Controller',
    statics: {
    },

    refs : [
        {
            selector: 'viewport workspace KeyActionLogManage',
            ref: 'KeyActionLogManage'
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
