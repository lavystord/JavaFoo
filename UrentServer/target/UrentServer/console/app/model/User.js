Ext.define('Console.model.User', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'int' },
        { name: 'mobile', type: 'string'},
        { name: 'name', type: 'string' , useNull: true },
        { name: 'nickname', type: 'string' , useNull: true },
        { name: 'createDate', type: 'date' , useNull: true },
        { name: 'updateDate', type: 'date'  , useNull: true},
        { name: 'idCardNumber', type: 'string'  , useNull: true},
        { name: 'gender', type: 'string' , useNull: true },
        { name: 'headerImageId', type: 'string' , useNull: true },
        { name: 'lastLoginDevice', type: 'string' , useNull: true },
        { name: 'active', type: 'string' , useNull: true}
    ],
    proxy: {
        type: "rest",
        url: 'user',
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