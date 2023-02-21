package com.dq.repositories;

import com.dq.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff,Long> {
    List<Staff> findByNameContaining(String name);

    List<Staff> findAll(Sort sort);

    Page<Staff> findAll(Pageable page);
}
