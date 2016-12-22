Ext.define('Console.model.AllAppMenuTree', {
    extend: 'Ext.data.Model',
    
    fields: [
        { name: 'id', type: 'int' },
        { name: 'name', type: 'string' },
        { name: 'dest', type: 'string' }
    ],

    proxy: {
        type: "rest",
        //url: '/app/app/tempData/menu.json',
        url: 'allappmenu',
        reader: {
            type: "json"
        },
        writer: {
            type: "json",
            writeAllFields: false
        }
    }
});
