package com.example.newsService.controller;

import com.example.newsService.Entity.News;
import com.example.newsService.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с новостями
 */
@RestController
@RequestMapping("/api/news")
@Api(description = "Controller for work with telegram news")
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
    @GetMapping("/all")
    @ApiOperation("Getting all news")
    public List<News> getAllNews(){
        return service.getAllNews();
    }
    /**
     * Метод для получения новостей после указанного  id
     * @return {@link List}
     */
    @GetMapping("/after/{id}")
    @ApiOperation("Getting news after the specified news id")
    public List<News> getLastNews(@PathVariable("id") Integer id){
        return service.getNewsAfterId(id);
    }
    /**
     * Метод для получения последних новостей
     * @return {@link List}
     */
    @GetMapping("/last")
    @ApiOperation("Getting last news")
    public List<News> getLastNews(){
        return service.getLastNews();
    }
}
