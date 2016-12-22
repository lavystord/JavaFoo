/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.dataModificationLogManage.Details', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.DataModificationLog',
        'Console.view.panelViews.BaseFormView',
        'Ext.form.field.Date'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 800,
    height: 500,
    modal: true,
    title: '日志详细信息',

    initComponent: function () {

        this.items = [{
            title: '基本资料',
            xtype: 'baseformview',
            record: this.record,
            gridView: this.gridView,
            editable: false,
            layout: 'anchor',
            padding: '5 5 5 5',

            items: [{
                xtype: 'textfield',
                name: 'userName',
                fieldLabel: '用户名'
            }, {
                xtype: 'textfield',
                name: 'userMobile',
                fieldLabel: '用户手机号'
            }, {
                xtype: 'textfield',
                name: 'method',
                fieldLabel: 'HTTP请求方法'
            }, {
                xtype: 'textfield',
                name: 'calledClassName',
                fieldLabel: '请求调用类'
            }, {
                xtype: 'textfield',
                name: 'calledMethodName',
                fieldLabel: '请求调用方法'
            }, {
                xtype: 'textarea',
                width: 600,
                height: 80,
                name: 'userAgent',
                fieldLabel: '客户端'
            }, {
                xtype: 'textfield',
                name: 'remoteAddress',
                fieldLabel: '来源地址'
            }, {
                xtype: 'textarea',
                width: 600,
                height: 80,
                name: 'bodyData',
                fieldLabel: '请求数据'
            }, {
                xtype: 'datefield',
                name: 'createDate',
                format:  'y/M/d H:i:s',
                fieldLabel: '发生时间'
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


Ext.define('Console.view.panelViews.DataModificationLogManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.DataModificationLog',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Ext.form.field.Date'
    ],

    alias: 'widget.datamodificationlogmanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.DataModificationLog',
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
                detailView: 'Console.view.panelViews.dataModificationLogManage.Details',
                columns: [{
                    text: '用户名', dataIndex: 'userName', flex: 1
                }, {
                    text: '用户手机号', dataIndex: 'userMobile', flex: 2
                }, {
                    text: 'HTTP请求方法',  dataIndex: 'method',   flex: 1
                }, {
                    text: '请求调用类',  dataIndex: 'calledClassName',   flex: 1
                }, {
                    text: '请求调用方法',  dataIndex: 'calledMethodName',   flex: 1
                }, {
                    text: '客户端',  dataIndex: 'userAgent',   flex: 3
                }, {
                    text: '来源地址',  dataIndex: 'remoteAddress',   flex: 1
                }, {
                    text: '请求数据',  dataIndex: 'bodyData',   flex: 4
                }, {
                    text: '发生时间',  dataIndex: 'createDate', xtype: 'datecolumn', format:  'y/M/d H:i:s',  flex: 2
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
                        {label: '发生时间起始', name: 'createDateBegin', xtype: 'datefield', format:  'y/M/d H:i:s'},
                        {label: '发生时间结束', name: 'createDateEnd', xtype: 'datefield', format:  'y/M/d H:i:s'}
                    ],
                    [
                        {label: '用户名', name: 'userName', xtype: 'textfield'},
                        {label: '用户手机号', name: 'createDateEnd', xtype: 'textfield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
