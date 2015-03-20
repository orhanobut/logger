package com.orhanobut.logger;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Orhan Obut
 */
public class LoggerTest extends TestCase {

    public void testSetMethodCount() {
        try {
            Logger.init().setMethodCount(-1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.init().setMethodCount(6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.init().setMethodCount(3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetTag() {
        try {
            Logger.init(null);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.init("");
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.init("test");
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogDForMethodCount() {
        try {
            Logger.d("test", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.d("test", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.d("test", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogEForMethodCount() {
        try {
            Logger.e("test", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.e("test", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.e("test", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogWForMethodCount() {
        try {
            Logger.w("test", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.w("test", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.w("test", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogVForMethodCount() {
        try {
            Logger.v("test", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.v("test", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.v("test", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogWTFForMethodCount() {
        try {
            Logger.wtf("test", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.wtf("test", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.wtf("test", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetLogJsonForMethodCount() {
        try {
            Logger.json("{}", -1);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.json("{}", 6);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
        try {
            Logger.json("{}", 3);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testLogEforException() {
        try {
            Exception exception = null;
            Logger.e(exception);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testJson(){
        Logger.json("{\"test\":\"test\"}");
    }
}
