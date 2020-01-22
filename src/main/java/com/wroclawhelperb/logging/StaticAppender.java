//By BillyKorando
//https://medium.com/@BillyKorando/how-to-test-logging-in-java-fc36c6965bd9
//https://medium.com/@BillyKorando/how-to-test-logging-in-java-part-two-parallel-boogaloo-28d563c15a3d
package com.wroclawhelperb.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class StaticAppender extends AppenderBase<ILoggingEvent> {

    private static List<ILoggingEvent> events = new ArrayList<>();

    @Override
    public void append(ILoggingEvent e) {
        events.add(e);
    }

    public static List<ILoggingEvent> getEvents() {
        return events;
    }

    public static void clearEvents() {
        events.clear();
    }
}
