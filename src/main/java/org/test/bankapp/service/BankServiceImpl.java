package org.test.bankapp.service;

import org.test.bankapp.ClientExistsException;
import org.test.bankapp.model.Account;
import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BankServiceImpl implements BankService {

    public void addClient(Bank bank, Client client) throws ClientExistsException {

            bank.addClient(client);
    }

    public void removeClient(Bank bank, Client client) {

        Set<Client> clients = bank.getClients();
        Iterator iter = bank.getClients().iterator();
        while (iter.hasNext()) {
            Client tempClient = (Client) iter.next();
            if (tempClient.getName().equals(client.getName())) {
                iter.remove();
                break;
            }
        }
    }

    public void addAccount(Client client, Account account) {
        client.addAccount(account);
    }

    public void setActiveAccount(Client client, Account account) {
        client.setActiveAccount(account);
    }

    public Client findClientByName(Bank bank, String clientName) {
        for(Client client:bank.getClients()) {
            if (clientName.equals(client.getName())) {
                return client;
            }
        }
        return null;
    }

    public  Client getClient(Bank bank, String clientName) {
        return bank.getClientCache().get(clientName);
    }
}
