package com.xiayiye.honorfirst.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xiayiye.honorfirst.R;

/**
 * 网上找的自定义圆环，可以自行参考学习自定义的相关方法
 */
public class CustomProgressView extends View {
    private Context context;

    private String title;
    private String num;
    private String unit;

    private float titleTextsize;
    private float numTextsize;
    private float unitTextsize;

    private int titleTextColor;
    private int numTextColor;
    private int unitTextColor;

    private float backCircleWidth;
    private float outerCircleWidth;

    private int backCircleColor;
    private int outerCircleColor;

    private float endCircleWidth;
    private int endCircleColor;

    private Paint backCirclePaint,//画背景圆
            outerCirclePaint,//画进度圆弧
            endCirclePaint,//画终点实心大圆
            endCirclePaint2,//画终点实心小圆
            titlePaint,//画第一行文字
            numPaint,//画第二行文字
            unitPaint;//画第三行文字

    private int width, height;

    private float currentPercent = 0.3f;
    //背景圆与view边界的距离
    private float edgeDistance;
    //圆环的角度参数
    private float circleArc = 0;

    public CustomProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressView);

            title = array.getString(R.styleable.CustomProgressView_progress_title);
            num = array.getString(R.styleable.CustomProgressView_progress_num);
            unit = array.getString(R.styleable.CustomProgressView_progress_unit);

            titleTextsize = array.getDimension(R.styleable.CustomProgressView_titleTextSize, getResources().getDimension(R.dimen.px_to_dip_24));
            numTextsize = array.getDimension(R.styleable.CustomProgressView_numTextSize, getResources().getDimension(R.dimen.px_to_dip_48));
            unitTextsize = array.getDimension(R.styleable.CustomProgressView_unitTextSize, getResources().getDimension(R.dimen.px_to_dip_24));
            circleArc = array.getFloat(R.styleable.CustomProgressView_circle_arc, 0);

            titleTextColor = array.getColor(R.styleable.CustomProgressView_titleTextColor, Color.parseColor("#656d78"));
            numTextColor = array.getColor(R.styleable.CustomProgressView_numTextColor, Color.parseColor("#4fc1e9"));
            unitTextColor = array.getColor(R.styleable.CustomProgressView_unitTextColor, Color.parseColor("#4fc1e9"));

            backCircleWidth = array.getDimension(R.styleable.CustomProgressView_backCircleWidth, getResources().getDimension(R.dimen.px_to_dip_12));
            outerCircleWidth = array.getDimension(R.styleable.CustomProgressView_outerCircleWidth, getResources().getDimension(R.dimen.px_to_dip_20));

            backCircleColor = array.getColor(R.styleable.CustomProgressView_backCircleColor, Color.parseColor("#e6e9ed"));
            outerCircleColor = array.getColor(R.styleable.CustomProgressView_outerCircleColor, Color.parseColor("#4fc1e9"));

            endCircleWidth = array.getDimension(R.styleable.CustomProgressView_endCircleWidth, getResources().getDimension(R.dimen.px_to_dip_24));
            endCircleColor = array.getColor(R.styleable.CustomProgressView_endCircleColor, Color.parseColor("#4fc1e9"));

            edgeDistance = array.getDimension(R.styleable.CustomProgressView_edgeDistance, getResources().getDimension(R.dimen.px_to_dip_12));

            backCirclePaint = new Paint();
            backCirclePaint.setAntiAlias(true);
            backCirclePaint.setStrokeWidth(backCircleWidth);
            backCirclePaint.setColor(backCircleColor);
            backCirclePaint.setStyle(Paint.Style.STROKE);

            outerCirclePaint = new Paint();
            outerCirclePaint.setAntiAlias(true);
            outerCirclePaint.setStrokeWidth(outerCircleWidth);
            outerCirclePaint.setColor(outerCircleColor);
            outerCirclePaint.setStyle(Paint.Style.STROKE);
            outerCirclePaint.setStrokeCap(Paint.Cap.ROUND);

            endCirclePaint = new Paint();
            endCirclePaint.setAntiAlias(true);
            endCirclePaint.setColor(endCircleColor);
            endCirclePaint.setStyle(Paint.Style.FILL);


            endCirclePaint2 = new Paint();
            endCirclePaint2.setAntiAlias(true);
            endCirclePaint2.setColor(Color.WHITE);
            endCirclePaint2.setStyle(Paint.Style.FILL);

            titlePaint = new Paint();
            titlePaint.setAntiAlias(true);
            titlePaint.setColor(titleTextColor);
            titlePaint.setTextSize(titleTextsize);

            numPaint = new Paint();
            numPaint.setAntiAlias(true);
            numPaint.setColor(numTextColor);
            numPaint.setTextSize(numTextsize);

            unitPaint = new Paint();
            unitPaint.setAntiAlias(true);
            unitPaint.setColor(unitTextColor);
            unitPaint.setTextSize(unitTextsize);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height) {
            width = height;
        }

        if (height > width) {
            height = width;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = width / 2;
        int centerY = height / 2;

        //计算半径
        float radius = (width / 2.0f) - edgeDistance;

        //半径是圆心与画出来的圆环中间点连起来的位置
        //画背景圆
        canvas.drawCircle(centerX, centerY, radius, backCirclePaint);

        //根据进度话扫过一定角度的圆弧
        RectF rectF = new RectF(edgeDistance, edgeDistance, width - edgeDistance, height - edgeDistance);
//        canvas.drawArc(rectF, -90, 360 * currentPercent, false, outerCirclePaint);
        canvas.drawArc(rectF, 180, circleArc, false, outerCirclePaint);

        //画三行文字
        Rect textRect = new Rect();

        titlePaint.getTextBounds(title, 0, title.length(), textRect);
        canvas.drawText(title, width / 2 - textRect.width() / 2, height / 4 + textRect.height() / 2, titlePaint);

        numPaint.getTextBounds(num, 0, num.length(), textRect);
        canvas.drawText(num, width / 2 - textRect.width() / 2, height / 2 + textRect.height() / 2, numPaint);

        unitPaint.getTextBounds(unit, 0, unit.length(), textRect);
        canvas.drawText(unit, width / 2 - textRect.width() / 2, height * 2 / 3 + textRect.height() / 2, unitPaint);


        //我这里规定进度在0~100%的时候才会画终点小圆，可以自由改动
        if (currentPercent < 1 && currentPercent > 0) {
//            float jiaodu = 360 * currentPercent;
            float jiaodu = circleArc;
            canvas.drawCircle(centerX + rectF.width() / 2 * (float) Math.sin(jiaodu * Math.PI / 180),
                    centerY - rectF.width() / 2 * (float) Math.cos(jiaodu * Math.PI / 180), endCircleWidth / 2, endCirclePaint);


            canvas.drawCircle(centerX + rectF.width() / 2 * (float) Math.sin(jiaodu * Math.PI / 180),
                    centerY - rectF.width() / 2 * (float) Math.cos(jiaodu * Math.PI / 180), endCircleWidth / 4, endCirclePaint2);

        }
    }


    public void setValue(String value) {
        this.num = value;
        invalidate();
    }

    public void setCurrentPercent(float currentPercent) {
        this.currentPercent = currentPercent;
        invalidate();
    }
}
