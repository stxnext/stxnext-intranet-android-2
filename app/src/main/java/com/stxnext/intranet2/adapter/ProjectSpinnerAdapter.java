package com.stxnext.intranet2.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.stxnext.intranet2.backend.model.project.Project;

import java.util.List;

/**
 * Created by bkosarzycki on 03.11.15.
 */
public class ProjectSpinnerAdapter extends ArrayAdapter<String> {

    private List<Project> mProjects;

    public ProjectSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setProjects(List<Project> projectList) {
        mProjects = projectList;
    }

    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public String getItem(int position) {
        return mProjects.get(position).getName();
    }

    public Project getProject(int position) {
        return mProjects.get(position);
    }
}
