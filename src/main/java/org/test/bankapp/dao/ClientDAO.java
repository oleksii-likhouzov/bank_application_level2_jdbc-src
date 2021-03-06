package org.test.bankapp.dao;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by stalker on 08.03.16.
 */
public interface ClientDAO {
    /**
     * Return client by its name, initialize client accounts.
     *
     * @param bankId
     * @param name
     * @return
     */

    Client findClientByName(Long bankId, String name) throws SQLException;


    /**
     * Returns the list of all clients of the Bank
     * <p/>
     * and their accounts
     *
     * @param bankId
     * @return
     */

    List<Client> getAllClients(Long bankId) throws SQLException;

    /**
     * Method should insert new Client (if id==null)
     * <p/>
     * or update client in database
     *
     * @param client
     */

    Client save(Client client, Long bankId) throws SQLException;


    /**
     * Method removes client from Database
     *
     * @param client
     */

    void remove(Client client) throws SQLException;
}
