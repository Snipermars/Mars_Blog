package com.liupeidong.kafkaCore.progress;

import com.liupeidong.kafkaCore.constant.Config;

/**
 * OneByOneReporter class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:45
 */
public class OneByOneReporter extends Reporter {


    @Override
    public void report(int progress, Config config, boolean isProducer) {

        print(getSign(isProducer), false);

    }
}
