import http from "./axios";


export interface IUser {
    id: string,
    email: string,
    name: any,
    role: string,
    motto: any,
    areas: any
}

export default class UserLib {

    static getUserInfo = () => {
        return http.get("/user/get");
    }

    static editUserInfo = (vo: IUser) => {
        return http.post("/user/edit", vo);
    }

}