/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.userManage.Details', {
    extend: 'Ext.window.Window',

    statics: {
        fileUrlPrefix: 'file?id=',
        defaultHeaderImage: '/resources/pictures/nophoto.jpg'
    },

    requires: [
        'Console.model.User',
        'Console.view.panelViews.BaseFormView',
        'Console.view.panelViews.UserRoleManage',
        'Ext.Img'
    ],
    record: null,
    gridView: null,
    children: null,
    width: 900,
    height: 600,
    modal: true,
    title: '用户详细资料',

    initComponent: function () {

        this.items = [{
            xtype: 'tabpanel',
            layout: 'border',
            items: [
                {
                title: '基本资料',
                xtype: 'baseformview',
                record: this.record,
                gridView: this.gridView,
                editable: false,
                layout: 'column',
                padding: '5 5 5 5',

                items: [{
                    xtype: 'fieldset',
                    columnWidth: .25,
                    layout: 'anchor',
                    padding: '5 5 5 5',
                    items: [{
                        xtype: 'textfield',
                        name: 'headerImageId',
                        hidden: true
                    }, {
                        xtype: 'image',
                        name: 'headerImage',
                        src: '/resources/pictures/nophoto.jpg',
                        imgCls: 'headerImage'
                    }]
                }, {
                    xtype: 'fieldset',
                    columnWidth: .75,
                    layout: 'anchor',
                    defaultType: 'textfield',
                    items: [{
                        xtype: 'textfield',
                        name: 'name',
                        fieldLabel: '姓名'
                    }, {
                        xtype: 'textfield',
                        name: 'nickname',
                        fieldLabel: '昵称'
                    }, {
                        xtype: 'textfield',
                        name: 'idCardNumber',
                        fieldLabel: '身份证号码'
                    }, {
                        xtype: 'textfield',
                        name: 'mobile',
                        fieldLabel: '手机'
                    }, {
                        xtype: 'textfield',
                        name: 'gender',
                        fieldLabel: '性别'
                    }, {
                        xtype: 'textfield',
                        name: 'active',
                        fieldLabel: '是否活跃'
                    }, {
                        xtype: 'textfield',
                        name: 'lastLoginDevice',
                        fieldLabel: '上次登录设备'
                    }]
                }]
            }
                ]
        }];


        // 如果有用户角色子表格则处理
        var userRoleItem = null;
        for (i in this.children) {
            if (this.children[i].dest == 'UserRoleManage') {
                userRoleItem = this.children[i];
            }
        }

        if(userRoleItem != null) {
            this.items[0].items.push({
                xtype: 'panel',
                title: '关联角色',
                items: {
                    xtype: 'userrolemanage',
                    user: this.record,
                    acl: userRoleItem.acl,
                    children: userRoleItem.children,
                    createView: 'Console.view.panelViews.userRoleManage.AddItem',
                    defaultPropertiesForAdding:{
                        userId: this.record.get('id'),
                        userNickname: this.record.get('nickname')
                    }
                }
            })
        }

        this.callParent(arguments);
    },

    afterRender: function () {
        var imageId = this.record.get('headerImageId');
        if (imageId != null && imageId != undefined) {
            var imageField = this.down("image");
            var src = this.statics().fileUrlPrefix + imageId;
            this.on({
                'show': {
                    scope: this,
                    fn: function () {
                        imageField.setSrc(src);
                    }
                }
            });
        }


        /*this.add(Ext.create('Console.view.panelViews.UserRoleManage',{
         title: '关联角色',
         user: this.record,
         acl: userRoleItem.acl,
         children: userRoleItem.children
         }))*/

        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.UserManage', {
    extend: 'Ext.panel.Panel',

    requires: [
        'Console.model.User',
        'Console.view.panelViews.BaseGridView',
        'Console.view.panelViews.BaseSearchPanel',
        'Console.view.util.ActiveCombobox',
        'Console.view.panelViews.userManage.Details'
    ],

    alias: 'widget.usermanage',

    initComponent: function () {
        this.layout = {
            type: 'border'
        };

        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.User',
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
                detailView: 'Console.view.panelViews.userManage.Details',
                columns: [
                    {text: '姓名', dataIndex: 'name', flex: 1},
                    {text: '昵称', dataIndex: 'nickname', flex: 1},
                    {text: '手机号', dataIndex: 'mobile', flex: 1},
                    {text: '性别', dataIndex: 'gender', sortable: false, flex: 1},
                    {text: '身份证号码', dataIndex: 'idCardNumber', flex: 1},
                    {text: '最近登录设备', dataIndex: 'lastLoginDevice', sortable: false, flex: 1},
                    {text: '是否活跃', dataIndex: 'active', xtype: 'booleancolumn', trueText: '是', falseText: '否', flex: 1},
                    {text: '创建日期', xtype: 'datecolumn', format: 'Y-m-d', dataIndex: 'createDate', flex: 1},
                    {text: '最后更新日期', xtype: 'datecolumn', format: 'Y-m-d', dataIndex: 'updateDate', flex: 1}
                    //{xtype: 'activecolumn',editor: null, flex: 1}
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
                        {label: '姓名', name: 'name', xtype: 'textfield'}
                    ],
                    [
                        {label: '身份证号码', name: 'idCardNumber', xtype: 'textfield'}
                    ],
                    [
                        {label: '手机号', name: 'mobile', xtype: 'textfield'}
                    ],
                    [
                        {label: '是否活跃', name: 'active', xtype: 'activecombobox'}
                    ],
                    [
                        {label: '创建日期起始', name: 'createDateBegin', xtype: 'datefield'},
                        {label: '创建日期终止', name: 'createDateEnd', xtype: 'datefield'}
                    ],
                    [
                        {label: '更新日期起始', name: 'updateDateBegin', xtype: 'datefield'},
                        {label: '更新日期终止', name: 'updateDateEnd', xtype: 'datefield'}
                    ]
                ],
                store: store
            }

        ];

        this.callParent(arguments);
    }

});
