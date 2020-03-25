# AndroioTvFocusLayer

一个针对安卓TV开发中 View 获取焦点时进行放大变色的通用库。

**博客地址：**

https://github.com/TowerYe/AndroioTvFocusLayer

**效果图：**

用的 Android Studio 的 TV 虚拟机录制的，有点卡，在小米，天猫盒子上试过是不会出现这种卡顿的效果的。

![focus](media/focus.gif)

## 一、前言

&emsp;&emsp;最近在负责一款 Android TV 的项目，由于是第一次接触电视端的项目，最开始是打算直接采用谷歌官方的 [Leanback](https://developer.android.com/training/tv?hl=zh-cn) 那套框架来开发，但是由于相关 UI 过于封闭，不易自定义，所以就放弃了。电视端相较于手机端的 APP，最大的区别就是交互方式，电视端不能像手机 APP 那样直接通过触碰点击，而必须要通过遥控器去交互，这就涉及到电视端的相关控件的焦点问题。

&emsp;&emsp;在电视或一些电视盒子上，通常 View 在获取焦点时就会有个放大的效果，同时会有一个边框，以此来提示用户这个 View 是当前被选中的 View，为了实现这个效果，最开始在网上搜了下，发现都比较复杂，大部分都是在自定义 View 通过 Paint 那一套来自己画啊什么的，我嫌麻烦，最后就自己用相对简单的方式来实现，主要就是通过实现 `OnFocusChangeListener` 监听器来获取焦点变化状态，再在布局文件中通过 `clipChildren `以及 `clipToPadding` 两个属性来让控件可以超出父布局来进行绘制。

## 二、属性介绍

| 名称 | 类型 | 说明 |
| --- | --- | --- |
| isScale | boolean | 获取焦点后是否放大 |
| scaleSize | float | 获取焦点后需放大的倍数 |
| layerRadius | float | 圆角大小 |
| contentBgColor | int | 内容背景色 |
| focusContentBgColor | int | 获取焦点时内容背景色 |
| focusStrokeColor | int | 获取焦点时的边框颜色 |
| focusStrokeWidth | int | 获取焦点时的边框的宽度 |

## 三、具体使用

&emsp;&emsp;对布局中常用的 `LinearLayout`，`RelativeLayout`,`ConstraintLayout` 进行了重新封装，可根据自己的习惯引入对应的 layout 进行布局，具体对应关系如下：

| 原生 Layer | 焦点 Layer |
| --- | --- |
| LinearLayout | FocusLinearLayout |
| RelativeLayout | FocusRelativeLayout |
| ConstraintLayout | FocusConstraintLayout |

&emsp;&emsp;具体使用如下：

**FocusLinearLayout**

```xml
<com.tt.focuslayer.widget.FocusLinearLayout
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginStart="20dp"
            android:gravity="center"
            android:orientation="vertical"
            focus:contentBgColor="@color/black_80"
            focus:focusContentBgColor="@color/green"
            focus:focusStrokeColor="@color/white_60"
            focus:focusStrokeWidth="3"
            focus:layerRadius="8"
            focus:scaleSize="1.3">

            .
            .
            .

</com.tt.focuslayer.widget.FocusLinearLayout>
```

**FocusRelativeLayout**

```xml
 <com.tt.focuslayer.widget.FocusRelativeLayout
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginStart="20dp"
            android:layout_toEndOf="@+id/layer2"
            focus:focusStrokeColor="@color/colorPrimary"
            focus:focusStrokeWidth="2"
            focus:layerRadius="4"
            focus:scaleSize="1.2">

            .
            .
            .

</com.tt.focuslayer.widget.FocusRelativeLayout>
```

**FocusConstraintLayout**

```xml
 <com.tt.focuslayer.widget.FocusConstraintLayout
            android:layout_width="100dp"
            android:layout_height="100dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/layer2"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            focus:isScale="false">

            .
            .
            .

</com.tt.focuslayer.widget.FocusConstraintLayout>
```

&emsp;&emsp;如果相应的 layer 在获取焦点放大时，View 被裁切没有显示完整，此时需要在布局文件的根 View 中加上如下的代码：

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:focus="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipChildren="false"
    android:clipToPadding="false">
```

&emsp;&emsp;核心代码就是

```xml
android:clipChildren="false"
android:clipToPadding="false"
```

&emsp;&emsp;这二者的作用就是允许子 View 在进行绘制的时候是否可以超出父布局的边界进行绘制，默认是不允许的，所以没加该代码时，会导致 View 在获取焦点放大时被裁切以致显示不完整。

&emsp;&emsp;相关源码注释：

```
<!-- Defines whether a child is  limited to draw inside of its bounds or not.
             This is useful with animations that scale the size of the children to more
             than 100% for instance. In such a case, this property should be set to false
             to allow the children to draw outside of their bounds. The default value of
             this property is true. -->

<attr name="clipChildren" format="boolean" />

<!-- Defines whether the ViewGroup will clip its children and resize (but not clip) any
             EdgeEffect to its padding, if padding is not zero. This property is set to true by
             default. -->

<attr name="clipToPadding" format="boolean" />
```

