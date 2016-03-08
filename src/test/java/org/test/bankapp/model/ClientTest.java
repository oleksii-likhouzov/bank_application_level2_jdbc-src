package org.test.bankapp.model;

import org.junit.*;
import static org.junit.Assert.*;

public class ClientTest
{
    @Test
    public void testClientEquals() {
        Client client = new Client(Gender.FEMALE);
        client.getActiveAccount().deposit(10);
        client.addAccount(client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        client.addAccount(client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        AbstractAccount account = (AbstractAccount)client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
        account.deposit(20);
        client.addAccount(account);
        Client clientEqual = new Client(Gender.FEMALE);

        clientEqual.getActiveAccount().deposit(10);
        clientEqual.addAccount(clientEqual.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        clientEqual.addAccount(clientEqual.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        account = (AbstractAccount)clientEqual.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
        account.deposit(20);
        clientEqual.addAccount(account);
        assertEquals("Equal not valid for equals clients (gender)", client, clientEqual);

        client.printReport();

    }
    @Test
    public void testHashCode() {
        Client client = new Client(Gender.FEMALE);
        client.getActiveAccount().deposit(10);
        client.addAccount(client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        client.addAccount(client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        AbstractAccount account = (AbstractAccount)client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
        account.deposit(20);
        client.addAccount(account);

        System.out.println(client.hashCode());
    }
    @Test
    public void testToString() {
        Client client = new Client(Gender.FEMALE);
        client.getActiveAccount().deposit(10);
        client.addAccount(client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE));
        client.addAccount(client.createAccount(Client.CLIENT_SAVING_ACCOUNT_TYPE));
        AbstractAccount account = (AbstractAccount)client.createAccount(Client.CLIENT_CHECKING_ACCOUNT_TYPE);
        account.deposit(20);
        client.addAccount(account);

        System.out.println(client);
    }
}
