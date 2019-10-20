package com.liupeidong.kafkaCore.service;

import com.liupeidong.kafkaCore.constant.Config;

/**
 * SimpleMsgGenerator class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:59
 */
public class SimpleMsgGenerator implements MsgGenerator {

    @Override
    public String getMsg(Config config) {
        return "hello kafka";
    }
}
