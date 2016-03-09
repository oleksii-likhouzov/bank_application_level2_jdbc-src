package org.test.bankapp.model;

import org.test.bankapp.*;
import org.test.bankapp.dao.BankDAO;
import org.test.bankapp.dao.BankDAOImpl;
import org.test.bankapp.dao.ClientDAO;
import org.test.bankapp.dao.ClientDAOImpl;

import java.util.*;

public class Bank implements Report {
    private Long id;
    private String name;

    private Set<Client> clients = new HashSet<Client>();
    private List<ClientRegistrationListener> listeners = new ArrayList<ClientRegistrationListener>();
    private Map<String, Client> clientCache = new TreeMap<String, Client>();


    public class PrintClientListener implements ClientRegistrationListener {
        public void onClientAdded(Client client) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(" Client registration report ");
            System.out.println("  Client name       : " + client.getName());
            System.out.format("  Client overdraft  : %.2f\n", client.getInitialOverdraft());
            System.out.format("  Client balance    : %.2f\n", client.getBalance());
            if (client.getActiveAccount() != null) {
                System.out.println("  Active account    :");
                client.getActiveAccount().printReport();
            }
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //1    clientCache.put(client.getName(), client);

        }
    }

    public class EmailNotificationListener implements ClientRegistrationListener {
        public void onClientAdded(Client client) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("Notification email for client \"" + client.getName() + "\"… to be sent");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }

    }

    public void registerEvent(ClientRegistrationListener listener) {
        listeners.add(listener);
    }

    public Bank() {
        registerEvent(new PrintClientListener());
        registerEvent(new EmailNotificationListener());
    }

    public Bank(String name) {
        setName(name);
        registerEvent(new PrintClientListener());
        registerEvent(new EmailNotificationListener());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void checkDuplicateName(Client client) throws ClientExistsException {
        for (Client tmpClient : clients) {
            if (tmpClient.getName().equals(client.getName())) {
                throw new ClientExistsException();
            }
        }
    }

    public Client addClient(Client client) throws ClientExistsException {
        boolean isNew = client.getId()== null;
        if(isNew) {
            checkDuplicateName(client);
            ClientDAO clientDao = new ClientDAOImpl();
            client = clientDao.save(client, id);
        }
        clients.add(client);
        if(isNew) {

            for (ClientRegistrationListener listener : listeners) {
                listener.onClientAdded(client);
            }
        }
        clientCache.put(client.getName(), client);
        return client;
    }

    public Set<Client> getClients() {
        return clients;

    }

    public Map<String, Client> getClientCache() {
        return clientCache;
    }

    public void printReport() {

        float bankBalance = 0.f;
        for (Client client : clients) {
            bankBalance += client.getBalance();
        }

        System.out.println("\n\n\n\nBank report  : " + getName());
        System.out.println("Report date  : " + new Date());
        System.out.printf("Bank balance : %.2f\n", bankBalance);
        System.out.printf("Bank kredit : %.2f\n", BankReport.getBankCreditSum(this));
        System.out.println("Client lists (client count:" + BankReport.getNumberOfClients(this) +
                "" + " client account count: " + BankReport.getAccountsNumber(this) + "):");
        int i = 1;
        for (Client client : BankReport.getClientsSorted(this)) {
            System.out.println("==============================================================");
            System.out.println("Clinet # [" + i + "]");
            System.out.println("==============================================================");
            client.printReport();
            System.out.println("==============================================================");

            i++;
        }

        System.out.println("Stats by city:");
        Map<String, List<Client>> mapClinetOfCities = BankReport.getClientsByCity(this);

        for (String city : mapClinetOfCities.keySet()) {
            System.out.println("City: " + city);
            for (Client client : mapClinetOfCities.get(city)) {
                client.printReport();
            }
        }

        System.out.println(BankReport.getClientsByCity(this));
    }

    public static void removeClient(Client client) {
        ClientDAO clientDAO = new ClientDAOImpl();
        clientDAO.remove(client);

    }

        public static Bank getBankByName(String name) {
        BankDAO bankDao = new BankDAOImpl();
        ClientDAO clientDAO = new ClientDAOImpl();
        Bank bank = bankDao.getBankByName(name);
        if (bank!= null) {
            for (Client client : clientDAO.getAllClients(bank.getId())) {
                try {
                    bank.addClient(client);
                } catch (ClientExistsException e) {
                    e.printStackTrace();
                }
            }
        }
        return bank;
    }

    public static Client getClientByName(Bank bank, String name) {
        ClientDAO clientDao = new ClientDAOImpl();

        return clientDao.findClientByName(bank.id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;

        Bank bank = (Bank) o;

        if (id != null ? !id.equals(bank.id) : bank.id != null) return false;
        if (name != null ? !name.equals(bank.name) : bank.name != null) return false;
        if (clients != null ? !clients.equals(bank.clients) : bank.clients != null) return false;
        if (listeners != null ? !listeners.equals(bank.listeners) : bank.listeners != null) return false;
        return clientCache != null ? clientCache.equals(bank.clientCache) : bank.clientCache == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (listeners != null ? listeners.hashCode() : 0);
        result = 31 * result + (clientCache != null ? clientCache.hashCode() : 0);
        return result;
    }
}