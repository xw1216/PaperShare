import React from "react";
import {Box, Button, Chip, Container, createTheme, Grid, Stack, ThemeProvider, Typography} from "@mui/material";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import RepoCard from "../components/repo/repoCard";
import UserIcon from "../components/user/userIcon";
import RepoEditDialog from "../components/repo/repoEditDialog";
import {Link as RouterLink} from "react-router-dom";
import AuthLib from "../lib/auth";
import RepoLib from "../lib/repo";
import {DataUtil} from "../lib/axios";

const theme = createTheme();

export default class UserRepos extends React.Component<any, any> {
    constructor(props: any) {
        super(props);

        this.state = {
            isNewDialogOpen: false,
            repoInfoList: [],
            userInfo: {
                id: "",
                name: "",
                motto: "",
                areas: []
            },
        }
        this.emptyHandle.bind(this);
        this.closeDialogHandle.bind(this);
        this.openDialogHandle.bind(this);
        this.refreshDisplay.bind(this);
    }

    componentDidMount() {
        this.refreshDisplay();
    }

    refreshDisplay = () => {
        let userId = AuthLib.getUserId();
        if (!userId) {
            this.props.navigate("/login");
        }
        RepoLib.getUserRepoInfo(userId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response).data;
                this.setState({
                    userInfo: {
                        id: res.id,
                        name: res.name,
                        motto: res.motto,
                        areas: res.areas
                    }
                });

                this.setState({
                    repoInfoList: res.repos
                });

            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);

                if (res.code === 403) {
                    this.props.enqueue("未登录", {
                        variant: "error"
                    });
                    this.props.navigate("/login");
                } else {
                    this.props.enqueue(res.msg, {
                        variant: "error"
                    });
                }

            })
    }

    closeDialogHandle = () => {
        this.setState({isNewDialogOpen: false});
    }

    openDialogHandle = () => {
        this.setState({isNewDialogOpen: true});
    }

    emptyHandle() {
        return (this.state.repoInfoList === null || this.state.repoInfoList.length === 0) ? (
            <Typography
                component="h2"
                variant="h3"
                align="center"
                color="text.primary"
                gutterBottom
                sx={{fontWeight: "lighter"}}
            >
                暂无仓库
            </Typography>
        ) : (
            <Grid container spacing={6}>
                {this.state.repoInfoList.map((repo: any) => (
                    <Grid item key={repo.id} xs={12} sm={6} md={4}>
                        <RepoCard
                            repo={repo}
                            refresh={this.refreshDisplay}
                            enqueue={this.props.enqueue}
                        />
                    </Grid>
                ))}
            </Grid>
        );
    }

    userAreaDisplay() {
        if (this.state.userInfo.areas) {
            return this.state.userInfo.areas.map((area: any) => (
                <Grid mx={1} my={1} item key={area.id}>
                    <Chip label={area.name} variant={"outlined"}></Chip>
                </Grid>
            ))
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
                        <Container maxWidth="lg">
                            <Typography
                                component="h1"
                                variant="h1"
                                align="center"
                                color="text.primary"
                                gutterBottom
                                mb={8}
                            >
                                论文仓库
                            </Typography>
                            <UserIcon
                                userId={this.state.userInfo.id}
                                userName={this.state.userInfo.name}
                                size={"lg"}
                                sx={{
                                    alignItems: 'center',
                                    justifyContent: 'center'
                                }}
                            />
                            <Grid container
                                  flexDirection={"row"}
                                  maxWidth={"lg"}
                                  sx={{
                                      alignItems: 'center',
                                      justifyContent: 'center'
                                  }}
                                  my={3}
                            >
                                {this.userAreaDisplay()}
                            </Grid>

                            <Typography variant="h6" align="center" color="text.secondary" paragraph>
                                {this.state.userInfo.motto}
                            </Typography>
                            <Stack
                                sx={{pt: 4}}
                                direction="row"
                                spacing={2}
                                justifyContent="center"
                            >
                                <Button variant="contained"
                                        onClick={this.openDialogHandle}
                                >
                                    新建仓库
                                </Button>
                                <Button variant="outlined"
                                        component={RouterLink}
                                        to="/explore"
                                >
                                    探索更多
                                </Button>
                            </Stack>

                        </Container>
                    </Box>
                    <Container sx={{py: 8}} maxWidth={"lg"}>
                        {this.emptyHandle()}
                    </Container>
                    <RepoEditDialog
                        status={{
                            open: this.state.isNewDialogOpen,
                            setClose: this.closeDialogHandle,
                            refresh: this.refreshDisplay,
                        }}
                        type={"new"}
                    />
                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}