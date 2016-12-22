/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.houseManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.House',
        'Console.model.Address',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.BaseSelectGridView',
        'Console.view.panelViews.UserManage',
        'Console.view.panelViews.AddressManage',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 400,
    modal: true,
    title: '增加新房屋',

    initComponent: function () {

        this.items = [
            {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                model: 'Console.model.House',
                editable: true,
                layout: 'anchor',
                padding: '5 5 5 5',

                items: [
                    {
                        xtype: 'hidden',
                        name: 'ownerId'
                    }, {
                        xtype: 'hidden',
                        allowBlank: false,
                        name: 'addressId'
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textarea',
                                name: 'addressString',
                                readOnly: true,
                                fieldLabel: '地点'
                            },
                            {
                                xtype: 'button',
                                action: 'setAddress',
                                text: '选择地点'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'ownerNickname',
                                readOnly: true,
                                fieldLabel: '房主昵称'
                            },
                            {
                                xtype: 'button',
                                action: 'setOwner',
                                text: '选择房主'
                            }
                        ]
                    }, {
                        xtype: 'textfield',
                        name: 'ownerMobile',
                        readOnly: true,
                        fieldLabel: '房主手机'
                    },  {
                        xtype: 'textfield',
                        name: 'ownerName',
                        readOnly: true,
                        fieldLabel: '房主姓名'
                    },{
                        xtype: 'textfield',
                        fieldLabel: '栋号',
                        name: 'building'
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '单元号',
                        name: 'unit'
                    }, {
                        xtype: 'numberfield',
                        allowDecimal: false,
                        fieldLabel: '楼层',
                        name: 'floor'
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '门牌号',
                        name: 'number'
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
        var setOwnerButton = this.down('button[action=setOwner]');
        var setAddressButton = this.down('button[action=setAddress]');

        setAddressButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setAddress,
                        callbackScope: this,
                        title: '选择新地点',
                        gridClass: 'Console.view.panelViews.AddressManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setOwnerButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setOwner,
                        callbackScope: this,
                        title: '选择新房主',
                        gridClass: 'Console.view.panelViews.UserManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },


    setAddress: function(record) {
        if(record != null) {
            this.down('hidden[name=addressId]').setValue(record.get('id'));
            this.down('textarea[name=addressString]').setValue(record.get('addressString'));
        }
        else {
            Ext.example.msg('warning', '房屋必须有关联的地址');
        }
    },

    setOwner: function(record) {
        if(record != null) {
            this.down('hidden[name=ownerId]').setValue(record.get('id'));
            this.down('textfield[name=ownerName]').setValue(record.get('name'));
            this.down('textfield[name=ownerNickname]').setValue(record.get('nickname'));
            this.down('textfield[name=ownerMobile]').setValue(record.get('mobile'));
        }
        else {
            this.down('hidden[name=ownerId]').setValue(null);
            this.down('textfield[name=ownerName]').setValue(null);
            this.down('textfield[name=ownerMobile]').setValue(null);
        }
    }
});


Ext.define('Console.view.panelViews.houseManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.House',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.UserRoleManage',
        'Console.view.panelViews.RoleMenuManage',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 400,
    modal: true,
    layout: 'hbox',
    title: '房屋详细资料',

    initComponent: function () {

        this.items = [
            {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                model: 'Console.model.House',
                editable: true,
                layout: 'anchor',
                padding: '5 5 5 5',

                items: [
                    {
                        xtype: 'hidden',
                        name: 'ownerId'
                    }, {
                        xtype: 'hidden',
                        allowBlank: false,
                        name: 'addressId'
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textarea',
                                name: 'addressString',
                                readOnly: true,
                                fieldLabel: '地点',
                                skipDirty: true
                            },
                            {
                                xtype: 'button',
                                action: 'setAddress',
                                text: '选择地点'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'ownerNickname',
                                readOnly: true,
                                fieldLabel: '房主昵称'
                            },
                            {
                                xtype: 'button',
                                action: 'setOwner',
                                text: '选择房主'
                            }
                        ]
                    },
                    {
                        xtype: 'textfield',
                        name: 'ownerName',
                        readOnly: true,
                        fieldLabel: '房主姓名'
                    },{
                        xtype: 'textfield',
                        name: 'ownerMobile',
                        readOnly: true,
                        fieldLabel: '房主手机'
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '栋号',
                        name: 'building'
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '单元号',
                        name: 'unit'
                    }, {
                        xtype: 'numberfield',
                        allowDecimal: false,
                        fieldLabel: '楼层',
                        name: 'floor'
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '门牌号',
                        name: 'number'
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
        var setOwnerButton = this.down('button[action=setOwner]');
        var setAddressButton = this.down('button[action=setAddress]');

        setAddressButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setAddress,
                        callbackScope: this,
                        title: '选择新地点',
                        gridClass: 'Console.view.panelViews.AddressManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });

        setOwnerButton.on({
            'click': {
                scope: this,
                fn: function() {
                    Ext.create('Console.view.panelViews.BaseSelectGridView', {
                        callback: this.setOwner,
                        callbackScope: this,
                        title: '选择新房主',
                        gridClass: 'Console.view.panelViews.UserManage',
                        gridConfig: {}
                    }).show();
                }
            }
        });
        this.callParent(arguments);
    },

    setAddress: function(record) {
        if(record != null) {
            this.down('hidden[name=addressId]').setValue(record.get('id'));
            this.down('textarea[name=addressString]').setValue(record.get('addressString'));
        }
        else {
            this.down('hidden[name=addressId]').setValue(null);
            this.down('textarea[name=addressString]').setValue(null);
        }
    },

    setOwner: function(record) {
        if(record != null) {
            this.down('hidden[name=ownerId]').setValue(record.get('id'));
            this.down('textfield[name=ownerName]').setValue(record.get('name'));
            this.down('textfield[name=ownerNickname]').setValue(record.get('nickname'));
            this.down('textfield[name=ownerMobile]').setValue(record.get('mobile'));
        }
        else {
            this.down('hidden[name=ownerId]').setValue(null);
            this.down('textfield[name=ownerName]').setValue(null);
            this.down('textfield[name=ownerMobile]').setValue(null);
        }
    }
});


Ext.define('Console.view.panelViews.HouseManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.House',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.houseManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.housemanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.House',
            autoLoad: true,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: false       // 房屋就不支持远程排序了
        });
        this.items = [
            {
                region: 'center',
                xtype: 'basegridview',
                acl: this.acl,
                children: this.children,
                detailView: 'Console.view.panelViews.houseManage.Details',
                createView: 'Console.view.panelViews.houseManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [
                    {
                        text: '地点(小区)', dataIndex: 'addressSubdistrict', flex: 2
                    },
                    {
                        text: '街道', dataIndex: 'areaName', flex: 2
                    },
                    {
                        text: '房主姓名', dataIndex: 'ownerName', flex: 1
                    },
                    {
                        text: '房主昵称', dataIndex: 'ownerNickname', flex: 1
                    }, {
                        text: '房主手机',
                        dataIndex: 'ownerMobile',
                        flex: 2
                    }, {
                        text: '栋号',
                        dataIndex: 'building',
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: '单元号',
                        dataIndex: 'unit',
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
                        }
                    }, {
                        text: '楼层',
                        dataIndex: 'floor',
                        flex: 1,
                        editor: {
                            xtype: 'numberfield',
                            allowBlank: true,
                            allowDecimal: false
                        }
                    }, {
                        text: '门牌号',
                        dataIndex: 'number',
                        flex: 1,
                        editor: {
                            xtype: 'textfield',
                            allowBlank: true
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
                        {label: '地点', name: 'subdistrict', xtype: 'textfield'}
                    ],
                    [
                        {label: '所属街道', name: 'areaName', xtype: 'textfield'}
                    ],
                    [
                        {label: '房主姓名', name: 'ownerName', xtype: 'textfield'}
                    ],
                    [
                        {label: '房主手机', name: 'ownerMobile', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }
});
