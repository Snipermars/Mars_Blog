package com.liupeidong.multiLearning.scalabilityAndThreadSafety.coordinating;

import java.io.File;

/**
 * TotalFileSizeSequential class
 * 1)
 *
 * @author liupeidong
 * @time 2019/10/21 0021 21:53
 */
public class TotalFileSizeSequential {

    private long getTotalSizeOfFileInDir(final File file){
        if(file.isFile()) return file.length();

        final File[] children = file.listFiles();

        long total = 0;

        if(children != null)
            for(final File child: children)
                total += getTotalSizeOfFileInDir(child);
        return total;
    }

    public void execute(String filePath){
        final long start = System.nanoTime();
        final long total = new TotalFileSizeSequential().getTotalSizeOfFileInDir(new File(filePath));
        final long end = System.nanoTime();
        System.out.println("TotalSize: " + total);
        System.out.println("Time taken: " + (end - start)/1.0e9);
    }
}
