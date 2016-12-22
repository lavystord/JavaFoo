Ext.define('Console.model.House', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int', useNull: true  },
        { name: 'addressId', type: 'int', mapping: 'address.id', useNull: true },
        { name: 'addressSubdistrict', type: 'string', mapping: 'address.subdistrict', useNull: true},
        { name: 'addressString', type: 'string', mapping: 'address.addressString', useNull: true , persist: false},
        { name: 'areaName', type: 'string', mapping: 'address.area.areaName', useNull: true },
        { name: 'houseString', type: 'string' , useNull: true , persist: false },
        { name: 'ownerId', type: 'int', mapping: 'owner.id', useNull: true },
        { name: 'ownerName', type: 'string', mapping: 'owner.name', useNull: true },
        { name: 'ownerNickname', type: 'string', mapping: 'owner.nickname', useNull: true },
        { name: 'ownerMobile', type: 'string', mapping: 'owner.mobile', useNull: true },
        { name: 'building', type: 'string' , useNull: true },
        { name: 'unit', type: 'string'  , useNull: true},
        { name: 'floor', type: 'int'  , useNull: true},
        { name: 'number', type: 'string' , useNull: true }
    ],
    proxy: {
        type: "rest",
        url: 'house',
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