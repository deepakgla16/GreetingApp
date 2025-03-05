package com.example.GreetingApp.model;

import jakarta.persistence.*;

@Entity
@Table(name="greetingData")

public class GreetingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String message;

    public GreetingModel() {
    }
    public GreetingModel(String message){
        this.message=message;
    }

    public Long getId(){
        return  id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public  String getMessage(){
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
