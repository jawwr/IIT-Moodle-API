package com.example.newsService.parser;

import com.example.newsService.Entity.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Парсер новостной ленты телеграмма
 * */
@Component
public class NewsParser {

    /**
    * Метод для проверки существования новостей после указанного id
     * @param id id новости после которого нужно проверить существование следующих новостей
     * @return true если новость с указанным id оказалась последней в ленте/
     * false если после указанной новости есть еще новости
    * */
    public boolean checkNewNewsExist(int id) {
        boolean isLast = true;
        try {
            Document document = Jsoup.connect("https://t.me/s/iit_csu").get();
            var elements = document.body().getElementsByClass("tgme_widget_message_wrap");
            var lastIdElement = elements.get(elements.size() - 1).getElementsByClass("tgme_widget_message").first();
            isLast = id == getNewsId(lastIdElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLast;
    }

    /**
     * Метод парсинга всех новостей
     * @return {@link List<News>}
     */
    public List<News> parse() {
        List<News> news = new ArrayList<>();
        try {
            Document document = Jsoup.connect("https://t.me/s/iit_csu").get();
            news = findInfoFromHtml(document.body(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    /**
     * Метод парсинга новостей после указанной новости
     * @param parseAfterId id новости после которой нужно спарсить данные
     * @return {@link List<News>}
     */
    public List<News> parse(int parseAfterId) {
        List<News> news = new ArrayList<>();
        try {
            Document document = Jsoup.connect("https://t.me/s/iit_csu").get();
            news = findInfoFromHtml(document.body(), parseAfterId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    /**
     * Получение информации из html разметки
     * @param element элемент html разметки
     * @param parseAfterId id новости после которой нужно искать информацию
     * @return {@link List<News>}
     */
    private List<News> findInfoFromHtml(Element element, int parseAfterId) {
        List<News> news = new ArrayList<>();
        var elements = element.getElementsByClass("tgme_widget_message_wrap");
        for (Element el : elements) {
            int newsId = getNewsId(el);
            if (newsId < parseAfterId) {
                continue;
            }

            String photo = getNewsPhoto(el);
            String text = getNewsText(el);
            if (text.length() > 650){
                text = text.substring(0,650);
            }

            if (text.equals("") && photo.equals("")) {
                continue;
            }

            news.add(new News(newsId, text, photo));
        }
        return news;
    }

    /**
     * Получение из html разметки текста новости
     * @param el элемент html разметки
     * @return {@link String}
     */
    private String getNewsText(Element el) {
        var element = el.getElementsByClass("tgme_widget_message_text").text();
        String text;
        if (element.startsWith("http")) {
            text = el.getElementsByClass("link_preview_description").text();
        } else {
            text = element;
        }
        return text;
    }
    /**
     * Получение из html разметки фотографию новости
     * @param el элемент html разметки
     * @return {@link String}
     */
    private String getNewsPhoto(Element el) {
        var elementWithPhoto = el.getElementsByClass("tgme_widget_message_photo_wrap").first();
        if (elementWithPhoto == null) {
            return "";
        }
        var element = elementWithPhoto.attr("style");
        return element.split("url")[1].replace("('", "").replace("')", "");
    }
    /**
     * Получение из html разметки id новости
     * @param el элемент html разметки
     * @return {@link Integer}
     */
    private int getNewsId(Element el) {
        var attr = el.getElementsByClass("tgme_widget_message").attr("data-post");
        return Integer.parseInt(attr.split("/")[1]);
    }
}
