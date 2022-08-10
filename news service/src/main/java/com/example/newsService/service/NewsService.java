package com.example.newsService.service;

import com.example.newsService.Entity.News;
import com.example.newsService.parser.NewsParser;
import com.example.newsService.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
//    public News getLastNewsId(){
//        return repository.findMaximumId();
//    }
}
