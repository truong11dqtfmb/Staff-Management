package com.dq.services;

import com.dq.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    Staff save(Staff entity);

    Optional<Staff> findById(Long id);

    List<Staff> findAll();

    void deleteById(Long id);

    List<Staff> search(String name);

    List<Staff> findAll(Sort sort);

    Page<Staff> pagefindAll(int pageNumber, String sortField, String sortDir);

}
