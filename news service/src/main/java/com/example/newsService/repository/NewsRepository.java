package com.example.newsService.repository;

import com.example.newsService.Entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query(value = "SELECT COUNT(*) FROM News", nativeQuery = true)
    int NewsCount();
    @Query(value = "SELECT MAX(id) FROM News")
    Integer findMaximumId();

    @Query(value = "select * from news where id < :#{#idLast} order by id desc limit 10", nativeQuery = true)
    List<News> findNewsAfter(@Param("idLast") int idLast);

    @Query(value = "UPDATE News SET photo = :#{#photo} WHERE id = :#{#id}", nativeQuery = true)
    void updateNewsPhoto(@Param("photo") String photo, @Param("id") int id);
}