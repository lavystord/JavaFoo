/**
 * Created by Administrator on 2015/8/6.
 */

Ext.define('Console.view.panelViews.roleUrlManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.RoleUrl',
        'Console.model.Role',
        'Console.model.Method',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.ActiveCombobox'
    ],
    gridView: null,
    children: null,
    width: 800,
    height: 600,
    modal: true,
    title: '增加角色URL关联',

    initComponent: function () {
        var items = [];
        if (this.defaultPropertiesForAdding != null) {
            if (this.defaultPropertiesForAdding.urlId == undefined) {
                items.push({
                    xtype: 'fieldset',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'numberfield',
                            name: 'urlId',
                            hidden: true,
                            allowBlank: false
                        },
                        {
                            xtype: 'textfield',
                            name: 'urlValue',
                            fieldLabel: 'URL名',
                            readOnly: true
                        },
                        {
                            xtype: 'button',
                            action: 'setUrl',
                            text: '选择URL'
                        }
                    ]
                });
            }
            else {
                items.push({
                    xtype: 'fieldset',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'numberfield',
                            name: 'urlId',
                            hidden: true,
                            allowBlank: false
                        },
                        {
                            xtype: 'textfield',
                            name: 'urlValue',
                            fieldLabel: 'URL名',
                            allowBlank: false,
                            readOnly: true
                        }
                    ]

                })
            }

            if (this.defaultPropertiesForAdding.roleId == undefined) {
                items.push({
                    xtype: 'fieldset',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'numberfield',
                            name: 'roleId',
                            hidden: true,
                            allowBlank: false
                        },
                        {
                            xtype: 'textfield',
                            name: 'roleName',
                            fieldLabel: '角色名',
                            readOnly: true
                        },
                        {
                            xtype: 'button',
                            action: 'setRole',
                            text: '选择角色'
                        }
                    ]
                });
            }
            else {
                items.push({
                    xtype: 'fieldset',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'numberfield',
                            name: 'roleId',
                            hidden: true,
                            allowBlank: false
                        },
                        {
                            xtype: 'textfield',
                            name: 'roleName',
                            fieldLabel: '角色名',
                            allowBlank: false,
                            readOnly: true
                        }
                    ]

                });
            }
        }

        items.push(
            {
                xtype: 'combobox',
                name: 'method',
                fieldLabel: '方法',
                disabled: false,
                store: Ext.create('Ext.data.Store', {
                    model: 'Console.model.Method',
                    autoLoad: false,
                    remoteFilter: true,
                    proxy: {
                        type: 'rest',
                        url: 'unrelatedMethods',
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
                displayField: 'value',
                valueField: 'value',
                queryMode: 'remote',
                forceSelection: true,
                validator: function (value) {
                    return (value != null && value != undefined)
                }
            }
        );
        items.push(
            {
                xtype: 'textarea',
                width: 600,
                height: 150,
                name: 'comment',
                fieldLabel: '备注'
            }
        );
        this.items = [{
            title: '角色URL关联',
            xtype: 'baseformview',
            gridView: this.gridView,
            defaultPropertiesForAdding: this.defaultPropertiesForAdding,
            model: 'Console.model.RoleUrl',
            editable: true,
            layout: 'vbox',
            padding: '5 5 5 5',
            items: items

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
        var methodCombo = this.down('combobox[name=method]');
        var roleId = this.down('numberfield[name=roleId]');
        var urlId = this.down('numberfield[name=urlId]');
        methodCombo.on({
            'expand': {
                scope: this,
                fn: function () {
                    var store = methodCombo.getStore();
                    if (store.getCount() == 0) {
                        store.addFilter([
                            new Ext.util.Filter({
                                property: 'roleId',
                                value: roleId.getValue()
                            }),
                            new Ext.util.Filter({
                                property: 'urlId',
                                value: urlId.getValue()
                            })
                        ]);
                    }
                }
            }
        });

        // todo roleId和urlId的选择的逻辑
        roleId.on({
            'change': {
                scope: this,
                fn: function(){
                    if(roleId.isValid()&& urlId.isValid()) {
                        methodCombo.enable();
                        methodCombo.clearValue();
                        methodCombo.getStore().removeAll();
                    }
                    else {
                        methodCombo.disable();
                    }
                }
            }
        });

        urlId.on({
            'change': {
                scope: this,
                fn: function() {
                    if(roleId.isValid()&& urlId.isValid()) {
                        methodCombo.enable();
                        methodCombo.clearValue();
                    }
                    else {
                        methodCombo.disable();
                    }
                }
            }
        });

        var setRoleButton = this.down('button[action=setRole]');
        if(setRoleButton != null) {
            setRoleButton.on({
                'click': {
                    scope: this,
                    fn: function(){
                        Ext.create('Console.view.panelViews.BaseSelectGridView', {
                            callback: this.setRole,
                            callbackScope: this,
                            title: '选择角色',
                            gridClass: 'Console.view.panelViews.RoleManage',
                            gridConfig: {}
                        }).show();
                    }
                }
            })
        };
        var setUrlButton = this.down('button[action=setUrl]');
        if(setUrlButton != null) {
            setUrlButton.on({
                'click': {
                    scope: this,
                    fn: function(){
                        Ext.create('Console.view.panelViews.BaseSelectGridView', {
                            callback: this.setUrl,
                            callbackScope: this,
                            title: '选择角色',
                            gridClass: 'Console.view.panelViews.UrlManage',
                            gridConfig: {}
                        }).show();
                    }
                }
            })
        }

        this.callParent(arguments);
    },


    setRole: function(record){
        if(record != null) {
            this.down('numberfield[name=roleId]').setValue(record.get('id'));
            this.down('textfield[name=roleName]').setValue(record.get('name'));
        }
        else {
            Ext.example.msg('warning', '角色不允许置空');
        }
    },

    setUrl: function(record){

        if(record != null) {
            this.down('numberfield[name=urlId]').setValue(record.get('id'));
            this.down('textfield[name=urlValue]').setValue(record.get('value'));
        }
        else {
            Ext.example.msg('warning', 'URL不允许置空');
        }
    }

});


Ext.define('Console.view.panelViews.RoleUrlManage', {
    extend: 'Console.view.panelViews.BaseGridView',

    requires: [
        'Console.model.RoleUrl',
        'Console.view.panelViews.BaseGridView',
        'Ext.util.Filter'
    ],

    alias: 'widget.roleurlmanage',

    role: null,

    url: null,

    acl: null,

    initComponent: function () {
        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.RoleUrl',
            autoLoad: false,
            pageSize: 10,
            remoteFilter: true,
            remoteSort: true
        });
        this.columns = [];
        if (this.url == null) {
            this.columns.push({
                text: 'URL', dataIndex: 'urlValue', flex: 1
            });
        }
        if (this.role == null) {
            this.columns.push({
                text: '角色', dataIndex: 'roleName', flex: 1
            });
        }

        this.columns.push({
            text: '方法', dataIndex: 'method', flex: 1
        })
        this.columns.push({
            text: '备注', dataIndex: 'comment', flex: 3, editor: {
                xtype: 'textarea'
            }
        });
        if (this.url != null) {
            store.addFilter([
                new Ext.util.Filter({
                    property: 'urlId',
                    value: this.url.get('id')
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
        this.selType = 'cellmodel';
        this.plugins = [
            Ext.create('Ext.grid.plugin.CellEditing', {
                id: 'cellEditing',
                clicksToEdit: 1
            })
        ],

        this.callParent(arguments);
    },

    afterRender: function () {
        this.callParent(arguments);
    }

});