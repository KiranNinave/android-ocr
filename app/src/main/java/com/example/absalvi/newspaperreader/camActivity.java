package com.example.absalvi.newspaperreader;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class camActivity extends AppCompatActivity {
    SurfaceView sf ;
    TextView t1;
    CameraSource cms;
    StringBuilder str;
    public final String FileName = "OCRcache.txt";
    TextToSpeech mtts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);


        sf =(SurfaceView) findViewById(R.id.surfaceView);
        t1 = (TextView) findViewById(R.id.text1);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational())
        {
            Toast.makeText(this,"not avalaible",Toast.LENGTH_SHORT).show();
        }
        else
        {
            cms = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .build();
            sf.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try
                    {
                        cms.start(sf.getHolder());
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cms.stop();

                }
            });
        }
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections .getDetectedItems();
                if(items.size()!=0)
                {
                    t1.post(new Runnable() {
                        @Override
                        public void run() {
                            str = new StringBuilder();
                            for(int i=0;i<items.size();i++)
                            {
                                TextBlock item =items.valueAt(i);
                                str.append(item.getValue());
                                str.append("\n");
                            }
                            t1.setText(str.toString());
                        }
                    });
                }

            }
        });

        mtts = new TextToSpeech(this , new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    int result = mtts.setLanguage(Locale.ENGLISH);

                }
                else {
                    Toast.makeText(camActivity.this , "TextToSpeech error" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onCaptureButtonClicked(View v){
        try{
            FileOutputStream fos = openFileOutput(FileName , MODE_PRIVATE);
            fos.write(str.toString().getBytes());
            Toast.makeText(camActivity.this , "file location : "+getFilesDir()+"/"+FileName , Toast.LENGTH_LONG).show();
            fos.close();
            Intent transferToFile = new Intent(camActivity.this , fileEdit.class);
            startActivity(transferToFile);

        }catch (Exception e){
            Toast.makeText(camActivity.this , "cache fail!" , Toast.LENGTH_LONG).show();
        }
    }

    public void onSoundButtonClicked(View v){
        mtts.speak(str.toString() , TextToSpeech.QUEUE_FLUSH , null);
    }
    public void onStopButtonClicked(View v){
        mtts.speak("" , TextToSpeech.QUEUE_FLUSH , null);
    }
}
