Ext.define('Console.model.Method', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.UserRole'
    ],

    fields: [
        { name: 'value', type: 'string'}
    ],
    proxy: {
        type: "rest",
        url: 'method',
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
    idProperty: 'value'
    //hasMany: {model: 'Console.model.UserRole', name: 'userRoles'}
})