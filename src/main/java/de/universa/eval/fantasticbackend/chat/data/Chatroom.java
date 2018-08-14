package de.universa.eval.fantasticbackend.chat.data;

import lombok.Data;
import lombok.NonNull;

import java.util.TreeSet;

@Data
public class Chatroom {

    @NonNull
    private String chatname;

    private TreeSet<Message> messages = new TreeSet<>();

}
