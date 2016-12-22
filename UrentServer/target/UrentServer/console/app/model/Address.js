Ext.define('Console.model.Address', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int', useNull: true },
        { name: 'areaId', type: 'int', mapping: 'area.id', useNull: true},
        { name: 'areaName', type: 'string', mapping: 'area.areaName', useNull: true},
        { name: 'areaString', type: 'string', mapping: 'area.areaString', useNull: true , persist: false},
        { name: 'subdistrict', type: 'string' , useNull: true },
        { name: 'longitude', type: 'float'  , useNull: true},
        { name: 'latitude', type: 'float'  , useNull: true},
        { name: 'addressString', type: 'string' , useNull: true , persist: false }
    ],
    proxy: {
        type: "rest",
        url: 'address',
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