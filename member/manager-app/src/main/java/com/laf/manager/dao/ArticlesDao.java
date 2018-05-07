package com.laf.manager.dao;

import com.laf.manager.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticlesDao {

    @Autowired
    ArticlesRepository repository;

    public int saveArticle(String content, int mallId) {
        return repository.insertArticle(content, mallId);
    }

    public int editArticle(int articleId, String content) {
        return repository.updateArticle(articleId, content);
    }

    public int deleteArticle(final int articleId) {
        return repository.deleteArticle(articleId);
    }
}
