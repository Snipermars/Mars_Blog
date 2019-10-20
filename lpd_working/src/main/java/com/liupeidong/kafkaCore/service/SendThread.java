package com.liupeidong.kafkaCore.service;

import com.liupeidong.kafkaCore.constant.Config;
import com.liupeidong.kafkaCore.progress.ProgressReportClient;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * SendThread class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 0006 17:58
 */
public class SendThread implements Runnable {
    private Config config;
    private Container<ProducerRecord<byte[], byte[]>> container;
    private static Properties kafkaProperties = new Properties();
    private static KafkaProducer<byte[], byte[]> kafkaProducer = null;
    private  CountDownLatch consumeStartSwitch;
    private CountDownLatch consumeEndSwitch;


    public SendThread(Config config) {
        this.config = config;
    }

    public SendThread(Config config, Container container, CountDownLatch consumeStartSwitch, CountDownLatch consumeEndSwitch ) {
        this(config);
        this.container = container;
        this.consumeStartSwitch = consumeStartSwitch;
        this. consumeEndSwitch = consumeEndSwitch;

        kafkaProperties.put("bootstrap.servers", config.getBootstrapServers());
        kafkaProperties.put("zookeeper.connector", config.getZookeeperConnector());
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProperties.put("serializer.class", "kafka.serializer.StringEncoder");
        kafkaProperties.put("request.required.acks", config.getRequestRequiredAcks());
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties);



    }

    @Override
    public void run() {

        try {
            consumeStartSwitch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        for (;;) {

            if (container.getConsumeCount() >= config.getTotalCount()) {
                consumeEndSwitch.countDown();
                break;
            }

            ProducerRecord<byte[], byte[]> record = container.get();

            if (record != null) {
                kafkaProducer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                            System.out.println("该条消息发送失败");
                        }

                        ProgressReportClient.report(container.getProduceCount(), config, false);

                    }
                });
                container.addConsumeCount(1);
            }

        }

    }

}
