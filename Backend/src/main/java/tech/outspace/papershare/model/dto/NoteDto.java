package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.entity.objs.Note;
import tech.outspace.papershare.utils.time.TimeUtil;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private String id;
    private String title;
    private String cont;
    private LocalDateTime updateTime;

    public NoteDto(Note note) {
        id = note.getId();
        title = note.getTitle();
        cont = note.getCont();
        updateTime = TimeUtil.toSystemTime(note.getUpdateTime());
    }
}
