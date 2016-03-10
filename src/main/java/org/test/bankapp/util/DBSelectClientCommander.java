package org.test.bankapp.util;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by stalker on 08.03.16.
 */
public class DBSelectClientCommander implements Command {
    private String clientName;


    private void readClientData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, input client name:");
        clientName = br.readLine();
        if (clientName == null || clientName.length() == 0) {
            throw new RuntimeException("Invalid client name!");
        }
    }

    public void execute() throws SQLException {
        System.out.println("--------------------------------\n" +
                "- [Find client] \n" +
                "--------------------------------");
        BankCommander.checkCurrentBank();
        try {
            readClientData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Client tmpClient = null;
        try {
            tmpClient = Bank.getClientByName(BankCommander.currentBank, clientName);
            ContextLocal.conn.commit();
        } catch (SQLException e) {
            ContextLocal.conn.rollback();
            e.printStackTrace();
            return;
        }
        if (tmpClient == null) {
            throw new RuntimeException("Client not found by name!");
        }
        BankCommander.currentClient = tmpClient;
    }

    public void printCommandInfo() {
        System.out.println("Select client by name from DB");
    }
}
