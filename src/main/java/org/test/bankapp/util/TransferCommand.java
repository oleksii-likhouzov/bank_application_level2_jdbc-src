package org.test.bankapp.util;

import org.test.bankapp.NotEnoughFundsException;
import org.test.bankapp.dao.ClientDAO;
import org.test.bankapp.dao.ClientDAOImpl;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;
import org.test.bankapp.service.BankService;
import org.test.bankapp.service.BankServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class TransferCommand implements Command {
    private float transferValue;
    private String clientName;

    private void readAccountData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input transfer velue:");
        try {
            transferValue = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid number Format!");
        }
    }

    private void readClientData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input client name:");

        clientName = br.readLine();
        if (clientName == null || clientName.length() == 0) {
            throw new RuntimeException("Invalid Client name!");
        }
    }

    public void execute()throws SQLException {
        if (BankCommander.currentClient == null) {
            throw new RuntimeException("Active client not defined");
        }
        if (BankCommander.currentClient.getActiveAccount() == null) {
            throw new RuntimeException("Active account for active client not defined");
        }
        try {
            readAccountData();
            readClientData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BankService bankService = new BankServiceImpl();
        Client tmpClient = bankService.findClientByName(BankCommander.currentBank, clientName);
        if (tmpClient == null) {
            throw new RuntimeException("Client not found by name!");
        }

        try {
            try {
                BankCommander.currentClient.withdraw(transferValue);
                tmpClient.deposit(transferValue);
            } catch (NotEnoughFundsException e) {
                throw new RuntimeException(e);
            }
            ClientDAO clientDAO = new ClientDAOImpl();
            clientDAO.save(tmpClient, BankCommander.currentBank.getId());
            BankCommander.currentClient = clientDAO.save(BankCommander.currentClient, BankCommander.currentBank.getId());
            ContextLocal.conn.commit();
        } catch (Exception e) {
            ContextLocal.conn.rollback();
            // Надо перечитать клиентов текущего и изменяемого
            //BankCommander.currentClient =
        }
    }

    public void printCommandInfo() {
        System.out.println("Transfer command");
    }
}
