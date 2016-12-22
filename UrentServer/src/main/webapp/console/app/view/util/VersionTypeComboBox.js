Ext.define('Console.view.util.VersionTypeComboBox',{
    extend: 'Ext.form.field.ComboBox',
    requires: [
        'Console.store.VersionTypeStore'
    ],
    alias: 'widget.versiontypecombobox',
    queryMode: 'local',
    displayField: 'name',
    valueField: 'value',
    forceSelection: true,
    editable: false,
    store: 'VersionTypeStore',

    initComponent : function() {
        this.callParent(arguments);
    }
})