package org.curriculumdesign.bookserp.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author lesl
 */
@Slf4j
@Component
public class DemoSchedule {

    /**
     * 启动的时候执行一次 每一个小时更新一次Token
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 60 * 60 * 1000)
//    @DistributedLock(timeoutSecond = 30 * 60, completedRelease = false)
    public void taskExecute() {
        //
    }
}
