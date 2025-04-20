package gabrielzrz.com.github.controllers;

import gabrielzrz.com.github.model.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

@RestController
public class GreetingController {

    private static final String template = "oi , %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name2", defaultValue = "Word") String name){
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
