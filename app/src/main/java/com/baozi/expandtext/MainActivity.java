package com.baozi.expandtext;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    private String content = "其实在自定义中，在流程上也是差不多的，我们需要重写方法，" +
            "这个方法会在初始化或者数据集更新时回调，在这方法里面，需要其实在自定义中，在流程上也是差不多的，" +
            "我们需要重写方法，这个方法会在初始化或者数据集更新时回调，在这方法里面，需要包子吃猫粮吃很多，不然包子会很饿";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        ExpandRvAdapter adapter=new ExpandRvAdapter(this);
        rv.setAdapter(adapter);
        ArrayList<ExpandBean> list=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ExpandBean data=new ExpandBean();
            data.setId(i);
            if (i==10||i==12||i==19){
                data.setContent("");
            }else {
            data.setContent(i+content);}
            list.add(data);
        }
        adapter.setList(list);
    }
}
