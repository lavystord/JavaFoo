Ext.define('Console.controller.panelControllers.KeyManage', {
    extend: 'Ext.app.Controller',
    statics: {},

    refs: [
        {
            selector: 'viewport workspace keymanage',
            ref: 'keyManage'
        },
        {
            selector: 'keymanage #activate',
            ref: 'activateTool'
        },
        {
            selector: 'keymanage #deactivate',
            ref: 'deactivateTool'
        }
    ],

    config: {
        accessControl: [],
        subPanelAcls: null
    },

    requires: [
        'Ext.window.Window'
    ],

    constructor: function (acl, children) {
        this.accessControl = acl;
        this.subPanelAcls = children;

        this.callParent(arguments);
    },

    init: function () {
        this.listen({
            component: {
                'keymanage #activate': {
                    'click': function () {
                        this.activateItems();
                    }
                },
                'keymanage #deactivate': {
                    'click': function () {
                        this.deactivateItems();
                    }
                },
                'keymanage': {
                    'afterrender': function () {
                    }
                }
            }
        });


        var selectionModel = this.getKeyManage().down('grid').getSelectionModel();
        selectionModel.on({
            'selectionchange': {
                scope: this,
                fn: this.onSelectionChanged
            }
        })

    },

    activateItems: function () {
        var selected = this.getKeyManage().down('grid').getSelectionModel().getSelection();
        var record = selected[0];
        var jsonData = Ext.JSON.encode(record.getData());
        Ext.Ajax.request({
                url: 'key/activate',
                method: 'POST',
                scope: this,
                jsonData: jsonData,
                success: function (response, eOpts) {
                    this.getKeyManage().down('grid').getSelectionModel().deselectAll();
                    this.getKeyManage().down('grid').getStore().reload();
                },
                failure: function (response, eOpts) {
                    Console.config.Config.ajaxFailure(response);
                }
            }
        );
    },

    deactivateItems: function () {
        var selected = this.getKeyManage().down('grid').getSelectionModel().getSelection();
        var record = selected[0];
        var jsonData = Ext.JSON.encode(record.getData());
        if (record.get('type') == Console.config.Config.keyTypePrimary) {
            Ext.MessageBox.confirm(
                '提示', '停用一个主钥匙会导致其所有分享的钥匙被停用',
                function (buttonId) {
                    console.log(buttonId);
                    if (buttonId == 'yes') {
                        this.doDeactivate(jsonData);
                    }
                },
                this
            )
        }
        else {
            this.doDeactivate(jsonData);
        }
    },

    doDeactivate: function(jsonData) {
        Ext.Ajax.request({
                url: 'key/deactivate',
                method: 'POST',
                scope: this,
                jsonData: jsonData,
                success: function (response, eOpts) {
                    this.getKeyManage().down('grid').getSelectionModel().deselectAll();
                    this.getKeyManage().down('grid').getStore().reload();
                },
                failure: function (response, eOpts) {
                    Console.config.Config.ajaxFailure(response);
                }
            }
        );
    },

    onSelectionChanged: function (model, selected, eOpts) {
        var status = null;
        var type = null;
        if (selected.length == 1) {
            if (selected[0].get('type') == Console.config.Config.keyTypePrimary || selected[0].get('type') == Console.config.Config.keyTypeTemp) {
                type = selected[0].get('type');
                status = selected[0].get('status');
            }
        }

        if (((status == Console.config.Config.keyStatusActive
            || status == Console.config.Config.keyStatusLent) && type == Console.config.Config.keyTypePrimary) ||
            (status == Console.config.Config.keyStatusInUseAndOverTime && type == Console.config.Config.keyTypeTemp)) {
            this.getDeactivateTool().enable();
        }
        else if (status ==  Console.config.Config.keyStatusLent) {
            this.getActivateTool().enable();
        }
        else {
            this.getActivateTool().disable();
            this.getDeactivateTool().disable();
        }
    }


});
