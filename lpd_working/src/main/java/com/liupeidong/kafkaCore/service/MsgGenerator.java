package com.liupeidong.kafkaCore.service;

import com.liupeidong.kafkaCore.constant.Config;

/**
 * MsgGenerator class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:54
 */
public interface MsgGenerator {

    public String getMsg(Config config);

}
