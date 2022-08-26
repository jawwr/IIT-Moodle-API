package com.example.newsService.controller;

import com.example.newsService.Entity.News;
import com.example.newsService.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с новостями
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService service;

    @Autowired
    public NewsController(NewsService service) {
        this.service = service;
    }

    /**
     * Метод для получения всех новостей из базы данных
     * @return {@link List}
     */
    @GetMapping("/all")//
    public List<News> getAllNews(){
        return service.getAllNews();
    }
    /**
     * Метод для получения новостей после указанного  id
     * @return {@link List}
     */
    @GetMapping("/after/{id}")
    public List<News> getLastNews(@PathVariable("id") Integer id){
        return service.getNewsAfterId(id);
    }
    /**
     * Метод для получения последних новостей
     * @return {@link List}
     */
    @GetMapping("/last")
    public List<News> getLastNews(){
        return service.getLastNews();
    }
}
