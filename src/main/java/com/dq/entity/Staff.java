package com.dq.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tbl_staffs")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private Date birthday;

    private boolean gender;

    private String phone;

    private String email;

    private String photo;

    private float salary;

}
