/**
 * Created by Administrator on 2015/8/3.
 */
Ext.define('Console.view.panelViews.BaseSearchPanel', {
    extend: 'Ext.form.Panel',

    alias: 'widget.basesearchpanel',

    padding: '5 5 0 5',

    style: 'background-color: #fff;',

    border: false,

    buttons: [
        {
            text: '查询',
            action: 'search',
            disabled: true
        },
        {
            text: '清空',
            action: 'clear',
            disabled: true
        }
    ],

    initComponent: function() {
        this.collapsible = true;
        this.title = '搜索';
        this.items = [];
        for(var i =0; i < this.fields.length; i ++) {
            var array = new Array();
            array = this.fields[i];
            var f = {
                xtype: 'fieldset',
                layout:  {
                    type: 'hbox',
                    defaultMargins: {
                        top: 0,
                        right: 25,
                        bottom: 0,
                        left: 0
                    }
                },
                items: [

                ]
            };
            for(var j in array) {
                var item = {
                    xtype: array[j].xtype,
                    name: array[j].name,
                    fieldLabel: array[j].label
                };
                // 格式优化
                if(item.xtype == 'datefield') {
                    item.format = 'Y/m/d';
                }
                f.items.push(item);
                f.items.push({
                    xtype: 'displayfield',
                    name: array[j].name + '-savedValue',
                    fieldLabel: null,
                    readOnly: true,
                    fieldStyle: 'background-color: #A4D3EE; color: #CD3700'
                })
            }
            this.items.push(f);
        }

        this.callParent(arguments);
    },

    afterRender: function() {
        var searchButton = this.down('button[action=search]');
        var clearButton = this.down('button[action=clear]');
        var store = this.store;


        store.on({
            'load': {
                scope: this,
                fn: function(){
                    // 这里调用isFiltered方法不能正确返回有无filter，可能是remoteFilter的关系
                    if(store.filters.getCount() > 0) {
                        clearButton.enable();
                        clearButton.setHandler(this.clear, this);
                    }
                    else {
                        clearButton.disable();
                    }
                }
            }
        });

        var items = this.getForm().getFields().items;
        for(var i = 0; i < items.length; i ++) {
            items[i].savedValue = items[i].getValue();
            items[i].on ({
                'change': {
                    scope: this,
                    fn: function(field, value, oldValue) {
                        if(value != field.savedValue && searchButton.isDisabled()) {
                            searchButton.enable();
                            searchButton.setHandler(this.search, this);
                        }
                        else if(value == field.savedValue && !searchButton.isDisabled()) {
                            if(this.checkItemsChanged(items) == false) {
                                searchButton.disable();
                            }
                        }
                    }
                }
            });
        }

        this.callParent(arguments);
    },

    search: function() {
        var items = this.getForm().getFields().items;
        this.saveItems(items);
        var searchButton = this.down('button[action=search]');
        searchButton.disable();

        var object = this.getValues();
        var filters = new Array();

        for (var i in items) {
            if(items[i].getName().indexOf('savedValue') == -1) {
                var value = items[i].getValue();
                if(items[i].xtype == 'textfield' && value == ''){
                    value = null;
                }
                if (value != null && value != undefined) {
                    // 对Date类型作特殊处理
                    if(items[i].xtype == 'datefield') {
                        value = Ext.Date.format(value, 'Ymd');
                    }
                    var filter = new Ext.util.Filter({
                        property: items[i].getName(),
                        value: value
                    });
                    filters.push(filter);
                }
            }
        }
        if(filters.length > 0) {
            this.store.clearFilter(true);
            this.store.addFilter(filters);
            this.setTitle("<font color=\"red\">搜索（有过滤条件）</font>");
        }
        else {
            this.store.clearFilter();
            this.setTitle("搜索");
        }
    },

    clear: function() {
        this.resetItems(this.getForm().getFields().items);
        this.store.clearFilter();

        var searchButton = this.down('button[action=search]');
        searchButton.disable();

        var clearButton = this.down('button[action=clear]');
        clearButton.disable();
        this.setTitle("搜索");
    },

    checkItemsChanged: function(items) {
        for(var i = 0; i < items.length; i ++) {
            if(items[i].savedValue != items[i].getValue()) {
                return true;
            }
        }

        return false;
    },

    saveItems: function(items) {
        for(var i = 0; i < items.length; i ++) {
            if(items[i].getName().indexOf('savedValue') == -1)
                items[i].savedValue = items[i].getValue();
            else {
                var value = items[i-1].getValue();
                if(value != null && value != undefined) {
                    if (items[i - 1].xtype == 'datefield') {
                        value = value.toLocaleDateString();
                    }
                }

                items[i].setValue(value);
            }
        }
    },

    resetItems: function(items) {
        for(var i = 0; i < items.length; i ++) {
            items[i].reset();
            if(items[i].getName().indexOf('savedValue') == -1)
                items[i].savedValue = items[i].getValue();
        }
    }



});