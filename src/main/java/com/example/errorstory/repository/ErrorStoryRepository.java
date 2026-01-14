package com.example.errorstory.repository;

import com.example.errorstory.model.ErrorStory;
import com.example.errorstory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorStoryRepository extends JpaRepository<ErrorStory, Long> {

    // Fetch all stories for a particular user (their bookshelf)
    List<ErrorStory> findByUser(User user);

    // CRUD already included:
    // save(ErrorStory s)
    // findById(Long id)
    // findAll()
    // delete(ErrorStory s)
    // deleteById(Long id)
}
