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
}
