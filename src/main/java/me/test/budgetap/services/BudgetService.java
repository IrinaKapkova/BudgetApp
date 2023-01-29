package me.test.budgetap.services;

import me.test.budgetap.model.Transaction;

public interface BudgetService {
    int getDailyBudget();
    int getBalance();

    long addTransaction(Transaction transaction);

    int getVacationBonus(int daysCount);

    int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth);

    int getDailyBalance();

    int getAllSpend();

    Transaction getTransaction(long id);

    Transaction editTransaction(long id, Transaction transaction);

    boolean deleteTransaction(long id);

    void deleteAllTransaction();
}
