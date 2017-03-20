package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import learn.yc.androidui.R;

/**
 * Created by yingcheng on 17/3/20.
 */

public class FlodView extends View {
    private int mWidth;
    private int mHeight;
    private static final int NUM_POINT = 8;

    //图片
    private Bitmap mBitmap;
    //将图片折成几分
    private int mFlodNum = 8;
    //折叠后每等分宽度
    private float mPerFlodWidth;
    //每等分未折叠的宽度
    private float mPerOrignWidth;
    //折叠后的总长度是原来的多少
    private float mFactor = 0.8f;
    //每等分折叠Matrix
    private Matrix[] mMatrix = null;
    //开始绘制阴影
    private Paint mRightPaint;//折叠线的右边
    private Paint mLeftPaint; //折叠线的左边

    private LinearGradient mShadowGradient;




    public FlodView(Context context) {
        super(context);
        init();
    }

    public FlodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty);
        mPerOrignWidth = Math.min(mBitmap.getWidth(),getResources().getDisplayMetrics().widthPixels) / mFlodNum * 1.0f;
        mPerFlodWidth = mPerOrignWidth * mFactor;
        mMatrix = new Matrix[mFlodNum];

        mLeftPaint = new Paint();
        mLeftPaint.setStyle(Paint.Style.FILL);
        mLeftPaint.setColor(Color.BLACK);
        mLeftPaint.setAlpha((int)(255 * 0.64f));

        mRightPaint = new Paint();
        mRightPaint.setStyle(Paint.Style.FILL);
        mShadowGradient = new LinearGradient(0,0,0.5f,0,Color.BLACK,Color.TRANSPARENT, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setScale(mPerOrignWidth,1);
        mShadowGradient.setLocalMatrix(matrix);
        mRightPaint.setShader(mShadowGradient);
        mRightPaint.setAlpha((int)(255 * 0.64f));

        float depth = (float) Math.sqrt(mPerOrignWidth * mPerOrignWidth - mPerFlodWidth * mPerFlodWidth) * 0.5f;

        float[] src = new float[NUM_POINT];
        float[] dst = new float[NUM_POINT];
        for (int i = 0; i < mMatrix.length; i++) {
            src[0] = i * mPerOrignWidth;
            src[1] = 0;
            src[2] = (i + 1) * mPerOrignWidth;
            src[3] = 0;
            src[4] = src[2];
            src[5] = mBitmap.getHeight();
            src[6] = src[0];
            src[7] = src[5];

            dst[0] = i * mPerFlodWidth;
            dst[1] = i % 2 == 0 ? 0 : depth;
            dst[2] = (i + 1) * mPerFlodWidth;
            dst[3] = (i + 1) % 2 == 0 ? 0 : depth;
            dst[4] = dst[2];
            dst[5] = mBitmap.getHeight() - ((i + 1) % 2 == 0 ? 0 : depth);
            dst[6] = dst[0];
            dst[7] = mBitmap.getHeight() - (i % 2 == 0 ? 0 : depth);

            mMatrix[i] = new Matrix();
            mMatrix[i].setPolyToPoly(src,0,dst,0,src.length >> 1);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(mBitmap.getWidth(),MeasureSpec.getSize(widthMeasureSpec));
        int height = Math.min(mBitmap.getHeight(),MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0;i<mMatrix.length;i++){
            canvas.save();
            canvas.concat(mMatrix[i]);
            canvas.clipRect(i*mPerOrignWidth,0,(i +1) * mPerOrignWidth,mBitmap.getHeight());
            canvas.drawBitmap(mBitmap,0,0,null);
            //绘制阴影
            canvas.translate(i*mPerOrignWidth,0);
            if(i % 2 == 0){
                //左边
                canvas.drawRect(0,0,mPerOrignWidth,mBitmap.getHeight(),mLeftPaint);
            }else{
                //右边
                canvas.drawRect(0,0,mPerOrignWidth,mBitmap.getHeight(),mRightPaint);

            }
            canvas.restore();
        }
    }
    public void scale(float factor){
        mFactor = factor;
        init();
        invalidate();
    }
    public float getScaleFator(){
        return mFactor;
    }
}
