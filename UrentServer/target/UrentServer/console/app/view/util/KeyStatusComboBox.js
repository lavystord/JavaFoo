Ext.define('Console.view.util.KeyStatusComboBox',{
    extend: 'Ext.form.field.ComboBox',
    requires: [
        'Console.store.KeyStatusStore'
    ],
    alias: 'widget.keystatuscombobox',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    forceSelection: true,
    editable: false,

    initComponent : function() {
        this.store = Ext.create('Console.store.KeyStatusStore');
        this.callParent(arguments);
    }
})