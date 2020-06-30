package com.pma.server.repository;

import com.pma.server.model.Task;
import com.pma.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByDeadlineAsc();
    List<Task> findAllByUserAndState(User user, String state);
    Task findTaskById(Long id);

}
