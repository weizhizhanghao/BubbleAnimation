package android.small.indeedfortunate.com.bubbleapplication.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.small.indeedfortunate.com.bubbleapplication.R;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by HuangMei on 2017/5/8.
 */

public class BubbleView extends View{

    /**起泡运动时间间隔*/
    private static final int PLAY_INTERVAL_TIME = 2800;
    /**起泡和圆形按钮的切线运动频率*/
    private static final double TANGENT_MOTION_RATE = 3.5;
    /**起泡出现随机间隔*/
    private static final int PLAY_APPEAR_INTERVAL_TIME = 5;

    private boolean repeatShow = true;
    private int[] resourceId = {R.drawable.big_bubble, R.drawable.small_bubble};
    private int[][] rects = {
            {getResources().getDimensionPixelOffset(R.dimen.z_small1_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small1_bubbleheight)}
            , {getResources().getDimensionPixelOffset(R.dimen.z_small2_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small2_bubbleheight)}
            , {getResources().getDimensionPixelOffset(R.dimen.z_small3_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small3_bubbleheight)}
            , {getResources().getDimensionPixelOffset(R.dimen.z_small4_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small4_bubbleheight)}
            , {getResources().getDimensionPixelOffset(R.dimen.z_small5_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small5_bubbleheight)}
            , {getResources().getDimensionPixelOffset(R.dimen.z_small6_bubblewidth), getResources().getDimensionPixelOffset(R.dimen.z_small6_bubbleheight)}
    };

    private List<BubbleViewPoint> bubbleViewPointList;
    private ValueAnimator objectAnimator;
    private int bigBubblePointWidth = getResources().getDimensionPixelOffset(R.dimen.bubblewidth);
    private int viewWidth = getResources().getDimensionPixelOffset(R.dimen.bubble_view_width);
    private int viewHeight = getResources().getDimensionPixelOffset(R.dimen.bubble_view_height);
    float lastY = viewHeight;
    int count = 0;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (repeatShow){
                initPointList();
                startAnimal();
            }
        }
    };

    public BubbleView(Context context) {
        super(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPointList();

        objectAnimator = ValueAnimator.ofFloat(1.5f, 0.0f);
        objectAnimator.setDuration(PLAY_INTERVAL_TIME);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (repeatShow){
                    handler.postDelayed(runnable, 3000);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                float y = value * viewHeight;
                float dy = lastY - y;

                lastY = y;
                count ++;

                Iterator<BubbleViewPoint> iterator = bubbleViewPointList.iterator();
                while (iterator.hasNext()){
                    BubbleViewPoint point = iterator.next();
                    point.alpha = value;

                    if (point.side == Side.LEFT){
                        int c = bigBubblePointWidth / 2 + point.width / 2;
                        double b = Math.sin(Math.toRadians(point.degree - 180)) * c;
                        double smallCirclePointY = getMeasuredHeight() / 2 + b;
                        double smallCirclePointX = getMeasuredWidth() / 2
                                - Math.sqrt((Math.pow(c, 2) - Math.pow(b, 2)));

                        point.x = (int)(smallCirclePointX - point.width / 2);
                        point.y = (int)(smallCirclePointY - point.width / 2);

                        if (point.degree <= 180){
                            point.side = Side.UP;
                            return;
                        }
                        point.degree -= TANGENT_MOTION_RATE;
                        if (point.degree <= 180)
                            point.degree = 180;

                    } else if (point.side == Side.RIGHT){
                        int c = bigBubblePointWidth / 2 + point.width / 2;
                        double b = Math.sin(Math.toRadians(360 - point.degree)) * c;
                        double smallCirclePointY = getMeasuredHeight() / 2 + b;
                        double smallCirclePointX = getMeasuredWidth() / 2
                                + Math.sqrt((Math.pow(c, 2) - Math.pow(b, 2)));

                        point.x = (int)(smallCirclePointX - point.width / 2);
                        point.y = (int)(smallCirclePointY - point.width / 2);

                        if (point.degree >= 360){
                            point.side = Side.UP;
                            return;
                        }

                        point.degree += TANGENT_MOTION_RATE;
                        if (point.degree > 360)
                            point.degree = 360;

                    } else {
                        double smallCirclePointY = point.x + point.width / 2;
                        double smallCirclePointX = point.y + point.width / 2;

                        double sumDistance = Math.sqrt(Math.pow(smallCirclePointX - getMeasuredWidth() / 2, 2)
                                + Math.pow(smallCirclePointY - getMeasuredHeight() / 2, 2));
                        if (sumDistance < point.width / 2 + bigBubblePointWidth / 2){
                            double b = Math.abs(viewWidth / 2 - smallCirclePointX);
                            double c = sumDistance;
                            double a = (int) Math.sqrt((Math.pow(c, 2)) - Math.pow(b, 2));
                            if (b < 0){
                                point.degree = 270;
                                point.side = Side.LEFT;
                            } else {
                                double aCos = (a * a + c * c - b * b) / (2 * a * c);
                                double degree = Math.toDegrees(Math.acos(aCos));
                                if (smallCirclePointX > viewWidth / 2){
                                    point.side = Side.RIGHT;
                                    point.degree = 270 + (int)degree;
                                } else {
                                    point.side = Side.LEFT;
                                    point.degree = 270 - degree;
                                }
                            }
                        } else {
                            point.y -= dy;
                        }
                    }
                    point.countDown --;
                }
                invalidate();
            }
        });

    }


    private void initPointList(){
        lastY = viewHeight;
        count = 0;
        bubbleViewPointList = new ArrayList<>();
        for (int i = 0; i < rects.length; i ++){
            BubbleViewPoint bubbleViewPoint = new BubbleViewPoint();
            bubbleViewPoint.x = i * viewWidth / rects.length;
            bubbleViewPoint.y = viewHeight;
            bubbleViewPoint.width = rects[i][0];
            bubbleViewPoint.height = rects[i][1];
            bubbleViewPoint.resourcesId = resourceId[1];
            bubbleViewPoint.alpha = 1.0f;
            bubbleViewPoint.countDown = new Random().nextInt(PLAY_APPEAR_INTERVAL_TIME);
            bubbleViewPointList.add(bubbleViewPoint);
        }
    }

    public void startAnimal(){
        objectAnimator.cancel();
        initPointList();
        objectAnimator.start();
        repeatShow = true;
    }

    public void cancelAnimal(){
        objectAnimator.cancel();
        repeatShow = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.small_bubble);
        for (BubbleViewPoint point : bubbleViewPointList){
            if (point.countDown <= 0){
                Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect destRect = new Rect(point.x, point.y, point.x + point.width, point.y + point.height);
                double alpha = (double) point.y /(double) viewHeight * 1.5 * 255;
                alpha = alpha < 30 ? 0 : alpha;
                alpha=point.alpha<0.01?0:alpha;
                paint.setAlpha((int)(alpha > 255 ? 255 : alpha));
                canvas.drawBitmap(bitmap, srcRect, destRect, paint);
            }
        }
    }
}
