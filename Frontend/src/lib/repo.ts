import http from "./axios";

export interface IRepo {
    id?: any,
    name: any,
    visible: any,
    cont: any
}

export default class RepoLib {
    static getUserRepoInfo = (id: string | null) => {
        return http.get("/user/repos", {
            params: {
                id: id,
            }
        });
    }

    static findExploreRepo = () => {
        return http.get("/repo/explore");
    }

    static getStarsRepo = () => {
        return http.get("/repo/stars");
    }

    static addStarsRepo = (repoId: string) => {
        return http.post("/repo/stars/add", {}, {
            params: {
                id: repoId,
            }
        })
    }

    static removeStarsRepo = (repoId: string) => {
        return http.delete("/repo/stars/delete", {
            params: {
                id: repoId,
            }
        })
    }

    static addRepo = (vo: IRepo) => {
        return http.post("/repo/add", vo);
    }

    static editRepo = (vo: IRepo) => {
        return http.post("/repo/edit", vo);
    }

    static deleteRepo = (id: string) => {
        return http.delete("/repo/delete", {
            params: {
                id: id,
            }
        })
    }

    static getRepoPaper = (id: string) => {
        return http.get("/repo/detail", {
            params: {
                id: id,
            }
        })
    }

    static getValidRepos = () => {
        return http.get("/repo/valid");
    }
}