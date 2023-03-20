package me.test.budgetap.controllers;

import me.test.budgetap.services.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// анатация @RestController прописываем
@RestController
// @RequestMapping нужен чтоб уточнить URL  запроса всех методов используемых в этом классе
@RequestMapping("/budget")
public class BudgetController {
    //тут только  API программный интерфейс приложения,  это контракт, который предоставляет программа, договоренности как она работает
    // формируем и результат запроса ,
    // создается как java class в Package "controllers" наименование обычно состоит из наименования модели+слово Controller
    // к созданному сервису с бизнеслогикой связанной с этой моделью
    //одно из полей этого класса это private  сервис который будет использоваться в контракте
    // далее из контекстного меню вызываем конструктор
    // @GetMapping анатация "вывести" под этой анатацией пишем соответствующий метод он будет public параметр int как в сервисе
    // название мы взяли как у нас в сервисе только без Get "Дневной бюджет" ()
    // { данный контроллер выдает return результат работы определенного метода сервиса через точку к нему обращаемся "вывести Дневной бюджет" }
    // указываем поля с указанием используемого интерфейса
    // Аннотация @Qualifier позволяет уточнить имя бина, который надо внедрить если реализаций несколько. Используется прямо перед аргументом.

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
    //  в браузере http://localhost:8080/budget/daily либо через swagger

    @GetMapping("/daily")
    public int dailyBudget() {
        return budgetService.getDailyBudget();
    }
    @GetMapping("/balance")
    public int balance(){
      return budgetService.getBalance();
    }

}
