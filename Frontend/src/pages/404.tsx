import React, {Component} from "react";
import Header from "../components/global/header";
import Button from "@mui/material/Button";
import {Link as RouterLink} from "react-router-dom";
import Footer from "../components/global/footer";
import {Typography} from "@mui/material";

export default class PageNotFound extends Component<any, any> {
    render() {
        return (
            <div>
                <Header></Header>
                <Typography variant={"h1"} mt={8} mb={4} sx={{fontWeight: "bold"}}>
                    404
                </Typography>
                <Typography variant={"h2"} mb={8}>
                    找不到页面
                </Typography>
                <div>
                    <Button variant={"contained"} component={RouterLink} to="/">返回首页</Button>
                </div>
                <Footer></Footer>
            </div>
        );
    }
}