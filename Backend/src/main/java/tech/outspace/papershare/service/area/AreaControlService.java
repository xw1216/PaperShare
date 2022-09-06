package tech.outspace.papershare.service.area;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.dto.AreaNameDto;
import tech.outspace.papershare.model.dto.AreaRowDto;
import tech.outspace.papershare.model.entity.objs.Area;
import tech.outspace.papershare.model.entity.rels.UserAreaRel;
import tech.outspace.papershare.repo.objs.AreaRepo;
import tech.outspace.papershare.repo.rels.PaperAreaRelRepo;
import tech.outspace.papershare.repo.rels.UserAreaRelRepo;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaControlService {

    private final AreaRepo areaRepo;
    private final PaperAreaRelRepo paperAreaRelRepo;
    private final UserAreaRelRepo userAreaRelRepo;

    public AreaControlService(AreaRepo areaRepo, PaperAreaRelRepo paperAreaRelRepo, UserAreaRelRepo userAreaRelRepo) {
        this.areaRepo = areaRepo;
        this.paperAreaRelRepo = paperAreaRelRepo;
        this.userAreaRelRepo = userAreaRelRepo;
    }

    public Result<List<AreaNameDto>> findAllArea() {
        List<Area> areaList = areaRepo.findAllNotNull();
        List<AreaNameDto> areaNameList = new ArrayList<>();
        for (Area area : areaList) {
            areaNameList.add(new AreaNameDto(area.getId(), area.getName()));
        }
        return Result.factory(EResult.SUCCESS, "成功获取所有领域信息", areaNameList);
    }

    public List<AreaNameDto> findUserAllArea(String userId) {
        List<UserAreaRel> userAreaRelList = userAreaRelRepo.findByUserIdEquals(userId);
        return findAreaNameList(userAreaRelList);
    }

    public List<AreaNameDto> findAreaNameList(List<UserAreaRel> areaRelsList) {
        List<AreaNameDto> areaNameList = new ArrayList<>();
        for (UserAreaRel rel :
                areaRelsList) {
            Area area = areaRepo.findByIdEquals(rel.getAreaId());
            areaNameList.add(new AreaNameDto(area.getId(), area.getName()));
        }
        return areaNameList;
    }

    public List<AreaNameDto> findAreaNameListByIdList(List<String> areaIdList) {
        List<AreaNameDto> areaNameList = new ArrayList<>();
        for (String areaId : areaIdList) {
            Area area = areaRepo.findByIdEquals(areaId);
            areaNameList.add(new AreaNameDto(area.getId(), area.getName()));
        }
        return areaNameList;
    }

    public Result<List<AreaRowDto>> findAllAreaTableRow() {
        List<Area> areaList = areaRepo.findAllNotNull();
        List<AreaRowDto> areaRowDtoList = new ArrayList<>();
        for (Area area : areaList) {
            String id = area.getId();
            AreaRowDto dto = new AreaRowDto();
            dto.setName(area.getName());
            dto.setId(id);
            dto.setPaperUsage(paperAreaRelRepo.countByAreaIdEquals(id));
            dto.setUserUsage(userAreaRelRepo.countByAreaIdEquals(id));
            areaRowDtoList.add(dto);
        }
        return Result.factory(EResult.SUCCESS, "获取领域表成功", areaRowDtoList);
    }

    public void addUserAreaRel(String userId, String areaId) {
        userAreaRelRepo.save(new UserAreaRel(userId, areaId));
    }

    public void removeUserAreaRel(String userId, String areaId) {
        userAreaRelRepo.deleteByUserIdAndAreaId(userId, areaId);
    }

    public Result<String> addNewArea(String name) {

        boolean duplicate = areaRepo.existsByNameEquals(name);

        if (duplicate) {
            throw new DuplicateKeyException("重复的领域名称");
        }

        Area area = new Area(name);
        areaRepo.save(area);
        String str = "添加领域成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> deleteArea(String id) {
        areaRepo.deleteByIdEquals(id);
        String str = "删除领域成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }
}
