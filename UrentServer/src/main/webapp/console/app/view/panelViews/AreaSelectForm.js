/**
 * Created by Administrator on 2015/8/13.
 */
Ext.define('Console.view.panelViews.AreaSelectForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.areaselectform',
    title: '选择地区',

    requires: [
        'Ext.form.field.ComboBox',
        'Console.model.Area'
    ],

    callback: null,
    callbackScope: null,
    selectedRecord: null,

    comboBoxConfig: {
        fieldLabel: '',
        store:  {
            model: 'Console.model.Area',
            autoLoad: false,
            pageSize: 100,
            remoteFilter: true,
            proxy: {
                type: 'rest',
                url: 'area',
                reader: {
                    type: 'json',
                    totalProperty: "total",
                    root: "list"
                }
            }
        },
        lastQuery: '',
        editable: false,
        allowBlank: false,
        displayField: 'areaName',
        valueField: 'id',
        queryMode: 'remote',
        forceSelection: true
    },

    initComponent: function () {
        this.comboBoxConfig.level = 0;
        this.comboBoxConfig.fieldLabel = "层级0";
        this.items = [
            Ext.create('Ext.form.field.ComboBox', this.comboBoxConfig)
        ];
        this.buttons = [{
            xtype: 'button',
            text: '确定',
            action: 'save',
            disabled: true
        }, {
            xtype: 'button',
            text: '重置',
            action: 'reset',
            disabled: true
        }];
        this.callParent(arguments);
    },

    afterRender: function () {
        var comboBox = this.down('combobox');
        var store = comboBox.getStore();
        store.addFilter(
            Ext.create('Ext.util.Filter', {
                property: 'parentId',
                value: 0
            })
        )
        comboBox.on({
            'select': {
                scope: this,
                fn: function (combo, records, eOpts) {
                    var record = records[0];
                    this.onAreaSelected(combo, record);
                }
            }
        });

        var saveButton = this.down('button[action=save]');
        saveButton.on({
            'click': {
                scope: this,
                fn: function(btn){
                    this.onSaveButtonClicked(btn);
                }
            }
        });

        var resetButton = this.down('button[action=reset]');
        resetButton.on({
            'click': {
                scope: this,
                fn: function(btn) {
                    this.onResetButtonClicked(btn);
                }
            }
        })

        this.callParent(arguments);
    },


    onAreaSelected: function (combo, record) {
        var resetButton = this.down('button[action=reset]');
        if(resetButton.isDisabled()){
            resetButton.enable();
        }

        var level = combo.level;
        this.removeAllDecsendents(level);
        this.comboBoxConfig.level = level + 1;
        this.comboBoxConfig.fieldLabel = "层级" + this.comboBoxConfig.level;
        this.comboBoxConfig.store.autoLoad = false;
        var childCombo = Ext.create('Ext.form.field.ComboBox', this.comboBoxConfig);
        var store = childCombo.getStore();
        store.addFilter({
            property: 'parentId',
            value: record.get('id')
        }, false);
        store.load({
            scope: this,
            callback: function(records, oper, eOpts) {
                if(records.length > 0) {
                    childCombo.on({
                        'select': {
                            scope: this,
                            fn: function (combo, records, eOpts) {
                                var record = records[0];
                                this.onAreaSelected(combo, record);
                            }
                        }
                    })
                    this.add(childCombo);
                }
                else {
                    var saveButton = this.down('button[action=save]');
                    saveButton.enable();
                    this.selectedRecord = record;
                }
            }
        });
    },

    removeAllDecsendents: function (level) {
        do {
            level = level + 1;
            var childCombo = this.down('combobox[level=' + level + ']');
            if (childCombo != null) {
                this.remove(childCombo);
            }
            else {
                break;
            }
        } while (true);
    },

    onSaveButtonClicked: function(btn) {
        btn.disable();
        if(this.callback != null && typeof this.callback == 'function'){
            this.callback.call(this.callbackScope, this.selectedRecord);
        }
        else{
            Ext.example.msg('warning', '必须给地址选择组件赋予回调函数');
        }
    },

    onResetButtonClicked: function(btn) {
        this.removeAllDecsendents(0);
        var comboBox = this.down('combobox');
        comboBox.reset();
        btn.disable();
    }

});
