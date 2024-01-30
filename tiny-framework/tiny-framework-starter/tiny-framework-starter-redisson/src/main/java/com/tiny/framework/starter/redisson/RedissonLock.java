package com.tiny.framework.starter.redisson;

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
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

    private RLock getLock(LockType lockType, String key) {
        RLock rLock;
        switch (lockType) {
            case FAIR:
                rLock = redissonClient.getFairLock(key);
                break;
            case READ:
                RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
                rLock = readWriteLock.readLock();
                break;
            case WRITE:
                RReadWriteLock rwLock = redissonClient.getReadWriteLock(key);
                rLock = rwLock.writeLock();
                break;
            default:
                //默认可重入锁
                rLock = redissonClient.getLock(key);
        }
        return rLock;
    }

    /**
     * 加锁操作，时间单位：秒
     *
     * @return boolean
     */
    public boolean lock(LockType lockType, String key, long expireSeconds) {
        boolean flag = false;
        try {
            RLock rLock = getLock(lockType, key);
            try {
                flag = rLock.tryLock(0, expireSeconds, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Redisson分布式锁【异常】，key = " + key, e);
            } finally {
                if (flag) {
                    log.info("Redisson分布式锁【成功】，key = {}", key);
                } else {
                    log.info("Redisson分布式锁【失败】，key = {}", key);
                }
            }
        } catch (Exception e) {
            log.error("lock异常", e);
        }
        return flag;
    }

    public boolean asyncLock(LockType lockType, String key, long expireSeconds) {
        boolean flag = false;
        RLock rLock = getLock(lockType, key);
        try {
            try {
                flag = rLock.tryLockAsync(0, expireSeconds, TimeUnit.SECONDS).get();
            } catch (Exception e) {
                log.error("Redisson分布式锁【异步加锁异常】，key = " + key, e);
            } finally {
                if (flag) {
                    log.info("Redisson分布式锁【异步加锁成功】，key = {}", key);
                } else {
                    log.info("Redisson分布式锁【异步加锁失败】，key = {}", key);
                }
            }
        } catch (Exception e) {
            log.error("asyncLock异常", e);
        }
        return flag;
    }

    /**
     * 解锁
     *
     * @param key 锁名称
     */
    public void release(String key) {
        log.info("Redisson分布式锁【解锁】，key = {}", key);
        try {
            redissonClient.getLock(key).unlock();
        } catch (Exception e) {
            log.error("release异常", e);
        }
    }

    /**
     * 异步解锁
     *
     * @param key
     */
    public void asyncRelease(String key, Long... threadId) {
        log.info("Redisson分布式锁【异步解锁】，key = {}", key);
        try {
            if (ArrayUtil.isNotEmpty(threadId)) {
                redissonClient.getLock(key).unlockAsync(threadId[0]);
            } else {
                redissonClient.getLock(key).unlockAsync();
            }
        } catch (Exception e) {
            log.error("asyncRelease异常", e);
        }
    }

    /**
     * 查询是否有锁
     */
    public boolean isLocked(String key) {
        return redissonClient.getLock(key).isLocked();
    }

    /**
     * 查询是否有异步锁
     */
    public boolean isAsyncLocked(String lockName) {
        Boolean isLocked = Boolean.FALSE;
        RFuture<Boolean> lockedAsync = redissonClient.getLock(lockName).isLockedAsync();
        try {
            isLocked = lockedAsync.get();
        } catch (InterruptedException | ExecutionException e) {
            log.info("Redisson分布式锁【是否有异步锁失败】", e);
        }
        return isLocked;
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
     * 获取过期时间
     *
     * @param key
     */
    public Long getExpire(String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.remainTimeToLive(key);
    }

}
