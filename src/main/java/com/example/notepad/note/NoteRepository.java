package com.example.notepad.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("""
            SELECT n FROM Note n
            WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY n.updatedAt DESC
            """)
    List<Note> search(@Param("keyword") String keyword);

    List<Note> findAllByOrderByUpdatedAtDesc();
}
