Ext.define('Console.model.DataModificationLog', {
    extend: 'Ext.data.Model',
    requires: [
       //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'long' },
        { name: 'userName', type: 'string', useNull: true, mapping: 'user.name'},
        { name: 'userMobile', type: 'string', useNull: true, mapping: 'user.mobile'},
        { name: 'calledClassName', type: 'string' , useNull: true },
        { name: 'calledMethodName', type: 'string' , useNull: true },
        { name: 'method', type: 'string' , useNull: true },
        { name: 'userAgent', type: 'string' , useNull: true },
        { name: 'remoteAddress', type: 'string' , useNull: true },
        { name: 'bodyData', type: 'string' , useNull: true },
        { name: 'createDate', type: 'date' , useNull: true }
    ],
    proxy: {
        type: "rest",
        url: 'dataModificationLog',
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
    //hasMany: {model: 'Console.model.UserRole', name: 'userRoles'}
})