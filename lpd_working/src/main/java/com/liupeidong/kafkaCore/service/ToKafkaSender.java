package com.liupeidong.kafkaCore.service;

import org.apache.kafka.clients.producer.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * ToKafkaSender class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 17:29
 */
public class ToKafkaSender {
    private static Properties kafkaProperties = new Properties();
    private static KafkaProducer<byte[], byte[]> kafkaProducer = null;

    @Before
    public void init() {

        String ip = "10.16.70.211";
        /**
         * 用于自举（bootstrapping ），producer只是用它来获得元数据（topic, partition, replicas）
         * 实际用户发送消息的socket会根据返回的元数据来确定
         */
        // kafkaProperties.put("metadata.broker.list", "vsp13:9092");
        kafkaProperties.put("bootstrap.servers", ip.trim() + ":9092");
        kafkaProperties.put("zookeeper.connector", ip.trim() + ":2181");
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        kafkaProperties.put("serializer.class", "kafka.serializer.StringEncoder");
        /**
         * producer发送消息后是否等待broker的ACK，默认是0
         * 1 表示等待ACK，保证消息的可靠性
         */
        kafkaProperties.put("request.required.acks", "1");
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties);
    }


    @Test
    public void testSend() {
        long now = System.currentTimeMillis();
        for(int i = 0;  i < 20; i++) {
            send();
        }
        System.out.println(System.currentTimeMillis() - now);
    }

    private void send() {
        String msg = getMsg();
        ProducerRecord<byte[], byte[]> record = null;


        try {
            //MY_TOPIC 是kafka集群中的某个topic
            record = getProducerRecord("MY_TOPIC",  msg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    System.out.println("发送失败");
                }
            }
        });

    }


    @After
    public void clean() {
        if (kafkaProducer != null) {
            try {
                kafkaProducer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String getMsg() {
        return "hello kafka !";
    }



    private ProducerRecord<byte[], byte[]> getProducerRecord(String topic, byte[] msg) {
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, msg);
        return record;
    }

}
