package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by User on 04-Dec-15.
 */
public class Database {
    private DatabaseController dbControl;
    private Context context;

    public Database(Context context) {
        this.context = context;
    }

    public SQLiteDatabase open() throws SQLException {
        dbControl = new DatabaseController(context);
        SQLiteDatabase database = dbControl.getWritableDatabase();
        return database;
    }

    public void close() {
        dbControl.close();
    }
}
