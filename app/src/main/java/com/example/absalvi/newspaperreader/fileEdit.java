package com.example.absalvi.newspaperreader;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class fileEdit extends AppCompatActivity {

    public final String FileName = "OCRcache.txt";
    EditText name, content;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);

        name = (EditText) findViewById(R.id.ETname);
        content = (EditText) findViewById(R.id.ETcontent);

        save = (Button) findViewById(R.id.BTsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        setContentText();
    }
    public void onSaveButtonClicked(){
        FileOutputStream fos = null;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ocr/text" , name.getText().toString()+".txt");
        try{
            fos = new FileOutputStream(file);
            fos.write(content.getText().toString().getBytes());
            Toast.makeText(fileEdit.this , "saved!" , Toast.LENGTH_LONG).show();
            Intent transferToDashboard = new Intent(fileEdit.this , MainActivity.class);
            startActivity(transferToDashboard);
            fos.close();
        }catch (Exception e){
            Toast.makeText(fileEdit.this , e.getMessage().toString() , Toast.LENGTH_LONG).show();
        }
    }

    public void setContentText(){
        FileInputStream fin = null;
        try{
            fin = openFileInput(FileName);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = null;
            while ((text = br.readLine())!=null){
                sb.append(text).append("\n");
            }
            content.setText(sb.toString());
        }catch (Exception e){
            Toast.makeText(fileEdit.this , "cache fail!!" , Toast.LENGTH_LONG).show();
        }
    }
}
