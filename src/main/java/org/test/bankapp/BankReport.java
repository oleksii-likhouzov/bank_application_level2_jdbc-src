package org.test.bankapp;

import org.test.bankapp.model.Account;
import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;

import java.util.*;

public class BankReport {
    /**
     * Выводит количество клиентов банка
     * @param bank
     * @return
     */
    public static int getNumberOfClients(Bank bank) {
        return bank.getClients() != null ? bank.getClients().size() : 0;
    }

    /**
     * Выводит суммарное количество счетов у всех клиентов банка
     * @param bank
     * @return
     */
    public static int getAccountsNumber(Bank bank) {
        int result =0;

        for (Client client:bank.getClients()) {
            result += (client.getAccounts()!= null?client.getAccounts().size():0);
        }
        return result;
    }

    /**
     * Вывести суммарную величину кредита, взятую клиентами банка. Т.е. сумму всех значений, взятых сверх баланса для CheckingAccount
     * @param bank
     * @return
     */
    public static float getBankCreditSum(Bank bank) {
        float result =0.f;

        for (Client client:bank.getClients()) {
            for(Account account:client.getAccounts()) {
                float tmpResult = account.getBalance();
                if (tmpResult <0.f)
                    result += -tmpResult;
            }
        }
        return result;
    }

    /**
     * Выводит список всех клиентов, отсортированный по текущему остатку счета.
     * @param bank
     * @return
     */
    public static Set<Client> getClientsSorted(Bank bank) {

        Set<Client> clients = new TreeSet<Client>(new Comparator() {
            public int compare(Object o1, Object o2) {
                return (int) ((((Client)o1).getBalance()- ((Client)o2).getBalance())*100);
            }
        });
        for (Client client:bank.getClients()) {
            clients.add(client);
        }
        return clients;
    }

    /**
     * таблицу, ключами которой будут города, а значениями – список клиентов в каждом городе.
     * Выведите результирующую таблицу, отсортировав города по алфавиту.
     * @param bank
     * @return
     */

    public static Map<String, List<Client>> getClientsByCity(Bank bank) {
        Map<String, List<Client>> result= new TreeMap<String, List<Client>>();
        Map<String,String> tmpCityList = new TreeMap<String,String>();
        for (Client client:bank.getClients()) {
            tmpCityList.put(
                    (client.getCity() == null? new String():
                    client.getCity()),client.getCity());
        }

        for (String city:tmpCityList.keySet()) {
            List<Client> tmpClientList =  new ArrayList<Client>();
            for(Client client:bank.getClients()){
                if (!city.equals("") && client.getCity()!= null &&
                        city.equals(client.getCity())  ) {
                    tmpClientList.add(client);
                } else {
                    if (city.equals("") &&client.getCity() == null) {
                        tmpClientList.add(client);
                    }
                }
            }
            result.put(city, tmpClientList);

        }

        return result;
    }
}
