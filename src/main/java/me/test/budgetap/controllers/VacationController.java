package me.test.budgetap.controllers;

import me.test.budgetap.services.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vacation")
// данный контроллер не имеет какойто управляемой сущности,
// выделение его в отдельный класс идет по функционалу- расчет отпускных
public class VacationController {
    private final BudgetService budgetService;

    public VacationController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public int vacationBonus(@RequestParam int vacationDays){
        return budgetService.getVacationBonus(vacationDays);
    }
    @GetMapping("/salary")
    public int salaryWithVacation(@RequestParam int vacationDays, @RequestParam int vacWorkDays, @RequestParam int workingDays){
        return  budgetService.getSalaryWithVacation(vacationDays, vacWorkDays, workingDays);
//       пример запроса в браузере http://localhost:8080/vacation/salary?vacationDays=14&workingDays=16&vacWorkDays=10
    }
}
