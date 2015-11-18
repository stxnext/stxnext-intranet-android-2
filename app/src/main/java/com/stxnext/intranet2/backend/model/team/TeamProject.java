package com.stxnext.intranet2.backend.model.team;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Łukasz Ciupa on 17.11.2015.
 */
@DatabaseTable(tableName = "teamproject")
public class TeamProject {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(uniqueCombo = true, foreign = true, foreignAutoRefresh = true)
    private Team team;
    @DatabaseField(uniqueCombo = true, foreign = true, foreignAutoRefresh = true)
    private Project project;

    public TeamProject() {}

    public TeamProject(Team team, Project project) {
        this.team = team;
        this.project = project;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
