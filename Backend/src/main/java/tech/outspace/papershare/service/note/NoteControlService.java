package tech.outspace.papershare.service.note;

import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.NoteDto;
import tech.outspace.papershare.model.entity.objs.Note;
import tech.outspace.papershare.model.vo.NoteEditVo;
import tech.outspace.papershare.model.vo.NoteGetVo;
import tech.outspace.papershare.model.vo.NoteNewVo;
import tech.outspace.papershare.repo.objs.NoteRepo;
import tech.outspace.papershare.repo.objs.PaperRepo;
import tech.outspace.papershare.repo.objs.RepoRepo;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;
import tech.outspace.papershare.utils.time.TimeUtil;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteControlService {
    private final NoteRepo noteRepo;

    private final PaperRepo paperRepo;

    private final RepoRepo repoRepo;

    public NoteControlService(NoteRepo noteRepo, PaperRepo paperRepo, RepoRepo repoRepo) {
        this.noteRepo = noteRepo;
        this.paperRepo = paperRepo;
        this.repoRepo = repoRepo;
    }


    public Result<String> addNote(NoteNewVo vo, String userId) throws NameNotFoundException {
        if (!(repoRepo.existsByIdEquals(vo.getRepoId())) || !(paperRepo.existsByIdEquals(vo.getPaperId()))) {
            throw new NameNotFoundException("指定仓库或论文不存在");
        }

        Note note = new Note(vo.getTitle(), vo.getCont(),
                vo.getRepoId(), vo.getPaperId(), userId, vo.getPage());
        noteRepo.save(note);
        String str = "添加笔记成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> editNote(NoteEditVo vo) {
        noteRepo.updateTitleAndContAndUpdateTimeById(
                vo.getTitle(), vo.getCont(),
                TimeUtil.getUTC(), vo.getId());
        String str = "修改笔记成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> deleteNote(String noteId) {
        noteRepo.deleteByIdEquals(noteId);
        String str = "删除笔记成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<List<NoteDto>> getNotesForSpecified(NoteGetVo vo) {
        List<Note> noteList = noteRepo.findByRepoPaperAndPage(vo.getRepoId(), vo.getPaperId(), vo.getPage());
        List<NoteDto> dtoList = new ArrayList<>();
        for (Note note : noteList) {
            NoteDto dto = new NoteDto(note);
            dtoList.add(dto);
        }
        return Result.factory(EResult.SUCCESS, "获取当前页笔记成功", dtoList);
    }


}
