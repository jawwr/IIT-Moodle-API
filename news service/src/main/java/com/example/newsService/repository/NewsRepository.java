package com.example.newsService.repository;

import com.example.newsService.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query(value = "SELECT COUNT(id) FROM News")
    int NewsCount();
    @Query(value = "SELECT MAX(id) FROM News")
    Integer findMaximumId();
}