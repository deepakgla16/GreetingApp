package com.example.GreetingApp.controller;

import com.example.GreetingApp.controller.Greeting;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @GetMapping
    public Greeting getGreeting() {
        return new Greeting("Hello from GET");
    }

    @PostMapping
    public Greeting postGreeting(@RequestBody Greeting request) {
        return new Greeting("Hello from POST, received: "+request.getMessage() );
    }

    @PutMapping
    public Greeting putGreeting(@RequestBody Greeting request) {
        return new Greeting("Hello from PUT, updated: "+ request.getMessage());
    }

    @DeleteMapping
    public Greeting deleteGreeting() {
        return new Greeting("Hello from DELETE");
    }
}
