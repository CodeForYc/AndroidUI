package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yc on 17/3/4.
 */

public class CanvasTest extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    public CanvasTest(Context context) {
        super(context);
        init();
    }

    public CanvasTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
//        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
//        canvas.rotate(-30);
//        canvas.drawLine(0,0,mWidth/2,0,mPaint);
        //1.画个外圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(0,0,Math.min(mWidth,mHeight)/2,mPaint);
        canvas.drawCircle(0,0,Math.min(mWidth,mHeight)*2/5,mPaint);
        for(int i = 0;i<36;i++){
            mPaint.setColor(Color.GREEN);
            canvas.drawLine(Math.min(mWidth,mHeight)*2/5,0,Math.min(mWidth,mHeight)/2,0,mPaint);
            canvas.rotate(10);
        }

    }
}

