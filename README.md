[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Logger-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1658) [![](https://img.shields.io/badge/AndroidWeekly-%23147-blue.svg)](http://androidweekly.net/issues/issue-147)

#Logger
Simple,pretty and powerful logger for android

<img src='https://github.com/orhanobut/logger/blob/master/images/logger-logo.png' width='128' height='128'/>

Logger provides :
- Thread information
- Class information
- Method information
- Pretty-print for json content
- Pretty-print for new line "\n"
- Clean output
- Jump to source

### Gradle
```groovy
compile 'com.orhanobut:logger:1.6'
```

### Current Log system
```java
Log.d(TAG,"hello");
```

<img src='https://github.com/orhanobut/logger/blob/master/images/current-log.png'/>


### Logger
```java
Logger.d("hello");
Logger.d("hello %s %d", "world", 5);   // String.format
```
<img src='https://github.com/orhanobut/logger/blob/master/images/description.png'/>

### Usage
```java
Logger.d("hello");
Logger.e("hello");
Logger.w("hello");
Logger.v("hello");
Logger.wtf("hello");
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
```

### Change TAG
All logs
```java
Logger.init(YOUR_TAG);
```
Log based
```java
Logger.t("mytag").d("hello");
```
<img src='https://github.com/orhanobut/logger/blob/master/images/custom-tag.png'/>


### Settings (optional)
Change the settings with init. This should be called only once. Best place would be in application class. All of them
 are optional.
```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger
             .init(YOUR_TAG)               // default PRETTYLOGGER or use just init()
             .setMethodCount(3)            // default 2
             .hideThreadInfo()             // default shown
             .setLogLevel(LogLevel.NONE);  // default LogLevel.FULL
    }
}
```
Note: Use LogLevel.NONE for the release versions.

### More log samples
```java
Logger.d("hello");
Logger.e(exception, "message");
Logger.json(JSON_CONTENT);
```
<img src='https://github.com/orhanobut/logger/blob/master/images/logger-log.png'/>

### Method info
Observe the caller methods in the order they are invoked and also thread information.
```java
void methodA(){
   methodB();
}
void methodA(){
   Logger.d("hello");
}
```
Both method information will be shown in the order of invocation.

<img src='https://github.com/orhanobut/logger/blob/master/images/two-method-with-thread-desc.png'/>

### Change method count (Default: 2)
All logs
```java
Logger.init().setMethodCount(1);
```
Log based
```java
Logger.t(1).d("hello");
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-with-thread.png'/>


### Hide thread information
```java
Logger.init().setMethodCount(1).hideThreadInfo();
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-no-header.png'/>

### Only show the message
```java
Logger.init().setMethodCount(0).hideThreadInfo();
```

<img src='https://github.com/orhanobut/logger/blob/master/images/just-content.png'/>

### Pretty print json, Logger.json
Format the json content in a pretty way
```java
Logger.json(YOUR_JSON_DATA);
```

<img src='https://github.com/orhanobut/logger/blob/master/images/json-log.png'/>

### Log exceptions in a simple way
Show the cause of the exception
```java
Logger.e(exception,"message");
```

### Notes
- Use the filter for a better result

<img src='https://github.com/orhanobut/logger/blob/master/images/filter.png'/>

- Make sure that the wrap option is disabled

<img src='https://github.com/orhanobut/logger/blob/master/images/wrap-closed.png'/>


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
