package com.example.notepad.note;

import com.example.notepad.NotepadApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NotepadApplication.class, properties = {
        "spring.datasource.url=jdbc:h2:mem:notepad-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void supportsNoteCrudAndKeywordSearch() throws Exception {
        String response = mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Spring notes","content":"Build a notes API"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .build()
                .readTree(response)
                .get("id")
                .asLong();

        mockMvc.perform(get("/api/notes").param("keyword", "API"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Spring notes"));

        mockMvc.perform(put("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Updated","content":"New content"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));

        mockMvc.perform(delete("/api/notes/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"  ","content":"A note"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
