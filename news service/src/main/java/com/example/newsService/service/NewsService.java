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

    public List<News> getAllNews() {
        int count = repository.NewsCount();
        Integer lastIdInDB = repository.findMaximumId();
        int lastId = lastIdInDB == null ? 0 : lastIdInDB;
        if (count != 0 && parser.checkNewNewsExist(lastId)){
            return repository.findAll();
        }
        List<News> news = parser.parse(lastId);
        saveAllNews(news);
        return repository.findAll();
    }
    public void saveNews(News news){
        repository.save(news);
    }

    public void saveAllNews(List<News> news){
        repository.saveAll(news);
    }

    public List<News> getNewsAfterId(Integer id) {
        if (id == null) {
            var last = repository.findMaximumId();
            return repository.findNewsAfter(last + 1);
        }
        return repository.findNewsAfter(id);
    }
    public List<News> getNewsAfterId() {
        var last = repository.findMaximumId();
        return repository.findNewsAfter(last + 1);
    }
    @Scheduled(cron = "0 0 4 * * *")
    private void checkNewsPhoto(){
        var allNews = getAllNews();
        List<News> crashedPhotoList = new ArrayList<>();
        for (var news : allNews){
            if (!checkNormalConnection(news.getPhoto())){
                crashedPhotoList.add(news);
            }
        }
        var list = parser.parse();
        for (var news : crashedPhotoList){
            var n = list.stream().filter(x -> x.getId() == news.getId()).findFirst().get();
            if (n != null){
                repository.updateNewsPhoto(n.getPhoto(),n.getId());
            }
        }

    }
    private boolean checkNormalConnection(String path){
        int responseCode = 0;
        try{
            URL uri = new URL(path);
            var con = (HttpURLConnection) uri.openConnection();
            responseCode = con.getResponseCode();
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseCode == 200;
    }
}
