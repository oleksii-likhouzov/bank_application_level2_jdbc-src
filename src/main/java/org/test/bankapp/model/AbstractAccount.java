package org.test.bankapp.model;



import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAccount implements Account {
    private static final Logger log = LogManager.getLogger(AbstractAccount.class);
    private float balance;
    private Long id;
    private boolean isActive;


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
        System.out.println("       ID:" + id);
        System.out.println(" isActive:" + isActive);

    }

    public void decimalValue() {
        System.out.println(Math.round(balance*100)/100.f);
    }

    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "AbstractAccount{" +
                "id=" + id +
                ", balance=" + balance +
                ", isActive=" + isActive +
                '}';
    }
}
