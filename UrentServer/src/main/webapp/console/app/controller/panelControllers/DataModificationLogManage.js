Ext.define('Console.controller.panelControllers.DataModificationLogManage', {
    extend: 'Ext.app.Controller',
    statics: {
    },

    refs : [
        {
            selector: 'viewport workspace datamodificationlog',
            ref: 'DataModificationLog'
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
