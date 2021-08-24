package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final List<Task> tasks;
    private final OnTaskItemClickListener listener;

    public TaskAdapter(List<Task> tasks, OnTaskItemClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }


    public interface OnTaskItemClickListener {
        void onItemClicked(int position);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder( @NonNull TaskAdapter.ViewHolder holder, int position) {
        Task item = tasks.get(position);
        holder.title.setText(item.getTitle());
        holder.body.setText(item.getBody());
        holder.status.setText(item.getState());
//
    }

    @Override
    public int getItemCount() {
        return  tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView body;
        private final TextView status;

        ViewHolder(@NonNull View itemView, OnTaskItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title_label);
            body = itemView.findViewById(R.id.body_label);
            status = itemView.findViewById(R.id.status_label);

            itemView.setOnClickListener(v -> listener.onItemClicked(getAdapterPosition()));


        }




    }
}
