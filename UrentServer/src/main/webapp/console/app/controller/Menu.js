Ext.define('Console.controller.Menu', {
    extend: 'Ext.app.Controller' ,
    models: [
        'Menu'
    ],

    init: function() {
        this.listen({
            component:{
                '#app-menu' :{
                    itemclick : this.onItemClick
                }
            }
        })
    },

    onItemClick : function(view, record){
        if(record.isLeaf())
            this.application.fireEvent('menuclicked', record);
    }
});
