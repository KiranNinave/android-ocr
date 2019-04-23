package com.example.absalvi.newspaperreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String database_name = "ocrfiles.db";
    public static final String table_name = "files";
    public static final String col1 = "id";
    public static final String col2 = "name";
    public static final String col3 = "upload";
    SQLiteDatabase sqLiteDatabase;
    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+table_name+" (id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , upload TEXT)");
        //this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+table_name);
        this.onCreate(sqLiteDatabase);
    }

    public boolean insertDatabese(String value_name , String value_upload){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        //c.put("id" , value_id);
        c.put("name" , value_name);
        c.put("upload" , value_upload);
        long result = sqLiteDatabase.insert(table_name , null , c);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }


    public Cursor getAllData(){
        SQLiteDatabase cur = this.getWritableDatabase();
        Cursor res = cur.rawQuery("select * from "+table_name , null);
        return res;
    }
}
