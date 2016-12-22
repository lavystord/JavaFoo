Ext.define('Console.model.Key', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int' , useNull: true},
        { name: 'lock', type: 'object', useNull: true},
        { name: 'lockId', type: 'int', mapping: 'lock.id', useNull: true},
        { name: 'lockGapAddress', type: 'string', mapping: 'lock.gapAddress', useNull: true},
        { name: 'houseString', type: 'string', mapping: 'lock.house.houseString', useNull: true , persist: false},
        { name: 'ownerId', type: 'int', mapping: 'owner.id', useNull: true},
        { name: 'ownerName', type: 'string', mapping: 'owner.name', useNull: true},
        { name: 'ownerMobile', type: 'string', mapping: 'owner.mobile', useNull: true},
        { name: 'ownerNickname', type: 'string', mapping: 'owner.nickname', useNull: true},
        { name: 'sharedFrom', type: 'object', useNull: true},
        { name: 'sharedFromId', type: 'int', useNull: true, mapping: 'sharedFrom.id'},
        { name: 'sharedFromUserName', type: 'string', mapping: 'sharedFrom.owner.name', useNull: true},
        //{ name: 'active', type: 'boolean', useNull: true},
        { name: 'status', type: 'int', useNull: true},
        { name: 'statusString', convert: function(value, record) {
            var status = record.get('status');
            if(status != null && status != undefined) {
                var versionTypeStore = Ext.data.StoreManager.lookup('KeyStatusStore');
                var r = versionTypeStore.findRecord('value', status);
                if(r != null && r != undefined) {
                    return r.get('name');
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }, persist: false},
        { name: 'typeString', convert: function(value, record) {
            var type = record.get('type');
            if(status != null && status != undefined) {
                var versionTypeStore = Ext.data.StoreManager.lookup('KeyTypeStore');
                var r = versionTypeStore.findRecord('value', type);
                if(r != null && r != undefined) {
                    return r.get('name');
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }, persist: false},
        { name: 'alias', type: 'string',  useNull: true},
        { name: 'maxSharedCount', type: 'int',  useNull: true},
        { name: 'type', type: 'string',  useNull: true},
        { name: 'expiredDate', type: 'date',  useNull: true},
        { name: 'createDate', type: 'date',  useNull: true},
        { name: 'updateDate', type: 'date',  useNull: true}
    ],
    proxy: {
        type: "rest",
        url: 'key',
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