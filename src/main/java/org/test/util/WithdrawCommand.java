package org.test.util;


import org.test.bankapp.NotEnoughFundsException;
import org.test.bankapp.model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void execute()  {
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
        } catch (NotEnoughFundsException e) {
            throw new RuntimeException(e);
        }

    }

    public void printCommandInfo() {
        System.out.println("Withdraw Active account");
    }
}
