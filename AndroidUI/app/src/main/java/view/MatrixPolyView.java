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
 * Created by yc on 17/3/16.
 */

public class MatrixPolyView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Paint mPaint;
    private LinearGradient mLinearShader;
    private Matrix mShaderMatrix;
    public MatrixPolyView(Context context) {
        super(context);
        init();
    }

    public MatrixPolyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty,options);
        mMatrix = new Matrix();
        float []src ={
                0,0,
                mBitmap.getWidth(),0,
                mBitmap.getWidth(),mBitmap.getHeight(),
                0,mBitmap.getHeight()

        };
        float []dst ={
                0,0,
                mBitmap.getWidth(),mBitmap.getHeight()*1/4,
                mBitmap.getWidth(),mBitmap.getHeight()*3/4,
                0,mBitmap.getHeight()

        };
        mMatrix.setPolyToPoly(src,0,dst,0,src.length >>1);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mLinearShader = new LinearGradient(0,0,0.5f,0, Color.RED,Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearShader);

        mShaderMatrix = new Matrix();
        mShaderMatrix.setScale(mBitmap.getWidth()/2,1);
        mLinearShader.setLocalMatrix(mShaderMatrix);
        mPaint.setAlpha((int)(0.9*255));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        android.util.Log.e("yincheng",">>>>>orign:" + canvas.getMatrix().toShortString());
//        android.util.Log.e("yincheng",">>>> matrix:" + mMatrix.toShortString());
        canvas.concat(mMatrix);//此句
//        android.util.Log.e("yincheng",">>>>>orign1:" + canvas.getMatrix().toShortString());
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),mPaint);
        canvas.restore();

    }


}
