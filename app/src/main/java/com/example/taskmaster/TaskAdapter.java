package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> tasks =new ArrayList<Task>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.titleRV);
        }

        public View getTextView() {
            return textView;
        }
    }
    public TaskAdapter(List<Task> list){
        tasks = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( TaskAdapter.ViewHolder holder, int position) {
        String task =   "Title: "+ tasks.get(position).getTitle() + "\n" +
                "Body: "+ tasks.get(position).getBody() + "\n" +
                "Status: " + tasks.get(position).getState();
        holder.textView.setText(task);
//
    }

    @Override
    public int getItemCount() {
        return  tasks.size();
    }
}
