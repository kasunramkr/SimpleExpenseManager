package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 04-Dec-15.
 */
public class DatabaseController extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "130482F.db";

    public static final String acountTableName="account";
    public static final String transactionTableName="transaction";

    public static final String account_number="account_number";
    public static final String bank_name="bank_name";
    public static final String acount_owner="acount_owner";
    public static final String balance="balance";

    public static final String transaction_account_number="transaction_account_number";
    public static final String date="date";
    public static final String expense_type="expense_type";
    public static final String amount="amount";

    private static final String CreateAccountTable=
            "create table if not exists "+acountTableName+"("
                    + account_number+" String primary key,"
                    + bank_name+" String not null,"
                    + acount_owner+" String not null ,"
                    + balance+" double not null check balance>=0" +
                    ");";

    private static final String CreateTransactionTable=
            "create table if not exists "+transactionTableName+"(" +
                    transaction_account_number+" String primary key,"
                    +date+" String not null,"
                    +expense_type+" String not null check expense_type ='INCOME' or 'EXPENSE',"
                    +amount+" double not null check amount>=0"+
                    ");";

    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateAccountTable);
        db.execSQL(CreateTransactionTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Database Upgrading from "+oldVersion+" to "+newVersion);
        db.execSQL("DROP TABLE IF EXISTS ");
        onCreate(db);
    }
}
