package me.test.budgetap.controllers;

import me.test.budgetap.services.FilesService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

// аннатация @RestController определяет методы
// заинжектили с  сервисом FilesService
// и добавили через ALT+ENTER  и выбор генерацию конструктор
// добавили RequestMapping в виде /files  для точной индивидуальности URL
@RestController
@RequestMapping("/files")
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    // создаем метод downloadDataFile "скачать файл данных"
    // первый эндпоинт это экспорт ResponseEntity "Объект ответа" производится, если файл существует
    // для экспорта используем поток как ресурс InputStreamResource "Ресурс входного потока",
    // используем инфу о самом файле пишем
    // FilesService.getDataFile -> Alt Inter->introduce local variable "ввести локальную переменную"-> назвали переменную file
    //  есть обработка ошибок: пишем -> if (file.exists()) "Если файл существует"
    // создадим  InputStreamResource "Ресурс входного потока" для пользователя
    // из существующего файла открывая поток, но читать его не будем , а используем как ресурс для пользователя
    //Тут надо обработать ошибки-> FileInputStream->Add exception to method signature "Добавить исключение в сигнатуру метода"->
    // IDEA предлагает в том числе "выдает исключение "файл не найден"-> public ResponseEntity<InputStreamResource> downloadDataFile() throws FileNotFoundException, его и выбираем
    //  иначе ResponseEntity.noContent().build() "Объект ответа.Нет контента(статус 204).строить()"
    // в теле передаем результат если все ок после,
    // для этого после return ResponseEntity.ok() "Объект ответа.ok" через точку все что выдаем в этом случае
    // в тело ответа сам выбранный ресурс  .body(resource);
    // .contentLength(file.length()) нужен чтоб передать ВЕСЬ файл указываем его длину "длина содержимого (файл. длина ())"
    //  тут заголовок запроса .contentType(MediaType.APPLICATION_JSON) говорит о том что мы передем json "Тип контента (Тип носителя. ПРИЛОЖЕНИЕ JSON"
    //  заголовок добавленный вручную через метод .header
    //  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=\"TransactionsLog.json\"")
    //  .заголовок (заголовки HTTP.СОДЕРЖАНИЕ, " вложение ; имя файла =\" Журнал транзакций. json\
    //  утилитный класс HttpHeaders который содержит разные константы нам нужно "СОДЕРЖАНИЕ" CONTENT_DISPOSITION,
    //  он передает информацию о контенте пишем что это вложение attachment и его надо скачивать
    //  определяем имя файла (экранируем ковычки) и тип filename=\"TransactionsLog.json\"") ,
    //  те он отвечает за название и тип сохраняемого файла
    //  можно вместо .contentType(MediaType.APPLICATION_JSON) в return было написать в анатации
    //  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    //  "производит = Тип носителя. ПРИЛОЖЕНИЕ _ JSON _ ЗНАЧЕНИЕ)"
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> downloadDataFile() throws FileNotFoundException {
        File file = filesService.getDataFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=\"TransactionsLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
            //  скачать нечего, так как нет пока файла
        }
    }

    // Метод для импорта нашего файла в веб приложение
//  ResponseEntity<Void> uploadDataFile(@RequestParam MultipartFile file)
// "Сущность ответа <пустота> загрузить файл данных (параметр запроса  составной файл файл)"
//применяем если файл короткий так как пользователь инициализировал поток его читаем file.getInputStream();
// File dataFile = filesService.getDataFile();
// "Файл данныхФайл = службаФайлов . получить файл данных()";
//  new FileOutputStream(dataFile);
//  "новый потокВыводаФайлов (файлДанных);
//  мы сначала чистим файл filesService.cleanDataFile();
//  , а потом в него импортируем прочтенный из потока контент
//  мы должны закрыть поток  для этого применяется конструкция try "попытка"  с ресурсами,
//  то есть в параметрах в скобках try наша переменная (fos)  если набрать FOS выдаст -> FileOutputStream
//  получили строку  try (FileOutputStream fos= new FileOutputStream(dataFile))
//              "попытка (поток вывода файлов fos = новый поток вывода файлов (файл данных)) {"
//
//              по FileOutputStream->Alt+Inter -> add 'catch' clause(S) "добавить пункт (S) «поймать»"
//              ->  } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        "} поймать (файл не найден, исключение e) {
//            создать новое исключение времени выполнения (e);
//        } поймать (исключение ввода-вывода e) {
//            создать новое исключение времени выполнения (e);
//        }"

    //    В итоге получили кусок кода
//          @PostMapping("/import")
//    public ResponseEntity<Void> uploadDataFile(@RequestParam MultipartFile file) {
//        filesService.cleanDataFile();
//        File dataFile = filesService.getDataFile();
//        try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
//             FileOutputStream fos = new FileOutputStream(dataFile);
//             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
//            byte[] buffer = new byte[1024];
//            while (bis.read(buffer) > 0) {
//                bos.write(buffer);
//            }
//        } catch (
//                FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (
//                IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//        чтоб сократить код скопируем зависимость на https://mvnrepository.com/ -> Apache Commons IO-> version 2.11.0

//    в обработке ошибок  заменили на вывод e.printStackTrace();
//
//  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//"вернуть объект ответа. статус (Статус HTTP.ВНУТРЕННЯЯ_СЕРВЕРНАЯ_ОШИБКА).строить();"
//  исправили, дополнили  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//                                                      "потребляет = тип носителя. СОСТАВ _ ФОРМА _ ДАННЫЕ _ ЗНАЧЕНИЕ)"

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDataFile(@RequestParam MultipartFile file) {
        filesService.cleanDataFile();
        File dataFile = filesService.getDataFile();

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return  ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

