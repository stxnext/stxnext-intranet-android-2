package com.stxnext.intranet2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.EmployeesListAdapter;
import com.stxnext.intranet2.backend.api.EmployeesApi;
import com.stxnext.intranet2.backend.api.EmployeesApiImpl;
import com.stxnext.intranet2.backend.callback.EmployeesApiCallback;
import com.stxnext.intranet2.backend.model.Absence;
import com.stxnext.intranet2.backend.model.User;

import java.util.List;
import java.util.Set;

public class EmployeesActivity extends AppCompatActivity implements EmployeesApiCallback, EmployeesListAdapter.OnItemClickListener {

    private RecyclerView recycleView;
    private Toolbar toolbar;
    private View searchContainer;
    private EditText searchEditText;
    private EmployeesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchContainer = findViewById(R.id.search_container);
        searchEditText = (EditText) findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        recycleView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        EmployeesApi api = new EmployeesApiImpl(this, this);
        api.requestForEmployees();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.employees_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            toggleSearch();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onEmployeesListReceived(List<User> employees) {
        adapter = new EmployeesListAdapter(this, employees, this);
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onAbsenceEmployeesListReceived(Set<Absence> absenceEmployees) {
    }

    @Override
    public void onItemClick(String userId) {
        startActivity(new Intent(this, ProfileActivity.class).putExtra(ProfileActivity.USER_ID_TAG, userId));
    }

    @Override
    public void onBackPressed() {
        if (toggleSearch()) {
            return;
        }

        super.onBackPressed();
    }

    private boolean toggleSearch() {
        if (searchContainer.getVisibility() == View.GONE) {
            toolbar.animate().alpha(0f).setDuration(400);
            searchContainer.setVisibility(View.VISIBLE);
            searchContainer.setAlpha(0);
            searchContainer.animate().alpha(1f).setDuration(300);
            searchEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            toolbar.animate().alpha(1f).setDuration(300);
            searchContainer.animate().alpha(0f).setDuration(400).withEndAction(new Runnable() {
                @Override
                public void run() {
                    searchContainer.setVisibility(View.GONE);
                    searchEditText.setText("");
                }
            });

            return true;
        }
    }
}
