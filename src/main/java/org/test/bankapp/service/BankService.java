package org.test.bankapp.service;

import org.test.bankapp.ClientExistsException;
import org.test.bankapp.model.Account;
import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;

import java.sql.SQLException;

/**
 * Created by alykhouzov on 03.03.2016.
 * который будет осуществлять добавление клиента и прочие сервисные операции
 */
public interface BankService {
    Client addClient(Bank bank, Client client) throws ClientExistsException, SQLException;
    void removeClient(Bank bank,Client client);
    void addAccount(Client client, Account account);
    void setActiveAccount(Client client, Account account);
    Client findClientByName(Bank bank, String clientName);
    Client getClient(Bank bank, String clientName);
}
