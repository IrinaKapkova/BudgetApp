package me.test.budgetap.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.test.budgetap.model.Category;
import me.test.budgetap.model.Transaction;
import me.test.budgetap.services.BudgetService;
import me.test.budgetap.services.FilesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

// @Service анатация необходима чтоб в том числе можно было в контроллере к нему как к сервису обращаться
@Service
public class BudgetServiceImpl implements BudgetService {
    //  тут прописывают бизнеслогику , константы
    // одно из полей FilesService было добавлено после прописания FilesServicesimpl и FilesService для компиляции кода требует наличия конструктора
    // для этого ALT+ENTER  выбираем Add constructor parameter, после чего генерируется конструктор ниже как метод
    //    public BudgetServiceImpl(FilesService filesService) {
    //        this.filesService = filesService;
// чтоб использовать сохранение файла и чтение нужно создать два void приватных метода в этом классе
//    saveToFile() и  ReadFromFile()
    final private FilesService filesService;
    public static final int SALARY = 30_000 - 9_750;
    public static final int SAVING = 3_000;
    public static final int DAILY_BUDGET = (SALARY - SAVING) / LocalDate.now().lengthOfMonth();
    public static final int balance = 0;
    public static final int AVG_SALARY = (10000 + 10000 + 10000 + 10000 + 15000 + 15000 + 20000 + 20000 + 20000 + 25000 + 25000 + 25000) / 12;
    public static final double AVG_DAYS = 29.3;
    // хранилище где все будем сохранять - карта одно поле месяц (enam) второе поле вложеная карта с номером трансакции и сама трансакция
//    import class Map через контекстное меню поле приватное для инкапсуляции,
//    тип такой же и называться он будет пусть также transactions и чтоб сразу была пустая карта
//    new можно тут выбрать варик так как тут enam то используя для порядка TreeMap
    // если пересоздавать не надо то можно final
    private static TreeMap<Month, LinkedHashMap<Long, Transaction>> transactions = new TreeMap<>();
    // индентификатор трансакции для возможности обращения к каждой из них
    private static long lastId = 0;

    public BudgetServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @Override
    public int getDailyBudget() {
        return SALARY / 30;
    }

    //  чтобы созданные методы были доступны в интерфейсе требуется анатация @Override
//  Далее ALT+ENTER  для контектного меню и вибираем там  Pull metod 'название метода' to 'название сервиса'
//  таким образов методы переносятся в интерфейс
//  1
    @Override
    public int getBalance() {
        return SALARY - getAllSpend() - SAVING;
    }


    // long это тут номер трансакции, который далее нам пригодится
    // мы заменили изначально проставленный void на long
    // потом щелкнув его и из контекстного меню выбрали Make 'BudgetService.addTransaction()' return 'long'
    // метод добавления трансакции  принимать на вход будет трансацию
    // в текущий месяц добавляем трансакции , для этого берем
    // Map transactions.getOrDefault это дефолтное значение в случае если пока нет значения впринципе тк начало месяца
    // (месяц- LocalDate.now().getMonth(),
    // трансация-карта с порядком добавления не сортировка  new LinkedHashMap<>() и он будет типа Map<Long, Transaction> )
    // переводим через контекстное меню (ALT и одновременно Enter) все это в переменную (introduce local variable)
    // выбираем Places the result of an expression into a separate variable
    // установив курсор на  getOrDefault вызвав контекстное меню (через ALT и одновременно Enter) переименовываю переменную
    // на понятное название monthTransactions трансакции текущего месяца, при этом тип переменной подставился
    //  мы создали мапу, теперь точно можно к ней обращаться через monthTransactions.put так как используем карту
    //  (lastId, transaction); индентификатор и трансакция
    //то есть теперь есть учет трансакций в месяце


//    так как тут вложенная Map то мы должны добавить значение и в список трансакций в текущем месяце
//    и список трансакций данного месяца в общую Map  за весь период где месяц как одно из значений
//    переменная monthTransactions это список всех трансакций за месяц ( т.е. Map<Long, Transaction>> transactions , у нас пока только одна там будет)
//     поле private static Map<Month, Map<Long, Transaction>> transactions = new TreeMap<>(); должно соответствовать методу ...add
//     учесть что после перезапуска в Idea  в Postman все заново надо запускать, можно из истории запросов

    // чтоб сохранять после выхода из программы все созданные трансакции нами в последствии был дополнен код методом saveToFile();
    @Override
    public long addTransaction(Transaction transaction) {
        LinkedHashMap<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        monthTransactions.put(lastId, transaction);
        transactions.put(LocalDate.now().getMonth(), monthTransactions);
        saveToFile();
        return lastId++;
    }
//  в результате выдает ID  добавленной трансакции , изначально ++ было в put  , потом он перенес его в return зачем-то

    @Override
    public int getVacationBonus(int daysCount) {
        //прверка по типу
        // String s= "";
        // if (s!=null&& s!=isEmpty&& s!=isBlank());{}
        // можно заменить методом
        // StringUtils.isBlank(s) проверка строчки на пустоту, на то содержит одни пробелы  и не null
        // эта же библиотека содержит метод поиска символа в строке
        double avgDaySalary = AVG_SALARY / AVG_DAYS;
        return (int) (daysCount * avgDaySalary);
    }

    @Override
    public int getSalaryWithVacation(int vacationDaysCount, int vacationWorkingDaysCount, int workingDaysInMonth) {
        getVacationBonus(vacationWorkingDaysCount);
        int salary = (SALARY / workingDaysInMonth) * (workingDaysInMonth - vacationWorkingDaysCount);
        return salary + getVacationBonus(vacationDaysCount);
    }

    // метод расчета остатка на сегодня
    @Override
    public int getDailyBalance() {
        return DAILY_BUDGET * LocalDate.now().getDayOfMonth() - getAllSpend();
    }

    @Override
    //  метод считающий все потраченные деньги
    public int getAllSpend() {
        // копируем и вставляем нашу карту из метода add
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        // проитерируем iter стрелка вниз далее ENTER для выбора стрелка вбок и вниз
        // для выбора именно значений переменной выбрала monthTransactions.values()
        // при нажатии на слово value (ENTER) выбираем новое название переменной  transaction стрелкой вниз и выбором
        int sum = 0;
        for (Transaction transaction : monthTransactions.values()) {
            //  для суммирования всех значений изначально нулевую переменную увеличиваем на сумму трансакции transaction.getSum()
            sum += transaction.getSum();
        }
        //  в итоге возвращаем значение получившейся суммы
        return sum;
    }

    // т.к. тут есть обработка
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
    // по параметрам ID и transaction
    // CTRL+ALT+L сочетания для форматирования кода
    // решили для редактирования не использовать корректировку каждого поля через
    // конструкцию transaction.setCategory(newTransaction.getCategory(),
    //а просто удалить по id  и заменить
    // для проверки используем transactionByMonth.containsKey(id)
    // проверка через Code Completion Ctrl+_
    // удалить строку CTRL+Y

// после улучшения кода и получения возможности сохранять в файл
// данный метод был дополнен использованием  saveToFile();


    @Override
    public Transaction editTransaction(long id, Transaction transaction) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id))
            //  если нашелся id (containsKey(id))
            {
                transactionByMonth.put(id, transaction);
                // в месяце где нашли id  затираем стрые значения полей новыми
                saveToFile();
                return transaction;
                // возвращает новое значение
            }
        }
        return null;
    }

    //
    @Override
    // если найдена по id то будет удалена, но перед этим показана пользователю (что удаляется), выходит истина
    // , иначе ложь

    public boolean deleteTransaction(long id) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id)) {
                transactionByMonth.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAllTransaction() {
        transactions = new TreeMap<>();
    }


    // добавляем два приватных метода чтения и записи в файл трансакций, их нельзя вызывать из контроллера
    // первый private void saveToFile()
    // используем мапер ObjectMapper из библиотеки jackson для работы с json ,
    // используем его для перевода карты Transaction в json объект с помощью метода writeValueAsString(transactions)
    // переводится "записать значение как строку"
    // IDEA окрасила красным созданный метод так как нет обработки возможных ошибок
    // поэтому у нас есть два варианта написать throws  и указать что исключение может произойти
    //  тогда тот кто вызывает этот метод будет его обрабатывать
    // либо выбрать через контекстное меню по красной лампе
    // Surround with try/catch и автоматически обернули его в RuntimeException
    // по writeValueAsString   -> ALT+ENTER -> Introduce local variable, то есть "Ввести локальную переменную"
    // наименование переменной задали  json
    //  если try то вызываем из filesService метод saveToFile(json)
    //  он становится доступным для использования тут только если прописаны в impl
    //  и просунут в сервесе FilesService с помощью аннатации @Override над ним  в скобках параметр написали json
    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(transactions);
            filesService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // на выбранном методе filesService.readFromFile() ->ALT+ENTER ->Introduce local variable
    // наименование переменной задали  json мы его читаем и должны снова превратить в карту
    // для этого выбираем метод из того же ObjectMapper для перевода json в Map
    // только для чтение readValue и преобраования json ,
    // вторым параметром тут пишут во что преобразовываем - это класс TypeReference (переводится как Ссылка на тип) из библиотеки jackson
    // а внутри пишем какого вида конечный объект
    // (в нашем случае это тримап где  по месяцам соответственно списки с уточнением порядка добавления из id типа long  и транзакций)
    // ранее нами было определено для сущности трансакция
    // private static Map<Month, Map<Long, Transaction>> transactions = new TreeMap<>();
    // и был определен метод добавления трансакции public long addTransaction(Transaction transaction) {
    //Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>())
    // то есть мы определили что у нас внешний TreeMap содержит набор ежемесячных LinkedHashMap
    //  проставляем ; и делаем обработку исключений
    // после этого IDEA нашла несоответствие типов, при этом можно сделать еще одно приведение типов , либо исправить
    // private static Map<Month, Map<Long, Transaction>> transactions = new TreeMap<>(); на
    // private static TreeMap<Month, LinkedHashMap<Long, Transaction>> transactions = new TreeMap<>(); что мы и сделали
    // пересенной transactions присвоили результат работы ObjectMapper, но
    // вылезла ошибка  в поле privat static Map.... и методе public long addTransaction(Transaction transaction) {
    // там уточнили TreeMapp  и  Map до LinkedHashMap<Long, Transaction>
    // теперь надо добавить   использование созданного метода saveToFile() в addTransaction и editTransaction(long id, Transaction transaction)
    // чтоб работал метод readFromFile() мапер  в класс Transaction добавить анатацию @NoArgsConstructor чтоб был заранее пустой конструктор
    // для проверки сохранения в файл ПКМ  BudgetAp проекта -> Reload from Disk (Перезагрузить с диска)
    // Так как с первого раза файл не сохранился добавили после вывода ошибки
    // e.printStackTrace где мы игнорировали ошибку
    private void readFromFile() {
        String json = filesService.readFromFile();
        try {
            transactions = new ObjectMapper().readValue(json, new TypeReference<TreeMap<Month, LinkedHashMap<Long, Transaction>>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    // данный метод инициализирует чтение из файла после запуска приложения тк есть анатация @PostConstruct (Почтовая конструкция)
    // то есть он запускается автоматически после запуска приложения в нем и вызываем метод readFromFile() чтоб
    // в текущую оперативную память прочитал данные из долгосрочной памяти (из сохраненного на компьюторе файла)
    @PostConstruct
    private void init() {
        readFromFile();
    }
    @Override
public void addTransactionsFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line=reader.readLine()) !=null) {
                String[] array= StringUtils.split(line, '|');
                Transaction transaction = new Transaction(Category.valueOf(array[0]), Integer.valueOf(array[1]), array[2]);
            addTransaction(transaction);
            }
        }
    }
}

