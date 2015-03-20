#Logger
Simple,pretty and powerful log for android

<img src='https://github.com/orhanobut/logger/blob/master/images/logger-logo.png' width='128' height='128'/>

Logger provides :
- Thread information
- Class information
- Method information
- Pretty-print for json content
- Clean log
- Jump to source feature : Easily go to source which made the log info

### Gradle
```groovy
compile 'com.orhanobut:logger:1.0'
```

### Current Log system
```java
Log.d(TAG,"something");
```

<img src='https://github.com/orhanobut/logger/blob/master/images/current-log.png'/>


### Logger
```java
Logger.d("something");
```
<img src='https://github.com/orhanobut/logger/blob/master/images/description.png'/>

### More logs
```java
Logger.d("something");
Logger.e(exception);
Logger.json(JSON_CONTENT);
```
<img src='https://github.com/orhanobut/logger/blob/master/images/logger-log.png'/>

### Method info
With logger, you can see methods in order and thread information. For example
```java
void methodA(){
   methodB();
}
void methodA(){
   Logger.d("something");
}
```
In logger, both method information will be shown, thus you will know which method actually called this method.

<img src='https://github.com/orhanobut/logger/blob/master/images/two-method-with-thread-desc.png'/>

### Change method count (Default: 2)
```java
Logger.init().setMethodCount(1);
```
or set it for specific logs
```java
Logger.d("message", 1);
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-with-thread.png'/>


### Hide thread information
```java
Logger.init().setMethodCount(1).hideThreadInfo();
```

<img src='https://github.com/orhanobut/logger/blob/master/images/one-method-no-header.png'/>

### Only show the content
```java
Logger.init().setMethodCount(0).hideThreadInfo();
```

<img src='https://github.com/orhanobut/logger/blob/master/images/just-content.png'/>

### Pretty print json, Logger.json
Logger has json call in order to format the json content.
```java
Logger.json(YOUR_JSON_DATA);
```

<img src='https://github.com/orhanobut/logger/blob/master/images/json-log.png'/>

### Log exceptions in simple way
Logger will show the cause of the exception
```java
Logger.e(exception);
```

### Change TAG
```java
Logger.init(YOUR_TAG);
```

### Notes
For the consistency, use filter
<img src='https://github.com/orhanobut/logger/blob/master/images/filter.png'/>


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
