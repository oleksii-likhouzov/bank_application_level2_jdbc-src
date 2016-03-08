package org.test.util;

import org.test.bankapp.model.Account;
import org.test.bankapp.model.Client;

public class GetAccountsCommand implements Command {
    public void execute() {
        System.out.println("--------------------------------\n" +
                "- [Active client account list] - \n" +
                "--------------------------------");
        Client currentClient = BankCommander.currentClient;
        if (currentClient == null) {
            throw new RuntimeException("Active client not defined");
        }
        if (currentClient.getActiveAccount() != null) {
            System.out.println("Active account:");
            currentClient.getActiveAccount().printReport();
        }
        System.out.println("Account list:");
        int i=0;
        for (Account account:currentClient.getAccounts()) {
            System.out.println("Account[" + i + "]:");
            account.printReport();
            i++;
        }
    }

    public void printCommandInfo() {
        System.out.println("Get Client accounts list");
    }
}
