package com.example.marcos.solayerdquestion;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by marcos on 4/15/18.
 */

public class MyImageView extends AppCompatImageView {
    public MyImageView(Context context) {
        super(context);

    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void	onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        MyLayerDrawable layerDrawable=(MyLayerDrawable) getDrawable();
        setMeasuredDimension (layerDrawable.getWidth(), layerDrawable.getHeight());
    }


    @Override
    protected void onDraw(Canvas canvas){

        MyLayerDrawable layerDrawable=(MyLayerDrawable) getDrawable();
        layerDrawable.reEstablishDrawablesDimensions();


        if(layerDrawable!=null) {
            for(int i=0;i<layerDrawable.getNumberOfLayers();i++){
                layerDrawable.getDrawable(i).draw(canvas);
            }

        }
    }
}
