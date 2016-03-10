package org.test.bankapp.util;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.ContextLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
public class DBSelectBankCommander implements Command {
    private String bankName;

    private void readBankData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input bank name:");
        bankName = br.readLine();
        if (bankName == null || bankName.length() == 0) {
            throw new RuntimeException("Invalid Bank name!");
        }
    }

    public void execute() throws SQLException {
        System.out.println("--------------------------------\n" +
                "- [Find bank] \n" +
                "--------------------------------");
        try {
            readBankData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bank tmpBank = null;
        try {
            tmpBank = Bank.getBankByName(bankName);
            ContextLocal.conn.commit();
        } catch (SQLException e) {
            ContextLocal.conn.rollback();
            e.printStackTrace();
            return;
        }
        if (tmpBank == null) {
            throw new RuntimeException("Bank not found by name!");
        }
        if (!tmpBank.equals(BankCommander.currentBank)) {
            BankCommander.currentClient = null;
        }
        BankCommander.currentBank = tmpBank;
    }

    public void printCommandInfo() {
        System.out.println("Find Bank by name");
    }
}
