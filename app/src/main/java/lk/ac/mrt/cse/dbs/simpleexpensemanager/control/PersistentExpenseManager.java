package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseController;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by User on 04-Dec-15.
 */
public class PersistentExpenseManager extends ExpenseManager {
    private Context context = null;
    private DatabaseController dbControler=null;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.context = context;
        dbControler = DatabaseController.getInstance(context);
        setup();
    }
    public void setup () throws ExpenseManagerException {
        TransactionDAO dbMemoryTransaction=new PersistentTransactionDAO(context);

    }
}
