import {AxiosError, AxiosResponse} from "axios";

const axios = require('axios').default;

const http = axios.create();
http.defaults.baseURL = 'http://localhost:8080';
http.defaults.headers.post['Content-Type'] = "application/json";

interface IResponseData {
    code: number,
    msg: string,
    data: any
}

export class DataUtil {
    static getErrorData = (e: AxiosError): IResponseData => {
        if (e.response) {
            console.log(e.response);
            return e.response.data as IResponseData;
        } else {
            return {code: 500, msg: "服务器未知错误", data: null};
        }
    }

    static getSuccessData = (response: AxiosResponse): IResponseData => {
        return response.data as IResponseData;
    }
}

export default http;
