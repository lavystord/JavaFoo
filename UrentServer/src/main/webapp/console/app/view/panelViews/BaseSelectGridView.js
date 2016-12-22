/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-26
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 *
 *  这个抽象的gridview是用来处理选择另一个grid中的某行关联的统一逻辑
 */

Ext.define('Console.view.panelViews.BaseSelectGridView', {
    extend: 'Ext.window.Window',
    callback: null,
    callbackScope: null,
    gridClass: null,
    gridConfig: null,

    width: 800,
    height: 600,

    selectedRecord: null,
    alias: 'widget.baseselectgridview',
    layout: 'fit',
    modal: true,

    requires: [
        'Ext.grid.Panel'
    ],

    initComponent: function(){
        this.gridConfig.acl = {
            read: false,
            create: false,
            update: false,
            deletee: false
        };
        this.items = [
            Ext.create(this.gridClass, this.gridConfig)
        ];
        this.buttons = [
            {
                xtype: 'button',
                action: 'select',
                text: '选择',
                disabled: true
            },
            {
                xtype: 'button',
                action: 'setNull',
                text: '置空'
            }
        ]
        this.callParent(arguments);
    },

    afterRender: function() {
        var grid = this.down('grid');
        var selectButton = this.down('button[action=select]');
        var clearButton = this.down('button[action=setNull]');

        grid.on({
            'select': {
                scope: this,
                fn: function(rowModel, record, index){
                    this.selectedRecord = record;
                    selectButton.enable();
                }
            },
            'deselect': {
                scope: this,
                fn: function(){
                    this.selectedRecord = null;
                    selectButton.disable();
                }
            }
        });

        selectButton.on({
            'click': {
                scope: this,
                fn: function(btn){
                    if(this.callback != null && typeof this.callback == 'function'){
                        this.callback.call(this.callbackScope, this.selectedRecord);
                        this.close();
                    }
                    else{
                        Ext.example.msg('warning', '必须给表格选择组件赋予回调函数');
                    }
                }
            }
        });

        clearButton.on({
            'click': {
                scope: this,
                fn: function(btn) {
                    if(this.callback != null && typeof this.callback == 'function'){
                        this.callback.call(this.callbackScope, null);
                        this.close();
                    }
                    else{
                        Ext.example.msg('warning', '必须给表格选择组件赋予回调函数');
                    }

                }
            }
        })
        this.callParent(arguments);
    }
});

