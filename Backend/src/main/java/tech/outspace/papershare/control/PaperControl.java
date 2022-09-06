package tech.outspace.papershare.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.outspace.papershare.model.dto.PaperInfoDto;
import tech.outspace.papershare.model.vo.AddPaperToRepoVo;
import tech.outspace.papershare.model.vo.PaperKeywordVo;
import tech.outspace.papershare.model.vo.PaperVo;
import tech.outspace.papershare.model.vo.RemovePaperFromRepoVo;
import tech.outspace.papershare.service.paper.PaperControlService;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("paper")
public class PaperControl {

    private final PaperControlService paperService;

    public PaperControl(PaperControlService paperService) {
        this.paperService = paperService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Result<String> addPaper(@RequestBody PaperVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return paperService.addPaper(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "论文添加失败");
        }
    }

    @PostMapping("/search")
    public Result<List<PaperInfoDto>> searchPaper(@RequestBody PaperKeywordVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            List<PaperInfoDto> dtoList = paperService.searchPaper(vo.getKeyword());
            return Result.factory(EResult.SUCCESS, "论文搜索成功", dtoList);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<PaperInfoDto> findPaperInfo(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return Result.factory(EResult.SUCCESS, "获取论文详情成功", paperService.findPaperInfo(id));
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "获取论文详情失败");
        }
    }

    @PostMapping("/add-to-repo")
    public Result<String> addPaperToRepo(@RequestBody AddPaperToRepoVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return paperService.addPaperToRepo(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "添加到仓库失败");
        }
    }

    @PostMapping("/remove-from-repo")
    public Result<String> removePaperFromRepo(@RequestBody RemovePaperFromRepoVo vo, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return paperService.removePaperFromRepo(vo);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, "从仓库移出失败");
        }
    }

    @GetMapping("/pdf")
    public Result<String> getPaperPdf(@RequestParam String id, HttpServletResponse response) {
        try {
            HttpFormat.reviseResponse(response, EResult.SUCCESS.getCode());
            return paperService.getPaperPdf(id);
        } catch (Exception e) {
            return HttpFormat.reviseErrorResponse(response, EResult.UNKNOWN_ERROR, e.getMessage());
        }
    }


}
