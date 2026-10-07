package com.example.notepad.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteRequest(
        @NotBlank(message = "标题不能为空")
        @Size(max = 200, message = "标题不能超过 200 个字符")
        String title,
        @NotNull(message = "内容不能为空")
        String content
) {
}
