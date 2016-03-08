package org.test.bankapp.model;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.bankapp.NotEnoughFundsException;
import org.test.bankapp.OverDraftLimitExceededException;

public final class CheckingAccount extends AbstractAccount {
    private static final Logger log = LogManager.getLogger(CheckingAccount.class);
    private float overdraft;

    public float getOverdraft() {
        return overdraft;
    }

    public CheckingAccount(float overdraft) {
        setOverdraft(overdraft);
    }

    public void setOverdraft(float x) {
        if (x < 0.) {
            log.log(Level.ERROR, "Value of \"overdraft\" = " + x + "  < 0. ");
            throw new IllegalArgumentException("Value of \"overdraft\" = " + x + "  < 0. ");
        }
        overdraft = x;
    }


    public void withdraft(float x) throws NotEnoughFundsException {
        if (x < 0.) {
            log.log(Level.ERROR, "Value of \"withdraft\" = " + x + "  < 0. ");
            throw new IllegalArgumentException("Value of \"withdraft\" = " + x + "  < 0. ");
        }
        if ((getBalance() + overdraft - x) < 0) {
            log.log(Level.INFO, "Withdraw is not possible. \n" +
                    "Withdraft with value " +
                    x +
                    "  for account is not possible. Account balance: " +
                    getBalance() +
                    ". Account overdraft:" + overdraft);
            throw new OverDraftLimitExceededException(this, x);
        }
        setBalance(getBalance() - x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckingAccount account = (CheckingAccount) o;
        if (Float.compare(account.overdraft, overdraft) != 0) return false;
        return Float.compare(account.getBalance(), getBalance()) == 0;
    }

    @Override
    public int hashCode() {
        System.out.println(this);
        int result = getBalance() != +0.0f ? Float.floatToIntBits(getBalance()) : 0;
        return (31 * result + (overdraft != +0.0f ? Float.floatToIntBits(overdraft) : 0));
    }

    @Override
    public void printReport() {
        super.printReport();
        System.out.println("  Overdraft: " + overdraft);
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                super.toString() +
                ", overdraft=" + overdraft +
                '}';
    }
}
