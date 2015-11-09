package com.stxnext.intranet2.backend.model.project;

import java.util.List;

/**
 * Created by bkosarzycki on 02.11.15.
 */
public class ProjectResponse {

    private List<Project> projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
