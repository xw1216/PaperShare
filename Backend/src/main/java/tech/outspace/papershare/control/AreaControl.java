package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.AreaNameDto;
import tech.outspace.papershare.model.dto.AreaRowDto;
import tech.outspace.papershare.model.vo.AreaVo;
import tech.outspace.papershare.service.area.AreaControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("area")
public class AreaControl {

    private final AreaControlService areaService;

    public AreaControl(AreaControlService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/all")
    public Result<List<AreaNameDto>> getAllArea(HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return areaService.findAllArea();
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取领域列表失败");
        }
    }

    @GetMapping("/table")
    public Result<List<AreaRowDto>> getAreaTable(HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return areaService.findAllAreaTableRow();
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取领域列表失败");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Result<String> addArea(@RequestBody AreaVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return areaService.addNewArea(vo.getName());
        } catch (DuplicateKeyException e) {
            return HttpFormat.reviseErrorResponse(response, EResult.DATA_DUPLICATE, "领域已经存在");
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "添加领域失败");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public Result<String> removeArea(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return areaService.deleteArea(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "删除领域失败");
        }
    }

}
