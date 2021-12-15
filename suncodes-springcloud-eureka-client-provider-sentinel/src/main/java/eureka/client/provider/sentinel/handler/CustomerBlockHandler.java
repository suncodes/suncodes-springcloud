package eureka.client.provider.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import eureka.client.provider.sentinel.pojo.bo.CommonResult;

public class CustomerBlockHandler {
    /**
     * 必须是 public static（公共静态方法）
     * @param exception
     * @return
     */
    public static CommonResult handleException(BlockException exception) {
        return new CommonResult(2020, "自定义限流处理信息....CustomerBlockHandler");
    }
}
