package com.example.yuanping.feihuaapp.bean;

/**
 * Created by yuanping on 2/3/18.
 * 从数据库查询到的诗词信息
 * 标题,作者,朝代,原文,原文2
 */

public class Poetry {
    private String title;
    private String author;
    private String dynasty;
    private String original;//原文
    private String original2;//原文2

    public Poetry(String title, String author, String dynasty, String original, String original2) {
        this.title = title;
        this.author = author;
        this.dynasty = dynasty;
        this.original = original;
        this.original2 = original2;
    }

    @Override
    public String toString() {
        return "Poetry{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", dynasty='" + dynasty + '\'' +
                ", original='" + original + '\'' +
                ", original2='" + original2 + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginal2() {
        return original2;
    }

    public void setOriginal2(String original2) {
        this.original2 = original2;
    }


}
