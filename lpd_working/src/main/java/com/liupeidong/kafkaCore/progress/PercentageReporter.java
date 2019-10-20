package com.liupeidong.kafkaCore.progress;

import com.liupeidong.kafkaCore.constant.Config;

/**
 * PercentageReporter class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:46
 */
public class PercentageReporter extends Reporter {

    @Override
    public void report(int progress, Config config, boolean isProducer) {


        if (config.getProgressInterval() > 0) {
            if (progress % config.getProgressInterval() == 0) {

                print(getSign(isProducer, progress, config.getTotalCount()), false);
            }

            return;
        }


        //间隔 <= 0,则没有间隔，退化为 ONE_BY_ONE 策略
        print(getSign(isProducer, progress, config.getTotalCount()) , false);


    }


    private String getSign(boolean isProducer, int progress, int total) {
        double percent = ((progress * 1.0) / total) * 100;
        if (isProducer) {
            return  "(" + getSign(isProducer) + " %" + percent + ")  ";
        }

        return "(%" + percent + " " + getSign(isProducer) + ")  ";

    }

}
