import http from "./axios";

export interface IArea {
    id: string,
    name: string
}

export default class AreaLib {
    static getAllArea = () => {
        return http.get("/area/all");
    }

    static getAreaTable = () => {
        return http.get("/area/table");
    }

    static addArea = (name: any) => {
        return http.post("/area/add", {
            name: name,
        })
    }

    static deleteArea = (id: string) => {
        return http.delete("/area/delete", {
            params: {
                id: id,
            }
        })
    }
}