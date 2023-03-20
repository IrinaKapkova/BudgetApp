package me.test.budgetap.services.impl;

import me.test.budgetap.services.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServicesimpl implements FilesService {
    // чтобы подтянуть из файла application.properties
    // как данность куда сохранять файлы путь path.to.data.file=/src/main/resources
    // и в каком виде их сохранять name.of.data.file=data.json
    // необходимо перед полями класса прописать аннатацию из библиотеки
    // org.springframework.beans.factory.annotation  @Value
    // при этом в () пишем в ковычках "" ${CTRL+пробел и выбрать необходимые данные}
    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("${name.of.data.file}")
    private String dataFileName;

    // далее определяем методы данного класса
    // этот метод записывает файл определенного типа , а именно строку
    //  не забывать пробросить в сервис с помощью аннатации @Override и выбора по красной лампе далее Pull metode ....

    @Override
        public boolean saveToFile(String json) {
        // перед записью нужно удалить имеющийся файл
        // тут выбрать необходимый тип создаваемого файла Files.write неизменное начало,
        // далее может следовать массив  байт из 8 бит, итерабол это коллекция строк,
        // или если знаем, что только одна строка то string
        // если буферизированная передача то newBufferedWriter
        // далее в  виде подсказки выходят возможные параметры которые мы можем указать
        // путь, имя опции сохранения необходимые параметры
        //  тут тоже нужно внедрить обработку ошибок
        // IDEA окрасила красным созданный метод так как нет обработки возможных исключений,ошибок
        // поэтому у нас есть два варианта написать throws  и указать что исключение может произойти
        //  тогда тот кто вызывает этот метод будет его обрабатывать
        // либо выбрать через контекстное меню по красной лампе
        // Surround with try/catch и автоматически обернули его в RuntimeException
        // throw new RuntimeException(e)  удалили с кода почему-то после условия catch (IOException e)
        // в выполняемом коде оставили только return false, а при истине оставили return true;
        // в качестве обработки написали return false;
        try {
            cleanDataFile();
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // тут указываем в зависимости от файла который надо прочитать тип read.... в () просто путь
    //  оставили автоматически сгенерированную обработку ошибки с выбросом RuntimeException
    //добавили  служебное слово return перед Files.readString(Path.of(dataFilePath));
    @Override
    public String readFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//ниже расположенные методы были сначала только внутренними,
// нужными для того чтоб указанные публичные методы работали нормально (декомпозиция методов) ,
// тк решили применить cleanDataFile для импорта файла через http, сделали public
// и добавили анатацию @Override-> Pull method cleanDataFile() to  FilesService
    @Override
    public boolean cleanDataFile() {
// данный метод удаляет файл только если он уже существует
        // Path.of это путь, в скобках пишем (выбираем) ранее прописанный путь,
        // IDEA окрасила красным созданный метод так как нет обработки возможных исключений,ошибок
        // поэтому у нас есть два варианта написать throws после имени метода
        // private boolean cleanDataFile() throws
        // и указать, что исключение может произойти
        //  тогда тот кто вызывает этот метод будет его обрабатывать
        // либо выбрать через контекстное меню по красной лампе
        // Surround with try/catch (обернуть в поймать ошибку) и автоматически обернули его в RuntimeException
        // после catch  дописываем return false;
        // после try дописываем в конце блока return true;
        // так как путь Path.of(dataFilePath) у нас дублируется надо его вынести как переменную после try
        // ставим ;  далее по ней левой кнопкой и  ALT+Enter  выбираем Introduce local variable
// получаем  Path path = Path.of(dataFilePath);
        // далее заменим везде (у нас это звучит как Replace all 3 occurrences
        // (есть еще вариант нам тут не нужный : Заменить только это вхождение -Replace this occurrence only)
        // слово of  автоматически выбранное программой для имени переменной заменяем на более понятное  путь - path
        // createFile - метод создается пустой файл
        //  e.printStackTrace() был добавлен чтоб понять ошибку
        try {
            Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //  метод возвращает сам файл без данных , он несет путь и наименование, методанные короче
    // без контента , тут слеш нужен, так как класс старый и тут все вручную пишут
    @Override
    public File getDataFile (){
        return new File(dataFilePath+"/" + dataFileName);
    }


}
