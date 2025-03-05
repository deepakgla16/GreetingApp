package com.example.GreetingApp.services;

import com.example.GreetingApp.model.GreetingModel;
import com.example.GreetingApp.repository.GreetingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GreetingService {

    private  final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository){
        this.greetingRepository=greetingRepository;
    }

    public String getGreetingMessage() {
        return "Hello World from services";
    }

    public String getGreetingMessageWithName(String firstName, String lastName) {
        if (firstName != null && lastName != null) {
            return "Hello, " + firstName + " " + lastName + "!";
        } else if (firstName != null) {
            return "Hello, " + firstName + "!";
        } else if (lastName != null) {
            return "Hello, " + lastName + "!";
        } else {
            return "Hello World!";
        }

    }
    public GreetingModel getGreetingMessageTosave(String firstName, String lastName) {
        String  message;
        if (firstName != null && lastName != null) {
            message="Hello, " + firstName + " " + lastName + "!";
        } else if (firstName != null) {
            message= "Hello, " + firstName + "!";
        } else if (lastName != null) {
            message="Hello, " + lastName + "!";
        } else {
            message= "Hello World";
        }
        GreetingModel greeting=new GreetingModel(message);
        return greetingRepository.save((greeting));
    }


    public GreetingModel getGreetingId(Long id){
        return greetingRepository.findById(id).orElse(null);
    }

    public List<GreetingModel> getAllGreetings(){
        return greetingRepository.findAll();
    }

    public GreetingModel updateGreeting(Long id, String newMesaage){
        return greetingRepository.findById(id)
                .map(foundGreeting ->{
                    foundGreeting.setMessage(newMesaage);
                    return greetingRepository.save((foundGreeting));
                }).orElseThrow(()->new RuntimeException("not found"));
    }



}