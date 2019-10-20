package com.liupeidong.kafkaCore.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Container class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/6 17:51
 */
public class Container<E> {

    private BlockingQueue<E> pool;
    //生产线程生成的消息数目
    private  volatile AtomicInteger produceCount = new AtomicInteger(0);
    //成功消费的消息数据
    private volatile AtomicInteger consumeCount = new AtomicInteger(0);
    private int num;

    public Container(int num) {
        this.num = num;
        this.pool = new ArrayBlockingQueue<E>(num);
    }

    public int addConsumeCount(int num) {
        return  consumeCount.addAndGet(num);
    }

    public E get() {
        try {
            E msg = pool.take();
//            consumeCount.addAndGet(1); //发送成功回调才累加,所以此处注释
            return  msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void offer(E msg) {
        try {
            pool.put(msg);
            produceCount.addAndGet(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int getProduceCount() {
        return produceCount.get();
    }

    public int getConsumeCount() {
        return consumeCount.get();
    }
}
