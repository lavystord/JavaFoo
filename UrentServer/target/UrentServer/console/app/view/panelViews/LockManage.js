/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.lockManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Lock',
        'Console.model.Area',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.HouseManage',
        'Console.view.panelViews.LockTypeManage',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 500,
    modal: true,
    title: '增加新锁',

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
                    model: 'Console.model.Lock',
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [ {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                allowBlank: false,
                                name: 'houseId'
                            },
                            {
                                xtype: 'textarea',
                                name: 'houseString',
                                readOnly: true,
                                fieldLabel: '房屋'
                            },
                            {
                                xtype: 'button',
                                action: 'setHouse',
                                text: '选择房屋'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                allowBlank: false,
                                name: 'currentFirmwareVersionId'
                            },
                            {
                                xtype: 'textfield',
                                name: 'currentFirmwareVersionString',
                                readOnly: true,
                                fieldLabel: '当前固件版本'
                            },
                            {
                                xtype: 'button',
                                action: 'setCurrentFirmwareVersion',
                                text: '选择当前固件版本'
                            }
                        ]
                    },{
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'typeId'
                            },
                            {
                                xtype: 'textarea',
                                name: 'typeName',
                                allowBlank: false,
                                readOnly: true,
                                fieldLabel: '锁类型'
                            },
                            {
                                xtype: 'button',
                                action: 'setType',
                                text: '选择锁类型'
                            }
                        ]
                    },{
                        xtype: 'textfield',
                        name: 'gapAddress',
                        allowBlank: false,
                        fieldLabel: 'GAP地址'
                    }, {
                        xtype: 'numberfield',
                        name: 'powerDensity',
                        allowBlank: false,
                        fieldLabel: '当前电量',
                        maxValue: 100,
                        minValue: 0
                    }, {
                        xtype: 'activecombobox',
                        name: 'active',
                        fieldLabel: '是否活跃'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '持久性原语过期时间',
                        name: 'constantKeyWordExpiredDate'
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
        var setHouseButton = this.down('button[action=setHouse]');
        var setTypeButton = this.down('button[action=setType]');
        var setCfvButton = this.down('button[action=setCurrentFirmwareVersion]');

        setHouseButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setHouse,
                        callbackScope: this,
                        title: '选择房屋',
                        gridClass: 'Console.view.panelViews.HouseManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setTypeButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setType,
                        callbackScope: this,
                        title: '选择锁类型',
                        gridClass: 'Console.view.panelViews.LockTypeManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setCfvButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setCfw,
                        callbackScope: this,
                        title: '选择当前固件版本',
                        gridClass: 'Console.view.panelViews.VersionManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },

    setHouse: function(record) {
        if(record != null) {
            this.down('hidden[name=houseId]').setValue(record.get('id'));
            this.down('textarea[name=houseString]').setValue(record.get('houseString'));
        }
        else {
            Ext.example.msg('warning', '锁必须有关联的房屋');
        }
    },

    setType: function(record) {
        if(record != null) {
            this.down('hidden[name=typeId]').setValue(record.get('id'));
            this.down('textfield[name=typeName]').setValue(record.get('name'));
        }
        else {
            //this.down('hidden[name=typeId]').setValue(null);
            //this.down('textfield[name=typeName]').setValue(null);
            Ext.example.msg('warning', '锁必须有关联的类型');
        }
    },

    setCfw: function(record) {
        if(record != null) {
            if(record.get('type') == Console.config.Config.versionTypeLockFirmware) {
                this.down('hidden[name=currentFirmwareVersionId]').setValue(record.get('id'));
                this.down('textfield[name=currentFirmwareVersionString]').setValue(record.get('stringFormat'));
            }
            else {
                Ext.example.msg('error', '必须选择固件版本号');
            }
        }
        else {
            //this.down('hidden[name=typeId]').setValue(null);
            //this.down('textfield[name=typeName]').setValue(null);
            Ext.example.msg('error', '锁必须设置初始化类型版本');
        }
    }
});


Ext.define('Console.view.panelViews.lockManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Lock',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.UserRoleManage',
        'Console.view.panelViews.RoleMenuManage',
        'Console.view.panelViews.KeyActionLogManage',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 1200,
    height: 500,
    modal: true,
    title: '锁详细资料',


    initComponent: function () {
        this.items = [{
            xtype: 'tabpanel',
            items: [
                {
                    title: '基本资料',
                    xtype: 'baseformview',
                    record: this.record,
                    gridView: this.gridView,
                    model: 'Console.model.Lock',
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [ {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                allowBlank: false,
                                name: 'houseId'
                            },
                            {
                                xtype: 'textarea',
                                name: 'houseString',
                                width: 300,
                                readOnly: true,
                                fieldLabel: '房屋',
                                skipDirty: true
                            },
                            {
                                xtype: 'button',
                                action: 'setHouse',
                                text: '选择房屋'
                            }
                        ]
                    },{
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'typeId'
                            },
                            {
                                xtype: 'textarea',
                                name: 'typeName',
                                width: 300,
                                allowBlank: false,
                                readOnly: true,
                                fieldLabel: '锁类型'
                            },
                            {
                                xtype: 'button',
                                action: 'setType',
                                text: '选择锁类型'
                            }
                        ]
                    },{
                        xtype: 'textfield',
                        name: 'gapAddress',
                        allowBlank: false,
                        fieldLabel: 'GAP地址'
                    }, {
                        xtype: 'textfield',
                        name: 'currentFirmwareVersionString',
                        readOnly: true,
                        fieldLabel: '当前固件版本'
                    }, {
                        xtype: 'textfield',
                        name: 'powerDensity',
                        readOnly: true,
                        fieldLabel: '当前电量'
                    }, {
                        xtype: 'activecombobox',
                        name: 'active',
                        fieldLabel: '是否活跃'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '持久性原语过期时间',
                        name: 'constantKeyWordExpiredDate'
                    }]
                },
                {
                    xtype: 'panel',
                    title: '锁上相关钥匙',
                    items: [
                        {
                            xtype: 'keymanage',
                            layout: 'anchor',
                            filters: [Ext.create('Ext.util.Filter',
                                {
                                    property: 'lockId',
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
                },
                {
                    xtype: 'panel',
                    title: '锁相关日志',
                    items: [
                        {
                            xtype: 'keyactionlogmanage',
                            layout: 'anchor',
                            filters: [Ext.create('Ext.util.Filter',
                                {
                                    property: 'lockId',
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
        var setHouseButton = this.down('button[action=setHouse]');
        var setTypeButton = this.down('button[action=setType]');

        setHouseButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setHouse,
                        callbackScope: this,
                        title: '选择新地点',
                        gridClass: 'Console.view.panelViews.HouseManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setTypeButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setType,
                        callbackScope: this,
                        title: '选择新房主',
                        gridClass: 'Console.view.panelViews.LockTypeManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },

    setHouse: function(record) {
        if(record != null) {
            this.down('hidden[name=houseId]').setValue(record.get('id'));
            this.down('textarea[name=houseString]').setValue(record.get('houseString'));
        }
        else {
            this.down('hidden[name=houseId]').setValue(null);
            this.down('textarea[name=houseString]').setValue(null);
        }
    },

    setType: function(record) {
        if(record != null) {
            this.down('hidden[name=typeId]').setValue(record.get('id'));
            this.down('textfield[name=typeName]').setValue(record.get('name'));
        }
        else {
            this.down('hidden[name=typeId]').setValue(null);
            this.down('textfield[name=typeName]').setValue(null);
        }
    }
});


Ext.define('Console.view.panelViews.LockManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Lock',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.lockManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.lockmanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.Lock',
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
                detailView: 'Console.view.panelViews.lockManage.Details',
                createView: 'Console.view.panelViews.lockManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                tools:[
                    {
                        itemId: 'deactivate',
                        type: 'down',
                        tooltip: '禁用',
                        disabled: true
                    }
                ],
                columns: [
                    {
                        text: '锁id', dataIndex: 'id', flex: 1, hidden: true
                    },
                    {
                        text: '房屋地址', dataIndex: 'houseString', flex: 3
                    }, {
                        text: '锁类型', dataIndex: 'typeName', flex: 1
                    }, {
                        text: 'GAP地址',
                        dataIndex: 'gapAddress',
                        flex: 1,
                        editor: {
                            xtype: 'textfield', allowBlank: false
                        }
                    },{
                        text: '是否活跃',
                        dataIndex: 'active',
                        xtype: 'booleancolumn',
                        trueText: '是',
                        falseText: '否',
                        flex: 1,
                        editor: {
                            xtype: 'activecombobox', allowBlank: true
                        }
                    }, {
                        text: '剩余电量',
                        dataIndex: 'powerDensity',
                        flex: 1
                    }, {
                        text: '当前固件版本',
                        dataIndex: 'currentFirmwareVersionString',
                        flex: 1
                    }, {
                        text: '持久性原语过期时间',
                        dataIndex: 'constantKeyWordExpiredDate',
                        flex: 2,
                        xtype: 'datecolumn',
                        format: 'Y-m-d H:i:s',
                        editor: {
                            xtype: 'datefield', allowBlank: false
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
                        {label: '房屋地址（小区名）', name: 'subdistrict', xtype: 'textfield'}
                    ],
                    [
                        {label: 'GAP地址', name: 'gapAddress', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
