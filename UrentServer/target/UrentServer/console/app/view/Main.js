Ext.define('Console.view.Main', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border',
        'Console.view.Header'
    ],

    alias: 'widget.app-main',

    layout: {
        type: 'border'
    },
    user: null,


    constructor: function(){
        this.callParent(arguments);
    },

    initComponent: function(){
        this.items =[
            {
                region: 'north',
                xtype: 'app-header' ,
                user: this.user
            },
            {
                region: 'west',
                xtype: 'app-menu',
                collapsible: true
                //title: 'west'
                // width: 100
            },
            {
                region: 'center',
                xtype: 'workspace'
            }];
        this.callParent(arguments);
    }


});