# distributed-message-push-system

- 分布式消息推送系统

![](./architecture.png)

## 支持客户端

- java
- python
- node
- go(待办)

## 已支持的功能

admin：

- 消息推送
- 消息推送记录

connector：

- 推送消息
    - 单用户
    - 广播
- 推送记录
- 推送确认

lookup:

- connector负载均衡

## 待办

监控：

- lookup调度日志
- connector监控
    - ~~健康监控~~
    - ~~在线用户~~
        - 踢出客户

运维：

- connector扩容缩容

安全：

- 客户端认证
- 消息传输加密