## 网关熔断限流

为了在集成多个组件的时候能够运行，不至于冲突，添加标识：

【\<组件名\> step \<数字\>】

例如：

【hystrix step 1】表示 hystrix 配置的第一步。

在测试的时候，只需要全局搜索，打开注释内容即可。


所有集成组件名称：
```text
【hystrix step 1】（在 gateway 高版本 中 hystrix 已经被移除了，既然移除了，就算了，之前还有 HystrixGatewayFilterFactory 呢。）
【resilience4j step 1】
【sentinel step 1】
【redis step 1】
```
