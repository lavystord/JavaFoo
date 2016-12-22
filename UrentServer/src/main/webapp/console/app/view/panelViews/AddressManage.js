/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.addressManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Address',
        'Console.model.Area',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.AreaSelectForm',
        'Console.view.util.ActiveCombobox',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 400,
    modal: true,
    title: '增加新地点',

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
                    model: 'Console.model.Address',
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [{
                        xtype: 'textarea',
                        name: 'subdistrict',
                        fieldLabel: '地点',
                        allowBlank: false
                    }, {
                        xtype: 'numberfield',
                        name: 'longitude',
                        fieldLabel: '地点经度',
                        decimalPrecision: 7,
                        allowBlank: false
                    }, {
                        xtype: 'numberfield',
                        name: 'latitude',
                        fieldLabel: '地点纬度',
                        decimalPrecision: 7,
                        allowBlank: false
                    }, {
                        xtype: 'textarea',
                        readOnly: 'true',
                        fieldLabel: '所在区域',
                        name: 'areaString',
                        allowBlank: false
                    }, {
                        xtype: 'hidden',
                        name: 'areaId'
                    }]
                },
                {
                    xtype: 'areaselectform',
                    title: '选择所在区域',
                    padding:'5 5 5 5',
                    callback: this.changeArea,
                    callbackScope: this,
                    collapsible: true,
                    width: 300,
                    height: 200
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
    },

    changeArea: function(record) {
        var areaStringField = this.down('textarea[name=areaString]');
        areaStringField.setValue(record.get('areaString'));
        var areaIdField = this.down('hidden[name=areaId]');
        areaIdField.setValue(record.get('id'));
    }
});


Ext.define('Console.view.panelViews.addressManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Address',
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
    height: 300,
    modal: true,
    title: '地点详细资料',

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
                    editable: true,
                    layout: 'anchor',
                    padding: '5 5 5 5',

                    items: [{
                        xtype: 'textarea',
                        name: 'subdistrict',
                        fieldLabel: '地点'
                    }, {
                        xtype: 'numberfield',
                        name: 'longitude',
                        fieldLabel: '地点经度'
                    }, {
                        xtype: 'numberfield',
                        name: 'latitude',
                        fieldLabel: '地点纬度'
                    }, {
                        xtype: 'textarea',
                        readOnly: 'true',
                        fieldLabel: '所在区域',
                        name: 'areaString',
                        skipDirty: true
                    }, {
                        xtype: 'hidden',
                        name: 'areaId'
                    }]
                },
                {
                    xtype: 'areaselectform',
                    title: '更新所在区域',
                    padding:'5 5 5 5',
                    callback: this.changeArea,
                    callbackScope: this,
                    collapsible: true,
                    width: 300,
                    height: 200
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
    },


    changeArea: function(record) {
        var areaIdField = this.down('hidden[name=areaId]');
        var areaStringField = this.down('textarea[name=areaString]');
        if(record != null) {
            areaIdField.setValue(record.get('id'));
            areaStringField.setValue(record.get('areaString'));
        }
        else {
            Ext.example.msg('warning', '地址必须有关联的区域');
        }
    }
});


Ext.define('Console.view.panelViews.AddressManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Address',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.addressManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.addressmanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.Address',
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
                detailView: 'Console.view.panelViews.addressManage.Details',
                createView: 'Console.view.panelViews.addressManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [
                    {
                        text: '名称', dataIndex: 'subdistrict', flex: 3
                    },{
                        text: '所属街道', dataIndex: 'areaName', flex: 3
                    }, {
                        text: '坐标经度', dataIndex: 'longitude', xtype: 'numbercolumn', flex: 1, editor: {
                            xtype: 'numberfield', allowBlank: true
                        }
                    }, {
                        text: '坐标纬度',
                        dataIndex: 'latitude',
                        xtype: 'numbercolumn',
                        flex: 1,
                        editor: {
                            xtype: 'numberfield', allowBlank: true
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
                        {label: '名称', name: 'subdistrict', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
