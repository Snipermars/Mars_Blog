package com.liupeidong.kafkaCore.progress;

import com.liupeidong.kafkaCore.constant.Config;
import org.apache.commons.lang3.StringUtils;

/**
 * ProgressReportClient class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:48
 */
public class ProgressReportClient {

    private final static OneByOneReporter oneByeOne = new OneByOneReporter();
    private final static IntervalReporter interval = new IntervalReporter();
    private final static PercentageReporter percentage = new PercentageReporter();


    public static void report(int progress, Config config, boolean isProducer) {
        //不打印进度，直接返回
        if (!config.printProgress()) {
            return;
        }

        ProgressReportType type = null;
        String strategy = config.getProgressStrategy();
        if (StringUtils.isBlank(strategy)) {
            type = ProgressReportType.ONE_BY_ONE;
        } else {
            type = ProgressReportType.valueOf(strategy);
        }

        switch (type) {
            case INTERVAL:
                interval.report(progress, config, isProducer);
                break;
            case ONE_BY_ONE:
                oneByeOne.report(progress, config, isProducer);
                break;
            case PERCENTAGE:
                percentage.report(progress, config, isProducer);
                break;
            default:
                oneByeOne.report(progress, config, isProducer);
        }
    }

}
