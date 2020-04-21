package com.baozi.expandtext;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

public class ExpandText extends androidx.appcompat.widget.AppCompatTextView {
    private static final int DEFAULT_LIMIT_LINE = 2;
    private static final int DEFAULT_ANIM_DURING = 400;
    private static final boolean DEFAULT_NEED_ANIM = true;
    private static final String DEFAULT_OPEN_END_TEXT = "收起";
    private static final String DEFAULT_CLOSE_END_TEXT = "展开全文";
    private static final String DEFAULT_ELLIPSE_TEXT = "...";
    private static final boolean DEFAULT_OPEN_STATE = false;
    private static final int DEFAULT_END_TEXT_COLOR = Color.RED;
    //收起行数
    private int limitLine;
    //省略文本
    private String ellipseText = DEFAULT_ELLIPSE_TEXT;
    //展开文字
    private String closeEndText = DEFAULT_CLOSE_END_TEXT;
    //收起文字
    private String openEndText = DEFAULT_OPEN_END_TEXT;

    //文字绘制宽度，用于计算高度
    private int showWidth;
    //打开状态
    private boolean isOpen;
    //关闭高度
    private int closeHeight;
    //展开高度
    private int openHeight;
    //是否已经在动画,避免重复动画
    private boolean isAnimating;
    //点击回调
    private OnExpandCallback listener;
    //动画时间
    private int animDuring;
    //是否需要动画
    private boolean allowAnim;
    //原始文本
    private CharSequence originText;
    //折叠后的文本
    private CharSequence mCloseText;
    //关闭文字的颜色
    private int closeTextColor;
    //打开文字的颜色
    private int openTextColor;

    public ExpandText(Context context) {
        this(context, null);
    }

    public ExpandText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnExpandCallback(OnExpandCallback callback) {
        this.listener = callback;
    }


    private void init(Context context, AttributeSet attr) {
        isOpen = false;
        isAnimating = false;
        if (attr != null) {
            TypedArray array = context.obtainStyledAttributes(attr, R.styleable.ExpandText);
            setLimitLine(array.getInt(R.styleable.ExpandText_limit_line, DEFAULT_LIMIT_LINE));
            int width = dip2px(context, array.getDimension(R.styleable.ExpandText_show_width, 0));
            width = width == 0 ? getScreenWidth(context) : width;
            setShowWidth(width-getPaddingLeft()-getPaddingRight());
            setCloseEndText(array.getString(R.styleable.ExpandText_close_end_text) == null ?
                    DEFAULT_CLOSE_END_TEXT : array.getString(R.styleable.ExpandText_close_end_text));
            setOpenEndText(array.getString(R.styleable.ExpandText_open_end_text) == null ?
                    DEFAULT_OPEN_END_TEXT : array.getString(R.styleable.ExpandText_open_end_text));
            setEllipseText(array.getString(R.styleable.ExpandText_ellipse_text) == null ?
                    DEFAULT_ELLIPSE_TEXT : array.getString(R.styleable.ExpandText_ellipse_text));
            setOpen(array.getBoolean(R.styleable.ExpandText_default_open, DEFAULT_OPEN_STATE));
            setAnimDuring(array.getInt(R.styleable.ExpandText_anim_during, DEFAULT_ANIM_DURING));
            setAllowAnim(array.getBoolean(R.styleable.ExpandText_allow_anim, DEFAULT_NEED_ANIM));
            setOpenTextColor(array.getColor(R.styleable.ExpandText_open_end_text_color,DEFAULT_END_TEXT_COLOR));
            setCloseTextColor(array.getColor(R.styleable.ExpandText_close_end_text_color,DEFAULT_END_TEXT_COLOR));
            array.recycle();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return width;
    }

    public void setCloseTextColor(int closeTextColor) {
        this.closeTextColor = closeTextColor;
    }

    public void setOpenTextColor(int openTextColor) {
        this.openTextColor = openTextColor;
    }

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
     * 设置关闭状态尾部文字
     *
     * @param closeEndText
     */
    public void setCloseEndText(String closeEndText) {
        this.closeEndText = closeEndText;
    }

    /**
     * 设置打开状态尾部文字
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
     * 设置文字宽度,不用减去padding
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

    private void setContent(CharSequence content, boolean nowAnim) {
        if (originText == null || !originText.equals(content)) {
            originText = content;
            closeHeight = 0;
            openHeight = 0;
        }
        DynamicLayout layout = getDynamicLayout(new SpannableStringBuilder(content));
        if (layout.getLineCount() <= limitLine) {
            setText(content);
            setHeight(layout.getHeight() + getPaddingTop() + getPaddingBottom());
            return;
        }
        //关闭状态
        if (!isOpen) {
            //有过记录，不用重新测量
            if (closeHeight == 0) {
                int endIndex = layout.getLineEnd(limitLine);
                mCloseText = content.toString().substring(0, endIndex);
                SpannableStringBuilder sb = new SpannableStringBuilder(mCloseText)
                        .append(ellipseText)
                        .append(getEndSpannable());
                DynamicLayout closeLayout = getDynamicLayout(sb);
                getLayout();
                while (closeLayout.getLineCount() > limitLine) {
                    endIndex--;
                    mCloseText = content.toString().substring(0, endIndex);
                    sb.clear();
                    sb.append(mCloseText).append(ellipseText).append(getEndSpannable());
                    closeLayout = getDynamicLayout(sb);
                }
                closeHeight = closeLayout.getHeight() + getPaddingTop() + getPaddingBottom();
            }
            SpannableStringBuilder closeSB = new SpannableStringBuilder(mCloseText)
                    .append(ellipseText)
                    .append(getEndSpannable());
            if (allowAnim && nowAnim) {
                closeAnimatorStart(closeSB);
            } else {
                setText(closeSB);
                setHeight(closeHeight);
            }
        } else {
            //展开状态
            //有过记录，不用重新测量
            SpannableStringBuilder sb = new SpannableStringBuilder(content).append(getEndSpannable());
            if (openHeight == 0) {
                openHeight = getDynamicLayout(sb).getHeight() + getPaddingTop() + getPaddingBottom();
            }
            if (allowAnim && nowAnim) {
                openAnimatorStart(sb);
            } else {
                setText(sb);
                setHeight(openHeight);
            }
        }
    }

    private DynamicLayout getDynamicLayout(SpannableStringBuilder sb) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            DynamicLayout.Builder builder = DynamicLayout.Builder.obtain(sb, getPaint(), showWidth);
            builder.setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setBreakStrategy(getBreakStrategy())
                    .setUseLineSpacingFromFallbacks(true)
                    .setHyphenationFrequency(getHyphenationFrequency())
                    .setIncludePad(true)
                    .setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier());
            return builder.build();
        } else {
            DynamicLayout layout = new DynamicLayout(sb, getPaint(), showWidth, Layout.Alignment.ALIGN_NORMAL,
                    getLineSpacingMultiplier(), getLineSpacingExtra(), true);
            return layout;
        }
    }

    private SpannableString getEndSpannable() {
        SpannableString end = new SpannableString(isOpen ? openEndText : closeEndText);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(isOpen?openTextColor:closeTextColor);
        setHighlightColor(Color.TRANSPARENT);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.e("hbb", "点击了" + isOpen);
                changeStateAnim(!isOpen);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        end.setSpan(clickableSpan, 0, end.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        setMovementMethod(LinkMovementMethod.getInstance());
        end.setSpan(colorSpan, 0, end.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        return end;
    }

    @Override
    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
    }

    private void changeStateAnim(boolean nowOpen) {
        if (allowAnim && isAnimating) {
            return;
        }
        isAnimating = true;
        setOpen(nowOpen);
        if (listener != null) {
            listener.expandClick(nowOpen);
        }
        setContent(originText, allowAnim);
    }

    private void openAnimatorStart(final SpannableStringBuilder sb) {
        ValueAnimator openAnimator = ValueAnimator.ofInt(closeHeight, openHeight);
        openAnimator.setDuration(animDuring);
        openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setHeight((Integer) animation.getAnimatedValue());
            }
        });
        openAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
                setText(sb);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setHeight(openHeight);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        openAnimator.start();
    }


    private void closeAnimatorStart(final SpannableStringBuilder sb) {
        ValueAnimator closeAnimator = ValueAnimator.ofInt(openHeight, closeHeight);
        closeAnimator.setDuration(animDuring);
        closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setHeight((Integer) animation.getAnimatedValue());
            }
        });
        closeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText(sb);
                setHeight(closeHeight);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        closeAnimator.start();
    }

    public interface OnExpandCallback {
        void expandClick(boolean isOpen);
    }
}
