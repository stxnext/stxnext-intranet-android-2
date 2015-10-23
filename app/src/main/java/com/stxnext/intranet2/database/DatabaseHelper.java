package com.stxnext.intranet2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.stxnext.intranet2.backend.model.impl.User;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static String TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "stxIntranet.db";
    private static final int DATABASE_VERSION = 3;

    private Dao<User, Integer> simpleDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.d(TAG, "onCreate ==== Database created from scratch ====");
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)  {
            try {
                if (oldVersion == 1 && newVersion == 2) {
                    getUserDao().executeRaw("ALTER TABLE 'user' ADD COLUMN isClient BOOLEAN DEFAULT 0;");
                    Log.i(TAG, "==== Database upgraded from v1 to v2 ====");
                }
                db.execSQL("DROP TABLE IF EXISTS " + "user");
                onCreate(db, connectionSource);
            } catch (SQLException exc) {
                Log.e(TAG, "Exception on database upgrade: " + exc.toString());
            }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<User, Integer> getUserDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(User.class);
        }
        return simpleDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
    }
}