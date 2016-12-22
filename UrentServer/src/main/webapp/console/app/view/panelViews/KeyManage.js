/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.keyManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Key',
        'Console.model.Area',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.LockManage',
        'Console.view.panelViews.UserManage',
        'Console.view.util.KeyTypeComboBox',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 800,
    height: 400,
    modal: true,
    title: '创建新钥匙',

    initComponent: function () {
        this.items = [{
            xtype: 'panel',
            layout: 'hbox',
            items: [
                {
                    title: '基本资料',
                    xtype: 'baseformview',
                    record: this.record,
                    gridView: this.gridView,
                    model: 'Console.model.Key',
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [{
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'lockId',
                                allowBlank: false
                            },
                            {
                                xtype: 'textarea',
                                name: 'houseString',
                                readOnly: true,
                                fieldLabel: '房屋',
                                skipDirty: true
                            },
                            {
                                xtype: 'button',
                                action: 'setLock',
                                text: '选择房屋(锁)'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'vbox',
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'ownerId'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerName',
                                readOnly: true,
                                fieldLabel: '钥匙拥有者姓名'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerNickname',
                                readOnly: true,
                                fieldLabel: '钥匙拥有者昵称'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerMobile',
                                readOnly: true,
                                fieldLabel: '钥匙拥有者手机号'
                            },
                            {
                                xtype: 'button',
                                action: 'setOwner',
                                text: '选择钥匙拥有者'
                            }
                        ]
                    }, {
                        xtype: 'keytypecombobox',
                        name: 'type',
                        allowBlank: false,
                        fieldLabel: '钥匙类型'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '过期时间',
                        name: 'expiredDate',
                        allowBlank: true,
                        format: 'Y-M-d H:i:s',
                        minValue: new Date()
                    }]
                }
            ]
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
        var setLockButton = this.down('button[action=setLock]');
        var setOwnerButton = this.down('button[action=setOwner]');

        setLockButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setLock,
                        callbackScope: this,
                        title: '选择关联的锁',
                        gridClass: 'Console.view.panelViews.LockManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setOwnerButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setOwner,
                        callbackScope: this,
                        title: '选择钥匙所有者',
                        gridClass: 'Console.view.panelViews.UserManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        var keyTypeComboBox = this.down('keytypecombobox[name=type]');
        keyTypeComboBox.on({
            'select': {
                scope: this,
                fn: function(combo, records) {
                    if((records[0]).get('value') == Console.config.Config.keyTypeTemp) {
                        var expiredDateField = this.down('datefield[name=expiredDate]');
                        var defaultDate = Ext.Date.add(new Date(), Ext.Date.Hour, 1);
                        expiredDateField.setValue(defaultDate);
                    }
                }
            }
        });

        this.callParent(arguments);
    },

    setLock: function (record) {
        if (record != null) {
            this.down('hidden[name=lockId]').setValue(record.get('id'));
            this.down('textarea[name=houseString]').setValue(record.get('houseString'));
        }
        else {
            Ext.example.msg('warning', '钥匙必须有关联的锁');
        }
    },

    setOwner: function (record) {
        if (record != null) {
            this.down('hidden[name=ownerId]').setValue(record.get('id'));
            this.down('textfield[name=ownerName]').setValue(record.get('name'));
            this.down('textfield[name=ownerNickname]').setValue(record.get('nickname'));
            this.down('textfield[name=ownerMobile]').setValue(record.get('mobile'));
        }
        else {
            Ext.example.msg('warning', '钥匙必须有关联的用户');
        }
    }
});


Ext.define('Console.view.panelViews.keyManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Key',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.KeyTypeComboBox',
        'Console.view.panelViews.KeyActionLogManage',
        'Ext.form.FieldSet'
    ],

    record: null,
    gridView: null,
    children: null,
    width: 1200,
    height: 600,
    modal: true,
    title: '钥匙详细资料',


    initComponent: function () {
        this.items = [{
            xtype: 'tabpanel',
            items: [
                {
                    title: '基本资料',
                    xtype: 'baseformview',
                    record: this.record,
                    gridView: this.gridView,
                    model: 'Console.model.Key',
                    editable: false,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [{
                        xtype: 'fieldset',
                        layout: 'anchor',
                        items: [
                            {
                                xtype: 'numberfield',
                                name: 'lockId',
                                fieldLabel: '锁id'
                            },
                            {
                                xtype: 'textfield',
                                name: 'lockGapAddress',
                                fieldLabel: '锁GAP地址'
                            },
                            {
                                xtype: 'textarea',
                                name: 'houseString',
                                width: 500,
                                fieldLabel: '房屋地址'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'anchor',
                        items: [
                            {
                                xtype: 'numberfield',
                                name: 'ownerId',
                                fieldLabel: '拥有者id'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerName',
                                fieldLabel: '拥有者姓名'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerNickname',
                                fieldLabel: '拥有者昵称'
                            },
                            {
                                xtype: 'textfield',
                                name: 'ownerMobile',
                                fieldLabel: '拥有者手机号'
                            }
                        ]
                    }, {
                        xtype: 'textfield',
                        name: 'statusString',
                        fieldLabel: '状态'
                    }, {
                        xtype: 'textfield',
                        name: 'typeString',
                        fieldLabel: '钥匙类型'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '过期时间',
                        format: 'y/M/d H:i:s',
                        name: 'expiredDate'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '创建时间',
                        name: 'createDate'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '更新时间',
                        name: 'updateDate'
                    }]
                },
                {
                    xtype: 'panel',
                    title: '钥匙相关日志',
                    items: [
                        {
                            xtype: 'keyactionlogmanage',
                            layout: 'anchor',
                            filters: [Ext.create('Ext.util.Filter',
                                {
                                    property: 'keyId',
                                    value: this.record.get('id')
                                })],
                            acl: {
                                read: true,
                                update: false,
                                deletee: false,
                                insert: false
                            }
                        }
                    ]
                }
            ]
        }];

        if (this.record.get('sharedFrom') != null) {
            this.items[0].items.push(
                {
                    xtype: 'panel',
                    title: '主钥匙资料',
                    items: {
                        xtype: 'keymanage',
                        layout: 'anchor',
                        filters: [Ext.create('Ext.util.Filter',
                            {
                                property: 'id',
                                value: this.record.get('sharedFrom').id
                            })],
                        acl: {
                            read: true,
                            update: false,
                            deletee: false,
                            insert: false
                        }
                    }
                }
            );
        }
        else if (this.record.get('type') == Console.config.Config.keyTypePrimary) {
            this.items[0].items.push(
                {
                    xtype: 'panel',
                    title: '分享钥匙资料',
                    items: {
                        xtype: 'keymanage',
                        layout: 'anchor',
                        filters: [Ext.create('Ext.util.Filter',
                            {
                                property: 'sharedFromId',
                                value: this.record.get('id')
                            })],
                        acl: {
                            read: true,
                            update: false,
                            deletee: false,
                            insert: false
                        }
                    }
                }
            )
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


Ext.define('Console.view.panelViews.KeyManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Key',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.KeyStatusComboBox',
        'Console.view.util.KeyTypeComboBox',
        'Console.view.panelViews.keyManage.Details',
        'Console.view.panelViews.keyManage.AddItem',
        'Ext.grid.plugin.CellEditing',
        'Ext.grid.column.Date'
    ],

    alias: 'widget.keymanage',
    filters: null,
    layout: 'border',

    initComponent: function () {

        var storeConfig = {
            model: 'Console.model.Key',
            autoLoad: true,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: true
        };
        if (this.filters != null) {
            storeConfig.filters = this.filters;
        }
        var store = Ext.create('Ext.data.Store',
            storeConfig);
        this.items = [
            {
                region: 'center',
                xtype: 'basegridview',
                acl: this.acl,
                children: this.children,
                detailView: 'Console.view.panelViews.keyManage.Details',
                createView: 'Console.view.panelViews.keyManage.AddItem',
                defaultPropertiesForAdding: {},
                tools:[
                    {
                        itemId: 'activate',
                        type: 'up',
                        tooltip: '启用',
                        disabled: true
                    },
                    {
                        itemId: 'deactivate',
                        type: 'down',
                        tooltip: '停用',
                        disabled: true
                    }
                ],
                selModel: Ext.create('Ext.selection.CheckboxModel', {
                    injectCheckbox: 'first',
                    mode: 'SINGLE',
                    allowDeselect: true,
                    checkOnly: 'true'
                }),
                columns: [
                    {
                        text: '钥匙id', dataIndex: 'id', flex: 1, hidden: true
                    },
                    {
                        text: '房屋地址', dataIndex: 'houseString', flex: 3
                    },
                    {
                        text: '拥有者昵称', dataIndex: 'ownerNickname', flex: 1
                    },
                    {
                        text: '拥有者姓名', dataIndex: 'ownerName', flex: 1
                    },
                    {
                        text: '拥有者手机号',
                        dataIndex: 'ownerMobile',
                        flex: 2
                    },
                    {
                        text: '分享者',
                        dataIndex: 'sharedFromUserName',
                        flex: 1
                    },
                    {
                        text: '钥匙状态',
                        dataIndex: 'statusString',
                        flex: 2
                    },
                    {
                        text: '命名',
                        dataIndex: 'alias',
                        flex: 1
                    },
                    {
                        text: '最大分享数目',
                        dataIndex: 'maxSharedCount',
                        allowDecimals: false,
                        flex: 1
                    },
                    {
                        text: '钥匙类型',
                        dataIndex: 'typeString',
                        flex: 1
                    },
                    {
                        text: '过期时间',
                        dataIndex: 'expiredDate',
                        xtype: 'datecolumn',
                        format: 'y/M/d H:i:s',
                        flex: 2
                    },
                    {
                        text: '创建时间',
                        dataIndex: 'createDate',
                        xtype: 'datecolumn',
                        format: 'y/M/d H:i:s'
                    },
                    {
                        text: '更新时间',
                        dataIndex: 'updateDate',
                        xtype: 'datecolumn',
                        format: 'y/M/d H:i:s'
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
                        {label: '拥有者', name: 'ownerName', xtype: 'textfield'}
                    ],
                    [
                        {label: '拥有者手机', name: 'ownerMobile', xtype: 'textfield'}
                    ],
                    [
                        {label: '分享者', name: 'sharedFromUserName', xtype: 'textfield'}
                    ],
                    [
                        {label: '状态', name: 'status', xtype: 'keystatuscombobox'}
                    ],
                    [
                        {label: '类型', name: 'type', xtype: 'keytypecombobox'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
