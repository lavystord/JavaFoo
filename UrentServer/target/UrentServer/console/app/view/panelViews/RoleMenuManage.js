/**
 * Created by Administrator on 2015/8/6.
 */

Ext.define('Console.view.panelViews.roleMenuManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.RoleMenu',
        'Console.model.Menu',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.ActiveCombobox',
        'Ext.form.field.Checkbox',
        'Ext.form.field.Hidden'
    ],
    gridView: null,
    children: null,
    width: 600,
    height: 500,
    modal: true,
    title: '增加角色菜单关联',

    initComponent: function () {
        this.items = [{
            title: '角色菜单关联',
            xtype: 'baseformview',
            gridView: this.gridView,
            defaultPropertiesForAdding: this.defaultPropertiesForAdding,
            model: 'Console.model.RoleMenu',
            editable: true,
            layout: 'anchor',
            padding: '5 5 5 5',

            items: [
                {
                    xtype: 'fieldset',
                    layout: 'vbox',
                    items: [{
                        xtype: 'textfield',
                        readOnly: 'true',
                        name: 'roleName',
                        fieldLabel: '角色名'
                    }, {
                        xtype: 'numberfield',
                        hidden: true,
                        name: 'roleId'
                    }]
                },
                {
                    xtype: 'fieldset',
                    layout: 'vbox',
                    items: [{
                        xtype: 'combobox',
                        name: 'menuId',
                        fieldLabel: '菜单',
                        store: Ext.create('Ext.data.Store', {
                            model: 'Console.model.Menu',
                            autoLoad: false,
                            remoteFilter: true,
                            proxy: {
                                type: 'rest',
                                url: 'menuUnrelated',
                                reader: {
                                    type: 'json',
                                    totalProperty: "total",
                                    root: "list"
                                }
                            }
                        }),
                        lastQuery: '',
                        editable: false,
                        allowBlank: false,
                        displayField: 'id',
                        valueField: 'id',
                        queryMode: 'remote',
                        forceSelection: true,
                        validator: function (value) {
                            return (value != null && value != undefined)
                        }
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '菜单名',
                        readOnly: true,
                        name: 'menuName'
                    }]
                }, {
                    xtype: 'fieldset',
                    layout: 'vbox',
                    items: [
                        {
                            xtype: 'hidden',
                            name: 'read',
                            value: true     //这里这样写没用，必须在defaultPropertiesForAdding里写才能使这个域为true
                        },
                        {
                            xtype: 'checkbox',
                            fieldLabel: 'create',
                            name: 'create',
                            inputValue: true
                        },
                        {
                            xtype: 'checkbox',
                            fieldLabel: 'update',
                            name: 'update',
                            inputValue: true
                        },
                        {
                            xtype: 'checkbox',
                            fieldLabel: 'delete',
                            name: 'deletee',
                            inputValue: true
                        }
                    ]
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
        var menuSelection = this.down('combobox[name=menuId]');
        var roleId = this.down('numberfield[name=roleId]');
        menuSelection.on({
            'expand': {
                scope: this,
                fn: function () {
                    var store = menuSelection.getStore();
                    if (store.getCount() == 0) {
                        store.addFilter([new Ext.util.Filter({
                            property: 'roleId',
                            value: roleId.getValue()
                        })]);
                    }
                }
            },
            'select': {
                scope: this,
                fn: function (combo, records) {
                    // 赋予联动域相应的值
                    var record = records[0];
                    var menuNameField = this.down('textfield[name=menuName]');
                    menuNameField.setValue(record.get('name'));
                }
            }
        })
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.RoleMenuManage', {
    extend: 'Console.view.panelViews.BaseGridView',

    requires: [
        'Console.model.RoleMenu',
        'Console.view.panelViews.BaseGridView',
        'Ext.util.Filter'
    ],

    alias: 'widget.rolemenumanage',

    role: null,

    menu: null,

    acl: null,

    selType: 'cellmodel',
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            id: 'cellEditing',
            clicksToEdit: 1
        })
    ],

    initComponent: function () {
        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.RoleMenu',
            autoLoad: false,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: true
        });

        this.columns = [{
            text: 'read', dataIndex: 'read', flex: 2
        }, {
            text: 'create', dataIndex: 'create', flex: 2, editor: {
                xtype: 'checkbox'
            }
        }, {
            text: 'update', dataIndex: 'update', flex: 2, editor: {
                xtype: 'checkbox'
            }
        }, {
            text: 'delete', dataIndex: 'deletee', flex: 2, editor: {
                xtype: 'checkbox'
            }
        }];

        if (this.menu == null) {
            this.columns.unshift({
                text: '菜单id', dataIndex: 'menuId', flex: 1
            }, {
                text: '菜单名', dataIndex: 'menuName', flex: 4
            });
        }
        if (this.role == null) {
            this.columns.unshift({
                text: '角色', dataIndex: 'roleName', flex: 4
            });
        }

        if (this.menu != null) {
            store.addFilter([
                new Ext.util.Filter({
                    property: 'menuId',
                    value: this.menu.get('id')
                })
            ]);
        }
        if (this.role != null) {
            store.addFilter([
                new Ext.util.Filter({
                    property: 'roleId',
                    value: this.role.get('id')
                })
            ]);
        }

        this.store = store;
        this.bbar = {
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
        };

        this.callParent(arguments);
    },

    afterRender: function () {
        this.callParent(arguments);
    }

});