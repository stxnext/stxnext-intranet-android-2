package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.EmployeesAdapter;

public class EmployeesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        RecyclerView recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        EmployeesAdapter employeesAdapter = new EmployeesAdapter();
        recycleView.setAdapter(employeesAdapter);
    }
}
