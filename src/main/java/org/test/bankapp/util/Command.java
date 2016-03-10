package org.test.bankapp.util;

import java.sql.SQLException;

public interface Command {
    void execute() throws SQLException;
    void printCommandInfo();
}
