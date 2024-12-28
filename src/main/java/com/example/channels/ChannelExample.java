package com.example.channels;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChannelExample {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private final Sinks.Many<String> unbufferedSink = Sinks.many().unicast().onBackpressureError();
    private final Sinks.Many<String> bufferedSink = Sinks.many().multicast().onBackpressureBuffer(10);
    
    private void log(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(timeFormatter), message);
    }
    
    public void demonstrateChannels() {
        log("Setting up channels demonstration");
        Flux<String> unbufferedChannel = unbufferedSink.asFlux();
        Flux<String> bufferedChannel = bufferedSink.asFlux();
        
        // Set up subscribers first
        unbufferedChannel
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(msg -> log("Unbuffered consumer received: " + msg));
            
        bufferedChannel
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(msg -> log("Buffered consumer received: " + msg));
        
        // Small delay to ensure subscribers are ready
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        Runnable unbufferedProducer = () -> {
            for (int i = 0; i < 5; i++) {
                log("Sending unbuffered message " + i);
                unbufferedSink.tryEmitNext("Unbuffered message " + i)
                    .orThrow();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            log("Completing unbuffered channel");
            unbufferedSink.tryEmitComplete();
        };
        
        Runnable bufferedProducer = () -> {
            for (int i = 0; i < 5; i++) {
                log("Sending buffered message " + i);
                bufferedSink.tryEmitNext("Buffered message " + i)
                    .orThrow();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            log("Completing buffered channel");
            bufferedSink.tryEmitComplete();
        };
        
        Thread unbufferedThread = new Thread(unbufferedProducer, "UnbufferedProducer");
        Thread bufferedThread = new Thread(bufferedProducer, "BufferedProducer");
        
        unbufferedThread.start();
        bufferedThread.start();
    }
    
    public void demonstrateSelect() {
        log("Setting up select demonstration");
        Sinks.Many<String> channel1 = Sinks.many().unicast().onBackpressureError();
        Sinks.Many<String> channel2 = Sinks.many().unicast().onBackpressureError();
        
        // Set up the merged flux first
        Flux.merge(channel1.asFlux(), channel2.asFlux())
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(msg -> log("Received from either channel: " + msg));
            
        // Add a small delay to ensure subscriber is ready
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        // Now emit messages on separate threads to better simulate concurrent behavior
        new Thread(() -> {
            log("Sending message on channel 1");
            channel1.tryEmitNext("Message from channel 1").orThrow();
        }, "Channel1Producer").start();
        
        new Thread(() -> {
            log("Sending message on channel 2");
            channel2.tryEmitNext("Message from channel 2").orThrow();
        }, "Channel2Producer").start();
    }
    
    public void demonstrateClosing() {
        log("Setting up closing demonstration");
        Sinks.Many<String> channel = Sinks.many().unicast().onBackpressureError();
        
        channel.asFlux()
            .doOnComplete(() -> log("Channel closed"))
            .subscribe(msg -> log("Received: " + msg));
            
        try {
            Thread.sleep(100);  // Ensure subscriber is ready
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        
        log("Sending last message");
        channel.tryEmitNext("Last message");
        
        log("Closing channel");
        channel.tryEmitComplete();
    }
}
