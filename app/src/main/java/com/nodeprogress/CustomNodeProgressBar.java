package com.nodeprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

//带节点的自定义渐变色ProgressBar
public class CustomNodeProgressBar extends View {

    private RectF mRectF = new RectF();
    private Paint mPaint, mPaintCircle, mPaintCircleW, mPaintSecCircle;
    private Path mPath;

    private int[] mShaderColors = new int[2];//进度条渐变色的2个色值
    private float[] mRadians = {300, 300, 300, 300, 300, 300, 300, 300};//内层progressBar弧度
    private float[] mBgRadians = {300, 300, 300, 300, 300, 300, 300, 300};//外层progressBar弧度
    private int DEF_HEIGHT = 30;//默认进度条高
    private int DEF_WIDTH = 300;//默认进度条宽
    private int mPbSecBgColor;//pb第二背景色
    private int mPbBgColor;//pb背景色
    private int mCurrValue = 0;//当前进度值
    private int mMaxValue = 100;//进度最大值
    private int mLineWidth = 10;//两节点之间的进度值，用来确定节点数
    private int mDefValue = 0;//pb默认值
    private int mPbBgWidth;//pb背景宽度
    private int mPbBgHeight;//pb背景高度
    private float mPadding;//进度条遇外层pb间的padding
    private float mUnitPbWidth;//1进度的长度值
    private int mSpaceMargRight = 0;//距离右端的距离不绘制进度，和mCurrValue，mMaxValue 单位一致

    public CustomNodeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttributes(attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL); //设置空心
        mPaintCircle.setStrokeWidth(3); //设置圆环的宽度

        mPaintSecCircle = new Paint();
        mPaintSecCircle.setAntiAlias(true);
        mPaintSecCircle.setStyle(Paint.Style.FILL); //设置空心
        mPaintSecCircle.setStrokeWidth(3); //设置圆环的宽度

        mPaintCircleW = new Paint();
        mPaintCircleW.setAntiAlias(true);
        mPaintCircleW.setStyle(Paint.Style.FILL); //设置空心
        mPaintCircleW.setStrokeWidth(3); //设置圆环的宽度
        mPaintCircleW.setColor(getResources().getColor(R.color.white));

        mPath = new Path();
    }

    //设置pb默认值
    public void setDefValue(int value) {
        mDefValue = value > 100 ? 100 : value;
    }

    public void setMax(int max) {
        mMaxValue = max;
    }

    public void setLineWidth(int width) {
        mLineWidth = width;
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    public synchronized void setProgressbarValue(int value) {
        value = value > mMaxValue ? mMaxValue : value;
        mCurrValue = value > mDefValue ? value : mDefValue;
        invalidate();
    }

    public synchronized int getProgressbarValue() {
        return mCurrValue;
    }

    private void obtainStyledAttributes(AttributeSet attrs) {

        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.NodeProgressBar);
        mPbBgColor = attributes.getColor(R.styleable.NodeProgressBar_node_pb_bg_color, Color.GRAY);
        mPbSecBgColor = attributes.getColor(R.styleable.NodeProgressBar_node_pb_bg_second_color, Color.GRAY);
        int mPbForeColorStart = attributes.getColor(R.styleable.NodeProgressBar_node_pb_foreground_color_start, 0xff25b7ff);
        mShaderColors[0] = mPbForeColorStart;
        int mPbForeColorEnd = attributes.getColor(R.styleable.NodeProgressBar_node_pb_foreground_color_end, 0xff25d0ff);
        mShaderColors[1] = mPbForeColorEnd;
        mPadding = attributes.getDimension(R.styleable.NodeProgressBar_node_pb_padding, 6);
        float mBgRadian = attributes.getDimension(R.styleable.NodeProgressBar_node_pb_bg_radian, 0);
        float mPbRadian = attributes.getDimension(R.styleable.NodeProgressBar_node_pb_radian, 0);
        mLineWidth = attributes.getInt(R.styleable.NodeProgressBar_node_pb_line_width, 10);
        mSpaceMargRight = attributes.getInt(R.styleable.NodeProgressBar_node_pb_space_margRight, 0);
        mMaxValue = attributes.getInt(R.styleable.NodeProgressBar_node_pb_max, 100);
        attributes.recycle();

        mRadians = new float[]{mPbRadian, mPbRadian, mPbRadian, mPbRadian, mPbRadian, mPbRadian, mPbRadian, mPbRadian};
        mBgRadians = new float[]{mBgRadian, mBgRadian, mBgRadian, mBgRadian, mBgRadian, mBgRadian, mBgRadian, mBgRadian};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mProgressBarBgWidth;//pb背景宽度
        int mProgressBarBgHeight;//pb背景高度

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mProgressBarBgWidth = widthSize;
        } else {
            mProgressBarBgWidth = DEF_WIDTH;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mProgressBarBgHeight = heightSize;
        } else {
            mProgressBarBgHeight = DEF_HEIGHT;
        }

        //内部进度条的宽度
        float mProgressBarWidth = mProgressBarBgWidth - (mPadding * 2);
        float mUnitPbWidth = mProgressBarWidth / mMaxValue;
        mPbBgHeight = mProgressBarBgHeight;
        mPbBgWidth = mProgressBarBgWidth;
        this.mUnitPbWidth = mUnitPbWidth;

        setMeasuredDimension(mProgressBarBgWidth, mProgressBarBgHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
    }

    protected void drawProgress(Canvas canvas) {

        //背景节点数 node num
        int nodeCount = 1;
        if (mLineWidth > 0) {
            nodeCount = (mMaxValue - mSpaceMargRight) / mLineWidth;//节点数
        }

        //1.绘制灰色progressBar背景
        mPaint.setColor(mPbBgColor);
        mPaint.setShader(null);
        mRectF.left = 0;
        mRectF.top = mPadding;
        mRectF.right = mPbBgWidth;
        mRectF.bottom = mPbBgHeight - mPadding;
        mPath.reset();
        mPath.addRoundRect(mRectF, mBgRadians, Direction.CCW);
        canvas.drawPath(mPath, mPaint);
        mPaintSecCircle.setColor(mPbBgColor);
        for (int i = 1; i <= nodeCount; i++) {
            //画渐变节点
            int radius = (int) ((mPbBgHeight - mPadding * 2) / 2); //里层小圆半径
            int centreX = (int) ((mLineWidth * mUnitPbWidth ) * i); //获取圆心的x坐标
            int centreY = mPbBgHeight / 2; //获取圆心的x坐标
            radius = mPbBgHeight / 2; //圆环的半径
            if (i == mMaxValue/mLineWidth){
                centreX -= mPadding*2;
            }
            canvas.drawCircle(centreX, centreY, radius, mPaintSecCircle); //画出圆环
        }

        //2.绘制secondProgressBar背景
        mPaint.setColor(mPbSecBgColor);
        mPaint.setShader(null);
        mRectF.left = mPadding;
        mRectF.top = mPadding * 2;
        mRectF.right = (mMaxValue - mSpaceMargRight) * mUnitPbWidth;
        mRectF.bottom = mPbBgHeight - mPadding * 2;
        mPath.reset();
        mPath.addRoundRect(mRectF, mBgRadians, Direction.CCW);
        canvas.drawPath(mPath, mPaint);
        mPaintSecCircle.setColor(mPbSecBgColor);
        for (int i = 1; i <= nodeCount; i++) {
            //画渐变节点
            int radius = (int) ((mPbBgHeight - mPadding * 2) / 2); //圆环的半径
            int centreX = (int) ((mLineWidth * mUnitPbWidth ) * i); //获取圆心的x坐标
            int centreY = mPbBgHeight / 2; //获取圆心的x坐标
            if (i == mMaxValue/mLineWidth){
                centreX -= mPadding*2;
            }
            canvas.drawCircle(centreX, centreY, radius, mPaintSecCircle); //画出圆环
            //画白色节点
            radius = radius / 2;
            canvas.drawCircle(centreX, centreY, radius, mPaintCircleW); //画出圆环
        }

        //需求需要最后一段不在画进度
        if (mCurrValue > mMaxValue  - mSpaceMargRight) {
            mCurrValue =  mMaxValue - mSpaceMargRight;
        }

        //当前进度节点数 node num
        int curNodeCount = 0;
        if (mLineWidth > 0) {
            curNodeCount = mCurrValue / mLineWidth;
        }

        //3.绘制progressBar进度
        float currWidth = mCurrValue * mUnitPbWidth;
        mRectF.left = mPadding;
        mRectF.top = mPadding * 2;
        mRectF.right = currWidth;
        mRectF.bottom = mPbBgHeight - mPadding * 2;
        mPath.reset();
        mPath.addRoundRect(mRectF, mRadians, Direction.CCW);
        LinearGradient shader = new LinearGradient(0, 0, mRectF.right, 0, mShaderColors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        mPaintCircle.setShader(shader);
        canvas.drawPath(mPath, mPaint);
        for (int i = 1; i <= curNodeCount; i++) {
            //画渐变节点
            int radius = (int) ((mPbBgHeight - mPadding * 2) / 2); //圆环的半径
            int centreX = (int) ((mLineWidth * mUnitPbWidth ) * i); //获取圆心的x坐标
            int centreY = mPbBgHeight / 2; //获取圆心的x坐标
            if (i == mMaxValue/mLineWidth){
                centreX -= mPadding*2;
            }
            canvas.drawCircle(centreX, centreY, radius, mPaintCircle); //画出圆环
            //画白色节点
            radius = radius / 2;
            canvas.drawCircle(centreX, centreY, radius, mPaintCircleW); //画出圆环
        }
    }

}
