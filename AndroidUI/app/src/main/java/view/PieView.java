package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yc on 17/3/4.
 */

public class PieView extends View {

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private RectF mRect;
    private int startAngle = 0;
    private int[] degress = {40, 10, 120, 80, 60, 50};
    private
    @ColorInt
    int[] colors = {Color.RED, Color.BLACK, Color.BLUE, Color.WHITE, Color.YELLOW,
            Color.GREEN};

    private Paint mPaint;

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth, mHeight);
//        mRect = new RectF((mWidth - mRadius) / 2, (mHeight - mRadius) / 2, (mRadius + mWidth) / 2, (mRadius + mHeight) / 2);
       mRect = new RectF(-mRadius/2f,-mRadius/2f,mRadius/2f,mRadius/2f);
       startAngle  = 0;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        startAngle = 0;
        canvas.translate(mWidth/2,mHeight/2);
        for(int i = 0;i<degress.length;i++){
            canvas.rotate(i==0?0:degress[i-1]);
            canvas.save();
            mPaint.setColor(colors[i]);
            canvas.drawArc(mRect,startAngle,degress[i],true,mPaint);
            canvas.restore();
//            startAngle += degress[i];
        }
    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        for (int i = 0; i < degress.length; i++) {
//            canvas.rotate(i > 0 ? degress[i - 1] : 0, mWidth / 2, mHeight / 2);
//            canvas.save();
//            mPaint.setColor(colors[i]);
//            canvas.drawArc(mRect, 0, degress[i], true, mPaint);
//            canvas.restore();
//        }
//    }

    public void setDegressAndColor(@ColorInt int [] colors, int [] pros){
        if(colors.length != pros.length){
            throw new RuntimeException("颜色和对应比例不匹配，个数对不上");
        }
        this.colors = colors;
        this.degress = pros;
        invalidate();
    }
}
