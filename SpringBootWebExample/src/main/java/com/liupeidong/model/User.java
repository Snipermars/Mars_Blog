package com.liupeidong.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: Marus
 * @Description:
 * @Date: 2021/1/25 18:17
 * @Version:
 * @Modified:
 **/
@Data
public class User {

    private int userId;

    private String userName;

    private String userLoginName;

    private String userSex;

    private Integer userAge;

    private Date userBirth;

    private String userJob;

}
