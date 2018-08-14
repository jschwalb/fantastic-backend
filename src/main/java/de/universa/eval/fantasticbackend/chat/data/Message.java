package de.universa.eval.fantasticbackend.chat.data;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class Message implements Comparable<Message> {

    @NonNull
    private String sender;

    @NonNull
    private LocalDateTime postingTime;

    @NonNull
    private String content;

    @Override
    public int compareTo(Message that) {
        return this.postingTime.compareTo(that.postingTime);
    }
}
