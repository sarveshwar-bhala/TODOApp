package com.example.todoapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.data.MyDbHandler;
import com.example.todoapp.model.TODO;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<TODO> list;
    private MainActivity activity;
    private MyDbHandler db;

    public RecyclerViewAdapter(MainActivity activity, MyDbHandler db) {
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TODO todo = list.get(position);
        holder.checkBox.setText(todo.getTask());
        holder.checkBox.setChecked(toBoolean(todo.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(todo.getId(),1);
                }else{
                    db.updateStatus(todo.getId(),0);
                }
            }
        });
    }

    public boolean toBoolean(int num){
        return  num!=0;
    }

    public Context getContext(){
        return activity;
    }
    public void setTask(List<TODO> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        TODO todo = list.get(position);
        db.deleteTask(todo.getId());
        list.remove(position);
        notifyItemChanged(position);
    }

    public void editItem(int position){
     TODO todo = list.get(position);
     Bundle bundle = new Bundle();
     bundle.putInt("id",todo.getId());
     bundle.putString("task",todo.getTask());

     AddNewTask task = new AddNewTask();
     task.setArguments(bundle);
     task.show(activity.getSupportFragmentManager(),task.getTag());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox= itemView.findViewById(R.id.checkbox);
        }
    }
}