package com.liupeidong.multiLearning.scalabilityAndThreadSafety.coordinating;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * NaivelyConcurrentTotalFileSize class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/21 0021 22:02
 */
public class NaivelyConcurrentTotalFileSize {

    private long getTotalSizeOfFileInDir(final ExecutorService service, final File file)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (file.isFile()) return file.length();

        long total = 0;
        final File[] children = file.listFiles();

        if (children != null) {
            final List<Future<Long>> partialTotalFutures =
                    new ArrayList<Future<Long>>();
            for (final File child : children) {
                partialTotalFutures.add(service.submit(new Callable<Long>() {
                    public Long call() throws InterruptedException, ExecutionException, TimeoutException {
                        return getTotalSizeOfFileInDir(service, child);
                    }
                }));
            }
            for (final Future<Long> partialTotalFuture : partialTotalFutures) {
                total += partialTotalFuture.get(100, TimeUnit.MICROSECONDS);
            }
        }
        return total;
    }

    private long getTotalSizeOfFileInDir(final String fileName)
        throws InterruptedException, ExecutionException, TimeoutException {
        final ExecutorService service = Executors.newFixedThreadPool(100);
        try{
            return getTotalSizeOfFileInDir(service, new File(fileName));
        } finally {
            service.shutdown();
        }
    }

}

