/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-5
 * Time: 下午1:17
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Console.model.RoleMenu', {
    extend: 'Ext.data.Model',

    fields: [
        { name: 'id', type: 'int', useNull: true},
        { name: 'roleId', type: 'long', useNull: true, mapping: 'role.id' },
        { name: 'roleName', type: 'string', useNull: true, mapping: 'role.name' },
        { name: 'menuId', type: 'long', useNull: true, mapping: 'menu.id' },
        { name: 'menuName', type: 'string', useNull: true, mapping: 'menu.name' },
        { name: 'read', type: 'boolean', useNull: false, defaultValue: true},
        { name: 'create', type: 'boolean' , useNull: false},
        { name: 'update', type: 'boolean' , useNull: false},
        { name: 'deletee', type: 'boolean' , useNull: false}
    ],

    proxy: {
        type: "rest",
        url: 'roleMenu',
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
    }
});