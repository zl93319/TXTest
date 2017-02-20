package com.bupa.txtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bupa.txtest.R;
import com.hyphenate.util.DensityUtil;

/**
 * 作者: l on 2017/2/6 15:41
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class SlideBar extends View {
    public static final String TAG = "SlideBar";

    private float mTextSize = 20;

    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;
    private float mBaseline;

    private OnSectionChangeListener mOnSectionChangeListener;

    private int mLastPosition = -1;

    /**
     * 在代码里面new对象时候使用
     *
     * @param context
     */
    public SlideBar(Context context) {
        this(context, null);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
        mPaint.setColor(getResources().getColor(R.color.slide_bar_text_color));
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 系统解析xml时会调用两个参数的构造方法来创建view, 后面才能通过findViewById找到view
     *
     * @param context
     * @param attrs
     */
    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
//        float y = mTextSize;
        float y = mBaseline;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, y, mPaint);
            y += mTextSize;
        }
    }

    /**
     * 控件大小发生变化的调用，布局之后会调用到
     * <p>
     * layout -> setFrame -> sizeChange -> onSizeChange
     * 初始化跟View宽高相关的参数
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTextSize = h * 1.0f / SECTIONS.length;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //求出字体高度
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        //计算文本居中时baseline的位置
        mBaseline = mTextSize / 2 + (textHeight / 2 - fontMetrics.descent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //背景变半透明
                setBackgroundResource(R.drawable.bg_slide_bar);
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_MOVE:
                notifySectionChange(event);
                break;
            case MotionEvent.ACTION_UP:
                //背景全透明
                setBackgroundColor(Color.TRANSPARENT);
                mLastPosition = -1;
                if (mOnSectionChangeListener != null) {
                    mOnSectionChangeListener.onSlideFinish();
                }
                break;
        }
        return true;
    }

    private void notifySectionChange(MotionEvent event) {
        //找出点击位置的下标
        int pos = (int) (event.getY() / mTextSize);
        //接口回调 通知外界用户点击哪个首字符
        //只有首字符发生切换，才通知外界
        if (mOnSectionChangeListener != null && pos != mLastPosition && pos>=0 && pos<26) {

            mOnSectionChangeListener.onSectionChange(SECTIONS[pos]);
        }
        mLastPosition = pos;
    }

    public interface OnSectionChangeListener {
        void onSectionChange(String section);

        //滑动结束的事件
        void onSlideFinish();
    }

    public void setOnSectionChangeListener(OnSectionChangeListener l) {
        mOnSectionChangeListener = l;
    }
}
