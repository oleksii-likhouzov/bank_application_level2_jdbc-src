package org.test.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.test.bankapp.model.Bank;
import org.test.bankapp.model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BankCommander {
    private static final Logger log = LogManager.getLogger(BankCommander.class);
    public static Bank currentBank = new Bank();
    public static Client currentClient;

    public static void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public static void removeCommand(String name) {
        commands.remove(name);
    }

    private static Map<String, Command> commands =
            new TreeMap<String, Command>() {{
                put("1", new FindClientCommand());  // 1 Находить клиента по имени
                put("2", new GetAccountsCommand()); // 2   Получать список счетов клиента и остаток на счетах
                put("3", new DepositCommand()); // 3 Пополнять счет клиента (DepositCommand)
                put("4", new WithdrawCommand()); //4 Снимать средства со счета клиента
                put("5", new TransferCommand()); //5 Осуществлять перевод со счета клиента на счет другого клиента банка
                put("6", new AddClientCommand()); //7 -  AddClientCommand
                put("7", new Command() { // 7 - Exit Command
                    public void execute() {
                        System.exit(0);
                    }

                    public void printCommandInfo() {
                        System.out.println("Exit");
                    }
                });
            }};

    public static void runCommander() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Integer commndNumber;

        while (true) {
            for (int i = 1; i <= 3; i++) {
                System.out.println("...................................");
            }
            if (currentClient != null) {
                System.out.println("Active client:");
                currentClient.printReport();
            }
            System.out.println("--------------\nCommand line list:");

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
                if (commands.get(commndNumber) == null) {
                    System.out.println("Not a valid command number!");
                    continue;
                }
                commands.get(commndNumber).execute();
            } catch (Exception e) {
                log.log(Level.ERROR, e);

            }
        }
    }
}
