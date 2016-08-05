package com.app.votodo.adapter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.votodo.R;
import com.app.votodo.model.Task;

import java.io.IOException;
import java.util.List;

/**
 * Created by anuj on 04/08/16.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private List<Task> taskList;
    public TaskListener taskListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView playButton;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.task);
            playButton =(ImageView) view.findViewById(R.id.play);

        }
    }

    public void setTaskListener(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    public void addTask(Task task){
        this.taskList.add(task);
        this.notifyDataSetChanged();
    }


    public TaskListAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.playButton.setImageResource(R.drawable.pause);
                try {
                    taskListener.playTask(" ",holder.playButton);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public interface TaskListener{
        public void playTask(String path,ImageView v) throws IOException;
    }

}


