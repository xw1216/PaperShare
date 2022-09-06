package tech.outspace.papershare.utils.general;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnowFlake {
    // Time start from 2022/01/01 00:00:00
    private static final long INIT_EPOCH = 1640966400000L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    private static final long WORKER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    private static final long WORK_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private final long datacenterId;
    private final long workerId;
    private long lastTimeMillis = -1L;
    private long sequence;

    public SnowFlake(long datacenterId, long workerId) {
        if (datacenterId < 0 || datacenterId > MAX_DATA_CENTER_ID ||
                workerId < 0 || workerId > MAX_WORKER_ID) {
            String msg = "Illegal argument for snow flake algorithm";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    public synchronized String NextId() {
        long CurrentTimeMillis = System.currentTimeMillis();
        if (CurrentTimeMillis < lastTimeMillis) {
            String msg = "Previous Server internal time detected";
            log.error(msg);
            throw new RuntimeException(msg);
        }

        if (CurrentTimeMillis == lastTimeMillis) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                CurrentTimeMillis = tillNextMillis(lastTimeMillis);
            }
        } else {
            sequence = 0;

        }
        lastTimeMillis = CurrentTimeMillis;

        long token = ((CurrentTimeMillis - INIT_EPOCH) << TIMESTAMP_SHIFT) | (datacenterId << DATA_CENTER_ID_SHIFT) |
                (workerId << WORK_ID_SHIFT) | sequence;

        return Long.toString(token);
    }

    private long tillNextMillis(long lastMillis) {
        long CurrentMillis;
        do {
            CurrentMillis = System.currentTimeMillis();
        } while (CurrentMillis <= lastMillis);
        return CurrentMillis;
    }
}
