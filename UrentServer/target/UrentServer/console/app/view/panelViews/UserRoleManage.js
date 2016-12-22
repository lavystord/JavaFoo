/**
 * Created by Administrator on 2015/8/6.
 */

Ext.define('Console.view.panelViews.userRoleManage.AddItem', {
    extend: 'Ext.window.Window',

    requires: [
        'Console.model.UserRole',
        'Console.model.Role',
        'Console.view.panelViews.BaseFormView',
        'Console.view.util.ActiveCombobox'
    ],
    gridView: null,
    children: null,
    width: 600,
    height: 300,
    modal: true,
    title: '增加用户角色关联',

    initComponent: function () {
        this.items = [{
            title: '用户角色关联',
            xtype: 'baseformview',
            gridView: this.gridView,
            defaultPropertiesForAdding: this.defaultPropertiesForAdding,
            model: 'Console.model.UserRole',
            editable: true,
            layout: 'vbox',
            padding: '5 5 5 5',

            items: [{
                xtype: 'textfield',
                readOnly: 'true',
                name: 'userNickname',
                fieldLabel: '用户昵称'
            },
                {
                    xtype: 'combobox',
                    name: 'roleId',
                    fieldLabel: '角色',
                    store: Ext.create('Ext.data.Store', {
                        model: 'Console.model.Role',
                        autoLoad: false,
                        remoteFilter: true,
                        proxy: {
                            type: 'rest',
                            url: 'roleUnrelated',
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
                    displayField: 'name',
                    valueField: 'id',
                    queryMode: 'remote',
                    forceSelection: true,
                    validator: function (value) {
                        return (value != null && value != undefined)
                    }
                }, {
                    xtype: 'numberfield',
                    hidden: true,
                    name: 'userId',
                    fieldLabel: '用户编号'
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
        var roleSelection = this.down('combobox[name=roleId]');
        var userId = this.down('numberfield[name=userId]');
        roleSelection.on({
            'expand': {
                scope: this,
                fn: function() {
                    var store = roleSelection.getStore();
                    if(store.getCount() == 0) {
                        store.addFilter([new Ext.util.Filter({
                            property: 'userId',
                            value: userId.getValue()
                        })]);
                    }
                }
            }
        })
        this.callParent(arguments);
    }
});


Ext.define('Console.view.panelViews.UserRoleManage', {
    extend: 'Console.view.panelViews.BaseGridView',

    requires: [
        'Console.model.UserRole',
        'Console.view.panelViews.BaseGridView',
        'Ext.util.Filter'
    ],

    alias: 'widget.userrolemanage',

    user: null,

    role: null,

    acl: null,

    initComponent: function () {
        var store = Ext.create('Ext.data.Store', {
            model: 'Console.model.UserRole',
            autoLoad: false,
            pageSize: 25,
            remoteFilter: true,
            remoteSort: true
        });
        this.columns = [];
        if (this.user == null) {
            this.columns.push({
                text: '用户', dataIndex: 'userNickname', flex: 1
            });
        }
        if (this.role == null) {
            this.columns.push({
                text: '角色', dataIndex: 'roleName', flex: 1
            });
        }

        if (this.user != null) {
            store.addFilter([
                new Ext.util.Filter({
                    property: 'userId',
                    value: this.user.get('id')
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

        this.callParent(arguments);
    },

    afterRender: function () {
        this.callParent(arguments);
    }

});