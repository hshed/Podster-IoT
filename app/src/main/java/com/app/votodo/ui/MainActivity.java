package com.app.votodo.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.votodo.R;
import com.app.votodo.adapter.TaskListAdapter;
import com.app.votodo.helper.TaskHelper;
import com.app.votodo.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.TaskListener {

    TaskListAdapter taskListAdapter;
    RecyclerView recyclerView;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        mMediaPlayer = new MediaPlayer();
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Syncing with Server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        syncWithServer();
                    }
                }, 2000);
            }
        });
        setupAdapter();
    }

    public void setupAdapter()
    {
        List<Task> taskList=new ArrayList<>();

        TaskHelper.getTaskList("admin_user", new Response.Listener<Task>() {
            @Override
            public void onResponse(Task response) {
                taskListAdapter.addTask(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Task List",error.getMessage());
            }
        },this);

        taskListAdapter=new TaskListAdapter(taskList);
        taskListAdapter.setTaskListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(taskListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void playTask(String path , final ImageView v) throws IOException {
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            v.setImageResource(R.drawable.play);
        }
        else {
            mMediaPlayer = MediaPlayer.create(this,R.raw.alarm);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
            v.setImageResource(R.drawable.pause);
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                v.setImageResource(R.drawable.play);
            }
        });
    }

    public  void syncWithServer(){
        List<Task> taskList=new ArrayList<>();

        TaskHelper.getTaskList("admin_user", new Response.Listener<Task>() {
            @Override
            public void onResponse(Task response) {
                taskListAdapter.addTask(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Task List",error.getMessage());
            }
        },this);
    }
}
