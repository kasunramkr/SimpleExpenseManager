package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 04-Dec-15.
 */
public class PermenentTransactionDAO implements TransactionDAO {
    SQLiteDatabase database=null;
    DatabaseController dbControler=null;

    public PermenentTransactionDAO(Context context) {
        if(dbControler==null)
            dbControler=new DatabaseController(context);
        database=dbControler.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues values = new ContentValues();
        values.put(dbControler.transaction_account_number, accountNo);
        values.put(DatabaseController.date, date.toString());
        values.put(dbControler.expense_type, expenseType.toString());
        values.put(dbControler.amount, amount);
        long insertId = database.insert(dbControler.transactionTableName, null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor tempCurser = database.query(dbControler.transactionTableName, new String[]{dbControler.account_number}, null, null, null, null, null);

        List<Transaction> TransactionList=new ArrayList<Transaction>();
        while(tempCurser.moveToNext()){
            String transaction_account_number=tempCurser.getString(tempCurser.getColumnIndex(dbControler.transaction_account_number));
            String date=tempCurser.getString(tempCurser.getColumnIndex(dbControler.date));
            String expense_type=tempCurser.getString(tempCurser.getColumnIndex(dbControler.expense_type));
            double amount=tempCurser.getDouble(tempCurser.getColumnIndex(dbControler.amount));
            TransactionList.add(new Transaction(dateConverteMethod(date),transaction_account_number, ExpenseType.valueOf(expense_type), amount));
        }
        tempCurser.close();
        database.close();
        return TransactionList;

    }

    private Date dateConverteMethod(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        String query = "Select * FROM " + dbControler.transactionTableName + " ORDER BY " + dbControler.date + " LIMIT " + limit ;
        Cursor tempCurser = database.rawQuery(query, null);

        //create list for Transaction objects
        List<Transaction> transactionsList =  new LinkedList<>();

        if (tempCurser.moveToFirst()) {
            do{
                Transaction transaction = null;
                //do proper formatting for the date
                String date=tempCurser.getString(tempCurser.getColumnIndex(dbControler.date));
                String transaction_account_number=tempCurser.getString(tempCurser.getColumnIndex(dbControler.transaction_account_number));
                String expense_type=tempCurser.getString(tempCurser.getColumnIndex(dbControler.expense_type));
                double amount=tempCurser.getDouble(tempCurser.getColumnIndex(dbControler.amount));
                transactionsList.add(new Transaction(dateConverteMethod(date),transaction_account_number, ExpenseType.valueOf(expense_type), amount));


                transactionsList.add(transaction);
            }while (tempCurser.moveToNext());
        }
        tempCurser.close();
        database.close();
        return transactionsList;

    }
}
