package com.stxnext.intranet2.sort;

import com.google.common.collect.Ordering;
import com.stxnext.intranet2.backend.model.project.Project;

/**
 * Created by bkosarzycki on 06.11.15.
 */
public class ProjectOrdering extends Ordering<Project> {
    @Override
    public int compare(Project left, Project right) {
        return left.getName().compareTo(right.getName());
    }
}
