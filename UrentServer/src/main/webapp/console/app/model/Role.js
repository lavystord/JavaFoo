Ext.define('Console.model.Role', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'long' },
        { name: 'name', type: 'string', useNull: true},
        { name: 'comment', type: 'string' , useNull: true },
        { name: 'active', type: 'boolean' , useNull: true }
    ],
    proxy: {
        type: "rest",
        url: 'role',
        reader: {
            type: "json",
            totalProperty: "total",
            root: "list"
        },
        writer: {
            type: "json",
            writeAllFields: false
        }
    },
    idProperty: 'id'
    //hasMany: {model: 'Console.model.UserRole', name: 'userRoles'}
})