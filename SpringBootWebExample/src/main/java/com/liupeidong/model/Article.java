package com.liupeidong.model;

import lombok.Data;

import java.sql.Blob;
import java.util.Date;

/**
 * @Author: Marus
 * @Description:
 * @Date: 2021/1/25 18:19
 * @Version:
 * @Modified:
 **/
@Data
public class Article {

    private int articleId;

    private String title;

    private String content;

    private Date createDt;

    private Date updateDt;

    private String descp;

    private Blob img;

    private String imgUrl;

}
