package com.liupeidong.multiLearning.scalabilityAndThreadSafety.coordinating;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ConcurrentTotalFileSizeWLatch class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/24 21:16
 */
public class ConcurrentTotalFileSizeWLatch {

    private ExecutorService service;
    final private AtomicLong pendingFileVisits = new AtomicLong();
    final private AtomicLong totalSize = new AtomicLong();
    final private CountDownLatch latch = new CountDownLatch(1);
    private void updateTotalSizeOfFileInDir(final File file){
        long fileSize = 0;
        if(file.isFile())
            fileSize = file.length();
        else{
            final File[] children = file.listFiles();
            if(children != null){
                for(final File child: children){
                    if(child.isFile()){
                        fileSize += child.length();
                    } else{
                        pendingFileVisits.incrementAndGet();
                        service.execute(new Runnable() {
                            public void run() {
                                updateTotalSizeOfFileInDir(child);
                            }
                        });
                    }
                }
            }
        }

        totalSize.addAndGet(fileSize);
        if(pendingFileVisits.decrementAndGet() == 0) latch.countDown();
    }

    private long getTotalSizeOfFile(final String fileName) throws InterruptedException{
        service = Executors.newFixedThreadPool(100);
        pendingFileVisits.incrementAndGet();
        try{
            updateTotalSizeOfFileInDir(new File(fileName));
            latch.await(100, TimeUnit.SECONDS);
            return totalSize.longValue();
        } finally{
            service.shutdown();
        }
    }
}
