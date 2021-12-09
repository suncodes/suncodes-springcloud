package eureka.client.provider.sentinel.service;

import api.entities.Dept;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import eureka.client.provider.sentinel.dao.DeptDao ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao dao;

    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    @SentinelResource(value = "get")
    public Dept get(Long id) {
        return dao.findById(id);
    }

    @SentinelResource(value = "list")
    public List<Dept> list() {
        return dao.findAll();
    }
}
