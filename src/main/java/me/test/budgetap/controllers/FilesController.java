package me.test.budgetap.controllers;

import me.test.budgetap.services.FilesService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    // первый эндпоинт экспорт ResponseEntity Объект ответа
    // если файл существует
    // для экспорта используем поток как ресурс
    //  есть обработка ошибок,
    // в теле передаем результат если все ок
    // .contentLength(file.length()) нужен чтоб передать ВЕСЬ файл
    //  тут заголовок запроса .contentType(MediaType.APPLICATION_JSON) говорит о том что мы передем json
   //  заголовок добавленный вручную через метод .header
    //  утилитный класс HttpHeaders который содержит разные константы нам нужно СОДЕРЖАНИЕ CONTENT_DISPOSITION,
    //  он передает информацию о контенте пишем что это вложение attachment и его надо скачивать
    //  определяем имя файла  и тип filename=\"TransactionsLog.json\"") , те он отвечает за название и тип сохраняемого файла
    @GetMapping("/export")
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
//    @PostMapping("/import")
//    public ResponseEntity<Void>uploadDataFile(RequestParam MultipartFile file){
//        file.getInputStream();
//    }
////public ResponseEntity<Void> uploadDataFile(RequestParam MultipartFile file){
////file.
////    }
}

