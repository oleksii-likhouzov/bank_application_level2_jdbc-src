package org.test.bankapp;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.bankapp.model.*;
import org.test.bankapp.service.BankService;
import org.test.bankapp.service.BankServiceImpl;
import org.test.util.BankCommander;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BankApplication {
    private static final Logger log = LogManager.getLogger(BankApplication.class);
    private Bank bank;


    private void addClient(String clientName, Gender gender) {
        BankService bankService = new BankServiceImpl();

        Client client = new Client(gender);
        client.setName(clientName);
        try {
        bankService.addClient(bank, client);
        } catch (ClientExistsException exception) {
            log.log(Level.ERROR, "Duplicate client name: \"" + client.getName() + "\"");
        }
    }

    private void addClient(String clientName, float initialOverdraft, Gender gender) {
        BankService bankService = new BankServiceImpl();

        Client client = new Client(gender, initialOverdraft);
        client.setName(clientName);
        try {
            bankService.addClient(bank, client);
        } catch (ClientExistsException exception) {
            log.log(Level.ERROR, "Duplicate client name: \"" + client.getName() + "\"");
        }
    }

    public void initialize() {
        BankService bankService = new BankServiceImpl();
        // findClientByName
        bank = new Bank();
        addClient("First Client", Gender.FEMALE);
        addClient("Second Client", 100, Gender.FEMALE);
        addClient("Third Client", 0, Gender.MALE);
        addClient("Third Client", 1, Gender.MALE);
        addClient("For delete", 200, Gender.FEMALE);
        addClient("Five Client", 78, Gender.MALE);
        Set<Client> clients = bank.getClients();
        Client tmpClient =  bankService.findClientByName(bank, "Second Client");
        tmpClient.setCity("Monreal");


        tmpClient =  bankService.findClientByName(bank, "Third Client");
        tmpClient.setCity("Monreal");
        bankService.addAccount(tmpClient, tmpClient.createAccount(Client.CLIENT_SAVING_ACCOUNT_TYPE));
        bankService.addAccount(tmpClient, tmpClient.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));

        Account tempAccount = tmpClient.createAccount(Client.CLIENT_SAVING_ACCOUNT_TYPE);
        bankService.addAccount(tmpClient, tempAccount);

        bankService.setActiveAccount(tmpClient, tempAccount);
        tmpClient =  bankService.findClientByName(bank, "For delete");
        bankService.removeClient(bank, tmpClient);
        for (Client client : clients) {
            Set<Account> accounts = client.getAccounts();
            for (Account account : accounts) {
                account.deposit(((int) (Math.random() * 100 * 100)) / 100.f);
            }
            for (Account account : accounts) {
                if (account != null &&
                        account instanceof CheckingAccount) {
                    try {
                        account.withdraft(
                                2 * account.getBalance() + (int) (((CheckingAccount) account).getOverdraft()));
                    } catch (OverDraftLimitExceededException exception) {
                        log.log(Level.WARN,
                                "Withdraft with value " +
                                        exception.getWithdraft() +
                                        "  for account is not possible. Account balance: " +
                                        exception.getBalance() +
                                        ". Account overdraft:" + exception.getOverdraft());

                        try {
                            account.withdraft(
                                    account.getBalance() + (int) (((CheckingAccount) account).getOverdraft() / 2));
                        } catch (NotEnoughFundsException innerException) {
                            log.log(Level.ERROR, exception);
                        }
                    } catch (NotEnoughFundsException exception) {
                        try {
                            account.withdraft(
                                    account.getBalance() + (int) (((CheckingAccount) account).getOverdraft() / 2));
                        } catch (NotEnoughFundsException innerException) {
                            log.log(Level.ERROR, exception);
                        }

                    }
                }

            }
        }
    }

    // оторый изменяет значение баланса (методы deposit() и withdraw()) для некоторых счетов клиентов банка.
    public void modifyBank() {
        if (bank != null) {
            Set <Client> clients = bank.getClients();
            for (Client client:clients) {
                Account clientActiveAccount = client.getActiveAccount();
                float clientBalance = clientActiveAccount.getBalance();
                if (clientBalance > 0) {
                    try {
                        clientActiveAccount.withdraft(Math.round(clientBalance / 2.f * 100) / 100);
                    } catch (NotEnoughFundsException exception) {
                        log.log(Level.WARN,
                                "Withdraft with value " +
                                        Math.round((clientBalance / 2.f * 100) / 100) +
                                        "  for account is not possible. Account balance: " +
                                        clientBalance);
                    }
                } else {
                    clientActiveAccount.deposit(-Math.round(clientBalance / 2.f * 100) / 100);
                }
            }
        }
    }

    public void printBankReport() {
        if (bank != null) {
            bank.printReport();
        } else {
            log.log(Level.ERROR, "Bank application is not initialized.");
            throw new RuntimeException("Bank application is not initialized.");
        }
    }

    public static void main(String[] argv) throws IOException {


        if (Arrays.asList(argv).contains("-report")) {
            System.out.println("Demo mode:");
            BankApplication bankApplication = new BankApplication();
            bankApplication.initialize();
            bankApplication.printBankReport();
            bankApplication.modifyBank();
            bankApplication.printBankReport();
        } else {
            BankCommander.runCommander();
        }

    }
}
