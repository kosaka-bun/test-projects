package de.honoka.test.spring.item.executeinbatches;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.map.MapUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BatchScheduler {

    @SuppressWarnings("unchecked")
    private static final Map<Integer, Integer> thresholdToSwitchBatchNum = (Map<Integer, Integer>) (Object) MapUtil.of(
        new Object[][] {
            { 1, 3 },
            { 2, 5 },
            { 3, 2 }
        }
    );

    private int batchNum = 1;

    private Map<Integer, Integer> finishedTaskCountOfBatch = new ConcurrentHashMap<>();

    private Set<Thread> waitingThreads = new ConcurrentHashSet<>();

    public void init() {
        batchNum = 1;
        finishedTaskCountOfBatch = new ConcurrentHashMap<>();
        if(!waitingThreads.isEmpty()) {
            waitingThreads.forEach(Thread::interrupt);
        }
        waitingThreads = new ConcurrentHashSet<>();
    }

    @SneakyThrows
    public void waitForBatchNum(int batchNum, int maxWaitSeconds) {
        int seconds = 0;
        waitingThreads.add(Thread.currentThread());
        while(seconds < maxWaitSeconds) {
            if(this.batchNum == batchNum) {
                waitingThreads.remove(Thread.currentThread());
                return;
            }
            if(this.batchNum < batchNum) {
                Thread.sleep(1000);
            } else {
                waitingThreads.remove(Thread.currentThread());
                throw new RuntimeException("等待的批次号为：" + batchNum + "，实际的批次号为：" + this.batchNum);
            }
            seconds++;
        }
        waitingThreads.remove(Thread.currentThread());
        throw new RuntimeException("等待批次号超时：" + batchNum);
    }

    public synchronized void reportFinishedTask(int batchNum) {
        Integer count = finishedTaskCountOfBatch.get(batchNum);
        count = count == null ? 1 : count + 1;
        finishedTaskCountOfBatch.put(batchNum, count);
        int threshold = thresholdToSwitchBatchNum.get(batchNum);
        if(count >= threshold) this.batchNum++;
    }
}
