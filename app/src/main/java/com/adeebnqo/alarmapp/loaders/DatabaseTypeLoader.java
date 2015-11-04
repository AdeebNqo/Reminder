package com.adeebnqo.alarmapp.loaders;

import com.adeebnqo.alarmapp.R;
import com.adeebnqo.alarmapp.database.ormlite.OrmLiteDatabase;
import com.adeebnqo.alarmapp.database.sharedpreference.SharedPreferencesDataStoreV2;
import com.adeebnqo.alarmapp.database.sqlite.SQLiteDB;
import com.adeebnqo.alarmapp.database.sharedpreference.SharedPreferencesDataStore;
import com.adeebnqo.alarmapp.interfaces.DataProvider;
import com.adeebnqo.alarmapp.utils.ApplicationData;


public class DatabaseTypeLoader {

    private static DatabaseTypeLoader INSTANCE;
    private String databaseType;

    private DataProvider loadedDatabase = null;

    private DatabaseTypeLoader() {
        databaseType = ApplicationData.getContext().getString(R.string.database_type);
    }

    public static DatabaseTypeLoader getInstance(){
        if (INSTANCE==null) {
            INSTANCE = new DatabaseTypeLoader();
        }
        return INSTANCE;
    }
    public static boolean isNullified(){
        return INSTANCE == null;
    }

    public DataProvider retrieveDatabase(){
        if (databaseType.equals("shared_preferences")){
            loadedDatabase = new SharedPreferencesDataStore();
        }else if (databaseType.equals("sqlite")){
            loadedDatabase = new SQLiteDB();
        }else if (databaseType.equals("ormlite")){
            loadedDatabase = new OrmLiteDatabase();
        }else if (databaseType.equals("shared_preferences_v2")){
            loadedDatabase = new SharedPreferencesDataStoreV2();
        }
        return loadedDatabase;
    }
    public DataProvider getDatabase(){
        if (loadedDatabase==null){
            retrieveDatabase();
        }
        return loadedDatabase;
    }
}
