package com.stxnext.intranet2.backend.model.team;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lukasz on 06.11.2015.
 */
@DatabaseTable(tableName = "client")
public class Client {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField
    private String name;

    public Client () {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
