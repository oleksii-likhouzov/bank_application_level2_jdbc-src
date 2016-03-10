package org.test.bankapp.dao;

import org.test.bankapp.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOImpl implements ClientDAO {
    public Client findClientByName(Long bankId, String name) throws SQLException {
        Client client = null;
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "SELECT C.ID, C.NAME, C.INITIAL_OVERDRAFT, C.EMAIL, C.PHONE,  RC.NAME AS CITY_NAME, GT.CODE  AS GT_CODE\n" +
                            "   FROM T_CLIENT C\n" +
                            "   LEFT JOIN REF_GENDER_TYPE GT ON GT.ID = C.GENDER_TYPE_ID\n" +
                            "   LEFT JOIN REF_CITY RC ON RC.ID = C.CITY_ID\n" +
                            " WHERE C.BANK_ID = ? and c.name=? ");
            stmt.setLong(1, bankId);
            stmt.setString(2, name);
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong("id");
                String tmpName = rs.getString("name");
                BigDecimal overdraft = rs.getBigDecimal("initial_overdraft");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String city = rs.getString("city_name");
                String genderCode = rs.getString("gt_code");
                client = new Client(Gender.valueOf(genderCode), overdraft.floatValue());
                client.setName(tmpName);
                client.setEmail(email);
                client.setPhone(phone);
                client.setCity(city);
                client.setId(id);
                AccountDAO accountDAO = new AccountDAOImpl();
                for (Account account : accountDAO.getAllAccounts(id)) {
                    client.addAccount(account);
                    if (account.isActive()) {
                        client.setActiveAccount(account);
                    }
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return client;
    }

    public Long findCityByName(String name) throws SQLException {
        Long result = null;
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "SELECT RC.ID\n" +
                            "   FROM  REF_CITY RC \n" +
                            " WHERE RC.NAME= ? ");
            stmt.setString(1, name);
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getLong("id");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

    public Long findGenderByCode(String code) throws SQLException {
        Long result = null;
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "SELECT RC.ID\n" +
                            "   FROM  REF_GENDER_TYPE RC \n" +
                            " WHERE RC.CODE= ? ");
            stmt.setString(1, code);
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getLong("id");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
    }

    public void saveCity(String name) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "INSERT into ref_city (name) " +
                            "values(?)");
            stmt.setString(1, name);
            int rowCount = stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public List<Client> getAllClients(Long bankId) throws SQLException {
        List<Client> clients = new ArrayList<Client>();
        PreparedStatement stmt = null;
        try {
            stmt = ContextLocal.conn.prepareStatement(
                    "SELECT C.ID, C.NAME, C.INITIAL_OVERDRAFT, C.EMAIL, C.PHONE,  RC.NAME AS CITY_NAME, GT.CODE  AS GT_CODE\n" +
                            "   FROM T_CLIENT C\n" +
                            "   LEFT JOIN REF_GENDER_TYPE GT ON GT.ID = C.GENDER_TYPE_ID\n" +
                            "   LEFT JOIN REF_CITY RC ON RC.ID = C.CITY_ID\n" +
                            " WHERE C.BANK_ID = ?");
            stmt.setInt(1, bankId.intValue());
            // 2) Execute query and get the ResultSet
            ResultSet rs = stmt.executeQuery();
            // Iterate over results and print it
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                BigDecimal overdraft = rs.getBigDecimal("initial_overdraft");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String city = rs.getString("city_name");
                String genderCode = rs.getString("gt_code");
                Client client;
                client = new Client(Gender.valueOf(genderCode), overdraft.floatValue());
                client.setName(name);
                client.setEmail(email);
                client.setPhone(phone);
                client.setCity(city);
                client.setId(id);
                AccountDAO accountDAO = new AccountDAOImpl();
                for (Account account : accountDAO.getAllAccounts(id)) {
                    client.addAccount(account);
                    if (account.isActive()) {
                        client.setActiveAccount(account);
                    }
                }
                clients.add(client);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return clients;
    }

    public Client save(Client client, Long bankId) throws SQLException {
        AccountDAO accountDAO = new AccountDAOImpl();
        PreparedStatement stmt = null;
        try {
            Long cityId = null;

            if (client.getCity() != null && !client.getCity().isEmpty()) {
                cityId = findCityByName(client.getCity());
                if (cityId == null) {
                    saveCity(client.getCity());
                    cityId = findCityByName(client.getCity());
                }
            }
            Long genderId = null;
            if (client.getGender() != null) {
                genderId = findGenderByCode(client.getGender().name());
            }

            if (client.getId() == null) {
                stmt = ContextLocal.conn.prepareStatement(
                        "INSERT into t_client (name, " +
                                "bank_id," +
                                "INITIAL_OVERDRAFT, " +
                                "gender_type_id, " +
                                "email, " +
                                "phone, " +
                                "city_id ) \n" +
                                "values(?,?,?,?,?,?,?)");
                stmt.setString(1, client.getName());
                stmt.setLong(2, bankId);
                stmt.setBigDecimal(3, new BigDecimal(client.getInitialOverdraft()));
                stmt.setLong(4, genderId);
                stmt.setString(5, client.getEmail());
                stmt.setString(6, client.getPhone());
                stmt.setLong(7, cityId);
            } else {
                stmt = ContextLocal.conn.prepareStatement(
                        "UPDATE t_client set name= ?, " +

                                "INITIAL_OVERDRAFT =?, " +
                                "gender_type_id =?, " +
                                "email= ?, " +
                                "phone =?, " +
                                "city_id =?  \n" +
                                "where id =? ");
                stmt.setString(1, client.getName());
                stmt.setBigDecimal(2, new BigDecimal(client.getInitialOverdraft()));
                stmt.setLong(3, genderId);
                stmt.setString(4, client.getEmail());
                stmt.setString(5, client.getPhone());
                stmt.setLong(6, cityId);
                stmt.setLong(7, client.getId());
            }
            // 2) Execute delete operation
            int rowCount = stmt.executeUpdate();
            if (client.getId() == null) {
                Client tmpClient = findClientByName(bankId, client.getName());
                if (tmpClient != null) {
                    client.setId(tmpClient.getId());
                }
            }
            for (Account account : client.getAccounts()) {
                accountDAO.save(account, client.getId());
            }
            client = findClientByName(bankId, client.getName());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return client;
    }

    public void remove(Client client) throws SQLException {
        AccountDAO accountDAO = new AccountDAOImpl();
        PreparedStatement stmt = null;
        try {
            for (Account account : client.getAccounts()) {
                accountDAO.remove(account);
            }
            stmt = ContextLocal.conn.prepareStatement(
                    "DELETE  from t_client \n" +
                            "where id = ?");

            stmt.setLong(1, client.getId().longValue());
            // 2) Execute delete operation
            int rowCount = stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
