package org.test.util;


import org.test.bankapp.model.Client;
import org.test.bankapp.service.BankService;
import org.test.bankapp.service.BankServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class FindClientCommand implements Command{
    private String clientName;
    private void readClientData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input client name:");

            clientName = br.readLine();
        if (clientName==null || clientName.length()==0) {
            throw new RuntimeException("Invalid Client name!");
        }
    }

    public void execute() {
        System.out.println("--------------------------------\n" +
                "- [Find client] \n" +
                "--------------------------------");

        try {
            readClientData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BankService bankService = new BankServiceImpl();
        Client tmpClient = bankService.getClient(BankCommander.currentBank, clientName);
        if (tmpClient== null) {
            throw new RuntimeException("Client not found by name!");
        }
        BankCommander.currentClient = tmpClient;
    }

    public void printCommandInfo() {
        System.out.println("Find Client by name");
    }
}
