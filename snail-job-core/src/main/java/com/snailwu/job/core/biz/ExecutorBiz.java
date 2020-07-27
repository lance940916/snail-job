package com.snailwu.job.core.biz;

import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

/**
 * 执行器需要的方法
 *
 * @author 吴庆龙
 * @date 2020/5/25 11:59 上午
 */
public interface ExecutorBiz {

    /**
     * 执行器是否在线
     */
    ResultT<String> beat();

    /**
     * 执行器是否忙碌
     */
    ResultT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * 执行任务
     */
    ResultT<String> run(TriggerParam triggerParam);

    /**
     * 终止任务
     */
    ResultT<String> kill(KillParam killParam);

}
