package org.test.bankapp.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;
import org.test.bankapp.model.ContextLocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class BankCommander {
    private static final Logger log = LogManager.getLogger(BankCommander.class);
    public static Bank currentBank;// = new Bank("My bank");
    public static Client currentClient;

    public static void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public static void removeCommand(String name) {
        commands.remove(name);
    }

    private static Map<String, Command> commands =
            new TreeMap<String, Command>(new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return Integer.parseInt(o1)-Integer.parseInt(o2);
                }
            }) {{
                put("1", new FindClientCommand());  // 1 Находить клиента по имени
                put("2", new GetAccountsCommand()); // 2   Получать список счетов клиента и остаток на счетах
                put("3", new DepositCommand()); // 3 Пополнять счет клиента (DepositCommand)
                put("4", new WithdrawCommand()); //4 Снимать средства со счета клиента
                put("5", new TransferCommand()); //5 Осуществлять перевод со счета клиента на счет другого клиента банка
                put("6", new AddClientCommand()); //7 -  AddClientCommand
                put("7", new DBSelectClientCommander()); //7
                put("8", new DBSelectBankCommander());
                put("9", new DBRemoveClientCommander());
                put("10", new DBReportCommander());
                put("11", new Command() { // 9 - Exit Command
                    public void execute() {
                        System.exit(0);
                    }

                    public void printCommandInfo() {
                        System.out.println("Exit");
                    }
                });
            }};

    public static void checkCurrentClient()
    {
        if (currentClient == null) {
            throw new RuntimeException("Active Client not defined");
        }
    }
    public static void checkCurrentBank()
    {
        if (currentBank == null) {
            throw new RuntimeException("Active Bank not defined");
        }
    }
    public static void runCommander() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Integer commndNumber;
        while (true) {

                System.out.println("..................................................................");
            if (currentBank != null) {
                System.out.println("[Active bank:] - ["+currentBank.getName()+"]");
             }
            if (currentClient != null) {
                System.out.println("{Active client:} -  {"+currentClient.getName()+"}");
            }
            System.out.println("Command line list:");

            for (String command : commands.keySet()) { // show menu
                System.out.print(command + ") ");
                commands.get(command).printCommandInfo();
            }
            System.out.print("Enter command number:\n");
            try {
                commndNumber = Integer.parseInt(br.readLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid Format!");
                continue;
            }
            try {
                if (commands.get(commndNumber.toString()) == null) {
                    System.out.println("Not a valid command number!");
                    continue;
                }
                commands.get(commndNumber.toString()).execute();
            }
            catch (Exception e) {
                log.log(Level.ERROR, e);
                e.printStackTrace();

            }
        }
    }
}
