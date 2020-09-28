package lv.maska;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lv.maska.processors.ProcessorRunner;

public class Processor {

    public Processor() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleAtFixedRate(new ProcessorRunner(), 3*60, 3*60, TimeUnit.SECONDS);
    }
    
}