Ext.define('Console.model.RoleUrl', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int', useNull: true  },
        { name: 'roleId', type: 'int', mapping: 'role.id', useNull: true },
        { name: 'roleName', type: 'string', mapping: 'role.name', useNull: true},
        { name: 'urlId', type: 'int', mapping: 'url.id', useNull: true},
        { name: 'urlValue', type: 'string', mapping: 'url.value', useNull: true },
        { name: 'method', type: 'string' , useNull: true},
        { name: 'comment', type: 'string', useNull: true }
    ],
    proxy: {
        type: "rest",
        url: 'roleUrl',
        reader: {
            type: "json",
            totalProperty: "total",
            root: "list"
        },
        writer: {
            type: "json",
            nameProperty: 'mapping',
            expandData: true,
            writeAllFields: false
        }
    },
    idProperty: 'id'
    //hasMany: {model: 'Console.model.AddressRole', name: 'AddressRoles'}
})