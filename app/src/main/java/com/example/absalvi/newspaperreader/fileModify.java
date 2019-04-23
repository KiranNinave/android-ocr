package com.example.absalvi.newspaperreader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class fileModify extends AppCompatActivity {

    EditText content;
    DatabaseHelper database;
    Cursor cur;
    TextToSpeech mtts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_modify);

        content = (EditText) findViewById(R.id.ETmodify);
        database = new DatabaseHelper(this);

        mtts = new TextToSpeech(this , new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    int result = mtts.setLanguage(Locale.ENGLISH);

                }
                else {
                    Toast.makeText(fileModify.this , "TextToSpeech error" , Toast.LENGTH_LONG).show();
                }
            }
        });

        setFileContent();
    }

    public void setFileContent(){
        cur = database.getAllData();
        cur.moveToLast();
        setTextBoxText(cur.getString(1));

    }
    public void setTextBoxText(String FileName){
        FileInputStream fin = null;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ocr/text" , FileName);
        try{
            fin = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = null;
            while ((text = br.readLine())!=null){
                sb.append(text).append("\n");
            }

            content.setText(sb.toString());
            fin.close();
        }catch (Exception e){
            Toast.makeText(fileModify.this , "file load fail!" , Toast.LENGTH_LONG).show();
        }
    }


    public void onDeleteButtonClicked(View v){
        cur.moveToLast();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ocr/text" , cur.getString(1));
        file.delete();
        Toast.makeText(fileModify.this , cur.getString(1)+"deleted!" , Toast.LENGTH_SHORT).show();
        Intent tranf = new Intent(fileModify.this , MainActivity.class);
        startActivity(tranf);
    }
    public void onSpeakButtonClicked(View v){
        mtts.speak(content.getText().toString() , TextToSpeech.QUEUE_FLUSH , null);
    }
    public void onStopButtonClicked(View v){
        mtts.speak("" , TextToSpeech.QUEUE_FLUSH , null);
    }
    public void onSaveButtonClicked(View v){
        FileOutputStream fos = null;
        cur.moveToLast();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ocr/text" , cur.getString(1));

        try{
            fos = new FileOutputStream(file);
            fos.write(content.getText().toString().getBytes());
            Toast.makeText(fileModify.this , "saved!" , Toast.LENGTH_LONG).show();
            Intent transferToDashboard = new Intent(fileModify.this , MainActivity.class);
            startActivity(transferToDashboard);
            fos.close();
        }catch (Exception e){
            Toast.makeText(fileModify.this , "something wrong!" , Toast.LENGTH_LONG).show();
        }
    }
}
