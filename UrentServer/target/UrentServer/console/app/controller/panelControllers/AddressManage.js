Ext.define('Console.controller.panelControllers.AddressManage', {
    extend: 'Ext.app.Controller',
    statics: {
    },

    refs : [
        {
            selector: 'viewport workspace AddressManage',
            ref: 'AddressManage'
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
