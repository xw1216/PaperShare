package tech.outspace.papershare.service.paper;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.dto.AreaNameDto;
import tech.outspace.papershare.model.dto.PaperInfoDto;
import tech.outspace.papershare.model.entity.objs.Paper;
import tech.outspace.papershare.model.entity.rels.PaperAreaRel;
import tech.outspace.papershare.model.entity.rels.RepoPaperRel;
import tech.outspace.papershare.model.vo.AddPaperToRepoVo;
import tech.outspace.papershare.model.vo.PaperVo;
import tech.outspace.papershare.model.vo.RemovePaperFromRepoVo;
import tech.outspace.papershare.repo.objs.NoteRepo;
import tech.outspace.papershare.repo.objs.PaperRepo;
import tech.outspace.papershare.repo.rels.PaperAreaRelRepo;
import tech.outspace.papershare.repo.rels.RepoPaperRelRepo;
import tech.outspace.papershare.service.area.AreaControlService;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.naming.NameNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaperControlService {
    private final PaperRepo paperRepo;

    private final NoteRepo noteRepo;

    private final PaperAreaRelRepo paperAreaRelRepo;

    private final RepoPaperRelRepo repoPaperRelRepo;

    private final AreaControlService areaService;


    public PaperControlService(PaperRepo paperRepo, NoteRepo noteRepo, PaperAreaRelRepo paperAreaRelRepo,
                               RepoPaperRelRepo repoPaperRelRepo, AreaControlService areaService) {
        this.paperRepo = paperRepo;
        this.noteRepo = noteRepo;
        this.paperAreaRelRepo = paperAreaRelRepo;
        this.repoPaperRelRepo = repoPaperRelRepo;
        this.areaService = areaService;
    }

    @Transactional
    public Result<String> addPaper(PaperVo vo) {
        if (paperRepo.existsByDoiEquals(vo.getDoi())) {
            throw new DuplicateKeyException("重复的论文");
        }
        Paper paper = new Paper(vo);
        paperRepo.save(paper);

        for (AreaNameDto dto : vo.getAreaList()) {
            PaperAreaRel rel = new PaperAreaRel(paper.getId(), dto.getId());
            paperAreaRelRepo.save(rel);
        }
        String str = "论文添加成功";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public PaperInfoDto findPaperInfo(String paperId) throws NameNotFoundException {
        Paper paper = paperRepo.findByIdEquals(paperId);
        if (paper == null) {
            throw new NameNotFoundException("论文不存在");
        }
        return mergePaperArea(paper);
    }

    public List<PaperInfoDto> searchPaper(String keywords) {
        List<Paper> paperList = paperRepo.findByTitleContainsOrderByYearDesc(keywords);
        List<PaperInfoDto> dtoList = new ArrayList<>();
        for (Paper paper : paperList) {
            dtoList.add(mergePaperArea(paper));
        }
        return dtoList;
    }

    private PaperInfoDto mergePaperArea(Paper paper) {
        List<PaperAreaRel> relList = paperAreaRelRepo.findByPaperIdEquals(paper.getId());
        List<String> areaIdList = new ArrayList<>();
        for (PaperAreaRel rel : relList) {
            areaIdList.add(rel.getAreaId());
        }
        List<AreaNameDto> areaList = areaService.findAreaNameListByIdList(areaIdList);
        return new PaperInfoDto(paper, areaList);
    }

    public Result<String> addPaperToRepo(AddPaperToRepoVo vo) {
        for (String repoId : vo.getRepoIds()) {
            if (repoPaperRelRepo.existsByRepoIdAndPaperId(vo.getPaperId(), repoId)) {
                continue;
            }
            RepoPaperRel rel = new RepoPaperRel(repoId, vo.getPaperId());
            repoPaperRelRepo.save(rel);
        }
        String str = "论文成功添加到仓库";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> removePaperFromRepo(RemovePaperFromRepoVo vo) {
        repoPaperRelRepo.deleteByRepoIdAndPaperId(vo.getRepoId(), vo.getPaperId());
        noteRepo.deleteByRepoIdAndPaperId(vo.getRepoId(), vo.getPaperId());
        String str = "论文与相关笔记已从仓库移除";
        return Result.factory(EResult.SUCCESS, str, str);
    }

    public Result<String> getPaperPdf(String paperId) throws NameNotFoundException {
        Paper paper = paperRepo.findByIdEquals(paperId);
        if (paper == null) {
            throw new NameNotFoundException("论文不存在");
        }
        return Result.factory(EResult.SUCCESS, "成功获取论文 PDF 文件", paper.getPath());
    }

}
