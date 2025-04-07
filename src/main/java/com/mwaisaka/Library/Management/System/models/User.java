package com.mwaisaka.Library.Management.System.models;

import com.mwaisaka.Library.Management.System.enums.UserRole;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user") //Can I get a
    private List<BorrowRecord> borrowRecords;
}
