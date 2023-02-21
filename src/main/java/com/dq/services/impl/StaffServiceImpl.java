package com.dq.services.impl;

import com.dq.constant.SystemConstant;
import com.dq.entity.Staff;
import com.dq.repositories.StaffRepository;
import com.dq.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public Staff save(Staff entity) {
        return staffRepository.save(entity);
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public List<Staff> search(String name) {
        return staffRepository.findByNameContaining(name);
    }

    @Override
    public List<Staff> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Staff> pagefindAll(int pageNumber, String sortField, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//        Pageable pageable = PageRequest.of(pageNumber - 1, 3,sort);
        Pageable pageable = PageRequest.of(pageNumber - 1, SystemConstant.PAGE_SIZE, sort);

        return staffRepository.findAll(pageable);
    }


}
