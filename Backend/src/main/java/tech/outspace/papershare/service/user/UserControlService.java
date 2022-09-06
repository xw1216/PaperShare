package tech.outspace.papershare.service.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.dto.AreaNameDto;
import tech.outspace.papershare.model.dto.UserDisplayDto;
import tech.outspace.papershare.model.dto.UserInfoDto;
import tech.outspace.papershare.model.entity.objs.User;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.service.area.AreaControlService;
import tech.outspace.papershare.service.repo.RepoControlService;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import java.util.List;

@Service
public class UserControlService {
    private final UserRepo userRepo;
    private final AreaControlService areaService;
    private final RepoControlService repoService;

    public UserControlService(UserRepo userRepo, AreaControlService areaService, RepoControlService repoService) {
        this.userRepo = userRepo;
        this.areaService = areaService;
        this.repoService = repoService;
    }


    public Result<UserDisplayDto> getUserDisplayInfo(String id) {
        User user = userRepo.findByIdEquals(id);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserDisplayDto dto = new UserDisplayDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setMotto(user.getMotto());
        dto.setAreas(areaService.findUserAllArea(id));
        dto.setRepos(repoService.GetUserAllRepoBriefInfo(id));

        return Result.factory(EResult.SUCCESS, "当前用户所有仓库获取成功", dto);
    }

    public Result<UserInfoDto> getUserInfo(String id) {
        User user = userRepo.findByIdEquals(id);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserInfoDto dto = new UserInfoDto(user, areaService.findUserAllArea(id));
        return Result.factory(EResult.SUCCESS, "获取用户信息成功", dto);
    }

    @Transactional
    public Result<String> editUserInfo(UserInfoDto dto) {
        userRepo.updateNameAndMottoByIdEquals(dto.getName(), dto.getMotto(), dto.getId());
        List<AreaNameDto> areaNameList = areaService.findUserAllArea(dto.getId());
        List<AreaNameDto> newAreaNameList = dto.getAreas();

        for (AreaNameDto areaNameDto : areaNameList) {
            for (int j = 0; j < newAreaNameList.size(); j++) {
                // area keep
                if (areaNameDto.getId().equals(newAreaNameList.get(j).getId())) {
                    newAreaNameList.remove(j);
                    break;
                }
            }
            // area delete
            areaService.removeUserAreaRel(dto.getId(), areaNameDto.getId());
        }

        for (AreaNameDto areaNameDto : newAreaNameList) {
            areaService.addUserAreaRel(dto.getId(), areaNameDto.getId());
        }
        return Result.factory(EResult.SUCCESS, "更该用户信息成功");
    }
}
