Ext.define('Console.view.util.KeyTypeComboBox',{
    extend: 'Ext.form.field.ComboBox',
    requires: [
        'Console.store.KeyTypeStore'
    ],
    alias: 'widget.keytypecombobox',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    forceSelection: true,
    editable: false,
    store: 'KeyTypeStore',

    initComponent : function() {
        this.callParent(arguments);
    }
})