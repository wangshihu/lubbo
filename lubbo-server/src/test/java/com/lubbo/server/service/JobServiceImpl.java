package com.lubbo.server.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lubbo.core.annotation.Lubbo;
import com.lubbo.server.entity.JobConf;

/**
 * @author benchu
 * @version 16/9/14.
 */
@Lubbo
@Component
public class JobServiceImpl implements JobService {
    Map<Long, JobConf> jobConfMap = new HashMap<>();

    @Override
    public void insert(JobConf jobConf) {
        jobConfMap.put(jobConf.getId(), jobConf);
    }

    @Override
    public JobConf findById(long id) {
        return jobConfMap.get(id);
    }

    @Override
    public int totals() {
        return jobConfMap.size();
    }
}
