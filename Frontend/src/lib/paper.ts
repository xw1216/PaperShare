import http from "./axios";

export interface PaperVo {
    title: any,
    brief: any,
    author: any,
    year: any,
    journal: any,
    doi: any,
    path: any,
    areaList: any,
}

export default class PaperLib {

    static addPaper = (vo: PaperVo) => {
        return http.post("/paper/add", vo);
    }

    static searchPaper = (keyword: string) => {
        return http.post("/paper/search", {
            keyword: keyword,
        })
    }

    static removePaperFromRepo = (paperId: string, repoId: string) => {
        return http.post("/paper/remove-from-repo", {
            paperId: paperId,
            repoId: repoId,
        })
    }

    static addPaperToRepo = (paperId: string, repoIds: Array<string>) => {
        return http.post("/paper/add-to-repo", {
            paperId: paperId,
            repoIds: repoIds,
        })
    }

    static getPaperInfo = (paperId: string) => {
        return http.get("/paper/info", {
            params: {
                id: paperId,
            }
        })
    }

    static getPaperPdf = (paperId: string) => {
        return http.get("/paper/pdf", {
            params: {
                id: paperId,
            }
        })
    }


}