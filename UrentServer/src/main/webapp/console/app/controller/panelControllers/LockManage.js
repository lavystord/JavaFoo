Ext.define('Console.controller.panelControllers.LockManage', {
    extend: 'Ext.app.Controller',
    statics: {

    },

    refs : [
        {
            selector: 'viewport workspace lockmanage',
            ref: 'lockManage'
        },
        {
            selector: 'lockmanage #deactivate',
            ref: 'deactivateTool'
        }
    ],

    config:{
        accessControl: [] ,
        subPanelAcls: null
    },

    requires: [
        'Ext.window.Window'
    ],

    constructor: function(acl, children) {
        this.accessControl = acl;
        this.subPanelAcls = children;

        this.callParent(arguments);
    },


    init: function () {
        this.listen({
            component: {
                'lockmanage #deactivate': {
                    'click': function () {
                        this.deactivateItems();
                    }
                },
                'lockmanage': {
                    'afterrender': function () {
                    }
                }
            }
        });


        var selectionModel = this.getLockManage().down('grid').getSelectionModel();
        selectionModel.on({
            'selectionchange': {
                scope: this,
                fn: this.onSelectionChanged
            }
        })

    },

    deactivateItems: function () {
        var selected = this.getLockManage().down('grid').getSelectionModel().getSelection();
        var record = selected[0];
        var jsonData = Ext.JSON.encode(record.getData());
        Ext.MessageBox.confirm(
            '提示', '禁用后的锁不可再启用，只能被重新初始化后再次安装',
            function (buttonId) {
                console.log(buttonId);
                if (buttonId == 'yes') {
                    Ext.Ajax.request({
                            url: 'lock/deactivate',
                            method: 'POST',
                            scope: this,
                            jsonData: jsonData,
                            success: function (response, eOpts) {
                                this.getLockManage().down('grid').getSelectionModel().deselectAll();
                                this.getLockManage().down('grid').getStore().reload();
                            },
                            failure: function (response, eOpts) {
                                Console.config.Config.ajaxFailure(response);
                            }
                        }
                    );
                }
            },
            this
        )
    },

    onSelectionChanged: function (model, selected, eOpts) {
        var active = null;
        if (selected.length == 1) {
                active = selected[0].get('active');
        }

        if (active == true) {
            this.getDeactivateTool().enable();
        }
        else {
            this.getDeactivateTool().disable();
        }
    }

});
