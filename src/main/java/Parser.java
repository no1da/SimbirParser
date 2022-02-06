import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Scanner;

public class Parser {

    //метод получения Document из ссылки
    public static Document getPage() throws IOException {
        //получаем отпользователя адрес страницы
        Scanner console = new Scanner(System.in);
        System.out.println("Введите адрес старницы. Пример:jsoup.org");
        String user = console.nextLine();
        String www = "https://";
        String url = www + user;
        //создаём Document, парся его из url
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static void main(String[] args) throws IOException {
        //создаём массив для заполнения его сепараторами
        String[] separator = {" ", ",", ".", "?","!", ";", ":","\n", "\t", "\r", "[", "]", "(", ")"};
        String pagetext = getPage().text();
        String separatorsString = String.join("|\\", separator);
        //создаём словарь ключ строка, значение класс Word ниже
        Map<String, Word> countMap = new HashMap<String, Word>();
        //создаем поток побайтового чтения данных из pageText
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pagetext.getBytes(StandardCharsets.UTF_8))));
        String line;
        //в цикле while считываем построчно поток данных
        while ((line = reader.readLine()) != null) {
            //создаем массив слов из каждой строки
            String[] words = line.split(separatorsString);
            //в цикле for проходимся по каждому слову
            for (String word : words) {
                //проверка на пустое слово
                if ("".equals(word)) {
                    continue;
                }
                //инициализируем объект Word, пытаясь получить его из map (по ключу получить значение)
                Word count = countMap.get(word);
                //если по такому ключу нет элемента, то создаём новый
                if (count == null) {
                    count = new Word();
                    count.word = word;
                    count.count = 0;
                    //и записываем созданный объект в map
                    countMap.put(word, count);
                }
                //увеличиваем счётчик каждого слова
                count.count++;
            }
        }
        //закрываем поток чтения
        reader.close();
        //вывод значений из map
        for (Word word : countMap.values()) {
            System.out.println(word.count + "\t" + word.word);
        }
    }
    public static class Word implements Comparable<Word> {
        String word;
        int count;
        public int compareTo(Word b) { return b.count - count; }
    }
}
