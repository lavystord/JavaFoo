Ext.define('Console.model.Version', {
    extend: 'Ext.data.Model',
    requires: [
        //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'long' },
        { name: 'major', type: 'int', useNull: true},
        { name: 'minor', type: 'int' , useNull: true },
        { name: 'firmwareFileId', type: 'string' , useNull: true },
        { name: 'intFormat', type: 'int', useNull: true},
        { name: 'stringFormat', type: 'string', useNull: true},
        { name: 'type', type: 'int', useNull: true},
        { name: 'comment', type: 'string', useNull: true},
        { name: 'createDate', type: 'date', useNull: true},
        { name: 'typeString', convert: function(value, record) {
            var type = record.get('type');
            var versionTypeStore = Ext.data.StoreManager.lookup('VersionTypeStore');
            var r = versionTypeStore.findRecord('value', type);
            return r.get('name');
        }, persist: false}
    ],
    proxy: {
        type: "rest",
        url: 'version',
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
})