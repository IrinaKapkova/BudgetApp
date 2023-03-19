package me.test.budgetap.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
    @GetMapping
    public String helloWorld(){
        return  "Hello, web!";
    }
    // http://localhost:8080/ проверка
//    @GetMapping("/page")
//    public String page(@RequestParam String page){
//        return  "Hello, "+page+ "!";
//    }
    @GetMapping("/path/to/page")
    public String page(@RequestParam String page) {
        return "Page: " + page;
    }
    // http://localhost:8080/path/to/page?page=1
    // для проверки кода пишем в URL путь , потом так как тут есть обязательный
    // строковый параметр @RequestParam String page
    // то после пути пишем ? наименование параметра= значение параметра

    // для проверки работы приложения подсоединили библиотеку springdoc-openapi-ui
    // в браузере http://localhost:8080/swagger-ui.html
    // после этого переходим на страницу свагера где видем наш сервер,
    // все наши контроллеры и для каждого из них его эндпоинты (http  запросы)
    //  для совершения конкретного запроса под ним нажать кнопку try it out,
    //  если никаких параметров по смыслу вводить не надо далее нажимается  кнопка "Execute"
    //  1

}
