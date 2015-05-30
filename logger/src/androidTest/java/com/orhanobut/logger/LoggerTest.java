package com.orhanobut.logger;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Orhan Obut
 */
public class LoggerTest extends TestCase {

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

    public void testJson() {
        Logger.json("{\"test\":\"test\"}");
    }

    public void testXmlLogger() {
        Logger.xml("<note>" +
                "<to>Tove</to>" +
                "<from>Jani</from>" +
                "<heading>Reminder</heading>" +
                "<body>Don't forget me this weekend!</body>" +
                "</note>");
    }

    public void testEmptyXml() {
        Logger.xml(" ");
    }

    public void testNegativeMethodCount() {
        try {
            Logger.t(-10).d("test");
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    public void testTagShouldNotBeNull() {
        try {
            Logger.init(null);
            fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    public void testStackTraceWithHugeMethodCount() {
        try {
            Logger.init("test").setMethodCount(40);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    public void testSetMethodOffset() {
        try {
            Logger.init().setMethodOffset(100);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
        try {
            Logger.init().setMethodOffset(0);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
        try {
            Logger.init()
                    .setMethodCount(100)
                    .setMethodOffset(100);
            Assert.assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}
