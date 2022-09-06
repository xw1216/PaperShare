import http from "./axios";

export interface NoteNewVo {
    title: any,
    cont: any,
    page: any,
    repoId: any,
    paperId: any,
}

export interface NoteEditVo {
    id: any,
    title: any,
    cont: any,
}

export interface NoteGetVo {
    paperId: any,
    repoId: any,
    page: any
}

export default class NoteLib {
    static addNote = (vo: NoteNewVo) => {
        return http.post("/note/add", vo);
    }

    static editNote = (vo: NoteEditVo) => {
        return http.post("/note/edit", vo);
    }

    static deleteNote = (id: string) => {
        return http.delete("/note/delete", {
            params: {
                id: id,
            }
        })
    }

    static getSpecifiedNote = (vo: NoteGetVo) => {
        return http.post("/note/specified", vo);
    }
}