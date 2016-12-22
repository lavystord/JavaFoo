Ext.define('Console.model.Area', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int' },
        { name: 'areaName', type: 'string', useNull: true},
        { name: 'shortName', type: 'string'  , useNull: true},
        { name: 'zipCode', type: 'int'  , useNull: true},
        { name: 'level', type: 'int' , useNull: true },
        { name: 'sort', type: 'int' , useNull: true },
        { name: 'areaString', type: 'string' , useNull: true }
    ],
    proxy: {
        type: "rest",
        url: 'Area',
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