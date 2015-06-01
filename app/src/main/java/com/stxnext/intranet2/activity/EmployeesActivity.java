package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.EmployeesListAdapter;
import com.stxnext.intranet2.backend.api.EmployeesApi;
import com.stxnext.intranet2.backend.api.EmployeesApiImpl;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;

import java.util.List;

public class EmployeesActivity extends AppCompatActivity implements EmployeesApiCallback, EmployeesListAdapter.OnItemClickListener {

    private RecyclerView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        EmployeesApi api = new EmployeesApiImpl(this, this);
        api.requestForEmployees();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onEmployeesListReceived(List<User> employees) {
        recycleView.setAdapter(new EmployeesListAdapter(this, employees, this));
    }

    @Override
    public void onAbsenceEmployeesListReceived(List<Absence> absenceEmployees) {}

    @Override
    public void onItemClick(String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra(ProfileActivity.USER_ID_TAG, userId));
    }
}
