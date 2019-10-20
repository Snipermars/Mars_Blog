package com.liupeidong.kafkaCore;

import com.liupeidong.kafkaCore.constant.Config;
import com.liupeidong.kafkaCore.service.Container;
import com.liupeidong.kafkaCore.service.ProduceThread;
import com.liupeidong.kafkaCore.service.SendThread;
import com.liupeidong.kafkaCore.util.JsonUtils;
import com.liupeidong.kafkaCore.util.PropertiesFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * TestApp class
 * 1) main class for this test-kafka project
 *
 * @author liupeidong
 * @time 2019/10/6 16:21
 */
public class TestApp {
    private static final String CONFIG_FILE = "send-cfg.properties";

    public static void main(String[] args){
        System.out.println("输入任意非空字符回车启动demo.");
        String line = null;
        do{
            Scanner scan = new Scanner(System.in);
            line = scan.nextLine();
        } while (StringUtils.isBlank(line));

        run(args);

    }

    public static void run(String... args) {

        long start = System.currentTimeMillis();

        PropertiesFile prop = null;
        if (args == null || args.length == 0) {
            prop = new PropertiesFile(CONFIG_FILE, false);
        } else {
            prop = new PropertiesFile(args[0], true);
        }

        Config config = new Config(prop);

        System.out.println("用户配置的参数是：\r\n" + JsonUtils.object2Json(config, true));

        check(config);

        Container<ProducerRecord<byte[], byte[]>> container = new Container<ProducerRecord<byte[], byte[]>>(config.getiInitialCount() + 10000);

        System.out.println("开始生成消息");
        Integer produceThreadNum = config.getProduceThreadNum();
        List<Thread> producerList = new ArrayList<Thread>();

        CountDownLatch produceSwitch = new CountDownLatch(config.getProduceThreadNum());
        for(int i = 0; i < produceThreadNum; i++) {
            Thread t = new Thread(new ProduceThread(config, container, produceSwitch));
            t.setDaemon(true);
            t.setName("send-thred-" + i);
            t.start();
            producerList.add(t);
        }

        try {
            produceSwitch.await();
            System.out.println();
            System.out.println("消息达到初始量" + config.getiInitialCount() + ", 开始发送消息");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////////////////////////////////////////////////

        Integer sendThreadNum = config.getSendThreadNum();
        CountDownLatch consumeStartSwitch = new CountDownLatch(1);
        CountDownLatch consumeEndSwitch = new CountDownLatch(sendThreadNum);
        List<Thread> senderList = new ArrayList<Thread>();
        for(int i = 0; i < sendThreadNum; i++) {
            Thread t = new Thread(new SendThread(config, container, consumeStartSwitch, consumeEndSwitch));
            t.setName("send-thread-" + i);
            t.setDaemon(true);
            t.start();
            senderList.add(t);
        }

        consumeStartSwitch.countDown();

        try {
            consumeEndSwitch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println("一共发送消息" + config.getTotalCount() + "条");

        int error = (container.getConsumeCount() - config.getTotalCount());
        System.out.println("发送的消息总量误差是[" + (error >= 0 ? "+" : "-") + error + "]条");

        System.out.println("耗时" + (System.currentTimeMillis() - start) + "毫秒");
    }

    public static void check(Config config) {
        if (config == null) {
            return;
        }

        if (config.getTotalCount() <= 0 || config.getiInitialCount() < 0) {
            throw new IllegalArgumentException("require: totalCount > 0 && initialCount >= 0");
        }

        if (config.getiInitialCount() > config.getTotalCount()) {
            throw new IllegalArgumentException("require: initialCount <= totalCount");
        }


        if (config.getProduceThreadNum() <= 0 || config.getSendThreadNum() <= 0) {
            throw new IllegalArgumentException("require: produce_thread_count > 0 && send_thread_count > 0");
        }


    }
}
