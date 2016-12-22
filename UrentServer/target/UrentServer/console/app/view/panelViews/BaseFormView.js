/**
 * Created by Administrator on 2015/8/4.
 */
Ext.define('Console.view.panelViews.BaseFormView', {
    extend: 'Ext.form.Panel',

    alias: 'widget.baseformview',

    editable: false,

    record: null,

    defaultPropertiesForAdding: null,

    gridView: null,

    model: null,

    trackResetOnLoad: true,

    padding: '5 5 0 5',

    initComponent: function() {
        var saveText;
        if(this.record != null) {
            saveText = '保存';
        }
        else {
            saveText = '插入';
        }
        if(this.editable == true) {
            this.buttons = [{
                xtype: 'button',
                action: 'save',
                text: saveText,
                disabled: true
            },{
                xtype: 'button',
                action: 'reset',
                text: '取消',
                disabled: true
            }
            ];
        }

        this.on({
            'afterrender': {
                scope: this,
                fn: function() {
                    if(this.record != null && this.record != undefined) {
                        this.loadRecord(this.record);
                        this.saveState();
                    }

                    if(this.defaultPropertiesForAdding != null && this.defaultPropertiesForAdding != undefined) {
                        var model = Ext.ModelManager.getModel(this.model);
                        var record = model.create(this.defaultPropertiesForAdding);
                        this.loadRecord(record);
                    }


                    var items = this.getForm().getFields().items;
                    if(this.editable == false) {
                        for(var i in items) {
                            var item = items[i];
                            if(typeof item.setReadOnly  == "function") {
                                item.setReadOnly(true);
                            }
                        }
                    }
                    else {
                        var saveButton = this.down('button[action=save]');
                        var resetButton = this.down('button[action=reset]');

                        for(var i in items) {
                            var item = items[i];
                            item.on({
                                'change': {
                                    scope: this,
                                    fn: function (field, newValue, oldValue) {
                                        if (newValue != oldValue) {
                                            if (this.isDirty()){
                                                if(this.isValid()) {
                                                    saveButton.enable();
                                                }
                                                resetButton.enable();

                                            }
                                            else {
                                                saveButton.disable();
                                                resetButton.disable();
                                            }
                                        }
                                    }
                                }
                            });
                        }

                        saveButton.on({
                            'click': {
                                scope: this,
                                fn: function() {
                                    if(this.record != null && this.record != undefined) {
                                        var record = this.getRecord();
                                        var values = this.getValues();
                                        record.set(values);
                                        var recordInStore = this.gridView.getStore().getById(this.record.get('id'));
                                        recordInStore.set(values);
                                        var myWindow = this.up('window');
                                        this.gridView.getStore().sync({
                                            scope: this,
                                            success: function() {
                                                this.gridView.getStore().load();
                                                myWindow.close();
                                            },
                                            failure: function() {
                                                this.gridView.getStore().load();
                                                myWindow.close();
                                                Ext.example.msg("error", "更新失败");
                                            }
                                        });

                                        // this.loadRecord(this.record);
                                    }
                                    else {
                                        // 插入操作
                                        var model = Ext.ModelManager.getModel(this.model);
                                        var values = this.getValues();
                                        // Console.log(values);
                                        var record = model.create(values);
                                        record = this.gridView.getStore().add([record])[0];
                                        // 插入操作默认强制关闭窗口，否则这里有两个问题不好处理：
                                        // 1、拿不到这个新插入的record的id
                                        // 2、有些record的域只能在新建时编辑
                                        var myWindow = this.up('window');
                                        this.gridView.getStore().sync({
                                            scope: this,
                                            success: function() {
                                                this.gridView.getStore().load();
                                                myWindow.close();
                                            },
                                            failure: function() {
                                                this.gridView.getStore().load();
                                                myWindow.close();
                                                Ext.example.msg("error", "插入失败");
                                            }
                                        });

                                    }
                                }
                            }
                        });

                        resetButton.on({
                            'click': {
                                scope: this,
                                fn: function(){
                                    this.getForm().reset();
                                }
                            }
                        })
                    }
                }
            }
        });
        this.callParent(arguments);
    },

    afterRender: function() {
        Ext.override(this.getForm(), {
            isDirty: function() {
                return !!this.getFields().findBy(function(f) {
                    return !f.skipDirty && f.isDirty();
                });
            }
        });
        this.callParent(arguments);
    }
})