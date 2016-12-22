Ext.define('Console.view.LoginWindow', {
    extend: 'Ext.window.Window' ,
    alias : 'widget.loginwindow',

    requires: ['Ext.form.Panel'],

    title : '登录',
    layout: 'fit',
    autoShow: true,
    closable: false,
    width: 280,

    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                padding: '5 5 0 5',
                border: false,
                style: 'background-color: #fff;',

                items: [
                    {
                        xtype: 'textfield',
                        name : 'username',
                        fieldLabel: '用户名'
                    },
                    {
                        xtype: 'textfield',
                        name : 'password',
                        inputType: 'password',
                        fieldLabel: '密码'
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: '登录',
                action: 'login'
            },
            {
                text: '退出',
                handler: window.close
            }
        ];
        this.callParent(arguments);
    },

    afterRender: function() {
        this.down('textfield[name=username]').focus(false, 200);
        this.callParent(arguments);
    }

});