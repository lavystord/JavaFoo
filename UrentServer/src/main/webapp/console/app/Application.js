Ext.define('Console.Application', {
    name: 'Console',

    extend: 'Ext.app.Application',

    views: [
        // TODO: add views here
        'LoginWindow',
        'Menu',
        'Workspace'
    ],

    controllers: [
        // TODO: add controllers here
        'Login',
        'Menu',
        'Workspace',
        'Logout'
    ],

    stores: [
        // TODO: add stores here
        'AppMenu',
        'VersionTypeStore',
        'KeyStatusStore',
        'KeyTypeStore',
        'KeyActionLogActionStore'
    ],


    createController: function() {
        // 由于需要带参数创建controller，这里相当于重写了Application原本的getController函数

        var me          = this,
            controllers = me.controllers,
            className, controller,
            name = arguments[0],
            config = arguments[1];

        controller = controllers.get(name);

        if (!controller) {
            className  = me.getModuleClassName(name, 'controller');
            config.application = me;
            config.id = name;

            controller = Ext.create(className, config);

            controllers.add(controller);

            if (me._initialized) {
                controller.doInit(me);
            }
        }

        return controller;
    }
});
