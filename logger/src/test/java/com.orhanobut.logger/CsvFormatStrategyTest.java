package com.orhanobut.logger;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CsvFormatStrategyTest {

  @Test public void log() {
    FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
        .logStrategy(new LogStrategy() {
          @Override public void log(int priority, String tag, String message) {
            assertThat(tag).isEqualTo("PRETTY_LOGGER-tag");
            assertThat(priority).isEqualTo(Logger.VERBOSE);
            assertThat(message).contains("VERBOSE,PRETTY_LOGGER-tag,message");
          }
        })
        .build();

    formatStrategy.log(Logger.VERBOSE, "tag", "message");
  }

  @Test public void defaultTag() {
    FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
        .logStrategy(new LogStrategy() {
          @Override public void log(int priority, String tag, String message) {
            assertThat(tag).isEqualTo("PRETTY_LOGGER");
          }
        })
        .build();

    formatStrategy.log(Logger.VERBOSE, null, "message");
  }

  @Test public void customTag() {
    FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
        .tag("custom")
        .logStrategy(new LogStrategy() {
          @Override public void log(int priority, String tag, String message) {
            assertThat(tag).isEqualTo("custom");
          }
        })
        .build();

    formatStrategy.log(Logger.VERBOSE, null, "message");
  }
}