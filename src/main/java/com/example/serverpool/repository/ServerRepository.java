package com.example.serverpool.repository;

import com.example.serverpool.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
    @Query("SELECT s FROM Server s WHERE s.isCloud = 1 and s.memory >= ?1")
    Collection<Server> findAllActiveServersWithMinSize(long size);

    @Query("SELECT s FROM Server s WHERE s.parent.id = ?1")
    Collection<Server> findByParentId(long parentId);
}
