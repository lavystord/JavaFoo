Ext.define('Console.view.Viewport', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.layout.container.Fit',
        'Console.view.Main'
    ],

    layout: {
        type: 'fit'
    },

    items: [{
        //xtype: 'app-main'
        xtype: 'loginwindow'
    }]
});
