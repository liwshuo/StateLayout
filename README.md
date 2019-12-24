# 便捷的状态切换控件
在平时的业务开发中，经常会遇到需要在页面中展示异常状态的要求。比如刚进入页面的时候需要有一个展示加载动画的页面；页面刷新失败，需要有一个点击重试的错误页面展示；页面刷新没有数据，需要有一个提示没有数据的错误页面展示。

遇到这些情况，可能会使用一个或者多个布局来根据状态控制其展示和隐藏。这样在布局文件中，就会include多个异常状态的布局文件；在Java代码中，也会保存多个View实例以便控制其显示状态。这种写法略显不便和臃肿。为了解决这个问题，可以使用[StateLayout](https://github.com/liwshuo/StateLayout)。

<img src="https://github.com/liwshuo/StateLayout/blob/master/test.gif?raw=true" width="20%" height="20%">

## 引用方法

gradle文件中添加：
`implementation 'com.baymax.statelayout:statelayout:1.0.0'`

## 添加异常页面
StateLayout可以非常便捷地添加异常页面、控制异常页面的展示。其默认提供了5种状态可供使用，已经覆盖了大多数的使用场景。
* Content，展示内容页面。
* Empty，展示空页面。
* Error，展示错误、重试页面。
* Loading，展示加载页面。
* Offline，展示无网络页面。

添加页面的方式有两种，一种是通过XML添加。
```xml
<com.baymax.statelayout.StateLayout
    android:id=“@+id/state_layout”
    android:layout_width=“match_parent”
    android:layout_height=“match_parent”
    app:contentSrc=“@layout/main_content_src”
    app:emptySrc=“@layout/main_empty_src”
    app:errorSrc=“@layout/main_error_src”
    app:loadingSrc=“@layout/main_loading_src”
    app:offlineSrc=“@layout/main_offline_src”/>
```

另外一种是通过Java代码添加。如果有需要想直接添加一个View，也可以使用`setXXView()`相关方法。
```java
stateLayout.setContentViewSrc(R.layout.main_content_src);
stateLayout.setEmptyViewSrc(R.layout.main_empty_src);
stateLayout.setErrorViewSrc(R.layout.main_error_src);
stateLayout.setLoadingViewSrc(R.layout.main_loading_src);
stateLayout.setOfflineViewSrc(R.layout.main_offline_src);
```

除了以上5种异常状态，StateLayout还提供了自定义状态的添加。不同于内置状态，自定义状态只能够通过Java代码添加。`setCustomViewSrc()`方法接收两个参数。
* 第一个是String类型的参数，标识状态的名称；
* 第二个是布局文件的Id
如果有需要想直接添加一个View，也可以使用`setCustomView()`方法。
```java
stateLayout.setCustomViewSrc(STATE_CUSTOM1, R.layout.main_custom1_src);
stateLayout.setCustomViewSrc(STATE_CUSTOM2, R.layout.main_custom2_src);
```

## 添加点击事件
有些情况下，某些异常状态的页面需要响应点击事件。比如展示了错误页面，可能希望点击错误页面进行重试。StateLayout提供了一个回调接口`OnStateClickListener`。

可以将所有的点击事件响应写到同一个Listener中，通过回调中的`state`变量来区分不同的点击事件。
```java
public class MainActivity extends AppCompatActivity implements StateLayout.OnStateClickListener {
    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		  ...
        stateLayout.setErrorClickListener(this);
        stateLayout.setEmptyClickListener(this);
        stateLayout.setCustomStateListener(STATE_CUSTOM1, this);
        stateLayout.setCustomStateListener(STATE_CUSTOM2, this);
    }
    ...
    @Override
    public void onClick(View v, String state) {
        switch (state) {
            case StateLayout.EMPTY:
            case StateLayout.ERROR:
            case STATE_CUSTOM1:
            case STATE_CUSTOM2:
                stateLayout.showLoading();
                break;
            default:
        }
    }
}
```

也可以为每一个状态设置一个Listener，这样就不需要判断状态。
```java
stateLayout.setEmptyClickListener(new StateLayout.OnStateClickListener() {
    @Override
    public void onClick(View v, String state) {
        stateLayout.showLoading();
    }
});
```

## 总结
StateLayout提供了便捷的异常页面添加和展示方法，可以灵活地设置异常页面的响应时间。将不同的异常页面分拆到不同的布局文件中，便于后期的管理和复用。

## 感谢
灵感来自于[Android-StatefullLayout)](https://github.com/jakubkinst/Android-StatefulLayout)


