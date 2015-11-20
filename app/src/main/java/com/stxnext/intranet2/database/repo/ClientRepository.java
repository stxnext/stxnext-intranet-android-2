package com.stxnext.intranet2.database.repo;

import com.j256.ormlite.dao.Dao;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.database.DatabaseHelper;

import java.sql.SQLException;

/**
 * Created by Łukasz Ciupa on 20.11.2015.
 */
public class ClientRepository {

    private Dao<Client, Long> clientDao;

    public ClientRepository(DatabaseHelper databaseHelper) {
        try {
            clientDao = databaseHelper.getClientDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdate(Client client) {
        try {
            clientDao.createOrUpdate(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
