package org.test.bankapp.dao;

import org.test.bankapp.NotEnoughFundsException;
import org.test.bankapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAOImpl implements AccountDAO {
    public Long findAccountByCode(String code) throws SQLException {
        Long result = null;
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "SELECT RC.ID\n" +
                            "   FROM  REF_ACCOUNT_TYPE RC \n" +
                            " WHERE RC.CODE= ? ");

            stmt.setString(1, code);
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getLong("id");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

    public List<Account> getAllAccounts(Long clientId) throws SQLException {
        List<Account> accounts = new ArrayList<Account>();
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "select  a.id, \n" +
                            "             a.is_primary,\n" +
                            "             a.balance,\n" +
                            "             a.overdraft,\n" +
                            "             at1.code\n" +
                            "  from t_account a\n" +
                            "  join ref_account_type at1 on at1.id = a.account_type_id\n" +
                            "where client_id = ?");
            stmt.setLong(1, clientId.longValue());
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            // Iterate over results and print it
            while (rs.next()) {
                Long id = rs.getLong("id");
                String accountType = rs.getString("code");
                BigDecimal balance = rs.getBigDecimal("balance");
                Boolean isPrimary = rs.getBoolean("is_primary");
                BigDecimal overdraft = rs.getBigDecimal("overdraft");
                if (accountType.equals(Client.CLIENT_SAVING_ACCOUNT_TYPE)) {
                    SavingAccount tempAccount = new SavingAccount();
                    tempAccount.setId(id);
                    tempAccount.setActive(isPrimary);
                    tempAccount.deposit(balance.floatValue());
                    accounts.add(tempAccount);
                }
                if (accountType.equals(Client.CLIENT_CHECKING_ACCOUNT_TYPE)) {
                    CheckingAccount tempAccount = new CheckingAccount(overdraft.floatValue());
                    tempAccount.setId(id);
                    tempAccount.setActive(isPrimary);
                    if (balance.floatValue() < 0.f) {
                        tempAccount.withdraft(-balance.floatValue());
                    } else {
                        tempAccount.deposit(balance.floatValue());
                    }
                    accounts.add(tempAccount);
                }
            }
        } catch (NotEnoughFundsException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return accounts;
    }

    public void save(Account account, Long clientId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            Float overdraft = null;
            Long accountTypeId = null;
            if (account.getId() == null) {
                stmt = ContextLocal.conn.prepareStatement(
                        "INSERT into t_account (" +
                                "client_id," +
                                "account_type_id, " +
                                "overdraft, " +
                                "balance, " +
                                "is_primary ) \n" +
                                "values(?,?,?,?,?)");
                if (account instanceof CheckingAccount) {
                    overdraft = ((CheckingAccount) account).getOverdraft();
                    accountTypeId = findAccountByCode(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
                } else {
                    accountTypeId = findAccountByCode(Client.CLIENT_SAVING_ACCOUNT_TYPE);
                }
                stmt.setLong(1, clientId);
                stmt.setLong(2, accountTypeId);
                stmt.setBigDecimal(3, new BigDecimal(overdraft != null ? overdraft : 0));
                stmt.setBigDecimal(4, new BigDecimal(account.getBalance()));
                stmt.setBoolean(5, account.isActive());
            } else {
                stmt = ContextLocal.conn.prepareStatement(
                        "UPDATE t_account set " +
                                "overdraft =?, " +
                                "balance= ?, " +
                                "is_primary =?\n" +
                                "where id =? ");
                stmt.setBigDecimal(1, new BigDecimal(overdraft != null ? overdraft : 0));
                stmt.setBigDecimal(2, new BigDecimal(account.getBalance()));
                stmt.setBoolean(3, account.isActive());
                stmt.setLong(4, account.getId());
            }
            // 2) Execute delete operation
            int rowCount = stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void remove(Account account) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "DELETE  from t_account  where id = ? ");

            stmt.setLong(1, account.getId());
            // 2) Execute delete operation
            int rowCount = stmt.executeUpdate();
            ContextLocal.conn.commit();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}