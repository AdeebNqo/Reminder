package com.adeebnqo.alarmapp.database.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import com.adeebnqo.alarmapp.models.Day;
import com.adeebnqo.alarmapp.models.Event;

/**
 * Created by adeeb on 5/20/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "eventStorage.db";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Event, Integer> eventDao = null;
    private RuntimeExceptionDao<Event, Integer> eventRuntimeDao = null;

    private Dao<Day, Integer> dayDao = null;
    private RuntimeExceptionDao<Day, Integer> dayRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        if (DATABASE_VERSION==1){
            try{
                TableUtils.createTable(connectionSource, Event.class);
                TableUtils.createTable(connectionSource, Day.class);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        //nothing yet
    }


    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Event, Integer> getEventDao() throws SQLException {
        if (eventDao == null) {
            eventDao = getDao(Event.class);
        }
        return eventDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Event, Integer> getEventRuntimeDao() {
        if (eventRuntimeDao == null) {
            eventRuntimeDao = getRuntimeExceptionDao(Event.class);
        }
        return eventRuntimeDao;
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Day, Integer> getDayDao() throws SQLException {
        if (dayDao == null) {
            dayDao = getDao(Day.class);
        }
        return dayDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Day, Integer> getDayRuntimeDao() {
        if (dayRuntimeDao == null) {
            dayRuntimeDao = getRuntimeExceptionDao(Day.class);
        }
        return dayRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();

        eventDao = null;
        eventRuntimeDao = null;

        dayDao = null;
        dayRuntimeDao = null;
    }
}
