import React, {Component} from "react";
import {Box, createTheme, Grid, ThemeProvider, Typography} from "@mui/material";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import {Navigate} from "react-router-dom";
import AuthLib from "../lib/auth";
import UserLib from "../lib/user";
import {DataUtil} from "../lib/axios";
import AreaLib from "../lib/area";
import ProfileForm from "../components/user/profileForm";

const theme = createTheme();

export default class Profile extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {
            logout: false,
            areaLoading: true,
            userInfoLoading: true,
            userInfo: {
                id: "",
                name: "",
                email: "",
                motto: "",
                role: "",
                areas: [],
            },
            areaList: [],
        };
        this.logout.bind(this);
        this.updateAreaList.bind(this);
        this.updateUserInfo.bind(this);
        this.waitForLoading.bind(this);
    }

    componentDidMount() {
        let userInfo = this.updateUserInfo();
        let areaList = this.updateAreaList();

        userInfo.then(data => {
            this.setState({
                userInfo: {
                    id: data.id,
                    name: data.name,
                    email: data.email,
                    motto: data.motto,
                    role: data.role,
                    areas: data.areas
                }
            });
            this.setState({userInfoLoading: false})
        })

        areaList.then(data => {
            this.setState({areaList: data});
            this.setState({areaLoading: false})
        })
    }

    updateUserInfo = async () => {
        let userInfo: any;
        await UserLib.getUserInfo()
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                userInfo = res.data;
            })
            .catch((response: any) => {
                console.log(response);
                userInfo = null;
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error"
                });
            });
        return userInfo;
    }

    updateAreaList = async () => {
        let areaList: any;
        await AreaLib.getAllArea()
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                areaList = res.data;
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                areaList = null;
                this.props.enqueue(res.msg, {
                    variant: "error"
                });
            });
        return areaList;
    }

    logout = () => {
        AuthLib
            .Logout()
            .then((response: any) => {
                AuthLib.clearHttpToken();
                this.setState({logout: true});
            }).catch((response: any) => {
            this.props.enqueue("注销失败", {
                variant: "error"
            });
        })
    }

    waitForLoading = () => {
        if (this.state.areaLoading === false && this.state.userInfoLoading === false) {
            return (
                <ProfileForm
                    userInfo={this.state.userInfo}
                    areaList={this.state.areaList}
                    logout={this.logout}
                />
            );
        } else {
            return (
                <Typography variant={"h6"}>
                    正在加载中···
                </Typography>
            );
        }
    };

    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Grid container>
                        {this.state.logout &&
                            <Navigate to={"/"} replace={true}/>
                        }
                        <Grid item md={2}></Grid>
                        <Grid item md={8}>
                            <Box my={8}>
                                <Typography textAlign={"start"} variant={"h3"}>
                                    个人资料
                                </Typography>
                            </Box>
                            {this.waitForLoading()}
                        </Grid>

                    </Grid>
                    <Grid item md={2}></Grid>
                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}