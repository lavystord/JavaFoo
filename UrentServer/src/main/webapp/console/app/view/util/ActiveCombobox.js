Ext.define('Console.view.util.ActiveCombobox',{
    extend: 'Ext.form.field.ComboBox',
    requires: [
        'Console.store.util.ActiveCombo'
    ],
    alias: 'widget.activecombobox',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    forceSelection: false,
    editable: false,

    initComponent : function() {
        this.store = Ext.create('Console.store.util.ActiveCombo');
        this.callParent(arguments);
    }
})