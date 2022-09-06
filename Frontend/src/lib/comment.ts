import http from "./axios";

interface CommentVo {
    cont: any,
    paperId: any,
}

export default class CommentLib {
    static addComment = (vo: CommentVo) => {
        return http.post("/comment/add", vo);
    }

    static getPaperComments = (paperId: string) => {
        return http.get("/comment/paper/get", {
            params: {
                id: paperId,
            }
        })
    }
}