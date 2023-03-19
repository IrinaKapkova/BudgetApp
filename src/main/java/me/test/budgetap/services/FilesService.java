package me.test.budgetap.services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public interface FilesService {
    // далее определяем методы данного класса
    // этот метод записывает файл определенного типа , а именно строку
    //  не забывать пробросить в сервис
    boolean saveToFile(String json);

    // тут указываем в зависимости от файла который надо прочитать тип read.... в () просто путь
    //  оставили автоматически сгенерированную обработку ошибки с выбросом RuntimeException
    //добавили  служебное слово return перед Files.readString(Path.of(dataFilePath));
    String readFromFile();

    File getDataFile();
}
