package com.tiny.framework.starter.redisson.config;

import cn.hutool.core.util.ArrayUtil;
import com.tiny.framework.starter.redisson.enums.RedissonCons.LockType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 分布式锁工具类
 *
 * @author: wzh
 * @date: 2023/11/02 20:53
 */
@Slf4j(topic = "tiny-framework-starter ==> RedissonLock")
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
                log.error("Redisson分布式锁【异常】，key = {}", key, e);
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
                log.error("Redisson分布式锁【异步加锁异常】，key = {}", key, e);
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
            log.error("获取Redisson分布式锁[异常]，lockName={}", lockName, e);
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 参数
     */
    public Long getExpire(String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.remainTimeToLive(key);
    }

    /**
     * 获取限流器
     *
     * @param key 参数
     */
    public RRateLimiter getRateLimiter(String key) {
        return redissonClient.getRateLimiter(key);
    }


    /**
     * 分布式队列，RQueue，FIFO
     *
     * @param name
     */
    public boolean rQueueOffer(String name, Object value) {
        RQueue<Object> queue = redissonClient.getQueue(name);
        return queue.offer(value);
    }

    public <t> t rQueuePoll(String name, Class<t> tClass) {
        RQueue<t> queue = redissonClient.getQueue(name);
        return queue.poll();
    }

    public int rQueueSize(String name) {
        return redissonClient.getQueue(name).size();
    }

    /**
     * 分布式队列，BQueue，阻塞
     */
    public boolean bQueueOffer(String name, Object value, long time, TimeUnit unit) {
        try {
            RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(name);
            return blockingQueue.offer(value, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public <t> t bQueuePoll(String name, long time, TimeUnit unit, Class<t> tClass) {
        try {
            RBlockingQueue<t> blockingQueue = redissonClient.getBlockingQueue(name);
            return blockingQueue.poll(time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int bQueueSize(String name) {
        return redissonClient.getBlockingQueue(name).size();
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch batch = redissonClient.createBatch();
        RBucketAsync<T> bucket = batch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        batch.execute();
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }

    /**
     * 检查缓存对象是否存在
     *
     * @param key 缓存的键值
     */
    public boolean isExistsObject(final String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> rList = redissonClient.getList(key);
        return rList.addAll(dataList);
    }


    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        RList<T> rList = redissonClient.getList(key);
        return rList.readAll();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public boolean deleteObject(final String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    public void setAtomicValue(String key, long value) {
        RAtomicLong atomic = redissonClient.getAtomicLong(key);
        atomic.set(value);
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long getAtomicValue(String key) {
        RAtomicLong atomic = redissonClient.getAtomicLong(key);
        return atomic.get();
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long incrAtomicValue(String key) {
        RAtomicLong atomic = redissonClient.getAtomicLong(key);
        return atomic.incrementAndGet();
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public long decrAtomicValue(String key) {
        RAtomicLong atomic = redissonClient.getAtomicLong(key);
        return atomic.decrementAndGet();
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    public Boolean hasKey(String key) {
        RKeys rKeys = redissonClient.getKeys();
        return rKeys.countExists(key) > 0;
    }
}
