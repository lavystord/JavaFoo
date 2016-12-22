Ext.define('Console.model.Lock', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int' , useNull: true},
        { name: 'house', type: 'object', useNull: true},
        { name: 'houseId', type: 'int', mapping: 'house.id', useNull: true},
        { name: 'houseString', type: 'string', mapping: 'house.houseString', useNull: true},
        { name: 'typeId', type: 'int', mapping: 'type.id', useNull: true},
        { name: 'typeName', type: 'string', mapping: 'type.name', useNull: true},
        { name: 'gapAddress', type: 'string', useNull: true},
        { name: 'currentFirmwareVersionId', type: 'int' ,mapping: 'currentFirmwareVersion.id', useNull: true},
        { name: 'currentFirmwareVersionString', type: 'string', mapping: 'currentFirmwareVersion.stringFormat', useNull: true},
        { name: 'powerDensity', type: 'int', useNull: true},
        { name: 'active', type: 'boolean', useNull: true},
        { name: 'constantKeyWordExpiredDate', type: 'date',  useNull: true}
    ],
    proxy: {
        type: "rest",
        url: 'lock',
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