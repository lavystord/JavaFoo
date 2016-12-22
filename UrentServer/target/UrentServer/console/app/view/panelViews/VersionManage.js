/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.versionManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Version',
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
    title: '增加版本信息',
    layout: 'anchor',

    initComponent: function () {
        this.items = [
            {
                xtype: 'form',          // 因为这里要上传文件，所以不能用baseformview，要自己写插入逻辑
                anchor: '100%',
                layout: 'anchor',
                items: [
                    {
                        xtype: 'numberfield',
                        name: 'major',
                        fieldLabel: '主版本号',
                        allowBlank: false

                    }, {
                        xtype: 'numberfield',
                        name: 'minor',
                        fieldLabel: '副版本号',
                        allowBlank: false
                    }, {
                        xtype: 'versiontypecombobox',
                        name: 'type',
                        fieldLabel: '类型',
                        allowBlank: false
                    }, {
                        xtype: 'filefield',
                        name: 'firmwareFile',
                        readOnly: true,
                        fieldLabel: '固件文件'
                    }, {
                        xtype: 'textarea',
                        name: 'comment',
                        width: 500,
                        height: 150,
                        fieldLabel: '备注'
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
            },
            {
                xtype: 'button',
                text: '增加',
                action: 'add'
            }
        ];

        this.callParent(arguments);
    },

    afterRender: function () {
        var addButton = this.down('button[action=add]');
        addButton.on({
            'click': {
                scope: this,
                fn: function(){
                    var form = this.down('form').getForm();
                    if(form.isValid()){
                        form.submit({
                            url: 'version',
                            waitMsg: '上传镜像文件中.',
                            scope: this,
                            headers: {
                                Accept: 'application/json'
                            },
                            success: function(form, action) {
                                this.close();
                                this.gridView.getStore().load();
                            },
                            failure: function(form, action) {
                                Ext.example.msg('error',
                                    "增加版本号失败，错误代码是" + action.result.errorCode
                                + "，错误信息是[" + action.result.message + "]");
                            }
                        });
                    }
                }
            }
        })
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.versionManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.Version',
        'Console.view.panelViews.BaseFormView',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 600,
    height: 400,
    modal: true,
    title: '版本详细资料',

    initComponent: function () {

        this.items = [
            {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                editable: true,
                layout: 'anchor',
                padding: '5 5 5 5',

                items: [{
                    xtype: 'numberfield',
                    name: 'major',
                    readOnly: true,
                    fieldLabel: '主版本号'
                }, {
                    xtype: 'numberfield',
                    name: 'minor',
                    readOnly: true,
                    fieldLabel: '副版本号'
                }, {
                    xtype: 'numberfield',
                    name: 'intFormat',
                    readOnly: true,
                    fieldLabel: '整型形式'
                }, {
                    xtype: 'textfield',
                    name: 'typeString',
                    readOnly: true,
                    fieldLabel: '类型'
                }, {
                    xtype: 'textfield',
                    name: 'stringFormat',
                    readOnly: true,
                    fieldLabel: '字符串形式'
                }, {
                    xtype: 'textfield',
                    name: 'firmwareFileId',
                    readOnly: true,
                    fieldLabel: '固件文件名称'
                }, {
                    xtype: 'textarea',
                    name: 'comment',
                    fieldLabel: '备注'
                }, {
                    xtype: 'datefield',
                    name: 'createDate',
                    readOnly: true,
                    fieldLabel: '创建时间'
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
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.VersionManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.Version',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.VersionTypeComboBox',
        'Console.view.panelViews.versionManage.Details',
        'Ext.grid.plugin.CellEditing'
    ],

    alias: 'widget.versionmanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.Version',
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
                detailView: 'Console.view.panelViews.versionManage.Details',
                createView: 'Console.view.panelViews.versionManage.AddItem',
                selType: 'cellmodel',
                plugins: [
                    Ext.create('Ext.grid.plugin.CellEditing', {
                        id: 'cellEditing',
                        clicksToEdit: 1
                    })
                ],
                columns: [{
                    text: '主版本号', dataIndex: 'major', flex: 1
                }, {
                    text: '副版本号', dataIndex: 'minor', flex: 1
                }, {
                    text: '整型形式',
                    dataIndex: 'intFormat',
                    xtype: 'numbercolumn',
                    flex: 1
                }, {
                    text: '字符串形式',
                    dataIndex: 'stringFormat',
                    flex: 2
                }, {
                    text: '类型',
                    dataIndex: 'typeString',
                    flex: 1
                }, {
                    text: '备注',
                    dataIndex: 'comment',
                    flex: 5, editor: {
                        xtype: 'textarea', allowBlank: true
                    }
                }, {
                    text: '创建时间',
                    dataIndex: 'createDate',
                    xtype: 'datecolumn',
                    format: 'Y-m-d',
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
                        {label: '主版本号', name: 'major', xtype: 'numberfield'}
                    ],
                    [
                        {label: '副版本号', name: 'minor', xtype: 'numberfield'}
                    ],
                    [
                        {label: '类型', name: 'type', xtype: 'versiontypecombobox'}
                    ],
                    [
                        {label: '备注', name: 'comment', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
