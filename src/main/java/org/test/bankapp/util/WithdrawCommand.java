package org.test.bankapp.util;


import org.test.bankapp.NotEnoughFundsException;
import org.test.bankapp.dao.ClientDAO;
import org.test.bankapp.dao.ClientDAOImpl;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class WithdrawCommand implements Command {
    private float withdrawValue;

    private void readAccountData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input withdraw velue:");
        try {
            withdrawValue = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid number Format!");
        }
    }

    public void execute() throws SQLException{
        System.out.println("--------------------------------\n" +
                "- [Active account withdraw] \n" +
                "--------------------------------");
        Client currentClient = BankCommander.currentClient;
        if (currentClient == null) {
            throw new RuntimeException("Active client not defined");
        }
        try {
            readAccountData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            currentClient.withdraw(withdrawValue);
            ClientDAO clientDAO = new ClientDAOImpl();
            try {
                BankCommander.currentClient = clientDAO.save(BankCommander.currentClient, BankCommander.currentBank.getId());
                ContextLocal.conn.commit();
            } catch (SQLException e) {
                ContextLocal.conn.rollback();
                e.printStackTrace();
            }
        } catch (NotEnoughFundsException e) {
            throw new RuntimeException(e);
        }

    }

    public void printCommandInfo() {
        System.out.println("Withdraw Active account");
    }
}

