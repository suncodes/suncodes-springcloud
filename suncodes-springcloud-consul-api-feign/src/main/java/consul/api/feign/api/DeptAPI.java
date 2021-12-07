package consul.api.feign.api;

import api.entities.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import consul.api.feign.fallback.DeptFallbackFactory;

import java.util.List;

@FeignClient(value = "consul-provider", fallbackFactory = DeptFallbackFactory.class)
public interface DeptAPI {
    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    boolean add(@RequestBody Dept dept);

    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    Dept get(@PathVariable("id") Long id);

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    List<Dept> list();
}
