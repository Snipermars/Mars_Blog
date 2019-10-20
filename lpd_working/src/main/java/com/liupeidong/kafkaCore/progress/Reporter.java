package com.liupeidong.kafkaCore.progress;

import com.liupeidong.kafkaCore.constant.Config;
import com.liupeidong.kafkaCore.constant.Constant;

/**
 * Reporter class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:47
 */
public abstract class Reporter {

    public static void print(Object obj, boolean newLine) {
        if (newLine) {
            System.out.println(obj);
        }else {
            System.out.print(obj);
        }
    }

    public static String getSign(boolean isProducer) {
        if (isProducer) {
            return Constant.PRODUCER_SIGN;
        } else {
            return Constant.CONSUMER_SIGN;
        }
    }


    public abstract void report(int progress, Config config, boolean isProducer);
}
