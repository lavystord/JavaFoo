Ext.define('Console.model.Menu', {
    extend: 'Ext.data.Model',
    
    fields: [
        { name: 'id', type: 'long' },
        { name: 'name', type: 'string' , useNull: true },
        { name: 'dest', type: 'string'  , useNull: true},
        { name : 'leaf', type: 'boolean' , useNull: true},
        { name: 'parentId', type: 'long' , useNull: true}
    ],

    proxy: {
        type: "rest",
        url: 'menu',
        reader: {
            type: "json"
        },
        writer: {
            type: "json",
            writeAllFields: false
        }
    }
});
