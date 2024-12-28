
package com.example.channels;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReactorChannelsDemo {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    private static void log(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(timeFormatter), message);
    }
    
    public static void main(String[] args) throws InterruptedException {
        log("Starting Reactor Channels Demo...\n");
        
        ChannelExample example = new ChannelExample();
        
        log("1. Demonstrating buffered and unbuffered channels:");
        example.demonstrateChannels();
        
        // Wait for channel demo to complete
        Thread.sleep(1500);
        
        log("\n2. Demonstrating select-like functionality:");
        example.demonstrateSelect();
        
        // Wait longer to ensure select demonstration completes
        Thread.sleep(1000);
        
        log("\n3. Demonstrating channel closing:");
        example.demonstrateClosing();
        
        // Wait for closing demonstration to complete
        Thread.sleep(500);
        
        log("\nDemo completed!");
    }
}
