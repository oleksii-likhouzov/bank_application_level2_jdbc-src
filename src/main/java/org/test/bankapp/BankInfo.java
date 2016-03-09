package org.test.bankapp;

import org.test.bankapp.model.Client;

import java.util.List;
import java.util.Map;

/**
 * Created by stalker on 08.03.16.
 */
public class BankInfo {
    /**
     * Total number of clients of the bank
     */

    private int numberOfClients;

    /**
     * The sum of all accounts of all clients
     */

    private double totalAccountSum;

    /**
     * List of clients by the city
     */

    private Map<String, List<Client>> clientsByCity;

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public double getTotalAccountSum() {
        return totalAccountSum;
    }

    public void setTotalAccountSum(double totalAccountSum) {
        this.totalAccountSum = totalAccountSum;
    }

    public Map<String, List<Client>> getClientsByCity() {
        return clientsByCity;
    }

    public void setClientsByCity(Map<String, List<Client>> clientsByCity) {
        this.clientsByCity = clientsByCity;
    }

}
