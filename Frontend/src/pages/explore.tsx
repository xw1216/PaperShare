import React, {Component} from "react";
import Header from "../components/global/header";
import {Box, createTheme, Grid, ThemeProvider, Typography} from "@mui/material";
import Footer from "../components/global/footer";
import RepoAccord from "../components/repo/repoAccord";
import RepoLib from "../lib/repo";
import {DataUtil} from "../lib/axios";

const theme = createTheme();

export default class Explore extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {repos: []}
        this.getExploreRepos.bind(this);
    }

    componentDidMount() {
        this.getExploreRepos();
    }

    addRepoToStar = (repoId: string) => {
        RepoLib.addStarsRepo(repoId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                this.props.enqueue("仓库关注成功", {
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

    getExploreRepos = () => {
        RepoLib.findExploreRepo()
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

    displayExplore = () => {
        if (this.state.repos !== null) {
            if (this.state.repos.length === 0) {
                return (
                    <Box my={4}>
                        <Typography variant={"h5"} textAlign={"center"}>
                            {"暂无可供探索的仓库"}
                        </Typography>
                    </Box>
                );
            }
            return this.state.repos.map((repo: any) =>
                <RepoAccord
                    key={repo.id}
                    repo={repo}
                    type={"explore"}
                    func={() => {
                        this.addRepoToStar(repo.id);
                    }}
                />
            )
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
                                    探索仓库
                                </Typography>
                            </Box>
                            <Grid container flexDirection={"column"}>
                                {this.displayExplore()}
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