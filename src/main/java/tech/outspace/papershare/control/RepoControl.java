package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.outspace.papershare.model.entity.objs.Note;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.entity.objs.Repo;
import tech.outspace.papershare.repo.objs.PaperRepo;
import tech.outspace.papershare.repo.objs.RepositoryRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class RepoControl {
    private final RepositoryRepo repositoryRepo;
    private final UserRepo userRepo;
    private final PaperRepo paperRepo;


    public RepoControl(RepositoryRepo repositoryRepo, UserRepo userRepo, PaperRepo paperRepo) {
        this.repositoryRepo = repositoryRepo;
        this.userRepo = userRepo;
        this.paperRepo = paperRepo;
    }

    @GetMapping(value = "/repos")
    public Result<List<Repo>> findAllRepos(HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/repos/update")
    public Result<Repo> updateRepo(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @PostMapping(value = "/repos/settings")
    public Result<String> setRepoSettings(HttpServletResponse response) {
        return null;
    }

    @GetMapping(value = "/repos/settings")
    public Result<Repo> getRepoSettings(HttpServletResponse response) {
        return null;
    }

    @GetMapping(value = "/repos/papers")
    public Result<List<Paper>> findRepoPapers(HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "/repos/papers/update")
    public Result<String> updateRepoPaper(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @GetMapping(value = "/repos/notes")
    public Result<Note> findRepoNotes(HttpServletResponse response) {
        return null;
    }


}
