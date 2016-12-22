Ext.define('Console.store.KeyTypeStore', {
        extend: 'Ext.data.Store',
        fields: ['name', 'value'],
        data: [
            {'name':'主钥匙', 'value': 'primary'},
            {'name':'分享钥匙', 'value': 'slave'},
            {'name':'临时钥匙', 'value': 'temp'}
        ]
    }
)