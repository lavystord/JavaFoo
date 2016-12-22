Ext.define('Console.controller.Workspace', {
    extend: 'Ext.app.Controller',

    requires: [
        'Console.config.Config',
        'Console.controller.panelControllers.*',
        'Console.view.panelViews.*'
        //'Console.view.panelViews.evalRelation'
    ],

    statics:{
        tabIdPrefix : 'wsTab_',
        tabViewFolder: 'panelViews',
        tabControllerFolder: 'panelControllers'
    },

    views: [
        'Workspace'
    ],

    refs:[{
        selector: '#workspace',
        ref: 'workspace'
    }],


    init: function(){
        this.application.on({
            menuclicked: this.onMenuItemClicked,
            scope: this
        });
    },

    onMenuItemClicked: function(item){
        var ws = this.getWorkspace();
        var tab = ws.child('#' + this.self.tabIdPrefix +item.get('id'));

        if(tab)
            ws.setActiveTab(tab);
        else{
            var newTab = this.createTabPanel(item);
            ws.setActiveTab(ws.add(newTab));
            if(item.get('clickCount') === undefined || item.get('clickCount') === 0 || item.get('clickCount') ===null) {
                this.createTabController(item, newTab);
                item.set('clickCount', 1);
            }
            else {
                item.set('clickCount', item.get('clickCount') + 1);
            }
        }
    },

    /*@ 根据所点击的menu获取页面的格式信息，并创建相应的tab页和controller*/
    createTabPanel: function(item){
        Ext.syncRequire('Console.view.' + this.self.tabViewFolder + '.' + item.get('dest'));
        //var view = this.getView(this.self.tabViewFolder + '.' + item.get('dest')).create();
        // view应该是每次都destroy的
        var view = Ext.create('Console.view.' + this.self.tabViewFolder + '.' + item.get('dest'), {
            acl: item.get('acl'),
            children: item.get('children')
        });
        var tabDesc = {
            title:    item.get('name'),
            closable: true,
            itemId:   this.self.tabIdPrefix + item.get('id'),
            items : [
                view
            ],
            layout: 'fit'
        };

        var tabPanel = Ext.create(
            'Ext.panel.Panel',
            tabDesc
        );
        return tabPanel;
        // console.log(view);
    },

    createTabController: function(item, newTab) {
        //Ext.syncRequire('Console.controller.' + this.self.panelControllers + '.' + item.get('dest'));
        /*Ext.create(
            'Console.controller.' + this.self.tabControllerFolder + '.' + item.get('dest'),
            item.get('acl'),
            item.get('children')
        ).init();*/
        var app = this.getApplication();
        app.createController( 'Console.controller.' + this.self.tabControllerFolder + '.' + item.get('dest'),
            {
                accessControl: item.get('acl'),
                subPanelAcls:  item.get('children')
            }
        );
    }
});
