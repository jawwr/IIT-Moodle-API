package com.example.newsService.parser;

import com.example.newsService.Entity.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsParser {

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

    private String getNewsPhoto(Element el) {
        var elementWithPhoto = el.getElementsByClass("tgme_widget_message_photo_wrap").first();
        if (elementWithPhoto == null) {
            return "";
        }
        var element = elementWithPhoto.attr("style");
        return element.split("url")[1].replace("('", "").replace("')", "");
    }

    private int getNewsId(Element el) {
        var attr = el.getElementsByClass("tgme_widget_message").attr("data-post");
        return Integer.parseInt(attr.split("/")[1]);
    }
}
