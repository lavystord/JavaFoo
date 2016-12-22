/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.keyActionLogManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.KeyActionLog',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.KeyTypeComboBox',
        'Ext.form.FieldSet'
    ],

    record: null,
    gridView: null,
    children: null,
    width: 1200,
    height: 600,
    modal: true,
    title: '钥匙操作详细资料',


    initComponent: function () {
        this.items = [{
            xtype: 'tabpanel',
            items: [
                {
                    title: '基本资料',
                    xtype: 'baseformview',
                    record: this.record,
                    gridView: this.gridView,
                    model: 'Console.model.KeyActionLog',
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
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'anchor',
                        items: [
                            {
                                xtype: 'numberfield',
                                name: 'houseId',
                                fieldLabel: '房屋id'
                            },
                            {
                                xtype: 'textfield',
                                name: 'subdistrict',
                                fieldLabel: '小区名'
                            },
                            {
                                xtype: 'textfield',
                                name: 'houseBuilding',
                                fieldLabel: '幢号'
                            },
                            {
                                xtype: 'textfield',
                                name: 'houseUnit',
                                fieldLabel: '单元号'
                            },
                            {
                                xtype: 'textfield',
                                name: 'houseLevel',
                                fieldLabel: '楼层'
                            },
                            {
                                xtype: 'textfield',
                                name: 'houseNumber',
                                fieldLabel: '门牌号'
                            }
                        ]
                    }, {
                        xtype: 'fieldset',
                        layout: 'anchor',
                        items: [
                            {
                                xtype: 'numberfield',
                                name: 'keyId',
                                fieldLabel: '钥匙id'
                            },
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
                        xtype: 'fieldset',
                        layout: 'anchor',
                        items: [
                            {
                                xtype: 'numberfield',
                                name: 'managerId',
                                fieldLabel: '操作者id'
                            },
                            {
                                xtype: 'textfield',
                                name: 'managerName',
                                fieldLabel: '操作者姓名'
                            },
                            {
                                xtype: 'textfield',
                                name: 'managerNickname',
                                fieldLabel: '操作者昵称'
                            },
                            {
                                xtype: 'textfield',
                                name: 'managerMobile',
                                fieldLabel: '操作者手机号'
                            }
                        ]
                    },{
                        xtype: 'textfield',
                        name: 'actionString',
                        fieldLabel: '动作'
                    },{
                        xtype: 'datefield',
                        fieldLabel: '发生时间',
                        format: 'y/M/d H:i:s',
                        name: 'time'
                    }, {
                        xtype: 'datefield',
                        fieldLabel: '登记时间',
                        format: 'y/M/d H:i:s',
                        name: 'createDate'
                    }, {
                        xtype: 'textarea',
                        fieldLabel: '数据',
                        name: 'data'
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
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.KeyActionLogManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.KeyActionLog',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.KeyActionLogActionComboBox',
        'Console.view.panelViews.keyActionLogManage.Details',
        'Ext.grid.plugin.CellEditing',
        'Ext.grid.column.Date'
    ],

    alias: 'widget.keyactionlogmanage',
    filters: null,
    layout: 'border',

    initComponent: function () {

        var storeConfig = {
            model: 'Console.model.KeyActionLog',
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
                detailView: 'Console.view.panelViews.keyActionLogManage.Details',
                columns: [
                    {
                        text: '钥匙id', dataIndex: 'keyId', flex: 1, hidden: true
                    },
                    {
                        text: '房屋地址', dataIndex: 'houseString', flex: 5, hidden: true
                    },
                    {
                        text: '锁id',
                        dataIndex: 'lockId',
                        flex: 1
                    },
                    {
                        text: '操作者',
                        dataIndex: 'managerNickname',
                        flex: 2
                    },
                    {
                        text: '操作者手机',
                        dataIndex: 'managerMobile',
                        flex: 1
                    },
                    {
                        text: '操作名称',
                        dataIndex: 'actionString',
                        flex: 2
                    },
                    {
                        text: '数据',
                        dataIndex: 'data',
                        flex: 1
                    },
                    {
                        text: '操作时间',
                        dataIndex: 'time',
                        xtype: 'datecolumn',
                        format: 'y/M/d H:i:s',
                        flex: 2
                    },
                    {
                        text: '登记时间',
                        dataIndex: 'createDate',
                        xtype: 'datecolumn',
                        format: 'y/M/d H:i:s',
                        flex: 2
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
                        {label: '拥有者姓名', name: 'ownerName', xtype: 'textfield'}
                    ],
                    [
                        {label: '拥有者昵称', name: 'ownerNickname', xtype: 'textfield'}
                    ],
                    [
                        {label: '拥有者手机', name: 'ownerMobile', xtype: 'textfield'}
                    ],
                    [
                        {label: '小区名', name: 'subdistrict', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
