<img align="right" src='https://github.com/orhanobut/logger/blob/master/art/logger-logo.png' width='128' height='128'/>

# Logger

Simple, pretty and powerful logging for Android.

## Setup

Add Google Maven and Maven Central to your dependency repositories:

```groovy
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}
```

The last published release is 2.2.0. The updated source builds as
`2.3.0-SNAPSHOT`; it has not been released to Maven Central.

```groovy
implementation 'com.orhanobut:logger:2.2.0'
```

Initialize
```java
Logger.addLogAdapter(new AndroidLogAdapter());
```
And use
```java
Logger.d("hello");
```

## Output
<img src='https://github.com/orhanobut/logger/blob/master/art/logger_output.png'/>


## Options
```java
Logger.d("debug");
Logger.e("error");
Logger.w("warning");
Logger.v("verbose");
Logger.i("information");
Logger.wtf("What a Terrible Failure");
```

String format arguments are supported
```java
Logger.d("hello %s", "world");
```

Collections are supported (only available for debug logs)
```java
Logger.d(MAP);
Logger.d(SET);
Logger.d(LIST);
Logger.d(ARRAY);
```

Json and Xml support (output will be in debug level)
```java
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
```

## Advanced
```java
FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
  .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
  .methodCount(0)         // (Optional) How many method line to show. Default 2
  .methodOffset(0)        // (Optional) Skips extra caller frames. Default 0
  .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
  .tag("My custom tag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
  .build();

Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
```

## Loggable
Log adapter checks whether the log should be printed or not by checking this function.
If you want to disable/hide logs for output, override `isLoggable` method.
`true` will print the log message, `false` will ignore it.
```java
Logger.addLogAdapter(new AndroidLogAdapter() {
  @Override public boolean isLoggable(int priority, String tag) {
    return BuildConfig.DEBUG;
  }
});
```

## Save logs to the file
The default `DiskLogAdapter` writes to a `logger` directory under shared external
storage. That legacy location is restricted by scoped storage on modern Android.
For current apps, supply a `DiskLogStrategy` using an app-owned directory such as
`context.getFilesDir()`, wrapped in a `CsvFormatStrategy`.
```java
Logger.addLogAdapter(new DiskLogAdapter());
```

Add custom tag to Csv format strategy
```java
FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
  .tag("custom")
  .build();

Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
```

## How it works
<img src='https://github.com/orhanobut/logger/blob/master/art/how_it_works.png'/>


## More
- Use filter for a better result. PRETTY_LOGGER or your custom tag
- Make sure that wrap option is disabled
- You can also simplify output by changing settings.

<img src='https://github.com/orhanobut/logger/blob/master/art/logcat_options.png'/>

- Timber Integration
```java
// Set methodOffset to 5 in order to hide internal method calls
Timber.plant(new Timber.DebugTree() {
  @Override protected void log(int priority, String tag, String message, Throwable t) {
    Logger.log(priority, tag, message, t);
  }
});
```

## Building from source

Use JDK 21 (required by the current Checkstyle tooling), Android SDK Platform 37,
and Android SDK Build Tools 37.0.0. Set `ANDROID_HOME` or put your SDK path in an
untracked `local.properties` file:

```properties
sdk.dir=/path/to/android/sdk
```

The Gradle wrapper downloads Gradle 9.8.0. The build uses Android Gradle Plugin
9.4.1 and built-in Kotlin support with Kotlin 2.4.20 for the tests. The library
retains its declared minimum SDK of 8 and Java 8 bytecode; the sample requires
API 23 and targets API 37. Robolectric tests run on APIs 23 and 36; this does not
verify runtime behavior on older devices.

```sh
./gradlew check :logger:assembleRelease :sample:assembleDebug
./gradlew :logger:generatePomFileForMavenPublication :logger:javaDocReleaseJar
```

## Publishing

Android libraries are distributed as AARs with dependency metadata in Maven
repositories. Maven Central remains the recommended destination for public
releases; consumers can use Gradle to install them. This project uses the
[Vanniktech Maven Publish plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)
with Sonatype's Central Portal, replacing the retired OSSRH upload workflow.

Publishing requires a verified namespace, a Central Portal user token, and a
GPG signing key. The existing `com.orhanobut` group requires publishing rights
to that namespace; forks should configure a namespace they control.

Keep credentials outside the repository, for example as CI environment variables:

```text
ORG_GRADLE_PROJECT_mavenCentralUsername
ORG_GRADLE_PROJECT_mavenCentralPassword
ORG_GRADLE_PROJECT_signingInMemoryKey
ORG_GRADLE_PROJECT_signingInMemoryKeyPassword
```

Set `VERSION_NAME` in `gradle.properties` to a new, unused release version before
publishing. To upload a release for validation and then approve it in the
[Central Portal](https://central.sonatype.com/publishing/deployments):

```sh
./gradlew :logger:publishToMavenCentral
```

For local integration testing without credentials:

```sh
./gradlew :logger:publishToMavenLocal -PsignAllPublications=false
```

Snapshot versions go to the Central Portal snapshot repository. Published release
versions are immutable.

## License
<pre>
Copyright 2018 Orhan Obut

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
