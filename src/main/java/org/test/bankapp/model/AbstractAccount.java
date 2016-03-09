package org.test.bankapp.model;


import com.sun.org.apache.xpath.internal.operations.Bool;
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

    public static Logger getLog() {
        return log;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        AbstractAccount that = (AbstractAccount) o;
//
//        if (Float.compare(that.balance, balance) != 0) return false;
//        if (isActive != that.isActive) return false;
//        return id != null ? id.equals(that.id) : that.id == null;
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result = (balance != +0.0f ? Float.floatToIntBits(balance) : 0);
//        result = 31 * result + (id != null ? id.hashCode() : 0);
//        result = 31 * result + (isActive ? 1 : 0);
//        return result;
//    }

    @Override
    public String toString() {
        return "AbstractAccount{" +
                "id=" + id +
                ", balance=" + balance +
                ", isActive=" + isActive +
                '}';
    }
}
