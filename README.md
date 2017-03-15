# AndroidUI
主要用于记录自身学习UI方面心得，该学习笔记主要从以下几个方面着手:
##1.基本控件的学习
无论是学习控件还是自定义View，首先都需要掌握View的生命周期，尽管很少地方涉及，但周期的确很重要：
![](/Users/yingcheng/view_life_cycle.png)
如果想觉得这个周期不是很清楚，想关注更多细节，可以通读此文[自定义View](http://chen-wei.me/2017/02/16/%E6%95%B4%E4%B8%AA%E8%87%AA%E5%AE%9A%E4%B9%89View/)

##2.自定义View的学习
进入自定义View,必须清楚以下基本点：
<p>1.在数学中常见坐标系与屏幕默认坐标系的区别
<p>2.View的坐标系是相对于父控件而言的，即（getTop(),getLeft(),getBottom(),getRight())
<p>3.MotionEvent中getX() & getY()与getRawX & getRawY区别
<p>4.角度和弧度的区别 rad（弧度）&deg（角度） rad=deg\*pi/180
   deg = rad*180/pi  屏幕坐标中角度增大方向是顺时针而数学系则反之
<p>5.颜色，ARGB（四通道，透明，红，绿，蓝，值从0x00-0xff表示透明到不透明，色值从浅值深）dst表示canvas原有的内容 src表示新的内容
**自定义View基本流程**
<p> step1: View初始化（构造函数）
<p> step2: 测量View的大小（onMeasure)
<p> step3: 确定View大小（onSizeChanged)
<p> step4: 确定View布局位置（onLayout,通常存在子View才需要）
<p> step5: 实际绘制（onDraw)
<p> step6: 控制View或监听View的某些状态（如：onTouchEvent，onClickListener)

<p>绘制的时候最重要的东西就Canvas(画布），Paint(画笔）以及图层
<p> 理解图层状态栈的意义，理解save和restore & saveLayer*** & restoreToCount & saveFlags等
<p> 采用Canvas绘制图片时，一般使用两个方法drawPicture（矢量图） & drawBitmap(位图）
<p> 1.drawPicture就好比录制，一般采用beginRecording和endRecording方法，方法成对出现，相当于把该区间内的绘制动作录下来，后面就可以重复该操作了，省时省力，建议使用canvas.drawPicture执行录制的东西，该方法不影响canvas状态
（但该方法使用需要关闭硬件加速）
<p>2.drawBitmap,bitmap就是问题的根源，什么OOM都很容易导致，至于这个方法大家都很熟悉不记录了，不过做帧动画的不妨尝试下这个方法

<p>绘制文字主要分三类：
<p>1.只能指定文本基线位置；
<p>2.可以指定每个文字的位置，但必须每个文字的位置都要指定，否则crash，且该方法性能不佳
<p>3.指定一个路径，根据路径绘制文字

<p> 现在登场的是Path，为了避免没必要的问题，请关闭硬件加速
<p> 第一组方法：moveTo & setLastPoint & lineTo & close
<p>第二组方法:addXxx & arcTo,主要在path上添加基本图形
<p> demo中RadaView就是对save和Path的具体实战

**PathMeasure使用**
<p>在使用PathMeasure之前，先确定其与一个Path相关联，如果后续对Path进行了更改，需重新调用setpath更新。
<p>*重要方法*:
<p> setPath:是PathMeasure与Path关联的重要方法
<p> isClosed:用于判断Path是否闭合
<p> getLength用于获取Path的总长度
<p> getSegment：用于获取Path的一个片段
<p> nextContour:用于跳转到下一条曲线，如果跳成功则返回true；
<p> getPosTan:用于获取路径上某个长度的位置以及该位置正切值
<p> getMatrix:得到某一长度的位置以及该位置的正切值的矩阵
<p>关于PathMeasure的实践可以参考PathMeasureView和SearchView






<br>**2.1 继承View**
<br>**2.2 继承ViewGroup**
需要定义ViewGroup，如果对getMeasuredWidth和getWidth分别在何时调用不太清楚，可以看下这个[文章](http://www.jianshu.com/p/a5b1e778744f)
<br>**2.3 继承现有的控件View**
<br>
##3.学习自己干兴趣的View以及自己觉得有意思的View
<br>
##4.动画的基本学习
<br>
##5.framework层对View的基本原理
<br>
##资料大汇总
[杂七杂八一堆](http://www.jianshu.com/p/a5b1e778744f
)
<p>
[android自定义View系列教程](http://www.gcssloop.com/customview/CustomViewIndex)