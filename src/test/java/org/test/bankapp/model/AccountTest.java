package org.test.bankapp.model;

import org.junit.*;
import static org.junit.Assert.*;

public class AccountTest {
    @Test
    public void testSavingAcountEquals() {
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setBalance(10);
        SavingAccount savingAccountEqueal= new SavingAccount();
        savingAccountEqueal.setBalance(10);
        assertEquals("Equal not valid for equals accounts", savingAccount, savingAccountEqueal);
        SavingAccount savingAccountDiff= new SavingAccount();
        savingAccountDiff.setBalance(11);
        assertNotEquals("Equal not valid for diff accounts", savingAccount, savingAccountDiff);
        CheckingAccount checkingAccount = new CheckingAccount(20);
        assertNotEquals("Equal not valid for diff account types", savingAccount, checkingAccount);
    }

    @Test
    public void testCheckingAcountEquals() {
        CheckingAccount checkinAccount= new CheckingAccount(10);
        checkinAccount.setBalance(20);
        CheckingAccount checkinAccountEqueal= new CheckingAccount(10);
        checkinAccountEqueal.setBalance(20);
        assertEquals("Equal not valid for equals accounts", checkinAccount, checkinAccountEqueal);
        CheckingAccount checkinAccountDiff= new CheckingAccount(18);
        checkinAccountDiff.setBalance(20);
        assertNotEquals("Equal not valid for diff accounts", checkinAccount, checkinAccountDiff);
        checkinAccountDiff.setBalance(8);
        checkinAccountDiff.setOverdraft(10);
        assertNotEquals("Equal not valid for diff accounts", checkinAccount, checkinAccountDiff);
        checkinAccountDiff.setBalance(20);
        assertEquals("Equal not valid for equals accounts", checkinAccount, checkinAccountDiff);
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setBalance(20);
        assertNotEquals("Equal not valid for diff account types", checkinAccount, savingAccount);
    }
    @Test
    public void testAbstractAccountEquals() {
        AbstractAccount abstractSavingAccount= new SavingAccount();
        abstractSavingAccount.setBalance(10);
        SavingAccount savingAccountEqueal= new SavingAccount();
        savingAccountEqueal.setBalance(10);
        assertEquals("Equal not valid for equals SavingAccount accounts", abstractSavingAccount, savingAccountEqueal);
        assertEquals("Equal not valid for equals SavingAccount accounts", savingAccountEqueal, abstractSavingAccount);
        CheckingAccount checkinAccount= new CheckingAccount(10);
        checkinAccount.setBalance(20);
        assertNotEquals("Equal not valid for diff account types", abstractSavingAccount, checkinAccount);
        AbstractAccount abstractCheckingAccount= new CheckingAccount(10);
        abstractCheckingAccount.setBalance(20);
        assertNotEquals("Equal not valid for diff account types", abstractSavingAccount, abstractCheckingAccount);
    }

    @Test
    public void testdecimalValue() {
        AbstractAccount abstractSavingAccount= new SavingAccount();
        abstractSavingAccount.setBalance(10/3.f);
        abstractSavingAccount.printReport();
        abstractSavingAccount.decimalValue();
        abstractSavingAccount.setBalance(3.359f);
        abstractSavingAccount.decimalValue();
    }

}
