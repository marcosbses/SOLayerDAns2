package com.example.marcos.solayerdquestion;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos on 12/31/17.
 */
public class MyLayerDrawable extends LayerDrawable{

    private int resource;
    private Context context;
    private List<Layer> layers;
    private List<Integer> layerWidthsList;
    private List<Integer> layerHeightsList;
    private int width=0;
    private int height=0;

    public static class Builder{
        public static MyLayerDrawable build(Context context,int resource){


            LayerDrawable ld=(LayerDrawable)ContextCompat.getDrawable(context, resource);
            List<Drawable> drawableList=new ArrayList<>();
            Drawable[] drawables={};
            for(int i=0;i<ld.getNumberOfLayers();i++){
                drawableList.add(ld.getDrawable(i));
            }
            drawables=drawableList.toArray(drawables);


            MyLayerDrawable myLayerDrawable=new MyLayerDrawable(drawables);


            myLayerDrawable.setResource(resource);
            myLayerDrawable.setContext(context);
            myLayerDrawable.initializeLayers();

            return myLayerDrawable;
        }



        public static MyLayerDrawable build(List<Layer> layers){
            List<Drawable> drawableList=new ArrayList<>();
            for(int i=0;i<drawableList.size();i++){
                drawableList.add(layers.get(i).drawable);
            }
            MyLayerDrawable myLayerDrawable=new MyLayerDrawable((Drawable[])drawableList.toArray());
            myLayerDrawable.setLayers(layers);
            return myLayerDrawable;
        }
        private static int parseMyLayerInsetTop(int index,Context context,int resource){
            XmlResourceParser xmlResourceParser=context.getResources().getXml(resource);


            try {
                xmlResourceParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int n=-1;
            while(n<index){
                try {
                    xmlResourceParser.next();
                    if(xmlResourceParser.getEventType()== XmlPullParser.START_TAG&&xmlResourceParser.getName().equals("item")){

                            n++;


                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            //String dip=xmlResourceParser.getAttributeValue(0);
            String dip="0.0dip";

            for(int i=0;i<xmlResourceParser.getAttributeCount();i++){
                if(xmlResourceParser.getAttributeName(i).equals("top")){
                    dip=xmlResourceParser.getAttributeValue(i);
                    break;
                }
            }

            int dipInt=Integer.parseInt(dip.substring(0,dip.length()-5));

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipInt, context.getResources().getDisplayMetrics());
            int pxInt=(int)px;


            xmlResourceParser.close();
            return pxInt;

        }
        private static int parseMyLayerInsetLeft(int index,Context context,int resource){
            XmlResourceParser xmlResourceParser=context.getResources().getXml(resource);

            try {
                xmlResourceParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int n=-1;
            while(n<index){
                try {
                    xmlResourceParser.next();
                    if(xmlResourceParser.getEventType()== XmlPullParser.START_TAG&&xmlResourceParser.getName().equals("item")){

                        n++;


                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
;
            //String dip=xmlResourceParser.getAttributeValue(0);
            String dip="0.0dip";

            for(int i=0;i<xmlResourceParser.getAttributeCount();i++){
                if(xmlResourceParser.getAttributeName(i).equals("left")){
                    dip=xmlResourceParser.getAttributeValue(i);
                    break;
                }
            }

            int dipInt=Integer.parseInt(dip.substring(0,dip.length()-5));

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipInt, context.getResources().getDisplayMetrics());
            int pxInt=(int)px;


            xmlResourceParser.close();
            return pxInt;

        }
    }

    private void initializeLayers(){
        parseLayerWidths();
        parseLayerHeights();
        layers=new ArrayList<>();
        for(int i=0;i<this.getNumberOfLayers();i++){
            int topInset=MyLayerDrawable.Builder.parseMyLayerInsetTop(i,context,resource);
            int leftInset=MyLayerDrawable.Builder.parseMyLayerInsetLeft(i,context,resource);

            Layer layer=new MyLayerDrawable.Layer(getDrawable(i),topInset,leftInset,layerWidthsList.get(i),layerHeightsList.get(i));
            layers.add(layer);
        }
    }

    private void parseLayerWidths(){
        Log.i("infor","parse layer widths");
        layerWidthsList=new ArrayList<>();

        XmlResourceParser xmlResourceParser=context.getResources().getXml(resource);


        try {
            xmlResourceParser.next();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index=0;
        try {
            while (xmlResourceParser.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xmlResourceParser.getEventType() == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("item")) {
                    Log.i("infor","item: "+String.valueOf(index));
                    String width;
                    boolean hasWidth=false;
                    for(int i=0;i<xmlResourceParser.getAttributeCount();i++){
                        if(xmlResourceParser.getAttributeName(i).equals("width")){
                            hasWidth=true;
                            width=xmlResourceParser.getAttributeValue(i);
                            int dipInt=Integer.parseInt(width.substring(0,width.length()-5));
                            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipInt, context.getResources().getDisplayMetrics());
                            int pxInt=(int)px;
                            layerWidthsList.add(pxInt);
                            if(pxInt>this.width){this.width=pxInt;}
                            Log.i("infor","width dip: "+width);
                            Log.i("infor","width px: "+pxInt);

                            break;
                        }
                    }
                    if(!hasWidth){
                        layerWidthsList.add(-1);
                    }
                    index++;
                }

                xmlResourceParser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void parseLayerHeights(){
        Log.i("infor","parse layer heights");
        layerHeightsList=new ArrayList<>();

        XmlResourceParser xmlResourceParser=context.getResources().getXml(resource);


        try {
            xmlResourceParser.next();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index=0;
        try {
            while (xmlResourceParser.getEventType()!=XmlPullParser.END_DOCUMENT) {
                if (xmlResourceParser.getEventType() == XmlPullParser.START_TAG && xmlResourceParser.getName().equals("item")) {
                    Log.i("infor","item: "+String.valueOf(index));
                    String width;
                    boolean hasWidth=false;
                    for(int i=0;i<xmlResourceParser.getAttributeCount();i++){
                        if(xmlResourceParser.getAttributeName(i).equals("height")){
                            hasWidth=true;
                            width=xmlResourceParser.getAttributeValue(i);
                            int dipInt=Integer.parseInt(width.substring(0,width.length()-5));
                            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipInt, context.getResources().getDisplayMetrics());
                            int pxInt=(int)px;
                            layerHeightsList.add(pxInt);
                            if(pxInt>this.height){this.height=pxInt;}
                            Log.i("infor","height dip: "+width);
                            Log.i("infor","height px: "+pxInt);

                            break;
                        }
                    }
                    if(!hasWidth){
                        layerHeightsList.add(-1);
                    }
                    index++;
                }

                xmlResourceParser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public class Layer{
        private Drawable drawable;
        private int topInset;
        private int leftInset;
        private int rightBoud;
        private int bottomBound;
        private Rect bounds;
        public Layer(Drawable drawable,int topInset,int leftInset,int width, int height){
            Log.i("infor","layer construct");
            this.leftInset=leftInset;
            this.drawable=drawable;
            this.topInset=topInset;
            this.bounds=drawable.getBounds();
            this.bounds.top=topInset;
            this.bounds.left=leftInset;
            this.bounds.right=leftInset+width;
            this.bounds.bottom=topInset+height;
            rightBoud=leftInset+width;
            bottomBound=topInset+height;
        }
        public void reEstablishDrawableDimensions(){
            getDrawable().setBounds(leftInset,topInset,rightBoud,bottomBound);
        }
        public Drawable getDrawable(){
            return drawable;
        }
        public int getTopInset(){
            return topInset;
        }
        public int getLeftInset(){return leftInset;}
        public int getRightBoud(){return rightBoud;}
        public int getBottomBound(){return bottomBound;}
    }
    public MyLayerDrawable(Drawable[] drawables){
        super(drawables);
    }
    public void reEstablishDrawablesDimensions(){
        for(int i=0;i<getNumberOfLayers();i++){
            getLayer(i).reEstablishDrawableDimensions();
        }
    }

    public void setResource(int resource){

        this.resource=resource;
    }

    public void setContext(Context context){
        this.context=context;
    }

    public void setLayers(List<Layer> layers){
        this.layers=layers;
    }

    public Layer getLayer(int index){
        return layers.get(index);
    }

    @Override
    public int getLayerWidth(int index){
        return layerWidthsList.get(index);
    }


    public int getWidth(){
        return width;
    }

    public int getHeight(){return height;}

}
