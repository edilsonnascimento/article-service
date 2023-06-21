package dev.ednascimento.articleservice.controller;

import dev.ednascimento.articleservice.exception.BusinessException;
import dev.ednascimento.articleservice.model.Article;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final List<Article> articles = new ArrayList<>();

    @PostConstruct
    private void init(){
        var article = new Article(1, "Hello word!", "This is my fisrt blog post about SpringBoot.");
        articles.add(article);
    }

    @GetMapping
    public List<Article> findAll() {
        return articles;
    }

    @GetMapping("/{id}")
    public Optional<Article> findById(@PathVariable Integer id) throws BusinessException {
        return Optional.ofNullable(articles.stream()
                .filter(article -> article.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Not found")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Article article) {
        articles.add(article);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Article article, @PathVariable Integer id) {
        var currentArticle = articles.stream().filter(a -> a.id().equals(id)).findFirst();
        currentArticle.ifPresent(valueArticle -> articles.set(articles.indexOf(valueArticle), article));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        articles.removeIf(article -> article.id().equals(id));
    }
}