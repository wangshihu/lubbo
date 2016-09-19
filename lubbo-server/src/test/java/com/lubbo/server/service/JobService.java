package com.lubbo.server.service;

import com.lubbo.server.entity.JobConf;

/**
 * @author benchu
 * @version 16/9/14.
 */
public interface JobService {
    void insert(JobConf jobConf);

    JobConf findById(long id);

    int totals();
}
