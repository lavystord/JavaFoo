Ext.define('Console.store.KeyActionLogActionStore', {
        extend: 'Ext.data.Store',
        fields: ['name', 'value'],
        data: [
            {'name':'计算一次性原语', 'value': 0},
            {'name':'计算持久性原语', 'value': 1},
            {'name':'开锁', 'value': 2},
            {'name':'锁固件升级', 'value': 3},
            {'name':'确认离开', 'value': 4},
            {'name':'使钥匙失效', 'value': 100},
            {'name':'使主钥匙失效时被连带失效', 'value': 101},
            {'name':'激活钥匙', 'value': 102}
        ]
    }
)