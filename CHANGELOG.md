# CHANGELOG
### 1.15
- Logger.log signature is changed
```java
Logger.log(int priority, String tag, String message, Throwable throwable);
```

### 1.14
- Logger.log(int priority, String tag, Object... args) added.
- Logger.d(Object object) added. Array, Map, Set and List are supported now. If the object type is none of them
Object.toString() will be used anyway.

### 1.13

- LogTool is renamed to LogAdapter
- Logger.init() is optional now. It used to be mandatory to call this method once.
- Log.e(Throwable) will print out all stacktrace
- Deprecated methods are removed
- Dependencies are updated