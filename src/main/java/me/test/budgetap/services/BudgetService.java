package me.test.budgetap.services;

import me.test.budgetap.model.Transaction;
// тут все используемые сервисы, чтоб использовать полиморфизм

public interface BudgetService {
//когда создан интерфейс нажимаем ЛКМ по наименованию
// у нас это "BudgetService" и через контекстное меню Alt+Enter
// выбираем implement interfase BudgetServiceimpl  предлагаемое формой оставляем
// он предлагает какие методы реализуем оставляем все предложенные
// все impl  лучше положить в папку Package в service "impl" так принято
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
