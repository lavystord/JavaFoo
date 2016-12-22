Ext.define('Console.store.AppMenu', {
    extend: 'Ext.data.TreeStore',
    model: 'Console.model.AppMenuTree'
    //autoLoad: true,
/*
    proxy :{
        type: 'ajax',
        api: {
            read: 'app/tempData/menu.json'
        },
        reader: {
            type: 'json',
            // root: 'root',
            successProperty: 'success'
        }
    }*/

   /* root: {
        name: 'Root',
        expanded: true,
        children: [
            {
                name: 'Child 1',
                leaf: true
            },
            {
                name: 'Child 2',
                leaf: true
            },
            {
                name: 'Child 3',
                expanded: true,
                children: [
                    {
                        name: 'Grandchild',
                        leaf: true
                    }
                ]
            }
        ]
    }   */
});