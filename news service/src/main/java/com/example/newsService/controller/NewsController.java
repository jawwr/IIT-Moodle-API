package com.example.newsService.controller;

import com.example.newsService.Entity.News;
import com.example.newsService.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private NewsService service;

    @Autowired
    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<News> getAllNews(){
        return service.getAllNews();
    }
}
