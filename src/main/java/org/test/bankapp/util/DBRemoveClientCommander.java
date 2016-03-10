package org.test.bankapp.util;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.ContextLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by stalker on 08.03.16.
 */
public class DBRemoveClientCommander implements Command {
    String userConfirmation;

    private void readConfirmation() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("-----------------------------------------------\n" +
                "Please, confirm action (Y/y)");

        userConfirmation = br.readLine();
        if (userConfirmation == null || userConfirmation.length() == 0
                ) {
            throw new RuntimeException("Invalid congormation!");
        }
    }

    public void execute() throws SQLException {
        System.out.println("--------------------------------\n" +
                "- [Remove current client from DB] \n" +
                "--------------------------------");
        BankCommander.checkCurrentBank();
        BankCommander.checkCurrentClient();
        try {
            readConfirmation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!userConfirmation.toUpperCase().equals("Y")) {
            return;
        }

        try {
            BankCommander.currentBank.removeClient(BankCommander.currentClient);
            ContextLocal.conn.commit();
            BankCommander.currentClient = null;
        } catch (SQLException e) {
            ContextLocal.conn.rollback();
            e.printStackTrace();
        }
    }

    public void printCommandInfo() {
        System.out.println("Remove current client from Bank");
    }
}
