package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.RepoBriefDto;
import tech.outspace.papershare.model.dto.RepoDetailDto;
import tech.outspace.papershare.model.dto.RepoNameDto;
import tech.outspace.papershare.model.vo.RepoEditVo;
import tech.outspace.papershare.model.vo.RepoNewVo;
import tech.outspace.papershare.service.auth.AuthControlService;
import tech.outspace.papershare.service.repo.RepoControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/repo")
public class RepoControl {
    private final RepoControlService repoService;
    private final AuthControlService authService;


    public RepoControl(RepoControlService repoService, AuthControlService authService) {
        this.repoService = repoService;
        this.authService = authService;
    }

    @GetMapping("/explore")
    public Result<List<RepoBriefDto>> getRepoExplore(HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.GetExploreRepos();
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取探索仓库失败");
        }
    }

    @GetMapping("/stars")
    public Result<List<RepoBriefDto>> getRepoStars(HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            String userId = authService.GetUserIdFromAuth();
            return repoService.GetStarsRepos(userId);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取关注仓库失败");
        }
    }

    @PostMapping("/stars/add")
    public Result<String> addStarsRepo(@RequestParam String id, HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.AddStarsRepo(userId, id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "关注仓库失败");
        }
    }

    @DeleteMapping("/stars/delete")
    public Result<String> removeStarsRepo(@RequestParam String id, HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.RemoveStarsRepo(userId, id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "取关仓库失败");
        }
    }


    @PostMapping("/add")
    public Result<String> addRepo(@RequestBody RepoNewVo vo, HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.AddRepo(vo, userId);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "新建仓库失败");
        }
    }

    @PostMapping("/edit")
    public Result<String> editRepo(@RequestBody RepoEditVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.EditRepo(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "修改仓库失败");
        }
    }

    @DeleteMapping("/delete")
    public Result<String> deleteRepo(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.DeleteRepo(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "删除仓库失败");
        }
    }

    @GetMapping("/detail")
    public Result<RepoDetailDto> getRepoDetail(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.getRepoDetail(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @GetMapping("/valid")
    public Result<List<RepoNameDto>> getAllValidRepoName(HttpServletResponse response) {
        try {
            String userId = authService.GetUserIdFromAuth();
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return repoService.getValidRepoNames(userId);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取可用仓库列表失败");
        }
    }


}
