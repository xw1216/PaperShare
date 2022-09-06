package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.NoteDto;
import tech.outspace.papershare.model.vo.NoteEditVo;
import tech.outspace.papershare.model.vo.NoteGetVo;
import tech.outspace.papershare.model.vo.NoteNewVo;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.service.note.NoteControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteControl {
    private final NoteControlService noteService;
    private final AuthControlService authService;

    public NoteControl(NoteControlService noteService, AuthControlService authService) {
        this.noteService = noteService;
        this.authService = authService;
    }

    @PostMapping("/add")
    public Result<String> addNote(@RequestBody NoteNewVo vo, HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return noteService.addNote(vo, userId);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @PostMapping("/edit")
    public Result<String> editNote(@RequestBody NoteEditVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return noteService.editNote(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public Result<String> deleteNote(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return noteService.deleteNote(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @PostMapping("/specified")
    public Result<List<NoteDto>> getSpecifiedNote(@RequestBody NoteGetVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return noteService.getNotesForSpecified(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

}
