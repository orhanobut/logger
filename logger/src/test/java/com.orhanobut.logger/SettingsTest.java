package com.orhanobut.logger;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Orhan Obut
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SettingsTest extends TestCase {

    private Settings settings;

    @Before
    public void setup() {
        settings = Settings.builder().build();
    }

    @After
    public void tearDown() {
        settings = null;
    }

    @Test
    public void testDefaultShowThreadInfo() {
        assertThat(settings.isShowThreadInfo()).isFalse();
    }

    @Test
    public void testCustomShowThreadInfo() {
        settings.isShowThreadInfo(true);
        assertThat(settings.isShowThreadInfo()).isTrue();
    }

    @Test
    public void testDefaultLogLevel() {
        assertThat(settings.getLogLevel()).isEqualTo(LogLevel.FULL);
    }

    @Test
    public void testCustomLogLevel() {
        settings.setLogLevel(LogLevel.NONE);
        assertThat(settings.getLogLevel()).isEqualTo(LogLevel.NONE);

        settings.setLogLevel(LogLevel.FULL);
        assertThat(settings.getLogLevel()).isEqualTo(LogLevel.FULL);
    }

    @Test
    public void testMethodCount() {
        //default
        assertThat(settings.getMethodCount()).isEqualTo(1);

        settings.setMethodCount(4);
        assertThat(settings.getMethodCount()).isEqualTo(4);

        //negative values should be convert to 0
        settings.setMethodCount(-10);
        assertThat(settings.getMethodCount()).isEqualTo(0);
    }

    @Test
    public void testMethodOffset() {
        //default
        assertThat(settings.getMethodOffset()).isEqualTo(0);

        settings.setMethodOffset(10);
        assertThat(settings.getMethodOffset()).isEqualTo(10);

        settings.setMethodOffset(-10);
        assertThat(settings.getMethodOffset()).isEqualTo(-10);
    }
}
