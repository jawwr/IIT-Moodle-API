package com.example.newsService.service;

import com.example.newsService.Entity.News;
import com.example.newsService.parser.NewsParser;
import com.example.newsService.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с новостямииз леты телеграмма
 */
@Service
@EnableScheduling
public class NewsService {
    private final NewsRepository repository;
    private final NewsParser parser;

    @Autowired
    public NewsService(NewsRepository repository, NewsParser parser) {
        this.repository = repository;
        this.parser = parser;
    }

    /**
     * Метод получения всего списка новостей из бд
     */
    public List<News> getAllNews() {
        return repository.findAll();
    }

    /**
     * Метод сохранения списка новостей в бд
     * @param news Список новостей, которое надо сохранить
     */
    public void saveAllNews(List<News> news) {
        repository.saveAll(news);
    }

    /**
     * Метод получения новостей после указаного id
     * @param id id новости после которой надо получить новости
     */
    public List<News> getNewsAfterId(Integer id) {
        if (id == null) {
            var last = repository.findMaximumId();
            return repository.findNewsAfter(last + 1);
        }
        return repository.findNewsAfter(id);
    }

    /**
     * Метод получения последних новостей
     */
    public List<News> getLastNews() {
        var last = repository.findMaximumId();
        return repository.findNewsAfter(last + 1);
    }

    /**
     * Метод парсинга новостей с расписанием
     */
    @Scheduled(cron = "0 */30 * * * *")
    private void checkNewNews(){
        Integer lastIdInDB = repository.findMaximumId();
        int lastId = lastIdInDB == null ? 0 : lastIdInDB;
        if (!parser.checkNewNewsExist(lastId)) {
            System.out.println("parse");
            List<News> news = parser.parse(lastId);
            saveAllNews(news);
        }
    }

    /**
     * Метод проверки новостей с фотографиями на валидность фотографий
     * (сделано потому что телеграмм постоянно меняет адрес фотографий)
     */
    @Scheduled(cron = "0 0 4 * * *")
    private void checkNewsPhoto() {
        var allNews = getAllNews();
        List<News> crashedPhotoList = new ArrayList<>();
        for (var news : allNews) {
            if (!checkNormalConnection(news.getPhoto())) {
                crashedPhotoList.add(news);
            }
        }
        var list = parser.parse();
        for (var news : crashedPhotoList) {
            var n = list.stream().filter(x -> x.getId() == news.getId()).findFirst().get();
            if (n != null) {
                repository.updateNewsPhoto(n.getPhoto(), n.getId());
            }
        }

    }

    /**
     * Метод проверки соединения с сервером
     * @param path путь, который нужно проверить
     * @return true - если соединение нормальное
     * false - если ответ сервера не правильный
     */

    private boolean checkNormalConnection(String path) {
        int responseCode = 0;
        try {
            URL uri = new URL(path);
            var con = (HttpURLConnection) uri.openConnection();
            responseCode = con.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseCode == 200;
    }
}
