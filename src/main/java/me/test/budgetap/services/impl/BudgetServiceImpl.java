package me.test.budgetap.services.impl;

import me.test.budgetap.model.Transaction;
import me.test.budgetap.services.BudgetService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BudgetServiceImpl implements BudgetService {
    public static final int SALARY = 20_000 - 9_750;
    public static final int SAVING = 3_000;
    public static final int DAILY_BUDGET = (SALARY - SAVING) / LocalDate.now().lengthOfMonth();
    public static final int balance = 0;
    public static final int AVG_SALARY = (10000 + 10000 + 10000 + 10000 + 15000 + 15000 + 20000 + 20000 + 20000 + 25000 + 25000 + 25000) / 12;
    public static final double AVG_DAYS = 29.3;
    public static Map<Month, Map<Long, Transaction>> transactions = new TreeMap<>();
    private static long lastId = 0;

    @Override
    public int getDailyBudget() {
        return SALARY / 30;
    }

    @Override
    public int getBalance() {
        return SALARY - getAllSpend() - SAVING;
    }

    @Override
    public long addTransaction(Transaction transaction) {
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        monthTransactions.put(lastId, transaction);
        transactions.put(LocalDate.now().getMonth(), monthTransactions);
        return lastId++;
    }

    @Override
    public int getVacationBonus(int daysCount) {
        double avgDaySalary = AVG_SALARY / AVG_DAYS;
        return (int) (daysCount * avgDaySalary);
    }

    @Override
    public int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth) {
        getVacationBonus(vacationWorkingDaysCount);
        int salary = (SALARY / workingDaysInMonth) * (workingDaysInMonth - vacationWorkingDaysCount);
        return salary + getVacationBonus(vacationDaysCount);
    }

    @Override
    public int getDailyBalance() {
        return DAILY_BUDGET * LocalDate.now().getDayOfMonth() - getAllSpend();
    }

    @Override
    public int getAllSpend() {
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        int sum = 0;
        for (Transaction transaction : monthTransactions.values()) {
            sum += transaction.getSum();
        }
        return sum;
    }

    @Override
    public Transaction getTransaction(long id) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            Transaction transaction = transactionByMonth.get(id);
            if (transaction != null) {
                return transaction;
            }
        }
        return null;

    }

    @Override
    public Transaction editTransaction(long id, Transaction transaction) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id)) {
                transactionByMonth.put(id, transaction);
                return transaction;
            }
        }
        return null;
    }
    @Override
    public boolean deleteTransaction(long id){
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id)) {
                transactionByMonth.remove(id);
                return true;
            }
        }
        return false;
    }
    @Override
    public void deleteAllTransaction(){
      transactions =  new TreeMap<>();
    }
}
