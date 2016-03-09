package org.test.bankapp.util;

import org.test.bankapp.dao.ClientDAO;
import org.test.bankapp.dao.ClientDAOImpl;
import org.test.bankapp.model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DepositCommand implements Command {
    private float depositeValue;

    private void readAccountData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input deposite velue:");
        try {
            depositeValue = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid number Format!");
        }
    }

    public void execute() {
        System.out.println("--------------------------------\n" +
                "- [Active account deposite] \n" +
                "--------------------------------");
        BankCommander.checkCurrentClient();
        BankCommander.checkCurrentBank();
        try {
            readAccountData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BankCommander.currentClient.deposit(depositeValue);
        ClientDAO clientDAO = new ClientDAOImpl();
        BankCommander.currentClient = clientDAO.save(BankCommander.currentClient, BankCommander.currentBank.getId());
     }

    public void printCommandInfo() {
        System.out.println("Deposit Active account");
    }
}