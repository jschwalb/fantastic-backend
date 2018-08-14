package de.universa.eval.fantasticbackend.chat.service;

import de.universa.eval.fantasticbackend.chat.data.Chatroom;
import de.universa.eval.fantasticbackend.chat.data.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "chat")
@Slf4j
public class ChatController {

    private Map<String, Chatroom> chatrooms = new HashMap<>();

    @GetMapping("/all")
    public Collection<String> getAllChats() {
        log.debug("Received request for all chats. Returning {} chats.", chatrooms.keySet().size());
        return chatrooms.keySet();
    }

    @PutMapping(path = "/create/{name}")
    public ResponseEntity<String> createChatroom(@PathVariable("name") String name) {
        log.debug("Received request for a new chatroom with name: {}.", name);
        if (chatrooms.containsKey(name)) {
            log.warn("A chatroom {} already exists.", name);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A Chatroom " + name + " already exists.");
        }

        log.info("Creating chatroom {}.", name);
        Chatroom chatroom = new Chatroom(name);
        chatrooms.put(name, chatroom);
        log.info("A chatroom {} was created.", name);

        chatroom.getMessages().add(new Message("ADMIN", LocalDateTime.now(), "Chatroom " + name + " is opened."));

        return ResponseEntity.ok().body("A Chatroom " + name + " was created.");
    }

    @GetMapping(path = "/{chatId}")
    public Collection<Message> getAllMessages(@PathVariable("chatId") String chatId) {
        log.debug("Received request for all Messages of chatroom {}.", chatId);
        if (chatrooms.containsKey(chatId)) {
            TreeSet<Message> messages = chatrooms.get(chatId).getMessages();
            log.debug("Returning {} messages of chatroom {}.", chatId, messages.size());
            return messages;
        }

        log.warn("A chatroom {} does not exist, returning zero messages.");
        return Collections.emptySet();
    }

    @GetMapping(path = "/{chatId}/after={after}")
    public Collection<Message> getMessages(@PathVariable("chatId") String chatId, @PathVariable("after") String after) {
        log.debug("Received request for all Messages of chatroom {} after {}.", chatId, after);
        if (chatrooms.containsKey(chatId)) {
            LocalDateTime dateTime = LocalDateTime.parse(after);
            TreeSet<Message> messages = chatrooms.get(chatId).getMessages();
            List<Message> messagesAfter = messages.stream().filter(m -> m.getPostingTime().isAfter(dateTime)).sorted().collect(Collectors.toList());
            log.debug("Returning {} messages of chatroom {} after {}.", messagesAfter.size(), chatId, after);
            return messagesAfter;
        }

        log.warn("A chatroom {} does not exist, returning zero messages.");
        return Collections.emptySet();
    }

    @PostMapping(path = "/{chatId}/message&sender={sender}&content={content}")
    public ResponseEntity<String> addMessage(@PathVariable("chatId") String chatId, @PathVariable("sender") String sender, @PathVariable("content") String content) {
        log.debug("Received request to add a Messages to chatroom {} with sender {} and content {}.", chatId, sender, content);
        if (chatrooms.containsKey(chatId)) {
            TreeSet<Message> messages = chatrooms.get(chatId).getMessages();
            Message message = new Message(sender, LocalDateTime.now(), content);
            messages.add(message);
            log.debug("Added new Message to Chatroom {} with sender {} and content {}.", chatId);

            return ResponseEntity.ok().build();
        }

        log.warn("A chatroom {} does not exist, unable to add message.", chatId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A Chatroom " + chatId + " does not exist.");
    }

}
