Ext.define('Console.store.VersionTypeStore', {
        extend: 'Ext.data.Store',
        fields: ['name', 'value'],
        data: [
            {'name':'锁结构', 'value': 1},
            {'name':'锁固件', 'value': 2},
            // {'name':'分享钥匙', 'value': 'slave'},
            {'name':'安卓APP', 'value': 101},
            {'name':'苹果APP', 'value': 201}
        ]
    }
)