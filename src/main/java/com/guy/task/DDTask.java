package com.guy.task;

import com.guy.system.schedulejob.entity.ScheduleJob;
import com.guy.system.user.entity.User;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时推送未发日志 通知
 */

@Component
public class DDTask implements Job {
    // @Autowired
    // private CSSUserService cssUserService;
//    @Autowired
//    private LogStatisticsService logStatisticsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        long begin = System.currentTimeMillis();
//        sortJob();
        long end = System.currentTimeMillis();
        long result = end - begin;
        System.out.println(scheduleJob.getName() + "执行耗时:" + result + "豪秒");
    }

    public void sortJob() {
//        List<User> userList=logStatisticsService.unSend();
//        DDMessage.sendMessage(userList);
    }
}
