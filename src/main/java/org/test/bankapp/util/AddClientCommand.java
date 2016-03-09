package org.test.bankapp.util;

import org.test.bankapp.ClientExistsException;
import org.test.bankapp.model.Account;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.Gender;
import org.test.bankapp.service.BankService;
import org.test.bankapp.service.BankServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddClientCommand implements Command {

    Client tmpClient;

    private void readClientData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String clientName;

        System.out.println("-----------------------------------------------\n" +
                "Please, input client data (name, gender ,overdraft, email,phone number):\n" +
                "Client name:");
        clientName = br.readLine();
        if (clientName == null ||
                clientName.length() == 0) {
            throw new RuntimeException("Not a valid client name!");
        }
        System.out.println("Client Gender (Mr,Ms):\n");
        Gender gender;
        gender = Gender.fromString(br.readLine());
        float clientOverdraft;
        System.out.println("Client overdraft value:\n");
        try {
            clientOverdraft = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number Format!");
            clientOverdraft = 0;
        }


        tmpClient = new Client(gender, clientOverdraft);

        String clientPhone;

        System.out.println("Client phone value:\n");
        clientPhone = br.readLine();
        if (clientPhone != null && clientPhone.length() > 0 && !StringUtility.checkIsPhone(clientPhone)) {
            throw new RuntimeException("Not a valid phone!");
        }


        String clientEmail;
        System.out.println("Client email value:\n");
        clientEmail = br.readLine();
        if (clientEmail != null && clientEmail.length() > 0 && !StringUtility.checkIsEmail(clientEmail)) {
            throw new RuntimeException("Not a valid email!");
        }
        String clientSity;
        System.out.println("Client city value:\n");
        clientSity = br.readLine();
        tmpClient.setName(clientName);
        tmpClient.setPhone(clientPhone);
        tmpClient.setEmail(clientEmail);
        tmpClient.setCity(clientSity);
    }

    public void execute() {
        BankCommander.checkCurrentBank();
        try {
            readClientData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BankService bankService = new BankServiceImpl();
        try {

            Account account = tmpClient.createAccount(Client.CLIENT_SAVING_ACCOUNT_TYPE);
            account.setActive(true);
            tmpClient.addAccount(account);
            tmpClient.setActiveAccount(account);
            if (tmpClient.getInitialOverdraft() > 0) {
                Account checkingAccount = tmpClient.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
                tmpClient.addAccount(checkingAccount);
            }
            tmpClient = bankService.addClient(BankCommander.currentBank, tmpClient);
            BankCommander.currentClient = tmpClient;
        } catch (ClientExistsException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void printCommandInfo() {
        System.out.println("Add Client");
    }
}

