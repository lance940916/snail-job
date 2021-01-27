![Travis (.com) branch](https://img.shields.io/travis/com/wu0916/wu-job/master)
![GitHub](https://img.shields.io/github/license/wu0916/wu-job)

# 分布式定时任务调度系统

首先这是一个 Demo 项目，适合学习使用。

然后项目是参考 [xxl-job](https://github.com/xuxueli/xxl-job) 源码编写。

最后项目目前状态：可运行，可调度。

# 技术选型

- Spring
- SpringBoot
- Freemarker
- MyBatis and dynamic-sql
- log4j
- Netty

# 项目技术点简介

- 分布式锁：使用的MySQL提供的 select for update 锁;
- Token校验：
- 任务调度通信：

只支持单调度中心连接


# TODO 支持的功能


# 个人总结（个人观点）

通过和xxl项目相比，能体会到一个好的开源项目作者是要付出很多心血的去维护的。

人多力量大，开源项目得到同志们的支持，也会越来越优秀的，这也是我由心体会到的。
