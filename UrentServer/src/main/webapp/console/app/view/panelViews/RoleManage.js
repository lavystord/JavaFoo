/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.roleManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Role',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 300,
    modal: true,
    title: '增加新角色',

    initComponent: function () {
        this.items = [{
            title: '基本资料',
            xtype: 'baseformview',
            gridView: this.gridView,
            model: 'Console.model.Role',
            editable: true,
            layout: 'anchor',
            padding: '5 5 5 5',

            items: [{
                xtype: 'textfield',
                name: 'name',
                fieldLabel: '角色名'
            }, {
                xtype: 'textarea',
                name: 'comment',
                fieldLabel: '备注'
            }, {
                xtype: 'activecombobox',
                name: 'active',
                fieldLabel: '是否活跃'
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


Ext.define('Console.view.panelViews.roleManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Role',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.UserRoleManage',
        'Console.view.panelViews.RoleMenuManage',
        'Console.view.panelViews.RoleUrlManage',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 800,
    height: 500,
    modal: true,
    title: '角色详细资料',

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
                        readOnly: true,
                        name: 'name',
                        fieldLabel: '角色名'
                    }, {
                        xtype: 'textarea',
                        name: 'comment',
                        fieldLabel: '备注'
                    }, {
                        xtype: 'activecombobox',
                        name: 'active',
                        fieldLabel: '是否活跃'
                    }]
                }
            ]
        }];


        var userRoleItem = null;
        var roleUrlItem = null;
        var roleMenuItem = null;
        for (var i in this.children) {
            if (this.children[i].dest == 'UserRoleManage') {
                userRoleItem = this.children[i];
            }
            else if (this.children[i].dest == 'RoleMenuManage') {
                roleMenuItem = this.children[i];
            }
            else if (this.children[i].dest == 'RoleUrlManage') {
                roleUrlItem = this.children[i];
            }
        }


        // 如果有用户角色子表格则关联
        if (userRoleItem != null) {
            this.items[0].items.push({
                xtype: 'panel',
                title: '关联用户',
                items: {
                    xtype: 'userrolemanage',
                    role: this.record,
                    acl: userRoleItem.acl,
                    children: userRoleItem.children
                }
            })
        }


        // 如果有角色菜单子表格则关联
        if (roleMenuItem != null) {
            this.items[0].items.push({
                xtype: 'panel',
                title: '关联菜单',
                items: {
                    xtype: 'rolemenumanage',
                    role: this.record,
                    acl: roleMenuItem.acl,
                    children: roleMenuItem.children,
                    createView: 'Console.view.panelViews.roleMenuManage.AddItem',
                    defaultPropertiesForAdding: {
                        roleId: this.record.get('id'),
                        roleName: this.record.get('name'),
                        read: true             // 必须在这里写才安全
                    }
                }
            })
        }

        // 如果有角色URL子表格则关联
        if (roleUrlItem != null) {
            this.items[0].items.push({
                xtype: 'panel',
                title: '关联URL',
                items: {
                    xtype: 'roleurlmanage',
                    role: this.record,
                    acl: roleUrlItem.acl,
                    children: roleUrlItem.children,
                    createView: 'Console.view.panelViews.roleUrlManage.AddItem',
                    defaultPropertiesForAdding: {
                        roleId: this.record.get('id'),
                        roleName: this.record.get('name')
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


Ext.define('Console.view.panelViews.RoleManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Role',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.roleManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.rolemanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.Role',
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
                detailView: 'Console.view.panelViews.roleManage.Details',
                createView: 'Console.view.panelViews.roleManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [{
                    text: '名称', dataIndex: 'name', flex: 1
                }, {
                    text: '备注', dataIndex: 'comment', flex: 1, editor: {
                        xtype: 'textarea', allowBlank: true
                    }
                }, {
                    text: '是否活跃',
                    dataIndex: 'active',
                    xtype: 'booleancolumn',
                    trueText: '是',
                    falseText: '否',
                    flex: 1,
                    editor: {
                        xtype: 'activecombobox', allowBlank: true
                    }
                }
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
                        {label: '名称', name: 'name', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
