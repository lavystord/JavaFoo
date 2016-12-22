/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.lockTypeManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.LockType',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.VersionManage',
        'Console.view.panelViews.BaseSelectGridView',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 300,
    modal: true,
    title: '增加新锁类型',

    initComponent: function () {

        this.items = [
            {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                model: 'Console.model.LockType',
                editable: true,
                layout: 'anchor',
                padding: '5 5 5 5',

                items: [
                    {
                        xtype: 'hidden',
                        name: 'hardwareVersionId'
                    }, {
                        xtype: 'hidden',
                        name: 'newestFirmwareVersionId'
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'hardwareVersionString',
                                readOnly: true,
                                allowBlank: false,
                                fieldLabel: '结构版本号'
                            },
                            {
                                xtype: 'button',
                                action: 'setHardwareVersion',
                                text: '选择结构版本'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'newestFirmwareVersionString',
                                readOnly: true,
                                allowBlank: false,
                                fieldLabel: '最新固件版本'
                            },
                            {
                                xtype: 'button',
                                action: 'setNewestFirmwareVersion',
                                text: '选择最新固件版本'
                            }
                        ]
                    }, {
                        xtype: 'textfield',
                        name: 'name',
                        fieldLabel: '版本名'
                    },  {
                        xtype: 'textarea',
                        name: 'description',
                        fieldLabel: '描述信息'
                    }]
            }
        ];

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
        var setHvButton = this.down('button[action=setHardwareVersion]');
        var setNfvButton = this.down('button[action=setNewestFirmwareVersion]');

        setHvButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setHardwareVersion,
                        callbackScope: this,
                        title: '选择新版本',
                        gridClass: 'Console.view.panelViews.VersionManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setNfvButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setNewestFirmwareVersion,
                        callbackScope: this,
                        title: '选择新版本',
                        gridClass: 'Console.view.panelViews.VersionManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },

    setHardwareVersion: function (record) {
        if(record.get('type') == Console.config.Config.versionTypeLockStructure) {
            this.down('hidden[name=hardwareVersionId]').setValue(record.get('id'));
            this.down('textfield[name=hardwareVersionString]').setValue(record.get('stringFormat'));
        }
        else {
            Ext.example.msg('error', '必须选择锁结构版本号');
        }
    },

    setNewestFirmwareVersion: function (record) {
        if(record.get('type') == Console.config.Config.versionTypeLockFirmware) {
            this.down('hidden[name=newestFirmwareVersionId]').setValue(record.get('id'));
            this.down('textfield[name=newestFirmwareVersionString]').setValue(record.get('stringFormat'));
        }
        else {
            Ext.example.msg('error', '必须选择固件版本号');
        }
    }
});


Ext.define('Console.view.panelViews.lockTypeManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.LockType',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.VersionManage',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 300,
    modal: true,
    layout: 'hbox',
    title: '锁类型详细资料',

    initComponent: function () {

        this.items = [
            {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                model: 'Console.model.LockType',
                editable: true,
                layout: 'anchor',
                padding: '5 5 5 5',

                items: [
                    {
                        xtype: 'hidden',
                        name: 'hardwareVersionId'
                    }, {
                        xtype: 'hidden',
                        name: 'newestFirmwareVersionId'
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'hardwareVersionString',
                                readOnly: true,
                                allowBlank: false,
                                fieldLabel: '结构版本号'
                            },
                            {
                                xtype: 'button',
                                action: 'setHardwareVersion',
                                text: '选择结构版本'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'newestFirmwareVersionString',
                                readOnly: true,
                                allowBlank: false,
                                fieldLabel: '最新固件版本'
                            },
                            {
                                xtype: 'button',
                                action: 'setNewestFirmwareVersion',
                                text: '选择最新固件版本'
                            }
                        ]
                    }, {
                        xtype: 'textfield',
                        name: 'name',
                        fieldLabel: '版本名'
                    }, {
                        xtype: 'textarea',
                        name: 'description',
                        fieldLabel: '描述信息'
                    }]
            }
        ];

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
        var setHvButton = this.down('button[action=setHardwareVersion]');
        var setNfvButton = this.down('button[action=setNewestFirmwareVersion]');

        setHvButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setHardwareVersion,
                        callbackScope: this,
                        title: '选择新版本',
                        gridClass: 'Console.view.panelViews.VersionManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setNfvButton.on({
            'click': {
                scope: this,
                fn: function () {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setNewestFirmwareVersion,
                        callbackScope: this,
                        title: '选择新版本',
                        gridClass: 'Console.view.panelViews.VersionManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },

    setHardwareVersion: function (record) {
        if(record != null) {
            this.down('hidden[name=hardwareVersionId]').setValue(record.get('id'));
            this.down('textfield[name=hardwareVersionString]').setValue(record.get('stringFormat'));
        }
        else {
            Ext.example.msg('warning', '锁类型必须有关联的结构版本号');
        }
    },

    setNewestFirmwareVersion: function (record) {
        if(record != null) {
            this.down('hidden[name=newestFirmwareVersionId]').setValue(record.get('id'));
            this.down('textfield[name=newestFirmwareVersionString]').setValue(record.get('stringFormat'));
        }
        else {
            Ext.example.msg('warning', '锁类型必须有关联的固件版本号');
        }
    }
});


Ext.define('Console.view.panelViews.LockTypeManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.LockType',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.panelViews.lockTypeManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.locktypemanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.LockType',
            autoLoad: true,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: false       // LockType就不支持远程排序了
        });
        this.items = [
            {
                region: 'center',
                xtype: 'basegridview',
                acl: this.acl,
                children: this.children,
                detailView: 'Console.view.panelViews.lockTypeManage.Details',
                createView: 'Console.view.panelViews.lockTypeManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [
                    {
                        text: 'ID', dataIndex: 'id', flex: 1
                    },
                    {
                        text: '版本名称', dataIndex: 'name', flex: 1, editor:
                    {
                        xtype: 'textfield',
                        allowBlank: false
                    }
                    },
                    {
                        text: '结构版本号', dataIndex: 'hardwareVersionString', flex: 1
                    },
                    {
                        text: '最新固件版本号', dataIndex: 'newestFirmwareVersionString', flex: 1
                    },
                    {
                        text: '描述',
                        dataIndex: 'description',
                        flex: 5, editor:
                    {
                        xtype: 'textfield',
                        allowBlank: false
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
                        {label: '结构版本号', name: 'hardwareVersionString', xtype: 'textfield'},
                        {name: 'major', xtype: 'hidden', hidden: true},
                        {name: 'minor', xtype: 'hidden', hidden: true}
                    ],
                    [
                        {label: '版本名称', name: 'name', xtype: 'textfield'}
                    ]
                ],
                store: store

            }

        ];

        this.callParent(arguments);
    },


    afterRender: function () {
        var hwvStringField = this.down('textfield[name=hardwareVersionString]');
        var majorField = this.down('hidden[name=major]');
        var minorField = this.down('hidden[name=minor]');
        hwvStringField.on({
            'change': {
                scope: this,
                fn: function (field, newValue) {
                    var a = newValue.split(".");
                    if (a.length == 2) {
                        var a0 = parseInt(a[0], 10);
                        var a1 = parseInt(a[1], 10);
                        if (!isNaN(a0) && !isNaN(a1)) {
                            majorField.setValue(a0);
                            minorField.setValue(a1);
                        }
                        else {
                            majorField.setValue(null);
                            minorField.setValue(null);
                        }
                    }
                }
            }
        });

        this.callParent(arguments);
    }
});
