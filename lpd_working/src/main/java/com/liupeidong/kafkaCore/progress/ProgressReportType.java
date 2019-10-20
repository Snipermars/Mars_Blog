package com.liupeidong.kafkaCore.progress;

/**
 * ProgressReportType class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:48
 */
public enum ProgressReportType {

    //一条消息报告一次进度
    ONE_BY_ONE,

    //间隔一定消息后才报告一次
    INTERVAL,

    //按照百分比汇报
    PERCENTAGE;
}
