package com.example.checkme.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.checkme.AddNewTask;
import com.example.checkme.MainActivity;
import com.example.checkme.Model.CheckMeModel;
import com.example.checkme.R;
import com.example.checkme.Utils.DatabaseHandler;

import java.util.List;

public class CheckMeAdapter extends RecyclerView.Adapter<CheckMeAdapter.ViewHolder>{
    private List<CheckMeModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public CheckMeAdapter(DatabaseHandler db,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        CheckMeModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setTasks(List<CheckMeModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void editItem(int position){
        CheckMeModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
