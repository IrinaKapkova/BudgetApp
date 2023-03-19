package me.test.budgetap.model;
// данные библиотеки lombok генерируют при запуске приложения  конструктор со всеми полями
// хэшкод иквалс, toString, getter, setter и пустой конструктор
// эту библиотеку мы добавили в проект при создании , проставив галочку в нужном артифакте
// если этого не сделать в уже действующий проект подключаем библиотеку
// для этого в pom.xml файл были добавлены зависимости из мавен  https://mvnrepository.com/
// сначала найти библиотеку внутри выбрать рабочую версию
// в ней на закладке Maven  скопировать зависимость и вставить копию в pom.xml файл после <dependencies>
// перезапустить мавен
// АРТЕМ советовал еще  удалить в pom.xml то, что написано он <plugin> до <plugin> включительно.
// Чтобы подключить именно те аннотации, что ты используешь, необходимо подключить следующую зависимость:
//<dependency>
//    <groupId>javax.validation</groupId>
//    <artifactId>validation-api</artifactId>
//    <version>2.0.1.Final</version>
//</dependency>
//Поместив между тегами <dependencies> и </dependencies> наравне с другими зависимостями.
// После добавления зависимости нужно во вкладке Maven (в правой части окна) нажать на кнопку "Reload all Maven Projects".
// После этого аннотации уже можно будет использовать.
// если указано  <optional>true</optional> в данном случае говорит о том
// что проект может запуститься и без этой зависимости, если не удалось скачать, обновить или так далее
// тег типа этого <version>2.5</version> может быть добавлен для фиксации версии
// чтоб не требовалось обновлять артифакт до последней версии при каждой загрузке проекта с репозитория
// если все сделано правильно в External Libraries появится указанная версия библиотекки которая уже используется в проекте


// чтоб вернуть последнее неправильное исправление на закладке активной CTRL+Z
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//toString+хешкод и иквалс, гетеры и сеторы, пустой конструктор
@AllArgsConstructor
// конструктор со всеми полями
@NoArgsConstructor
// пустой конструктор чтоб было сохранение в файл для маппера
// для проверки в swagger post запрос запускаем, в приложении проверяем что не сломалось ничего,
// далее правой кнопкой мыши на левой части IDEA  где Project выбираем пункт Reload from Disk (Перезагрузить с диска)
//  ничего не сохранилось, исправили ошибки
public class Transaction {
    private  Category category;
    private int sum;
    private String comment;


}
