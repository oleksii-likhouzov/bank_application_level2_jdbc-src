package org.test.bankapp.model;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAccount implements Account {
    private static final Logger log = LogManager.getLogger(AbstractAccount.class);
    private float balance;

    public void deposit(float x) {
        if (x < 0.) {
            log.log(Level.ERROR, "Value of \"balance\" = " + x + "  < 0. ");
            throw new IllegalArgumentException("Value of \"balance\" = " + x + "  < 0. ");
        }
        balance = balance + x;
    }

    public float getBalance() {
        return balance;
    }

    protected void setBalance(float balance) {
        this.balance = balance;
    }

    public void printReport() {
        System.out.println("  Balance:" + balance);
    }

    public void decimalValue() {
        System.out.println(Math.round(balance*100)/100.f);
    }

    @Override
    public String toString() {
        return "AbstractAccount{" +
                "balance=" + balance +
                '}';
    }
}
