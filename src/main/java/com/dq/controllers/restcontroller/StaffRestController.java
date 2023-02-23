package com.dq.controllers.restcontroller;


import com.dq.entity.ResponseObject;
import com.dq.entity.Staff;
import com.dq.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/staff")

public class StaffRestController {
    @Autowired
    private StaffService staffService;

    @GetMapping("")
    ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "find all staff", staffService.findAll()));
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Staff> staff = staffService.findById(id);

        return staff.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "find staff by id", staff)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "not found staff by id: " + id, ""));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insertStaff(@RequestBody Staff staff) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "insert staff successfully", staffService.save(staff)));
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateStaff(@RequestBody Staff staff, @PathVariable Long id) {
        Optional<Staff> st = staffService.findById(id);
        if (st.isPresent()) {
            st.get().setId(id);
            st.get().setPhoto(staff.getPhoto());
            st.get().setName(staff.getName());
            st.get().setPhone(staff.getPhone());
            st.get().setEmail(staff.getEmail());
            st.get().setGender(staff.isGender());
            st.get().setSalary(staff.getSalary());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "update staff successfully", staffService.save(st.get())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "insert staff successfully", staffService.save(staff)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteStaff(@PathVariable Long id) {
        Optional<Staff> staff = staffService.findById(id);

        if (staff.isPresent()) {
            staffService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "delete successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "can not found staff to delete", ""));
    }


}
