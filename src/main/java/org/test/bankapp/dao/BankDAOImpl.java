package org.test.bankapp.dao;

import java.sql.*;

import org.test.bankapp.BankInfo;
import org.test.bankapp.BankReport;
import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;

public class BankDAOImpl implements BankDAO {


    public Bank getBankByName(String name) {
        Bank tmpBank = null;

        try {
            Statement stmt = ContextLocal.conn.createStatement();
            String sql = "SELECT id FROM t_bank  \n" +
                    " Where name = '" + name + "'";
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery(sql);
            // Iterate over results and print it
            if (rs.next()) {
                // Retrieve by column name
                tmpBank = new Bank(name);
                tmpBank.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            tmpBank = null;
        }
        return tmpBank;
    }


    public BankInfo getBankInfo(Bank bank) {
        BankInfo bankInfo = new BankInfo();
        float bankBalance = 0.f;
        for (Client client : bank.getClients()) {
            bankBalance += client.getBalance();
        }
        bankInfo.setTotalAccountSum(bankBalance);
        bankInfo.setNumberOfClients(bank.getClients().size());
        bankInfo.setClientsByCity(BankReport.getClientsByCity(bank));
        return bankInfo;
    }
}
