# 一、功能介绍
为了能够更直观的让大家知道BEditText都能做什么，下面我列了一个功能列表，做一下简单描述；如下：
|  功能   | 描述  |
|  ----  | ----  |
| 圆角  | 可以统一设置圆角角度，也可以四个角单独设置 |
| 描边  | 可以设置描边大小、颜色，可以隐藏任意边|
| 阴影  | 可以设置阴影大小、颜色、偏移、距离、单边多边显示等|
| 渐变色| 可以设置文本渐变色、背景渐变色|
| 辅助功能  | 支持一键清空<br>支持密码可见性切换并可以自定义图标<br>支持输入法控制<br>支持异常提醒|
| 另类样式  | 支付密码输入样式、验证码输入样式等|

下面详细说说各个功能的使用方法：
####  ◆ 圆角

![圆角演示.gif](https://upload-images.jianshu.io/upload_images/10149003-f4cc1abd6d764ff3.gif?imageMogr2/auto-orient/strip)

|  XML设置   | 代码设置  | 描述  |
|  ----  | ----  | ----  |
| app:roundRadius="10dp"  | setRoundradius(int radius)| 设置四个角圆角值，优先级最高 |
| app:topLeftRadius="10dp"  | setTopLeftRoundRadius(int radius)| 左上角圆角值 |
| app:topRightRadius="10dp"  | setTopRightRoundRadius(int radius)| 右上角圆角值 |
| app:bottomLeftRadius="10dp"  | setBottomLeftRoundRadius(int radius)| 左下角圆角值 |
| app:bottomRightRadius="10dp"  | setBottomRightRoundRadius(int radius)| 左下角圆角值 |

如果设置了roundRadius的值默认会重置四个角度的值，以roundRadius为准

```
  <!--四个圆角都为10dp-->
  <com.bary.ui.view.BEditText
        ...
        app:roundRadius="10dp"
  />
  <!--左上、右上、坐下圆角为10dp-->
  <com.bary.ui.view.BEditText
        ...
        app:topLeftRadius="10dp"
        app:topRightRadius="10dp"
        app:bottomLeftRadius="10dp"
  />
```
<br>
####  ◆ 描边

![描边演示.gif](https://upload-images.jianshu.io/upload_images/10149003-60db359459bc4a3c.gif?imageMogr2/auto-orient/strip)

|  XML设置   | 代码设置  | 描述  |
|  ----  | ----  | ----  |
| app:borderSize="3dp"  | setBorderSize(float size)| 设置描边尺寸 |
| app:borderColor="#ff8585"  | setBorderColor(int color)| 设置描边颜色 |
| app:borderHideEdges="left\|top"  | hideBorderEdges(int... edges)| 设置需要隐藏的边,值为：<br>left、top、right、bottom；<br>可以同时设置多个用\|分隔<br>即可（代码设置的话，<br>hideBorderEdges方法为<br>不定参，传多个参数即可;<br>BorderBuilder.LEFT、<br>BorderBuilder.TOP、<br>BorderBuilder.RIGHT、<br>BorderBuilder.BOTTOM） |

想要显示描边borderShow必须为true
```xml
  <com.bary.ui.view.BEditText
        ...
        app:borderSize="3dp"
        app:borderColor="#ff8585"
        app:borderHideEdges="left|top"
  />
```
<br>
#### ◆ 阴影

![阴影演示.gif](https://upload-images.jianshu.io/upload_images/10149003-cbd2a8455a9bfbb6.gif?imageMogr2/auto-orient/strip)

|  XML设置   | 代码设置  | 描述  |
|  ----  | ----  | ----  |
| app:shadowShow="true"  | setShadowShow(boolean show)| 设置是否展示阴影 |
| app:shadowSize="3dp"  | setShadowSize(float size)| 设置阴影尺寸，优先级最高 |
| app:shadowXSize="3dp"  | setShadowXSize(float size)| 可以单独设置横向阴影尺寸 |
| app:shadowYSize="3dp"  | setShadowYSize(float size)| 可以单独设置纵向阴影尺寸 |
| app:shadowDx="1dp"  | setShadowDx(float dx)| 设置横向的偏移值，默认0<br> 视图在中心，左右阴影尺寸<br>相同 |
| app:shadowDy="1dp"  | setShadowDy(float dy)| 设置纵向的偏移值，默认0<br> 视图在中心，上下阴影尺寸<br>相同 |
| app:shadowColor="#ff8585"  | setShadowColor(int color)| 设置阴影颜色 |
| app:shadowAlpha="0.8"  | setShadowAlpha(float alpha)| 设置阴影透明度 |
| app:shadowHideEdges="left\|top"  | hideShadowEdges(int... edges)| 设置需要隐藏的边,值为：<br>left、top、right、bottom；<br>可以同时设置多个用\|分隔<br>即可（代码设置的话，<br>hideShadowEdges方法为<br>不定参，传多个参数即可;<br>ShadowBuilder.LEFT、<br>ShadowBuilder.TOP、<br>ShadowBuilder.RIGHT、<br>ShadowBuilder.BOTTOM） |

想要显示描边shadowShow必须为true，如果设置了shadowSize的值默认会重置shadowXSize，shadowYSize的值，以shadowSize为准，四边阴影尺寸相同，想要横向纵向尺寸不同，只设置shadowXSize，shadowYSize即可。
```xml
  <com.bary.ui.view.BEditText
        ...
        app:shadowShow="true"
        app:shadowSize="3dp"
        app:shadowDx="1dp"
        app:shadowDy="1dp"
        app:shadowColor="#ff8585"
        app:shadowAlpha="0.8"
        app:shadowHideEdges="left|top"
  />
```
<br>
####  ◆ 渐变色

![渐变色演示.gif](https://upload-images.jianshu.io/upload_images/10149003-4f596fc33578e1de.gif?imageMogr2/auto-orient/strip)

|  XML设置   | 代码设置  | 描述  |
|  ----  | ----  | ----  |
| app:backgroundGradientColor="#FF8585\|#FF8B15"  | setBackgroundGradientColor(String... colors)| 设置背景渐变色，至少两个16进制颜色值；<br>代码设置方法为不定参，传多个参数16进制颜色值 |
| app:backgroundGradientType="linear"  | setBackgroundGradientType<br>(GradientType type)| 设置渐变类型，参数为：<br>linear-线性<br>radial-圆环<br>sweep-角度<br>-------------------------<br>GradientType.LINEAR、<br>GradientType.RADIAL、<br>GradientType.SWEEP|
| app:backgroundGradientOrientation="horizontal"  | setBackgroundGradientOrientation<br>(GradientOrientation orientation)| 设置渐变方向，参数为：<br>horizontal-垂直<br>vertical-水平<br>diagonal-对角<br>-------------------------<br>GradientOrientation.HORIZONTAL、<br>GradientOrientation.VERTICAL、<br>GradientOrientation.DIAGONAL|
| app:textGradientColor="#FF8585\|#FF8B15"  | setBackgroundGradientColor(String... colors)| 设置背景渐变色，至少两个16进制颜色值；<br>代码设置方法为不定参，传多个参数16进制颜色值 |
| app:textGradientType="linear"  | setTextGradientType<br>(GradientType type)| 设置渐变类型，参数同背景渐变类型相同|
| app:textGradientOrientation="horizontal"  | setTextGradientOrientation<br>(GradientOrientation orientation)| 设置渐变方向，参数同背景渐变类型相同|

☛ 由于渐变色需要在同一个属性下设置多个颜色，所以只能设置16进制颜色值，不能使用color下的颜色资源<br>
背景渐变：
```xml
  <com.bary.ui.view.BEditText
        ...
        app:backgroundGradientColor="#FF8585|#FF8B15"
        app:backgroundGradientType="linear"
        app:backgroundGradientOrientation="horizontal"

  />
```
文本渐变：
```xml
  <com.bary.ui.view.BEditText
        ...
        app:textGradientColor="#FF8585|#FF8B15"
        app:textGradientType="linear"
        app:textGradientOrientation="horizontal"

  />
```
<br>
#### ◆ 辅助功能
######  1、一键清空
######  2、密码可见性切换&自定义图标
######  3、输入法控制
可以设置编辑模式,默认普通模式
【普通模式】
【不可编辑模式】
【可编辑不弹输入法】（有光标，一般用在语音录入）
######  4、添加校验异常提醒效果
变色、抖动....

<br>
#编写中....莫急！！！
<br>
# 二、使用方法
##### 1、把maven { url 'https://jitpack.io' }这段代码添加到项目的build.gradle存储库末尾
```java

  allprojects {
        repositories {
              ...
              maven { url 'https://jitpack.io' }
        }
  }

```
##### 2、在需要用的主程序或Module下的build.gradle中引用
```java

  dependencies {
        ...
        implementation 'com.github.barystudio:BSuperView:1.0.2'
  }

```


* * *
![Bary Studio](https://upload-images.jianshu.io/upload_images/10149003-5011dd901f0516ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# 更多内容都在【[Android开发这些年，沉淀下了什么？](https://www.jianshu.com/c/8e44ec207651)】专题中


* * *
