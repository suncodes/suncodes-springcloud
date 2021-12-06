package zookeeper.provider.two.service;

import api.entities.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zookeeper.provider.two.dao.DeptDao;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao dao;

    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    public Dept get(Long id) {
        return dao.findById(id);
    }

    public List<Dept> list() {
        return dao.findAll();
    }
}
