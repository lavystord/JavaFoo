/**
 * Created by Administrator on 2015/8/6.
 */
Ext.define('Console.model.UserRole', {
    extend: 'Ext.data.Model',
    requires: [
        //'Console.model.UserRole'
    ],

    fields: [
        { name: 'id', type: 'long' },
        { name: 'userId', type: 'long', useNull: true, mapping: 'user.id'},
        { name: 'userNickname', type: 'string', useNull: true, mapping: 'user.nickname'},
        { name: 'roleId', type: 'long', useNull: true, mapping: 'role.id' },
        { name: 'roleName', type: 'string', useNull: true, mapping: 'role.name' }
    ],
    proxy: {
        type: "rest",
        url: 'userRole',
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