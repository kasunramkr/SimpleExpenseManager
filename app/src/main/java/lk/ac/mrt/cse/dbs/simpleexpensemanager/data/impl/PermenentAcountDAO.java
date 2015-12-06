package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by User on 04-Dec-15.
 */
public class PermenentAcountDAO implements AccountDAO {

    SQLiteDatabase database=null;
    DatabaseController dbControler=null;

    public PermenentAcountDAO(Context context) {
        if(dbControler==null)
            dbControler=new DatabaseController(context);
        database=dbControler.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor tempCurser = database.query(dbControler.acountTableName, new String[]{dbControler.account_number}, null, null, null, null, null);

        List<String> accounNumbertList=new ArrayList<String>();
        while(tempCurser.moveToNext()){
            accounNumbertList.add(tempCurser.getString(tempCurser.getColumnIndex(dbControler.account_number)));
        }
        return accounNumbertList;    }

    @Override
    public List<Account> getAccountsList() {
        Cursor tempCurser = database.query(dbControler.acountTableName, new String[]{dbControler.account_number}, null, null, null, null, null);

        List<Account> accountList=new ArrayList<Account>();
        while(tempCurser.moveToNext()){
            String accountNo=tempCurser.getString(tempCurser.getColumnIndex(dbControler.account_number));
            String bankName=tempCurser.getString(tempCurser.getColumnIndex(dbControler.bank_name));
            String accHolderName=tempCurser.getString(tempCurser.getColumnIndex(dbControler.acount_owner));
            double balance=tempCurser.getDouble(tempCurser.getColumnIndex(dbControler.balance));
            accountList.add(new Account(accountNo, bankName, accHolderName, balance));
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor accountList = database.query(dbControler.acountTableName,new String[]{dbControler.account_number},"account_number=?",new String[]{accountNo}, null, null, null);
        String bankName=accountList.getString(accountList.getColumnIndex(dbControler.bank_name));
        String accHolderName=accountList.getString(accountList.getColumnIndex(dbControler.acount_owner));
        double balance=accountList.getDouble(accountList.getColumnIndex(dbControler.balance));
        return new Account(accountNo,bankName,accHolderName,balance);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(dbControler.account_number, account.getAccountNo());
        values.put(dbControler.bank_name, account.getBankName());
        values.put(dbControler.acount_owner, account.getAccountHolderName());
        values.put(dbControler.balance, account.getBalance());
        long insertId = database.insert(dbControler.acountTableName, null,values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        database.delete(dbControler.acountTableName, dbControler.account_number + " = " + accountNo, null);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);

        ContentValues values = new ContentValues();
        values.put(dbControler.account_number, account.getAccountNo());
        values.put(dbControler.bank_name, account.getBankName());
        values.put(dbControler.acount_owner, account.getAccountHolderName());

        switch (expenseType) {
            case EXPENSE:
                values.put(dbControler.balance, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(dbControler.balance, account.getBalance() + amount);
                break;
        }
        removeAccount(accountNo);
        long insertId = database.insert(dbControler.acountTableName, null, values);
    }
}
