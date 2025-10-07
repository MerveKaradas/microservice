package com.fintech.userservice.event;

import java.time.Instant;
public class AuthEvent<T extends EventData> {

    private String eventId;
    private AuthEventType eventType;
    private Instant timestamp;
    private String source;
    private T data; // Tip güvenliği için payload kısmı generic yapıldı


    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public AuthEventType getEventType() { return eventType; }
    public void setEventType(AuthEventType eventType) { this.eventType = eventType; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    
    public static class Builder<T extends EventData> {
        private String eventId;
        private AuthEventType eventType;
        private Instant timestamp;
        private String source;
        private T data;

        public Builder<T> eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder<T> eventType(AuthEventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder<T> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder<T> source(String source) {
            this.source = source;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public AuthEvent<T> build() {
            AuthEvent<T> event = new AuthEvent<>();
            event.setEventId(this.eventId);
            event.setEventType(this.eventType);
            event.setTimestamp(this.timestamp);
            event.setSource(this.source);
            event.setData(this.data);
            return event;
        }
    }

  
    public static <T extends EventData> Builder<T> builder() {
        return new Builder<>();
    }
}
