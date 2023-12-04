package com.tiny.common.starter.redisson;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 *
 * @author wzh
 * @date 2023/11/02 20:53
 */
@Slf4j(topic = "RedissonLock")
@Getter
@Component
public final class RedissonLock {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 加锁操作
     *
     * @return boolean
     */
    public boolean lock(LockType lockType, String lockName, long expireSeconds) {
        RLock rLock;
        switch (lockType) {
            case FAIR:
                rLock = redissonClient.getFairLock(lockName);
                break;
            case READ:
                RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName);
                rLock = readWriteLock.readLock();
                break;
            case WRITE:
                RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockName);
                rLock = rwLock.writeLock();
                break;
            default:
                //默认可重入锁
                rLock = redissonClient.getLock(lockName);
        }
        boolean flag;
        try {
            flag = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            if (flag) {
                log.info("获取Redisson分布式锁[成功]，lockName={}", lockName);
            } else {
                log.info("获取Redisson分布式锁[失败]，lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            return false;
        }
        return flag;
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void release(String lockName) {
        log.info("获取Redisson分布式锁[解锁]，lockName={}", lockName);
        redissonClient.getLock(lockName).unlock();
    }

    /**
     * 加锁
     */
    public void isLock(String lockName) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            rLock.lock();
        } catch (Exception e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
        }
    }

    /**
     * 查询是否有锁
     */
    public boolean isLocked(String lockName) {
        return redissonClient.getLock(lockName).isLocked();
    }

}
