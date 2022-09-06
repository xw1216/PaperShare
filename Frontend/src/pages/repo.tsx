import Header from "../components/global/header";
import Footer from "../components/global/footer";
import React, {Component} from "react";
import {useParams} from "react-router-dom";
import {Box, Container, createTheme, Grid, ThemeProvider, Typography} from "@mui/material";
import PaperAccord from "../components/paper/paperAccord";
import RepoSideCard from "../components/repo/repoSideCard";
import RepoLib from "../lib/repo";
import {DataUtil} from "../lib/axios";

const papers = [
    {id: "1", title: "论文标题", cont: "论文详情"},
    {id: "2", title: "paper tile", cont: "paper detail"}];
const labels = ["Computer Vision", "Computer Graphics"];

export const RepoWithRouter = (props: any) => {
    const params = useParams();
    return (
        <Repo repoId={params.id} enqueue={props.enqueue}></Repo>
    );
}

const theme = createTheme();

export default class Repo extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            repoDetail: null,
            paperList: null,
            needRefresh: true
        };
        this.requestRefresh.bind(this);
        this.fetchRepoPaperDetail.bind(this);
        this.papersDisplay.bind(this);
        this.sideCardDisplay.bind(this);
        this.doRefresh.bind(this);
    }

    componentDidMount() {
        if (this.state.needRefresh) {
            this.doRefresh();
        }
    }

    doRefresh = () => {
        let detail = this.fetchRepoPaperDetail();

        detail.then((data: any) => {
            this.setState({
                repoDetail: {
                    id: data.id,
                    name: data.name,
                    cont: data.cont,
                    userId: data.userId,
                    userName: data.userName,
                    visible: data.visible
                },
                paperList: data.papers,
                needRefresh: false
            });
        });
    }

    requestRefresh = () => {
        this.doRefresh();
    }

    fetchRepoPaperDetail = async () => {
        let details: any;
        await RepoLib.getRepoPaper(this.props.repoId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                details = res.data;
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error",
                });
            });
        return details;
    }

    papersDisplay = () => {
        if (this.state.paperList === null || this.state.paperList === undefined || this.state.paperList.length === 0) {
            return (
                <Typography variant={"h6"} textAlign={"center"}>
                    仓库内暂无论文
                </Typography>
            );
        } else {
            return this.state.paperList.map((paper: any) => (
                <PaperAccord
                    key={paper.id}
                    type={"repo"}
                    repoId={this.props.repoId}
                    paper={paper}
                    refresh={this.requestRefresh}
                />
            ));
        }
    }

    sideCardDisplay = () => {
        if (this.state.repoDetail !== null && this.state.repoDetail !== undefined) {
            return (
                <RepoSideCard
                    repo={this.state.repoDetail}
                />
            );
        } else {
            return (
                <Typography variant={"body2"} textAlign={"center"}>
                    加载中···
                </Typography>
            );
        }
    }

    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Box
                        sx={{
                            bgcolor: 'background.paper',
                            pt: 8,
                            pb: 6,
                        }}
                    >
                        <Grid container>
                            <Grid item md={1}></Grid>
                            <Grid item lg={7} md={6}>
                                <Container
                                    maxWidth="lg"
                                    sx={{justifyContent: "flex-start"}}>
                                    <Typography
                                        my={4}
                                        textAlign={"start"}
                                        variant={"h4"}>
                                        仓库详情
                                    </Typography>
                                    <Grid>
                                        {this.papersDisplay()}
                                    </Grid>
                                </Container>
                            </Grid>
                            <Grid item lg={3} md={4}>
                                <Grid mt={8} ml={4}>
                                    {this.sideCardDisplay()}
                                </Grid>
                            </Grid>
                            <Grid item md={1}></Grid>
                        </Grid>
                    </Box>
                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}