package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todoapp.adapter.RecyclerViewAdapter;
import com.example.todoapp.adapter.RecyclerViewTouchHelper;
import com.example.todoapp.data.MyDbHandler;
import com.example.todoapp.model.TODO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner{

    FloatingActionButton addButton;
    RecyclerView recyclerView;
    List<TODO> list;

    MyDbHandler db = new MyDbHandler(MainActivity.this);
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this,db);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TODO todo = new TODO();

        list = new ArrayList<>();

        addButton= findViewById(R.id.addtask);
        recyclerView = findViewById(R.id.recycle);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        list= db.getAllTask();
        Collections.reverse(list);
        adapter.setTask(list);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {

        list= db.getAllTask();
        Collections.reverse(list);
        adapter.setTask(list);
        adapter.notifyDataSetChanged();

    }
}