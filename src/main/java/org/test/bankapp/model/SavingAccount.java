package org.test.bankapp.model;

import org.apache.logging.log4j.*;

public final class SavingAccount extends AbstractAccount {
    private static final Logger log = LogManager.getLogger(SavingAccount.class);

    public void withdraft(float x) {
        if (x < 0.) {
            log.log(Level.ERROR, "Value of \"withdraft\" = " + x + "  < 0. ");
            throw new IllegalArgumentException("Value of \"balance\" = " + x + "  < 0. ");
        }
        if ((getBalance() - x) < 0) {
            log.log(Level.ERROR,
                    "Withdraw is not possible. \n" +
                            "Withdraft with value" +
                            x +
                            "  for account is not possible. Account balance: " +
                            getBalance());
            throw new RuntimeException("Withdraw is not possible. \n" +
                    "Withdraft with value" +
                    x +
                    "  for account is not possible. Account balance: " +
                    getBalance());
        }
        setBalance(getBalance() - x);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingAccount account = (SavingAccount) o;
        if (Float.compare(account.getBalance(), getBalance()) != 0) return false;
        if (isActive() != account.isActive()) return false;

        if(!(getId() != null ? getId().equals(account.getId()) : account.getId() == null))
        return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (getBalance() != +0.0f ? Float.floatToIntBits(getBalance()) : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (isActive() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SavingAccount{" +
                super.toString() +
                "}";
    }
}
