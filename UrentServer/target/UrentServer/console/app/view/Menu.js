Ext.define("Console.view.Menu", {
    extend: 'Ext.tree.Panel',
    requires: [
        'Ext.tree.*',
        'Ext.data.*',
        'Console.model.AppMenuTree'
    ],
    alias: "widget.app-menu",
    id: 'app-menu',
    title: "工作菜单",
    width: 200,
    rootVisible: false,
    useArrows: true,
/*
   // store: 'AppMenu',
    store: Ext.create('Ext.data.TreeStore', {
       model: 'Console.model.AppMenuTree'
    }),
    columns: [
        {xtype: 'treecolumn', text: '',  dataIndex: 'name', flex: 1}
     ]*/

    initComponent: function () {
        this.store =  Ext.create('Ext.data.TreeStore', {
            model: 'Console.model.AppMenuTree'
        });
        this.columns =  [
            {xtype: 'treecolumn', text: '',  dataIndex: 'name', flex: 1}
        ];
        this.callParent(arguments);
    }
});