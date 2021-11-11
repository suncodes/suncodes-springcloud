package provider.dept.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import api.entities.Dept;
import provider.dept.rest.dao.DeptDao;

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
