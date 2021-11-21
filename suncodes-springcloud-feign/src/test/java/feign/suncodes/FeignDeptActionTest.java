package feign.suncodes;


import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.StringDecoder;
import org.junit.Test;

public class FeignDeptActionTest {

    @Test
    public void list() {
        FeignDeptAction feignDeptAction = Feign.builder()
                .decoder(new StringDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(FeignDeptAction.class, "http://localhost:8001/");
        String list = feignDeptAction.list();
        System.out.println(list);
    }

    @Test
    public void getById() {
        FeignDeptAction feignDeptAction = Feign.builder()
                .decoder(new StringDecoder())
                .target(FeignDeptAction.class, "http://localhost:8001/");
        String list = feignDeptAction.getById(1L);
        System.out.println(list);
    }
}