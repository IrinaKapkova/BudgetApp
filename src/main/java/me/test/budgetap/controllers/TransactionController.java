package me.test.budgetap.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.test.budgetap.model.Category;
import me.test.budgetap.model.Transaction;
import me.test.budgetap.services.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
// В Spring мы сопоставляем запросы с обработчиками запросов через аннотацию @RequestMapping
// далее пишем название сущности с которой работаем
@RequestMapping("/transaction")
// подсказки через анатацию @Tag
@Tag(name = "Трансакция", description = "CRUT-операции и другие эндпоинты для работы с трансакциями")
public class TransactionController {
    private final BudgetService budgetService;

    public TransactionController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }
// запрос на создание трансакции принимет в теле запроса сущность . тут id нет еще
//     чтоб он сработал используем программа POSTMAN
//     там выбираем метод, пишем URL, и написать либо параметры  либо прописать body
//     у нас используется @RequestBody для класса Transaction
//     создаем json  на основе конструктора этого класса в postman  на закладке body
//     выбрав кнопкой переключателем raw
//     это мы сами будем писать что отправлять в теле запроса (вместо параметров)
//     выбираем перед созданием body  тип json а кнопка "Beautify"  позволяет поправить форматирование
//     в фигурных скобках в ковычках поля-параметры как основа  : "значение" ,
//     когда заполнили в  postman, в IDEA  запускаем приложение, а потом в postman нажимаем  sent для отправки запроса
//     в нижней части результат запроса видем,
//     если выдает ошибку то тип посмотреть ошибки, изучить стейк трейс ошибки и message
//     HTTP-запрос имеет тело запроса, содержимое может быть распознано как параметр в методе контроллера с помощью @RequestBody

    //
    // в postman post  URL http://localhost:8080/transaction
    //  в swagger  в браузере http://localhost:8080/swagger-ui
    //  выбрать нужный контроллер "transaction-controller" в нем нужный эндпоинт "POST"
    //  далее он предлагает заполнить тело запроса тип json
    // каркас делает сам со значениями по дефолту,
    // так в Enam = первое значение из возможных, если это число=0, если строка то значение "string"
    // необходимо поправить на необходимые значения после знака =
    // и нажать Execute
    @PostMapping
    public ResponseEntity<Long> addTransaction(@RequestBody Transaction transaction) {
        long id = budgetService.addTransaction(transaction);
        return ResponseEntity.ok(id);
    }

    //  get  запрос всегда без тела
//  тут в url  пишем параметр, индентификатор у нас это id, если параметров нет то выводится весь список
//  если выдает ошибку проверить код в контролллере url проверить
// при этом в классе ....impl проверить что сохраняется результат созданный который мы запрашиваем в запросе get
//  это ...add метод и проверяем ... get метод синтаксис
//  так как тут есть обработка ошибки то и postman при неверном id выйдет статус 400
    @GetMapping("/{id}")

        public ResponseEntity<Transaction> getTransactionById(@PathVariable long id) {
        Transaction transaction = budgetService.getTransaction(id);
        if (transaction == null) {
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(transaction);
    }

    //  get  запрос  без ИНДЕНТИФИКАТОРА выводит все созданные сущности
//  в url не пишем параметры
//    @GetMapping
//    public ResponseEntity<Transaction> getAllTransactions() {
//        Transaction transaction = budgetService.getTransaction();
//        if (transaction == null) {
//            ResponseEntity.notFound();
//        }
//        return .....;
//    }


//  //  url содержащий параметр не даст соверщить запрос без указания этого параметра
//    @GetMapping("/byMonth{month}")
//    public ResponseEntity<Transaction> getTransactionByMonth(@PathVariable Month month) {
//        return null;
//    }
    //  //  url несодержащий параметр, но если метод содержит фильтр в виде параметра через  @RequestParam
    // //   даст соверщить запрос без указания этого параметра тогда выводится все сущности без фильтрации по параметру
    // уточнение (required= false) дает полное право вообще не указывать параметр
    // можно указать возможные несколько фильтров через запятую,
    // хоть все поля класса ... impl  и участвующего в нем класса из модели
//    @GetMapping()
//    public ResponseEntity<Transaction>
//    TransactionByMonth(@RequestParam(required= false) Month month, @RequestParam(required= false) Category category)) {
//        return null;
//    }


//  PutMapping метод редактирования полей у ранее созданных объектов
// проверка на наличие вообще такой трансакции
// Для работы с параметрами,
// передаваемыми через адрес запроса в Spring WebMVC используется аннотация @PathVariable
// swagger по ссылке в браузере http://localhost:8080/swagger-ui/
// может проверить данный эндпоинт
// для этого в соотоветствующем контроллере "transaction-controller" выбирает PUT нажимаем try it out
// далее прописываем id изменяемой трансакции в теле пишем
// ВСЕ поля после = должны быть заполнены  (даже те что остались без изменения) нажимаем "Execute"
// в Postman  был бы запрос PUT c URL http://localhost:8080/transaction/0 например, тело писали бы вруки сами с соблюдением синтаксиса


    @PutMapping("/{id}")
    public ResponseEntity<Transaction> editTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        Transaction transaction1 = budgetService.editTransaction(id, transaction);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    //  анатация с указанием индентификатора в запросе который будем удалять
//  если индентификатора нет, то удаляется все сущности ранее созданные
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long id) {
        if (budgetService.deleteTransaction(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
// Postman  Delete запрос с URL http://localhost:8080/transaction/0 например
// swagger  требует только корректный ID
// в schemas можно увидеть названия полей сущностей
    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaction() {
        budgetService.deleteAllTransaction();
        return ResponseEntity.ok().build();
    }
    @GetMapping
    // для описания  метода используется анатацию   @Operation (Операция) библиотеки io.swagger.v3.oas.annotation
    // у неe очень много параметров , которые можно переопределить , например
    // description описание
    // summary резюме
    @Operation(
            summary = "Поиск транзакций по месяцу и/или категории",
            // пояснение в строке get  запроса в swagger
            description = "Можно искать по одному параметру, обоим или вообще без параметров"
            // пояснение в закладке запроса (внутри)  в swagger
    )
    // Для описания параметра можно использовать анатацию  @Parameters где value это смысл
    // например  name  имя  и example пример
    // при этом в swagger  можно видеть Parameters стал с примером и с наименованием
    @Parameters( value = {
            @Parameter(name = "month", example = "Декабрь")
    })
    //  анатации @ApiResponses API-ответы содержит в себе другие анатации массив , например
    //  первая @ApiResponse описывает  responseCode Код ответа 200 также содержит description описание и content содержание
    //  mediaType = "application/json"  Тип носителя = "приложение/json
    //  либо array = @ArraySchema(schema = @Schema(implementation = Transaction.class))
    //      массив = @ Схема массива (схема = @ Схема (реализация = Транзакция. класс))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Транзакции были найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Transaction.class))
                            )
                    }
            )
    })
    public ResponseEntity<Transaction> getAllTransactions(@RequestParam(required = false) Month month,
                                                          @RequestParam(required = false) Category category) {
        return null;
    }
}
