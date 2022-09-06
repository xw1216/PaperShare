import http from "./axios";

export interface SignUpVo {
    email: any,
    name: any,
    pass: any,
    checkCode: any
}

export interface SignInVo {
    email: any,
    pass: any
}

export default class AuthLib {
    static setHttpUser = (token: string, userId: string, userName: string, role: string) => {
        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);
        localStorage.setItem("userName", userName);
        localStorage.setItem("role", role);
        http.defaults.headers.common['Authorization'] = 'Bearer ' + token;
    };

    static isAdmin = (): boolean => {
        let role = localStorage.getItem("role");
        return role === "ADMIN";
    }

    static isLogin = (): boolean => {
        let token = localStorage.getItem("token");
        return !!token;
    };

    static getUserId = () => {
        return localStorage.getItem("userId");
    }

    static getUserName = () => {
        return localStorage.getItem("userName");
    }

    static clearHttpToken = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("userName");
        localStorage.removeItem("role");
        http.defaults.headers.common['Authorization'] = "";
    }

    static sendCheckCode = (email: string) => {
        return http.post("/auth/email/code", {
            email: email
        });
    }

    static signup = (args: SignUpVo) => {
        return http.post("/auth/register", args);
    }

    static signIn = (args: SignInVo) => {
        return http.post("/auth/login", args);
    }

    static Logout = () => {
        return http.post("/auth/logout");
    }
}



