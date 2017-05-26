[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Logger-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1658) [![](https://img.shields.io/badge/AndroidWeekly-%23147-blue.svg)](http://androidweekly.net/issues/issue-147)
[![Join the chat at https://gitter.im/orhanobut/logger](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/orhanobut/logger?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) <a href="http://www.methodscount.com/?lib=com.orhanobut%3Alogger%3A1.15"><img src="https://img.shields.io/badge/Methods and size-165 | 12 KB-e91e63.svg"/></a> [![Build Status](https://travis-ci.org/orhanobut/logger.svg?branch=master)](https://travis-ci.org/orhanobut/logger)

<img align="right" src='https://github.com/orhanobut/logger/blob/master/images/logger-logo.png' width='128' height='128'/>

### Logger
Simple, pretty and powerful logger for android

Logger provides :
- Thread information
- Class information
- Method information
- Pretty-print for json content
- Pretty-print for new line "\n"
- Clean output
- Jump to source

### Download
```groovy
compile 'com.orhanobut:logger:2.0'
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

```java
Logger.d("hello");
Logger.e("hello");
Logger.w("hello");
Logger.v("hello");
Logger.wtf("hello");
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
Logger.log(DEBUG, "tag", "message", throwable);
```

#### String format arguments are supported
```java
Logger.d("hello %s", "world");
```

#### Array, Map, Set and List are supported
```java
Logger.d(list);
Logger.d(map);
Logger.d(set);
Logger.d(new String[]);
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
 are optional. You can just use the default settings if you don't init Logger.
```java
Logger
  .init(YOUR_TAG)                 // default PRETTY_LOGGER or use just init()
  .methodCount(3)                 // default 2
  .hideThreadInfo()               // default shown
  .logLevel(LogLevel.NONE)        // default LogLevel.FULL
  .methodOffset(2)                // default 0
  .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
}

```
Note: Use LogLevel.NONE for the release versions.

### Use another log util instead of android.util.log
- Implement LogAdapter
- set it with init
```java
.logAdapter(new CustomLogAdapter())
```

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
Logger.init().methodCount(1);
```
Log based
```java
Logger.t(1).d("hello");
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-with-thread.png'/>

### Change method stack offset (Default: 0)
To integrate logger with other libraries, you can set the offset in order to avoid that library's methods.
```java
Logger.init().methodOffset(5);
```

### Hide thread information
```java
Logger.init().methodCount(1).hideThreadInfo();
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-no-header.png'/>

### Only show the message
```java
Logger.init().methodCount(0).hideThreadInfo();
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

### Timber Integration
You can also use logger along with Timber. 
```java
Logger.init().methodOffset(7);   //skip trace caused by Timber.
Timber.plant(new Timber.DebugTree() {
  @Override protected void log(int priority, String tag, String message, Throwable t) {
    Logger.log(priority, tag, message, t);
  }
});
```

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
