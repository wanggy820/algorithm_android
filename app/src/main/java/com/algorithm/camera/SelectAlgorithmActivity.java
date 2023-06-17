package com.algorithm.camera;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;


public class SelectAlgorithmActivity extends AppCompatActivity {
    private static final String TAG = "SelectAlgorithmActivity";
    ListView listView;

    private MyAdapter myAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_algorithm);


        listView = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter(ImageUtils.list,this);
        listView.setAdapter(myAdapter);
    }
}
