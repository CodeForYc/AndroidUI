package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yingcheng on 17/3/5.
 */

public class RadaView extends View{
    private int mVcount = 6;//顶点数目
    private int mLayer = 6;//网有几层
    private float mMaxRadius;//最大半径
    private int mWidth;
    private int mHeight;
    private Paint mPaintNet;//画网使用的笔
    private Path mPath;
    private int mCenterX,mCenterY;
    private float degree;
    private String [] title ={"a","b","c","d","e","f"};
    //将某个顶点的总覆盖为100，各层分别占100/mLayer
    private int [] overFactor={100,60,60,60,100,50};
    private int mMaxOverFactor = 100;
    private Paint mTextPaint;
    private Paint mCirclePaint;

    public RadaView(Context context) {
        super(context);
        init();
    }

    public RadaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaintNet = new Paint();
        mPaintNet.setAntiAlias(true);
        mPaintNet.setDither(true);
        mPaintNet.setStrokeWidth(5);
        mPaintNet.setStyle(Paint.Style.STROKE);
        mPaintNet.setColor(Color.RED);
        mPath = new Path();

        mTextPaint = new Paint();
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.BLACK);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mMaxRadius =Math.min(mWidth,mHeight)/2 * 0.9f;
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;
        degree = (float)(Math.PI * 2/mVcount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //step1:画网
        drawNet(canvas);
        //step2:画出网上的线
        drawLine(canvas);
        //step3:打出文字
        drawText(canvas);
        //step4:绘制覆盖层
        drawOverLayer(canvas);
    }

    private void drawOverLayer(Canvas canvas){
        float perLayerFactor = mMaxOverFactor/(mLayer - 1)*1.0f;
        canvas.save();
        canvas.translate(mCenterX,mCenterY);
        mPath.reset();

        for(int i = 0;i< mVcount;i ++){
            //计算各层交点的位置
            float radius = overFactor[i]/perLayerFactor * mMaxRadius/(mLayer - 1);
            float x = (float)(radius * Math.cos(degree * i));
            float y = (float)(radius * Math.sin(degree * i));
            if(i == 0){
                mPath.moveTo(x,y);
            }else{
                mPath.lineTo(x,y);
            }
            canvas.drawCircle(x,y,10,mCirclePaint);
        }
        mPath.close();

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);

        canvas.drawPath(mPath, mCirclePaint);
        mCirclePaint.setAlpha(127);
        //绘制填充区域
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(mPath, mCirclePaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        for (int i = 0; i < mVcount; i++) {
            float x = (float) ((mMaxRadius + textHeight / 2) * Math.cos(i * degree));
            float y = (float) ((mMaxRadius + textHeight / 2) * Math.sin(i * degree));

            if (x >= 0 && y >= 0) {
                //第一象限
                canvas.drawText(title[i], x, y, mTextPaint);
            } else if (x >= 0 && y <= 0) {
                //第四象限
                canvas.drawText(title[i], x, y, mTextPaint);
            } else if (x <= 0 && y >= 0) {
                //第二象限
                canvas.drawText(title[i], x - mTextPaint.measureText(title[i]), y, mTextPaint);
            } else if (x <= 0 && y <= 0) {
                //第三象限
                canvas.drawText(title[i], x - mTextPaint.measureText(title[i]), y, mTextPaint);
            }
        }
        canvas.restore();
    }

    private void drawLine(Canvas canvas){
        canvas.save();
        canvas.translate(mCenterX,mCenterY);
        for(int i = 0;i<mVcount;i++){
            canvas.rotate(360/mVcount);
            //划线
            canvas.drawLine(0,0,mMaxRadius,0,mPaintNet);
        }
        canvas.restore();
    }
    private void drawNet(Canvas canvas) {
        float layerDis = mMaxRadius / (mLayer -1.0f);
        canvas.save();
        canvas.translate(mCenterX,mCenterY);
        for (int i = 1; i < mLayer; i++) {
            mPath.reset();
            //将点移动到中心
            float layerRadius = layerDis * i;
            mPath.moveTo(layerRadius, 0);
            for (int j = 1; j < mVcount; j++) {
                mPath.lineTo((float) (layerRadius * Math.cos(j * degree)), (float) (layerRadius * Math.sin(j * degree)));
            }
            mPath.close();
            canvas.drawPath(mPath, mPaintNet);
        }
      canvas.restore();
    }
}
