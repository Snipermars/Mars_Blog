package com.liupeidong.kafkaCore.service;

import com.liupeidong.kafkaCore.constant.Config;
import com.liupeidong.kafkaCore.exception.IllegalParameterException;
import com.liupeidong.kafkaCore.progress.ProgressReportClient;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * ProduceThread class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:33
 */
public class ProduceThread implements Runnable {

    private Config config;
    private Container<ProducerRecord<byte[], byte[]>> container;
    private volatile CountDownLatch produceSwitch;
    private MsgGenerator msgGenerator;

    public ProduceThread(Config config, Container<ProducerRecord<byte[], byte[]>> container, CountDownLatch produceSwitch) {
        this.config = config;
        this.container = container;
        this.produceSwitch = produceSwitch;

        try {
            msgGenerator = (MsgGenerator) Class.forName(config.getMsgGenerateClass()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalParameterException("无法加载消息生成的实现类");
        }
    }


    @Override
    public void run() {
        for (; ; ) {

            //因为并发的原因，使用=号会导致countDown()调用遗漏
            if (container.getProduceCount() >= config.getiInitialCount()) {
                if (produceSwitch != null) {
                    produceSwitch.countDown();
                    //防止在区间[initialCount, totalCount]内出现多次countDown()调用，导致发送线程被过早唤醒
                    produceSwitch = null;
                }
            }

            if (container.getProduceCount() > config.getTotalCount()) {
                break;
            }

            try {
                // new SimpleMsgGenerator() ， 这里每次都创建一个实例，可以优化
                String  msg = msgGenerator.getMsg(config);
                ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(config.getTopic(), msg.getBytes("UTF-8"));
                container.offer(record);
                ProgressReportClient.report(container.getProduceCount(), config, true);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
