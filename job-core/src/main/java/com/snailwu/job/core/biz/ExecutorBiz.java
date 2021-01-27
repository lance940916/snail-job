package com.snailwu.job.core.biz;

import com.snailwu.job.core.biz.model.IdleBeatParam;
import com.snailwu.job.core.biz.model.KillParam;
import com.snailwu.job.core.biz.model.ResultT;
import com.snailwu.job.core.biz.model.TriggerParam;

/**
 * @author 吴庆龙
 * @date 2020/5/25 11:59 上午
 */
public interface ExecutorBiz {

    /**
     * 执行器是否在线
     *
     * @return 响应结果
     */
    ResultT<String> beat();

    /**
     * 执行器是否忙碌
     *
     * @param idleBeatParam 参数
     * @return 响应结果
     */
    ResultT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * 执行任务
     *
     * @param triggerParam 参数
     * @return 响应结果
     */
    ResultT<String> run(TriggerParam triggerParam);

    /**
     * 终止任务
     * true:终止成功;false:终止失败(任务已被执行或任务执行中)
     *
     * @param killParam 参数
     * @return 响应结果
     */
    ResultT<String> kill(KillParam killParam);

}
