Ext.define('Console.model.Url', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'long' },
        { name: 'value', type: 'string', useNull: true}
    ],
    proxy: {
        type: "rest",
        url: 'url',
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