package com.example.GreetingApp.controller;

import com.example.GreetingApp.controller.Greeting;
import com.example.GreetingApp.services.GreetingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private final GreetingService greetingService;
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

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

    @GetMapping("/service")
    public Greeting getGreetingWithService() {
        return new Greeting(greetingService.getGreetingMessage());
    }

    @GetMapping("/param")
    public Greeting getGreetingfirstNameAndLastName(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {

        String message = greetingService.getGreetingMessageWithName(firstName, lastName);
        return new Greeting(message);
    }
}
