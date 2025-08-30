package com.rhythmo.rhythmobackend.repository;

import com.rhythmo.rhythmobackend.model.Activity;
import com.rhythmo.rhythmobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByAuthor(User author);
}
