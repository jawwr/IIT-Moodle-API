package com.example.newsService.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
@Entity
public class News {
    @Id
    private Integer id;
    @Column(name = "text")
    private String text;
    @Column(name = "photo")
    private String photo;
}