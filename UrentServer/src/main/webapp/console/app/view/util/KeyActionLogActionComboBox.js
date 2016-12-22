Ext.define('Console.view.util.KeyActionLogActionComboBox',{
    extend: 'Ext.form.field.ComboBox',
    requires: [
        'Console.store.KeyActionLogActionStore'
    ],
    alias: 'widget.keyactionlogactioncombobox',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    forceSelection: true,
    editable: false,
    store: 'KeyActionLogActionStore',

    initComponent : function() {
        this.callParent(arguments);
    }
})