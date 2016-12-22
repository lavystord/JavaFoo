Ext.define('Console.model.AppMenuTree', {
    extend: 'Ext.data.Model',
    
    fields: [
        { name: 'id', type: 'int' },
        { name: 'name', type: 'string' },
        { name: 'dest', type: 'string' },
        { name: 'acl', type: 'object'}
    ],

    proxy: {
        type: "rest",
        //url: '/app/temp/menu.json',
         url: 'appmenu',
        reader: {
            type: "json"
        }
    }
});
