Ext.define('Console.store.KeyStatusStore', {
        extend: 'Ext.data.Store',
        fields: ['name', 'value'],
        data: [
            {'name':'可使用', 'value': 1},
            {'name':'已失效', 'value': 0},
            {'name':'已超时', 'value': 3},
            {'name':'使用中', 'value': 2},
            {'name':'借出中', 'value': 4},
            {'name':'未使用已失效', 'value': -1},
            {'name':'超时用户追加失效', 'value': -2},
            {'name':'超时管理员追加失效', 'value': -3}
        ]
    }
)