import React, {Component} from "react";
import Header from "../components/global/header";
import {Box, createTheme, Grid, ThemeProvider, Typography} from "@mui/material";
import RepoAccord from "../components/repo/repoAccord";
import Footer from "../components/global/footer";
import RepoLib from "../lib/repo";
import {DataUtil} from "../lib/axios";

const theme = createTheme();

export default class Stars extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {repos: []}
        this.getStarsRepos.bind(this);
        this.removeRepoFromStar.bind(this);
    }

    componentDidMount() {
        this.getStarsRepos();
    }

    removeRepoFromStar = (repoId: string) => {
        RepoLib.removeStarsRepo(repoId)
            .then((response: any) => {
                console.log(response);
                this.props.enqueue("仓库取关成功", {
                    variant: "success",
                })
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error",
                })
            })
    }

    getStarsRepos = () => {
        RepoLib.getStarsRepo()
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                this.setState({repos: res.data});
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error",
                })
            })
    }

    displayStars = () => {
        if (this.state.repos !== null) {
            if (this.state.repos.length === 0) {
                return (
                    <Box my={4}>
                        <Typography variant={"h5"} textAlign={"center"}>
                            {"暂无已关注的仓库"}
                        </Typography>
                    </Box>
                );
            } else {
                return this.state.repos.map((repo: any) =>
                    <RepoAccord
                        key={repo.id}
                        repo={repo}
                        type={"stars"}
                        func={() => {
                            this.removeRepoFromStar(repo.id);
                            this.getStarsRepos();
                        }}
                    />
                );
            }

        }
    }

    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Grid container>
                        <Grid item xs={2}></Grid>
                        <Grid item xs={8} mt={5}>
                            <Box my={4}>
                                <Typography textAlign={"start"} variant={"h3"}>
                                    关注的仓库
                                </Typography>
                            </Box>
                            <Grid container flexDirection={"column"}>
                                {this.displayStars()}
                            </Grid>
                        </Grid>
                        <Grid item xs={2}></Grid>
                    </Grid>
                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}