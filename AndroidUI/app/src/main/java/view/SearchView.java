package view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by yc on 17/3/15.
 */

public class SearchView extends View {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    public enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private State mCurState = State.NONE;

    private Path mPathSearch;
    private Path mPathCircle;

    private PathMeasure mPathMeasure;

    private long mDefaultDuration = 2000;

    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchAnimator;
    private ValueAnimator mEndAnimator;

    private float mAnimatorValue = 0;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private ValueAnimator.AnimatorListener mAnimatorListener;

    private Handler mAnimatorHandler;

    private int mCount = 0; //记录搜索圈几次


    public SearchView(Context context) {
        super(context);
        initEnv();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEnv();
    }

    private void initEnv() {
        initHandler();
        initPain();
        initPath();
        initAnimator();
        mAnimatorHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurState = State.STARTING;
                mStartingAnimator.start();
            }
        }, mDefaultDuration);
    }

    private void initHandler() {
        mAnimatorHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurState) {
                    case STARTING:
                        onStart();
                        break;
                    case SEARCHING:
                        onSearch();
                        break;
                    case ENDING:
                        onFinish();
                        break;
                    case NONE:
                    default:
                        break;
                }
            }
        };
    }

    private void onStart() {
        mCurState = State.SEARCHING;
        mCount = 1;
        mStartingAnimator.removeAllListeners();
        mSearchAnimator.start();

    }

    private void onSearch() {
        //外圆走了一个圈，需要走两周
        if (mCount < 3) {
            mCount++;
            mSearchAnimator.start();
        } else if (mCount == 3) {
            mCurState = State.ENDING;
            mSearchAnimator.removeAllListeners();
            mEndAnimator.start();
        }

    }

    private void onFinish() {
        mEndAnimator.removeAllListeners();
        mCurState = State.NONE;
    }

    private void initPain() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
    }

    private void initPath() {
        mPathCircle = new Path();
        //外圆
        mPathCircle.addArc(new RectF(-100, -100, 100, 100), 45, 359.99f);

        mPathSearch = new Path();
        mPathSearch.addArc(new RectF(-50, -50, 50, 50), 45, 359.99f);

        //search图
        mPathMeasure = new PathMeasure(mPathCircle, false);
        float[] pos = new float[2];
        mPathMeasure.getPosTan(0, pos, null);
        mPathSearch.lineTo(pos[0], pos[1]);
    }

    private void initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);
        mSearchAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);
        mEndAnimator = ValueAnimator.ofFloat(1, 0).setDuration(mDefaultDuration);
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorHandler.sendEmptyMessage(0);
            }
        };
        mSearchAnimator.addUpdateListener(mUpdateListener);
        mSearchAnimator.addListener(mAnimatorListener);
        mStartingAnimator.addUpdateListener(mUpdateListener);
        mStartingAnimator.addListener(mAnimatorListener);
        mEndAnimator.addUpdateListener(mUpdateListener);
        mEndAnimator.addListener(mAnimatorListener);
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
        drawAnimator(canvas);
    }

    private void drawAnimator(Canvas canvas) {
        //把坐标定位中间
        canvas.translate(mWidth / 2, mHeight / 2);
        switch (mCurState) {
            case NONE:
                //画出搜索框
                canvas.drawPath(mPathSearch, mPaint);
                break;
            case STARTING:
                //让搜索框慢慢消失
                Path dst = new Path();
                mPathMeasure.setPath(mPathSearch, false);
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SEARCHING:
                //让外圆旋转两周
                Path dst1 = new Path();
                mPathMeasure.setPath(mPathCircle, false);
                float stop = mPathMeasure.getLength() * mAnimatorValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
                mPathMeasure.getSegment(start, stop, dst1, true);
                canvas.drawPath(dst1, mPaint);
                break;
            case ENDING:
                Path dst2 = new Path();
                mPathMeasure.setPath(mPathSearch, false);
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
            default:
                canvas.drawPath(mPathSearch, mPaint);
        }

    }

}
