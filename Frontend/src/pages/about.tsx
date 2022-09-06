import {Link as RouterLink} from "react-router-dom";
import Button from "@mui/material/Button";
import React from "react";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import {Box, Grid, Typography} from "@mui/material";

export default class About extends React.Component {
    render() {
        return (
            <div>
                <Header></Header>
                <Grid container>
                    <Grid item xs={2}></Grid>
                    <Grid item xs={8} mt={5}>
                        <Typography my={8} variant={"h4"} textAlign={"start"}>
                            关于本站
                        </Typography>
                        <Typography my={3} variant={"h6"} textAlign={"start"}>
                            PaperShare 是一款兼具文献管理、随记笔记、交流讨论的学术工具网站。
                        </Typography>

                        <Typography my={3} variant={"h6"} textAlign={"start"}>
                            您可以在本站注册账号，查看管理员录入的学术论文，并将其收入进您创建的仓库。
                        </Typography>

                        <Typography my={3} variant={"h6"} textAlign={"start"}>
                            此外，您还可以与其他用户就某篇论文进行讨论，或者对论文进行网页笔记。
                        </Typography>

                        <Typography my={3} variant={"h6"} textAlign={"start"}>
                            本网站使用 Springboot v2.6 ， React v18.0 与 MUI v5.8 开发而成。
                        </Typography>
                        <Box my={4}>
                            <Button variant={"contained"} component={RouterLink} to="/">返回首页</Button>
                        </Box>
                    </Grid>
                    <Grid item xs={2}></Grid>
                </Grid>
                <Footer></Footer>
            </div>
        );
    }
}