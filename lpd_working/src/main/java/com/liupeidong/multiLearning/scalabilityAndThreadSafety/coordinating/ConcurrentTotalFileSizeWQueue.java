package com.liupeidong.multiLearning.scalabilityAndThreadSafety.coordinating;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ConcurrentTotalFileSizeWQueue class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/24 21:35
 */
public class ConcurrentTotalFileSizeWQueue {
    private ExecutorService service;
    final private BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<Long>(500);
    final AtomicLong pendingFileVisits = new AtomicLong();
    private void startExploreDir(final File file){
        pendingFileVisits.incrementAndGet();
        service.execute(new Runnable() {
            public void run() {
                exploreDir(file);
            }
        });
    }

    private void exploreDir(final File file){
        long fileSize = 0;
        if(file.isFile()){
            fileSize = file.length();
        } else{
            final File[] children = file.listFiles();
            if(children != null){
                for(final File child: children){
                    if(child.isFile()){
                        fileSize += child.length();
                    } else {
                        startExploreDir(child);
                    }
                }
            }
        }
        try{
            fileSizes.put(fileSize);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        pendingFileVisits.decrementAndGet();
    }

    private long getTotalSizeOfFile(final String fileName) throws InterruptedException{
        service = Executors.newFixedThreadPool(100);
        try{
            startExploreDir(new File(fileName));
            long totalSize = 0;
            while(pendingFileVisits.get() > 0 || fileSizes.size() > 0){
                final Long size = fileSizes.poll(10, TimeUnit.SECONDS);
                totalSize += size;
            }
            return totalSize;
        } finally {
            service.shutdown();
        }
    }


}
