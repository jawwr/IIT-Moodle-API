package com.example.newsService.controller;

import com.example.newsService.Entity.News;
import com.example.newsService.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/after/{id}")
    public List<News> getNewsAfterId(@PathVariable("id") Integer id){
        return service.getNewsAfterId(id);
    }
    @GetMapping("/last")
    public List<News> getNewsAfterId(){
        return service.getNewsAfterId();
    }
}
