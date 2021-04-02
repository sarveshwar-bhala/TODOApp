package com.example.todoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.model.TODO;
import com.example.todoapp.parameters.Params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {
    public MyDbHandler(@Nullable Context context) {
        super(context, Params.DATABASE_NAME, null, Params.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = " CREATE TABLE IF NOT EXISTS " + Params.TABLE_NAME+
                " ( " + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Params.KEY_TASK+" TEXT, "+
                Params.KEY_STATUS + " TEXT " +" ) ";
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+Params.TABLE_NAME);
        onCreate(db);
    }

    public void addTask(TODO todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_TASK,todo.getTask());
        values.put(Params.KEY_STATUS,0);
        db.insert(Params.TABLE_NAME,null,values);
        db.close();

    }

    public List<TODO> getAllTask(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<TODO> taskList = new ArrayList<>();
//        String select = "SELECT * FROM " + Params.TABLE_NAME;
//        Cursor cursor = db.rawQuery(select,null);

        db.beginTransaction();
        try {
            cursor= db.query(Params.TABLE_NAME,null,null,null,null,null,null);
            if (cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        TODO  todo = new TODO();
                        todo.setId(Integer.parseInt(cursor.getString(0)));
                        todo.setTask(cursor.getString(1));
                        todo.setStatus(Integer.parseInt(cursor.getString(2)));
                        taskList.add(todo);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }

    public int updateTask(int id,String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_TASK,task);
        return db.update(Params.TABLE_NAME,values,Params.KEY_ID+"=?",new String[]{String.valueOf(id)});
    }

    public int updateStatus(int id,int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_STATUS,status);
        return db.update(Params.TABLE_NAME,values,Params.KEY_ID+"=?",new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_NAME,Params.KEY_ID+"=?",new String[]{String.valueOf(id)});
    }

}
