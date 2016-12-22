Ext.define('Console.model.KeyActionLog', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.AddressRole'
    ],

    fields: [
        { name: 'id', type: 'int' , useNull: true},
        { name: 'lockGapAddress', type: 'string', useNull: true},
        { name: 'action', type: 'int', useNull: true},
        { name: 'data', type: 'data', useNull: true},
        { name: 'time', type: 'date', useNull: true},
        { name: 'lock', type: 'object', useNull: true},
        { name: 'lockId', type: 'int', mapping: 'lock.id', useNull: true},
        { name: 'key', type: 'object', useNull: true},
        { name: 'keyId', type: 'int', mapping: 'key.id', useNull: true},
        { name: 'house', type: 'object', useNull: true},
        { name: 'houseId', type: 'int', mapping: 'house.id', useNull: true},
        { name: 'houseBuilding', type: 'string', mapping: 'house.building', useNull: true},
        { name: 'houseUnit', type: 'string', mapping: 'house.unit', useNull: true},
        { name: 'houseFloor', type: 'int', mapping: 'house.floor', useNull: true},
        { name: 'houseNumber', type: 'string', mapping: 'house.number', useNull: true},
        { name: 'houseString', type: 'string', mapping: 'house.houseString', useNull: true , persist: false},
        { name: 'subdistrict', type: 'string', mapping: 'house.address.subdistrict', useNull: true},
        { name: 'ownerId', type: 'int', mapping: 'key.owner.id', useNull: true},
        { name: 'ownerName', type: 'string', mapping: 'key.owner.name', useNull: true},
        { name: 'ownerMobile', type: 'string', mapping: 'key.owner.mobile', useNull: true},
        { name: 'ownerNickname', type: 'string', mapping: 'key.owner.nickname', useNull: true},
        { name: 'managerId', type: 'int', mapping: 'manager.id', useNull: true},
        { name: 'managerName', type: 'string', mapping: 'manager.name', useNull: true},
        { name: 'managerMobile', type: 'string', mapping: 'manager.mobile', useNull: true},
        { name: 'managerNickname', type: 'string', mapping: 'manager.nickname', useNull: true},
        { name: 'createDate', type: 'date',  useNull: true},
        { name: 'actionString', convert: function(value, record) {
            var action = record.get('action');
            if(status != null && status != undefined) {
                var versionTypeStore = Ext.data.StoreManager.lookup('KeyActionLogActionStore');
                var r = versionTypeStore.findRecord('value', action);
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
    ],
    proxy: {
        type: "rest",
        url: 'keyActionLog',
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