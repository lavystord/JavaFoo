/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.urlManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Url',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 400,
    height: 200,
    modal: true,
    title: '增加新URL',

    initComponent: function () {
        this.items = [{
            title: '基本资料',
            xtype: 'baseformview',
            gridView: this.gridView,
            model: 'Console.model.Url',
            editable: true,
            layout: 'anchor',
            padding: '5 5 5 5',

            items: [{
                xtype: 'textfield',
                name: 'value',
                fieldLabel: 'URL'
            }]
        }];

        this.buttons = [
            {
                xtype: 'button',
                text: '关闭',
                action: 'close',
                scope: this,
                handler: function () {
                    this.close();
                }
            }
        ];

        this.callParent(arguments);
    },

    afterRender: function () {
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.urlManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Url',
        'Console.view.panelViews.BaseFormView',,
        'Console.view.panelViews.RoleUrlManage',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 450,
    modal: true,
    title: 'URL详细资料',

    initComponent: function () {

        this.items = [{
            xtype: 'tabpanel',
            layout: 'border',
            items: [
                {
                    title: '基本资料',
                    xtype: 'baseformview',
                    record: this.record,
                    gridView: this.gridView,
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [{
                        xtype: 'textfield',
                        name: 'value',
                        fieldLabel: 'URL'
                    }]
                }
            ]
        }];


        // 如果有角色URL子表格则关联
        var roleUrlItem = null;
        for (i in this.children) {
            if (this.children[i].dest == 'RoleUrlManage') {
                roleUrlItem = this.children[i];
            }
        }

        if (roleUrlItem != null) {
            this.items[0].items.push({
                xtype: 'panel',
                title: '关联角色',
                items: {
                    xtype: 'roleurlmanage',
                    url: this.record,
                    acl: roleUrlItem.acl,
                    children: roleUrlItem.children,
                    createView: 'Console.view.panelViews.roleUrlManage.AddItem',
                    defaultPropertiesForAdding: {
                        urlId: this.record.get('id'),
                        urlValue: this.record.get('value')
                    }
                }
            })
        }


        this.buttons = [
            {
                xtype: 'button',
                text: '关闭',
                action: 'close',
                scope: this,
                handler: function () {
                    this.close();
                }
            }
        ];

        this.callParent(arguments);
    },

    afterRender: function () {
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.UrlManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Url',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.urlManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.urlmanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.Url',
            autoLoad: true,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: true
        });
        this.items = [
            {
                region: 'center',
                xtype: 'basegridview',
                acl: this.acl,
                children: this.children,
                detailView: 'Console.view.panelViews.urlManage.Details',
                createView: 'Console.view.panelViews.urlManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [{
                    text: 'Id', dataIndex: 'id', flex: 1
                },
                    {
                    text: 'URL', dataIndex: 'value', flex: 4, editor: {
                        xtype: 'textfield',
                        allowBlank: false
                    }}
                ],

                flex: 7,

                store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    store: store,
                    emptyMsg: '没有数据',
                    displayInfo: true,
                    displayMsg: '当前显示{0}-{1}条记录 / 共{2}条记录 ',
                    beforePageText: '第',
                    afterPageText: '页/共{0}页',
                    nextText: '下一页',
                    prevText: '上一页',
                    lastText: '最后一页',
                    firstText: '第一页',
                    refreshText: '刷新'
                }
            },
            {
                region: 'south',
                xtype: 'basesearchpanel',
                fields: [
                    [
                        {label: 'URL', name: 'value', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
