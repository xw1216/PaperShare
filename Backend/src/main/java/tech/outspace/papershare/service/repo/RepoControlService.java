package tech.outspace.papershare.service.repo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.PaperInfoDto;
import tech.outspace.papershare.model.dto.RepoBriefDto;
import tech.outspace.papershare.model.dto.RepoDetailDto;
import tech.outspace.papershare.model.dto.RepoNameDto;
import tech.outspace.papershare.model.entity.objs.Repo;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;
import tech.outspace.papershare.model.entity.rels.UserRepoFocusRel;
import tech.outspace.papershare.model.vo.RepoEditVo;
import tech.outspace.papershare.model.vo.RepoNewVo;
import tech.outspace.papershare.repo.objs.RepoRepo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.repo.rels.RepoPaperRelRepo;
import tech.outspace.papershare.repo.rels.UserRepoFocusRelRepo;
import tech.outspace.papershare.service.paper.PaperControlService;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;
import tech.outspace.papershare.utils.time.TimeUtil;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepoControlService {

    private final RepoRepo repoRepo;

    private final UserRepo userRepo;

    private final RepoPaperRelRepo repoPaperRelRepo;

    private final UserRepoFocusRelRepo userRepoRelRepo;

    private final PaperControlService paperService;

    public RepoControlService(RepoRepo repoRepo, UserRepo userRepo,
                              RepoPaperRelRepo repoPaperRelRepo,
                              UserRepoFocusRelRepo userRepoFocusRelRepo,
                              PaperControlService paperService) {
        this.repoRepo = repoRepo;
        this.userRepo = userRepo;
        this.repoPaperRelRepo = repoPaperRelRepo;
        this.userRepoRelRepo = userRepoFocusRelRepo;
        this.paperService = paperService;
    }

    public List<RepoBriefDto> GetUserAllRepoBriefInfo(String userId) {
        List<Repo> repoList = repoRepo.findByUserIdEquals(userId);
        List<RepoBriefDto> repoBriefDtoList = new ArrayList<>();

        for (Repo repo : repoList) {
            RepoBriefDto dto = new RepoBriefDto();
            dto.setId(repo.getId());
            dto.setName(repo.getName());
            dto.setCont(repo.getCont());
            dto.setVisible(repo.getVisible());
            repoBriefDtoList.add(dto);
        }
        return repoBriefDtoList;
    }

    public List<RepoNameDto> GetUserAllRepoName(String userId) {
        List<Repo> repoList = repoRepo.findByUserIdEquals(userId);
        List<RepoNameDto> repoNameDtoList = new ArrayList<>();

        for (Repo repo : repoList) {
            RepoNameDto dto = new RepoNameDto();
            dto.setId(repo.getId());
            dto.setName(repo.getName());
            repoNameDtoList.add(dto);
        }
        return repoNameDtoList;
    }

    public Result<List<RepoBriefDto>> GetExploreRepos() {
        Pageable pageable = PageRequest.of(0, 20);

        List<Repo> repoList = repoRepo.findByExploreVisible(pageable);
        List<RepoBriefDto> repoBriefDtoList = new ArrayList<>();
        for (Repo repo : repoList) {
            RepoBriefDto dto = new RepoBriefDto(repo);
            repoBriefDtoList.add(dto);
        }
        return Result.factory(EResult.SUCCESS, "获取探索仓库成功", repoBriefDtoList);
    }

    public Result<List<RepoBriefDto>> GetStarsRepos(String userId) {
        List<UserRepoFocusRel> starsRepoIdList = userRepoRelRepo.findByUserIdEquals(userId);
        List<RepoBriefDto> dtoList = new ArrayList<>();
        for (UserRepoFocusRel rel : starsRepoIdList) {
            Repo repo = repoRepo.findByIdEquals(rel.getRepoId());
            if (repo != null) {
                dtoList.add(new RepoBriefDto(repo));
            }
        }
        return Result.factory(EResult.SUCCESS, "获取关注仓库成功", dtoList);
    }

    public Result<String> AddStarsRepo(String userId, String repoId) {
        UserRepoFocusRel userRepoFocusRel = new UserRepoFocusRel(userId, repoId, TimeUtil.getUTC());
        userRepoRelRepo.save(userRepoFocusRel);
        String str = "成功关注仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> RemoveStarsRepo(String userId, String repoId) {
        userRepoRelRepo.deleteByUserIdAndRepoId(userId, repoId);
        String str = "成功取关仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> AddRepo(RepoNewVo vo, String userId) {
        Repo repo = new Repo(vo.getName(), vo.isVisible(), vo.getCont());
        repo.setUserId(userId);
        repoRepo.save(repo);
        String str = "成功新建仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> EditRepo(RepoEditVo vo) {
        repoRepo.updateNameContVisibleByIdEquals(vo.getName(), vo.getCont(), vo.isVisible(), vo.getId());
        String str = "成功更改仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> DeleteRepo(String repoId) {
        repoRepo.deleteByIdEquals(repoId);
        String str = "成功删除仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<RepoDetailDto> getRepoDetail(String repoId) throws NameNotFoundException {
        Repo repo = repoRepo.findByIdEquals(repoId);
        if (repo == null) {
            throw new NameNotFoundException("仓库不存在");
        }
        User user = userRepo.findByIdEquals(repo.getUserId());
        List<RepoPaperRel> relList = repoPaperRelRepo.findByRepoIdEquals(repoId);

        List<PaperInfoDto> paperList = new ArrayList<>();
        for (RepoPaperRel rel : relList) {
            PaperInfoDto paper = paperService.findPaperInfo(rel.getPaperId());
            paperList.add(paper);
        }

        RepoDetailDto dto = new RepoDetailDto(repo, user, paperList);
        return Result.factory(EResult.SUCCESS, "获取仓库详情成功", dto);
    }

    public Result<List<RepoNameDto>> getValidRepoNames(String userId) {
        List<Repo> repoList = repoRepo.findByUserIdEquals(userId);
        List<RepoNameDto> dtoList = new ArrayList<>();
        for (Repo repo : repoList) {
            dtoList.add(new RepoNameDto(repo.getId(), repo.getName()));
        }
        return Result.factory(EResult.SUCCESS, "获取可用仓库列表成功", dtoList);
    }


}
