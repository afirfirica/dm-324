package xyz.pinaki.androidcamera.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gonçalo on 13/02/2015.
 */

/**
 * Class used to draw the points on screen. Handles user touch input.
 */
public class DrawView extends SurfaceView {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    List<Point> circlePoints;
    private Context context;

    private static int REFERENCE_POINT_COLOR = Color.YELLOW;


    public DrawView(Context context){
        super(context);
        this.context = context;
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        circlePoints = new ArrayList<>();
        setWillNotDraw (false);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onDraw(Canvas canvas){
        int size = circlePoints.size();
        for(int i = 0; i < size; i++){
            //Set color based on order. First 2 points are the reference points.
            paint.setColor(REFERENCE_POINT_COLOR);

            Point p = circlePoints.get(i);
            canvas.drawCircle(p.x, p.y, 10, paint);
            if(i == 1){
                canvas.drawLine(circlePoints.get(0).x, circlePoints.get(0).y, circlePoints.get(1).x, circlePoints.get(1).y, paint);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(circlePoints.size() < 2) {
                circlePoints.add(new Point(Math.round(event.getX()), Math.round(event.getY())));
                invalidate();
                if(circlePoints.size() == 2){
                    // ((TextView) ((Activity)context).findViewById(R.id.)).setText(getResources().getString(R.string.setMeasurePoints));
                }
            }
        }
        return false;
    }

    /**
     * Clears all drawn points and shapes
     */
    public void clearCanvas(){
        circlePoints.clear();
        // ((TextView) ((Activity)context).findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setPicture));
        invalidate();
    }

    /**
     * Calculates the measurement
     * @param distance The distance size

     * @return The value of the measurement, converted to outputUnitIndex
     */
    public double calculate(double distance){
        if(circlePoints.size() != 2){
            Toast.makeText(context, getResources().getString(R.string.error_noPoints), Toast.LENGTH_SHORT).show();
            return -1;
        }
        return Ruler.compute(circlePoints, distance);
    }


}
