package eureka.client.provider.sentinel.service;

import api.entities.Dept;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import eureka.client.provider.sentinel.dao.DeptDao ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DeptService {
    @Autowired
    private DeptDao dao;

    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    @SentinelResource(value = "get")
    public Dept get(Long id) {
        log();
        return dao.findById(id);
    }

    @SentinelResource(value = "list")
    public List<Dept> list() {
        log();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        log.info("请求部门列表");
        return dao.findAll();
    }

    @SentinelResource(value = "log")
    public void log() {
        log.info("打印日志");
    }
}
