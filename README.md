[![](https://jitpack.io/v/tianzhijiexian/logger.svg)](https://jitpack.io/#tianzhijiexian/logger)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Logger-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1658) [![](https://img.shields.io/badge/AndroidWeekly-%23147-blue.svg)](http://androidweekly.net/issues/issue-147)

<img align="right" src='https://raw.githubusercontent.com/tianzhijiexian/logger/master/images/logger-logo.png' width='128' height='128'/>

###Logger
Simple, pretty and powerful logger for android

Logger provides :
- Thread information
- Class information
- Method information
- Pretty-print for json content
- Pretty-print for new line "\n"
- Clean output
- Jump to source
- Smart log tag
- support large string  

### Gradle
Add it in your root build.gradle at the end of repositories:  
```groovy
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```  
Add the dependency  
> compile 'com.github.tianzhijiexian:logger:[Latest release](https://github.com/tianzhijiexian/logger/releases)'

and also compile https://github.com/JakeWharton/timber

### Logger  
```
D/MainActivity: ║first
D/MainActivity: ║second
D/MainActivity: ║third ==> levTest(MainActivity.java:62)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║just test ==> test(MainActivity.java:67)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║User{name=jack, sex=f, $change=Object}  ==> objTest(MainActivity.java:72)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║ArrayList size = 3 [
D/MainActivity: ║[0]:kale,
D/MainActivity: ║[1]:jack,
D/MainActivity: ║[2]:tony
D/MainActivity: ║
D/MainActivity: ║] ==> objTest(MainActivity.java:74)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║String[3] {
D/MainActivity: ║[Android,	ios,	wp]
D/MainActivity: ║} ==> objTest(MainActivity.java:76)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║double[4][5] {
D/MainActivity: ║[1.2,	1.6,	1.7,	30.0,	33.0]
D/MainActivity: ║[1.2,	1.6,	1.7,	30.0,	33.0]
D/MainActivity: ║[1.2,	1.6,	1.7,	30.0,	33.0]
D/MainActivity: ║[1.2,	1.6,	1.7,	30.0,	33.0]
D/MainActivity: ║
D/MainActivity: ║} ==> objTest(MainActivity.java:83)
D/MainActivity: ╚═══════════════════════════
D/MainActivity: ║{
D/MainActivity: ║    "widget": {
D/MainActivity: ║        "debug": "on",
D/MainActivity: ║        "window": {
D/MainActivity: ║            "title": "Sample Konfabulator Widget",
D/MainActivity: ║            "name": "main_window",
D/MainActivity: ║            "width": 500,
D/MainActivity: ║            "height": 500
D/MainActivity: ║        }
D/MainActivity: ║    }
D/MainActivity: ║} ==> jsonTest(MainActivity.java:87)
D/MainActivity: ╚═══════════════════════════
```

### Usage
```java
Logger.d("hello"); // debug

Logger.e("hello"); // error

Logger.w("hello"); // warnning

Logger.v("hello"); // verbose

Logger.wtf("hello"); // what the fuck

Logger.json(JSON_CONTENT); // json

Logger.xml(XML_CONTENT); // xml

logger.object(...); // bean/map/Collection...

Logger.t("Custom Tag").w("logger with custom tag");

try {
		Class.forName("kale");
} catch (ClassNotFoundException e) {
		Logger.e(e, "something happened"); // exception
}

Logger.d("first\nsecond\nthird"); // third line
```

### Settings (optional)
Change the settings with init. This should be called only once. Best place would be in application class. All of them
 are optional.
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
				Logger.initialize(
                new Settings()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
        );
    }
}
```
Note: Use LogLevel.NONE for the release versions.

#### You might also like
- [Hawk](https://github.com/orhanobut/hawk) Simple,powerful,secure key-value storage
- [Wasp](https://github.com/orhanobut/wasp) All-in-one network solution
- [Bee](https://github.com/orhanobut/bee) QA/Debug tool
- [DialogPlus](https://github.com/orhanobut/dialogplus) Easy,simple dialog solution
- [SimpleListView](https://github.com/orhanobut/simplelistview) Simple basic listview implementation with linearlayout

###License
<pre>
Copyright 2015 Orhan Obut

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
