ExpandText
====
一个Android的可折叠展开带动画的TextView
-------
![expandtext](https://github.com/huangbaozi/ExpandText/blob/master/ScreenShots/1587540886761.jpg)

#How to use Add this to your build.gradle:
```Java
dependencies {
    implementation 'com.baozi.expandtext:expandtext:1.0.0'
}
```
#in xml
```xml
 <com.baozi.expandlibrary.ExpandText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/colorAccent"
        app:limit_line="2"
        app:close_end_text_color="@color/colorAccent"
        app:close_end_text="展开全文"
        app:open_end_text="收起"
        app:open_end_text_color="@color/colorAccent"
        app:allow_anim="true"
        app:anim_during="400"
        app:default_open="false"
        app:ellipse_text="..."
        android:textSize="18sp"/>
```
#代码设置，如果在Recyclerview中使用，要自行记录展开收起状态
```Java
  holder.contentTv.setShowWidth(width);
  holder.contentTv.setOpen(bean.isOpen());
  holder.contentTv.setOnExpandCallback(new ExpandText.OnExpandCallback() {
          @Override
          public void expandClick(boolean isOpen) {
             list.get(position).setOpen(isOpen);
             }
          });
  holder.contentTv.setContent(bean.getContent());
```
#相关属性设置
```Java
 /**
     * 设置动画时长
     *
     * @param animDuring
     */
    public void setAnimDuring(int animDuring) {
        this.animDuring = animDuring;
    }

    /**
     * 设置是否需要动画
     *
     * @param allowAnim
     */
    public void setAllowAnim(boolean allowAnim) {
        this.allowAnim = allowAnim;
    }

    /**
     * 设置省略号
     *
     * @param ellipseText
     */
    public void setEllipseText(String ellipseText) {
        this.ellipseText = ellipseText;
    }

    /**
     * 设置关闭状态尾部文字，默认为“展开全文”
     *
     * @param closeEndText
     */
    public void setCloseEndText(String closeEndText) {
        this.closeEndText = closeEndText;
    }

    /**
     * 设置打开状态尾部文字，默认为“收起”
     *
     * @param openEndText
     */
    public void setOpenEndText(String openEndText) {
        this.openEndText = openEndText;
    }

    /**
     * 设置收起行数
     *
     * @param limitLine
     */
    public void setLimitLine(int limitLine) {
        this.limitLine = limitLine;
    }

    /**
     * 设置文字宽度,不用减去padding,默认为屏幕宽度
     *
     * @param showWidth
     */
    public void setShowWidth(int showWidth) {
        showWidth=showWidth-getPaddingLeft()-getPaddingRight();
        this.showWidth = showWidth;
    }

    /**
     * 外部调用，设置文本内容
     *
     * @param content
     */
    public void setContent(String content) {
        setContent(content, false);
    }

    /**
     * 设置展开状态
     *
     * @param open
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }
     /**
     * 展开收起点击状态回调
     *
     */
    public void setOnExpandCallback(OnExpandCallback callback) {
        this.listener = callback;
    }
```
#xml属性
```
    <declare-styleable name="ExpandText">
        <attr format="dimension" name="show_width"/>//文字宽度，用于测量行数
        <attr format="boolean" name="default_open"/>//默认是否打开
        <attr format="string" name="close_end_text"/>//关闭状态尾部文字，默认为“展开全文”
        <attr format="color" name="close_end_text_color"/>//关闭状态尾部文字颜色
        <attr format="string" name="open_end_text"/>//打开状态尾部文字，默认为“收起”
        <attr format="color" name="open_end_text_color"/>//打开状态尾部文字颜色
        <attr format="string" name="ellipse_text"/>//省略号，默认为“
        <attr format="boolean" name="allow_anim"/>//是否需要展开收起动画
        <attr format="integer" name="anim_during"/>//动画时长
        <attr format="integer" name="limit_line"/>//收起状态行数
    </declare-styleable>
```
