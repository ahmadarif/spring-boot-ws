package com.ahmadarif.spring.ws.controllers;

import com.ahmadarif.spring.ws.models.Greeting;
import com.ahmadarif.spring.ws.models.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Ahmad Arif on 9/1/2016.
 */
@Controller
public class GreetingController implements Runnable {

    private Thread thread;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @RequestMapping("/thread")
    @ResponseBody
    public Greeting greeting2() {
        thread = new Thread(this);
        thread.start();

        return new Greeting("Thread!");
    }

    @Override
    public void run() {
        while(true) {
            template.convertAndSend("/topic/greetings", new Greeting("Thread!"));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
