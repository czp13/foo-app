package com.example.application.data.service;

import com.example.application.data.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    public List<Event> getEvents() {
        return repository.findAll();
    }

    public void save(Event event) {
        repository.save(event);
    }

    public Optional<Event> getEventById(Long eventId) {
        return repository.findById(eventId);
    }
}
