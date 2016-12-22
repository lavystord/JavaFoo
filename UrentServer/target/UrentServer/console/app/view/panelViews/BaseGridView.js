/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-26
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 *
 *  这个抽象的gridview是用来统一权限控制对grid布局的影响
 */

Ext.define('Console.view.panelViews.BaseGridView', {
    extend: 'Ext.grid.Panel',
    acl: null,
    detailView: null,
    createView: null,
    children: null,
    defaultPropertiesForAdding: null,

    sortableColumns: false,         // 默认不允许排序，以免出现性能上的大瓶颈
    alias: 'widget.basegridview',

    requires: [
        'Ext.grid.column.CheckColumn'
    ],

    initComponent: function() {
        // 统一动态创建tools按钮
        if(null === this.tools || undefined == this.tools) {
            this.tools = [];
        }

        if(true === this.acl.read) {
            this.tools.push( {
                itemId: "refresh",
                type: "refresh" ,
                tooltip: "刷新"
            });
        }

        if(true === this.acl.create) {
            this.tools.push( {
                itemId: "plus",
                type: "plus" ,
                tooltip: "增加"
            });
        }

        if(true === this.acl.update) {
            this.tools.push( {
                itemId: "save",
                type: "save" ,
                tooltip: "保存"
            });
        }

        if(true === this.acl.deletee) {
            this.tools.push( {
                itemId: "minus",
                type: "minus" ,
                tooltip: "删除"
            });
        }

        // 如果有delete权限则创建选择框
        if(true === this.acl.deletee){
            if(null === this.selModel || undefined === this.selModel) {
                this.selModel = Ext.create('Ext.selection.CheckboxModel', {
                    injectCheckbox: 'first',
                    mode: 'MULTI',
                    checkOnly: 'true'
                });
            }
        }

        /* 如果没有update权限，刚把column里定义的editor给删除掉，由于editor的情况很多，这里只能删，不能增
         *  另外如果定义了像Ext.grid.column.CheckColumn这样的列，怎么禁止更新还不清楚
         */
        if(false === this.acl.update)  {
            for(var i =0; i < this.columns.length; i++) {
                var column = this.columns[i];
                column.editor = null;
            }
            this.plugins = [];
            this.selType = 'rowmodel';
        }
        this.callParent(arguments);
    },

    afterRender: function(){

        // 这里可以把公共的update/refresh/delete/double click/insert的逻辑处理掉
        this.setUpdateLogic();
        this.setRefreshLogic();
        this.setDeleteLogic();
        this.setCreateLogic();
        this.setViewDetailLogic();
        this.callParent(arguments);
    },

    setUpdateLogic: function(){
        var store = this.getStore();

        if(true === this.acl.update) {
            var saveTool =  this.down('tool[itemId=save]');
            saveTool.disable();
            store.on({
                'update': function(){
                    if(store.getUpdatedRecords().length > 0){
                        saveTool.enable();
                    }else{
                        saveTool.disable();
                    }
                }
            });
            saveTool.on({
                'click': function(){
                    store.sync();
                }
            });
            //this.store.load();
        }
    },

    setRefreshLogic: function(){
        var store = this.getStore();

        var refreshTool = this.down('tool[itemId=refresh]');
        if(refreshTool != null && refreshTool != undefined) {
            refreshTool.on({
                'click': function(){
                    store.load();
                }
            })
        }
    },

    setDeleteLogic: function(){
        var store = this.getStore();

        if(true === this.acl.deletee) {
            var minusTool = this.down('tool[itemId=minus]');
            if(minusTool != null && minusTool != undefined) {
                minusTool.disable();

                var selModel = this.getSelectionModel();
                if(null != selModel && undefined != selModel) {
                    selModel.on({
                        'selectionchange' : function(sm, selection){
                            if(selection.length > 0){
                                minusTool.enable();
                            }else{
                                minusTool.disable();
                            }
                        }

                    });
                }

                minusTool.on({
                    'click' : function(){
                        var selection = selModel.getSelection();
                        store.remove(selection);
                        store.sync({
                            failure: function() {
                                store.load();
                            }
                        });
                    }
                })
            }
        }
    },

    setViewDetailLogic: function() {
        if(this.detailView != null && this.detailView != undefined) {
            this.on({
                'itemdblclick': {
                    scope: this,
                    fn: function(view, record, item, index, evt){
                        /* 这里很奇怪，找不到extjs访问rest某个指定id的接口。唯一的一个还不能解析返回的数据格式
                            看了下代码好像是要求返回list，这与rest的接口定义不一致，因此只能自己硬写
                         */
                        var url = record.getProxy().url + "/" + record.getId();
                        Ext.Ajax.request({
                            url: url,
                            method: 'GET',
                            scope: this,
                            success: function(response, opts) {
                                var data = Ext.JSON.decode(response.responseText);
                                // 因为mapping是在reader里处理的，所以不能使用下面这行代码来创建model，而是用一种复杂的方案
                                // var model =  record.store.model.create(data);
                                var resultSet = record.store.model.getProxy().getReader().read([data]);
                                var model = resultSet.records[0];
                                Ext.create(this.detailView, {
                                    record: model,
                                    gridView: this,
                                    children: this.children
                                }).show();

                            },
                            failure: function(response, opts) {
                                Console.config.Config.ajaxFailure(response);
                            }
                        })

                        /*
                        record.store.model.load(record.getId(),{
                            scope: this,
                            success: function(record2) {
                                Console.log(record2);
                                Ext.create(this.detailView, {
                                    record: record2,
                                    gridView: this
                                }).show();
                            },
                            callback: function(record2, operation, success) {
                                if(success) {
                                    Console.log(record2);
                                    Ext.create(this.detailView, {
                                        record: record2,
                                        gridView: this
                                    }).show();
                                }
                            }
                        });
                        */
                    }
                }
            })
        }
    },

    setCreateLogic: function() {
        if(true === this.acl.create) {
            if (this.createView != null && this.createView != undefined) {
                var createTool = this.down('tool[itemId=plus]');
                if(createTool != null && createTool != undefined) {
                    createTool.on({
                        'click': {
                            scope: this,
                            fn: function () {
                                Ext.create(this.createView, {
                                    gridView: this,
                                    defaultPropertiesForAdding: this.defaultPropertiesForAdding
                                }).show();
                            }
                        }
                    })
                }
            }
        }
    }
});

