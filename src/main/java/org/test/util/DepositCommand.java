package org.test.util;

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
        Client currentClient = BankCommander.currentClient;
        if (currentClient == null) {
            throw new RuntimeException("Active client not defined");
        }
        try {
            readAccountData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentClient.deposit(depositeValue);
    }

    public void printCommandInfo() {
        System.out.println("Deposit Active account");
    }
}