package com.example.notepad.note;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse create(NoteRequest request) {
        Note note = noteRepository.save(new Note(request.title().trim(), request.content()));
        return NoteResponse.from(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> findAll(String keyword) {
        List<Note> notes = keyword == null || keyword.isBlank()
                ? noteRepository.findAllByOrderByUpdatedAtDesc()
                : noteRepository.search(keyword.trim());
        return notes.stream().map(NoteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public NoteResponse findById(Long id) {
        return NoteResponse.from(getNote(id));
    }

    public NoteResponse update(Long id, NoteRequest request) {
        Note note = getNote(id);
        note.update(request.title().trim(), request.content());
        return NoteResponse.from(noteRepository.save(note));
    }

    public void delete(Long id) {
        Note note = getNote(id);
        noteRepository.delete(note);
    }

    private Note getNote(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "记事不存在"));
    }
}
