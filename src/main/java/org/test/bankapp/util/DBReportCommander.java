package org.test.bankapp.util;

import org.test.bankapp.BankInfo;
import org.test.bankapp.dao.BankDAO;
import org.test.bankapp.dao.BankDAOImpl;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by stalker on 08.03.16.
 */
public class DBReportCommander  implements Command{
    public void execute()  throws SQLException {
        BankCommander.checkCurrentBank();
        BankCommander.currentBank.printReport();
        BankDAO bankDAO = new BankDAOImpl();

        BankInfo bankInfo = null;
        try {
            bankInfo = bankDAO.getBankInfo(BankCommander.currentBank);
            ContextLocal.conn.commit();
        } catch (SQLException e) {
            ContextLocal.conn.rollback();
            e.printStackTrace();
            return;
        }

        System.out.println("\n\n\n\nBank report  : " + BankCommander.currentBank.getName());
        System.out.println("Report date  : " + new Date());
        System.out.printf("Bank balance : %.2f\n", bankInfo.getTotalAccountSum());
        System.out.println("Clents count: " + bankInfo.getNumberOfClients());
        System.out.println("Clients by city:");
        for(String city: bankInfo.getClientsByCity().keySet()) {
            System.out.println("City:" + city);
            for(Client client:bankInfo.getClientsByCity().get(city)) {
                client.printReport();
            }
        }
    }

    public void printCommandInfo() {
        System.out.println("Print Bank Report");
    }
}
