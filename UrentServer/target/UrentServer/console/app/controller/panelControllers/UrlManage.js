Ext.define('Console.controller.panelControllers.UrlManage', {
    extend: 'Ext.app.Controller',
    statics: {
    },

    refs : [
        {
            selector: 'viewport workspace UrlManage',
            ref: 'urlManage'
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
