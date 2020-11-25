package com.snailwu.job.admin.core.route.strategy;

import com.snailwu.job.admin.core.route.ExecutorRouter;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性 Hash
 * 分组下机器地址相同，不同JOB均匀散列在不同机器上，保证分组下机器分配JOB平均；且每个JOB固定调度其中一台机器；
 * a、virtual node：解决不均衡问题
 * b、hash method replace hashCode：String的hashCode可能重复，需要进一步扩大hashCode的取值范围
 *
 * @author 吴庆龙
 * @date 2020/6/17 10:11 上午
 */
public class ExecutorRouteConsistentHash extends ExecutorRouter {

    private static final int VIRTUAL_NODE_NUM = 100;

    private static long hash(String key) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("不支持 MD5 算法");
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        md5.update(keyBytes);
        byte[] digest = md5.digest();

        // 转为 32 位
        long hasCode = ((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (long) (digest[0] & 0xFF);
        return hasCode & 0xffffffffL;
    }

    public String hashJob(int jobId, List<String> addressList) {
        TreeMap<Long, String> addressRing = new TreeMap<>();
        for (String address : addressList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                long addressHash = hash("SHARD-" + address + "-NONE-" + i);
                addressRing.put(addressHash, address);
            }
        }

        long jobHash = hash(String.valueOf(jobId));
        SortedMap<Long, String> lastRing = addressRing.tailMap(jobHash);
        if (!lastRing.isEmpty()) {
            return lastRing.get(lastRing.firstKey());
        }
        return addressRing.firstEntry().getValue();
    }

    @Override
    public ResultT<String> route(TriggerParam triggerParam, List<String> addressList) {
        try {
            String address = hashJob(triggerParam.getJobId(), addressList);
            return new ResultT<>(address);
        } catch (Exception e) {
            return new ResultT<>(ResultT.FAIL_CODE, NO_FOUND_ADDRESS_MSG);
        }
    }
}
