package com.orhanobut.logger;

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
public class LoggerTest {

    @Test
    public void testInit() {
        Logger.initialize(Settings.builder().build());
        assertThat(Logger.getSettings()).isInstanceOf(Settings.class);
    }

    @Test
    public void testT() {
        Settings settings = Logger.t("tag").getSettings();
        assertThat(settings.getMethodCount()).isEqualTo(2);

        settings = Logger.t(10).getSettings();
        assertThat(settings.getMethodCount()).isEqualTo(2);

        settings = Logger.t("tag", 5).getSettings();
        assertThat(settings.getMethodCount()).isEqualTo(2);
    }

}
