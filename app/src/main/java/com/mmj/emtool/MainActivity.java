package com.mmj.emtool;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefresh;
    private DrawerLayout mDrawerLayout;
    static Handler hander;
    static int UPDATE_T = 1;
    static int UPDATE_H = 2;
    static int UPDATE_V = 3;
    static int UPDATE_P = 4;

    private EmMessage[] emMessages = {
            new EmMessage(" 温度         ","0",R.drawable.blue),
            new EmMessage(" 湿度%      ","0%",R.drawable.red),
            new EmMessage(" PM2.5      ","0",R.drawable.yellow),
            new EmMessage(" VOCmg/L","0%",R.drawable.green)
                                      };
    private List<EmMessage> emMessageList = new ArrayList<>();
    private EmMessageAdapter adapter;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent startIntent = new Intent(this,MyService.class);
        startService(startIntent);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);//每行显示条数
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EmMessageAdapter(emMessageList);
        recyclerView.setAdapter(adapter);
        initMsg();

        hander = new Handler(){
            public void handleMessage(Message msg){
                switch(msg.what){
                    case  1:
                        emMessages[0].setMsg(MyService.valueT);
                        adapter.notifyDataSetChanged();
                        break;
                    case  3:
                        emMessages[3].setMsg(MyService.valueV);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshMsg();
            }
        });

    }

    private void initMsg()
    {
        emMessageList.clear();
        for(int i =0;i<4;i++)
        {
            emMessageList.add(emMessages[i]);
        }
    }

    private void refreshMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emMessages[0].setMsg(MyService.valueT);
//                        emMessages[1].setMsg(MyService.valuebuff);
//                        emMessages[2].setMsg(MyService.valuebuff);
                        emMessages[3].setMsg(MyService.valueV);
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}
