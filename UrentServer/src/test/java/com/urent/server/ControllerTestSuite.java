package com.urent.server;

import com.urent.server.controller.TestLockKeeperController;
import com.urent.server.controller.TestMyKeyController;
import com.urent.server.controller.TestUserController;
import com.urent.server.controller.UserController2;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Administrator on 2015/7/27.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        //TestHelloController.class,
        TestUserController.class,
        TestLockKeeperController.class,
        TestMyKeyController.class
})




public class ControllerTestSuite {

}
