package org.test.bankapp.dao;

import org.test.bankapp.BankInfo;
import org.test.bankapp.model.Bank;

import java.sql.SQLException;

public interface BankDAO {
    /**
     * Finds Bank by its name.
     * <p/>
     * Do not load the list of the clients.
     *
     * @param name
     * @return
     */

    Bank getBankByName(String name) throws SQLException;
    /**

     * Should fill the BankInfo

     */

    BankInfo getBankInfo(Bank bank) throws SQLException;
}
