package com.example.channels;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChannelExampleTest {
    @Test
    void demonstrateChannels_ShouldRunWithoutException() {
        ChannelExample example = new ChannelExample();
        assertDoesNotThrow(() -> {
            example.demonstrateChannels();
            Thread.sleep(1000); // Wait for async operations
        });
    }

    @Test
    void demonstrateSelect_ShouldRunWithoutException() {
        ChannelExample example = new ChannelExample();
        assertDoesNotThrow(() -> {
            example.demonstrateSelect();
            Thread.sleep(500); // Wait for async operations
        });
    }

    @Test
    void demonstrateClosing_ShouldRunWithoutException() {
        ChannelExample example = new ChannelExample();
        assertDoesNotThrow(() -> {
            example.demonstrateClosing();
            Thread.sleep(500); // Wait for async operations
        });
    }
}

