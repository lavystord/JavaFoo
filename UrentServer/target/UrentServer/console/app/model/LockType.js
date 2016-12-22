Ext.define('Console.model.LockType', {
    extend: 'Ext.data.Model',
    requires: [
    ],

    fields: [
        { name: 'id', type: 'long', useNull: true },
        { name: 'name', type: 'string', useNull: true },
        { name: 'hardwareVersion', type: 'object'},
        { name: 'hardwareVersionId', type: 'int', mapping: 'hardwareVersion.id'},
        { name: 'hardwareVersionString', type: 'string', mapping: 'hardwareVersion.stringFormat'},
        { name: 'newestFirmwareVersion', type: 'object'},
        { name: 'newestFirmwareVersionId', type: 'int', mapping: 'newestFirmwareVersion.id'},
        { name: 'newestFirmwareVersionString', type: 'string', mapping: 'newestFirmwareVersion.stringFormat'},
        { name: 'description', type: 'string', useNull: true},
        { name: 'imageIds', type: 'imageIds', useNull: true}
    ],
    proxy: {
        type: "rest",
        url: 'lockType',
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