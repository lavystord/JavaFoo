Ext.define('Console.controller.panelControllers.HouseManage', {
    extend: 'Ext.app.Controller',
    statics: {

    },

    refs : [
        {
            selector: 'viewport workspace HouseManage',
            ref: 'HouseManage'
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
