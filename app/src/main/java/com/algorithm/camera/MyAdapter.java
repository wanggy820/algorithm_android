package com.algorithm.camera;

import static com.algorithm.camera.ImageUtils.SELECT_ALGORRITHM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Deflater;

public class MyAdapter extends BaseAdapter {
    private static final String TAG = "MyAdapter";
    private List<AlgorithmBean> data;
    private Activity activity;

    public MyAdapter(List<AlgorithmBean> data,Activity activity) {
        this.data = data;
        this.activity = activity;
    }
    //item的数量
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //item的视图
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view ==null) {
            view = LayoutInflater.from(this.activity).inflate(R.layout.algorithm_item, viewGroup, false);
        }
        AlgorithmBean bean = data.get(i);
        TextView name = view.findViewById(R.id.name);
        name.setText(bean.getName());

        Button confirm_btn = view.findViewById(R.id.confirm_button);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setProgress((int)(bean.getParams()*255.f));
        if (bean.isHasParams()) {
            seekBar.setVisibility(View.VISIBLE);
        } else  {
            seekBar.setVisibility(View.INVISIBLE);
        }

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setParams(seekBar.getProgress()/255.0f);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("algorithm", bean);
                intent.putExtras(bundle);
                MyAdapter.this.activity.setResult(SELECT_ALGORRITHM, intent);
                MyAdapter.this.activity.finish();
            }
        });
        return view;
    }
}

