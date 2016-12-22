Ext.define('Console.view.panelViews.MenuManage', {
    extend: 'Ext.panel.Panel',
    // extend: 'Ext.grid.Panel',
    title: "菜单管理",

    requires: [
        'Console.model.Menu',
        'Console.model.AllAppMenuTree' ,
        'Ext.tree.*',
        'Ext.data.*'
    ],

    alias: 'widget.menumanage',

    layout: 'column',

    items: [
        {
            id: 'menutreepanel',
            xtype: 'treepanel',
            title: '菜单子树',
            columns: [
                {xtype: 'treecolumn', text: '菜单名', dataIndex: 'name', flex: 1},
                {text: 'id', dataIndex: 'id', flex: 1},
                {text: '目的路径', dataIndex: 'dest', flex: 2}
            ],
            rootVisible: false,
            store: {
                model: 'Console.model.AllAppMenuTree',
                autoLoad: true
            },
            columnWidth: 0.5,
            padding: '10, 10, 10, 10'
       },
        {
            id: 'subwindowpanel',
            xtype: 'gridpanel',
            title: '功能窗口子树',
            columns: [
                {text: 'id', dataIndex: 'id', flex: 1},
                {text: '菜单名', dataIndex: 'name', flex: 1},
                {text: '目的路径', dataIndex: 'dest', flex: 2}
            ] ,
            store: {
                model: 'Console.model.Menu',
                proxy: null,
                autoLoad: false
            },
            columnWidth: 0.5 ,
            padding: '10, 10, 10, 10'
        }
    ],

    initComponent: function() {
        this.callParent(arguments);
    }    ,

    afterRender: function() {
        this.callParent(arguments);
    }
});