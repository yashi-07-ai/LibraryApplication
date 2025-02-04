package com.example.LibraryApplication.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;             // user or admin

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
