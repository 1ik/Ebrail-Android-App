package com.renoscience.ebrail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

/**
 * Created by anik on 2/9/15.
 */

public class MainActivity extends Activity implements View.OnTouchListener {

    private ImageView screenshotImageView;
    private ImageView previewImageView;

    private Bitmap cropped_screenshot;
    public static final String DATA_PATH = "/sdcard/e-braille/";
    public static final String lang = "eng";

    private Bitmap screenshot;

    private TessBaseAPI baseApi;
    private TextView processedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        screenshotImageView = (ImageView) findViewById(R.id.imageView2);
        previewImageView = (ImageView) findViewById(R.id.imageView3);

        File imageFile = new  File("/sdcard/screenshot.jpg");
        if(imageFile.exists()){
            screenshot = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            screenshotImageView.setImageBitmap(screenshot);
        }

        baseApi = new TessBaseAPI();

        baseApi = new TessBaseAPI();
        baseApi.setDebug(false);
        baseApi.init(DATA_PATH, lang);
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, ".-+?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345789");
        processedTextView = (TextView) findViewById(R.id.textView);

        screenshotImageView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        String text;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                try {
                    cropped_screenshot = Bitmap.createBitmap(screenshot, (int) event.getX(), (int) event.getY(), 100, 150);
                    previewImageView.setImageBitmap(cropped_screenshot);

                    //baseApi.setImage(cropped_screenshot);
                    //text = baseApi.getUTF8Text();

                    //textView.setText(text);
                }
                catch(Exception e){}
                break;

            case MotionEvent.ACTION_MOVE:
                try{
                    cropped_screenshot = Bitmap.createBitmap(screenshot, (int) event.getX(), (int) event.getY()+100, 100, 50);
                    previewImageView.setImageBitmap(cropped_screenshot);

                    baseApi.setImage(cropped_screenshot);
                    text = baseApi.getUTF8Text();
                    processedTextView.setText(text);

                    //baseApi.setImage(cropped_screenshot);
                    //text = baseApi.getUTF8Text();
                    //textView.setText(text);
                }
                catch(Exception e){}
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

}
