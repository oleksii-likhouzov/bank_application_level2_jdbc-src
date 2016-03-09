package org.test.bankapp.dao;

import org.test.bankapp.model.Account;

import java.util.List;


public interface AccountDAO {
    /**
     * Returns the list of all client accounts
     *
     * @param clientId
     * @return
     */

    List<Account> getAllAccounts(Long clientId);

//    /**
//     * Returns the Active Account
//     *
//     * @param clientId
//     * @return
//     */
//    Account getActiveAccount(Long clientId);


    /**
     * Method should insert new Account (if id==null)
     * <p/>
     * or update account in database
     *
     * @param account
     */

    void save(Account account, Long clientId);


    /**
     * Method removes account from Database
     *
     * @param account
     */

    void remove(Account account);
}
