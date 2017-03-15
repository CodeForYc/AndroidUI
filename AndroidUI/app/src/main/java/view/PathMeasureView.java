package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import learn.yc.androidui.R;

/**
 * Created by yc on 17/3/15.
 */

public class PathMeasureView extends View {
    private Path mPath;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Bitmap mBitmap;
    private float mCurValue =0;
    private float [] mPos;
    private float [] mTan;
    private Matrix mMatrix;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public PathMeasureView(Context context) {
        super(context);
        init();
    }

    public PathMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPos = new float[2];
        mTan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.arrow,options);
        mMatrix = new Matrix();


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
        //在画布中间画圆
        canvas.translate(mWidth/2,mHeight/2);
        mPath.addCircle(0,0,200, Path.Direction.CW);
        //创建PathMeasure，并将其与path关联
        PathMeasure measure = new PathMeasure(mPath,false);
        //每次加0.05
        mCurValue += 0.05;
        if(mCurValue >= 1){
            mCurValue = 0;
        }
        //获取某个点的pos和tan
        measure.getPosTan(measure.getLength() * mCurValue,mPos,mTan);
        mMatrix.reset();
        //使用Math.atan2(mTan[1],mTan[0]获取正切角的弧度值,将弧度转换成角度，mTan[1]对边y，mTan[0]邻边x
        float degree = (float)(Math.atan2(mTan[1],mTan[0])*180.0/Math.PI);
        //对图片进行旋转
        mMatrix.postRotate(degree,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
        //将图片中心与圆形重合
        mMatrix.postTranslate(mPos[0]-mBitmap.getWidth()/2,mPos[1] - mBitmap.getHeight()/2);
        canvas.drawPath(mPath,mPaint);
        canvas.drawBitmap(mBitmap,mMatrix,mPaint);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        },500);

    }
}
