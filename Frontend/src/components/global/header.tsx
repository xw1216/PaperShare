import React from 'react';
import {AppBar, Box, Button, Link, Toolbar, Typography} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import AuthLib from "../../lib/auth";
import ColorAvatar from "../user/colorAvatar";

export default class Header extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {userId: null, userName: null};
    }

    componentDidMount() {
        if (AuthLib.isLogin()) {
            this.setState({userId: AuthLib.getUserId(), userName: AuthLib.getUserName()});
        } else {
            this.setState({userId: null, userName: null});
        }
    }

    toggleToolbar = () => {
        if (this.state.userId === null) {
            return (
                <nav>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/">首页</Button>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/about">关于</Button>
                    <Button variant="contained" sx={{my: 1, mx: 1.5}} component={RouterLink}
                            to="/login">登录</Button>
                </nav>
            );
        } else if (AuthLib.isAdmin()) {
            return (
                <nav>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/">首页</Button>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/admin">管理</Button>
                    <ColorAvatar type={"profile"} username={this.state.userName} size={36}></ColorAvatar>
                </nav>
            );
        } else {
            return (
                <nav>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/">首页</Button>
                    <Button variant={"text"} sx={{my: 1, mx: 1.5, fontSize: 16}} component={RouterLink}
                            to="/user">仓库</Button>
                    <Button variant={"text"} sx={{my: 1, ml: 1.5, mr: 3, fontSize: 16}} component={RouterLink}
                            to="/stars">关注</Button>
                    <ColorAvatar type={"profile"} username={this.state.userName} size={36}></ColorAvatar>
                </nav>
            );
        }
    }


    render() {
        return (
            <React.Fragment>
                <AppBar
                    position="sticky"
                    color={"default"}
                    elevation={0}
                    sx={{
                        borderBottom: (theme) => `1px solid ${theme.palette.divider}`,
                        align: "left",
                        zIndex: 10000,
                        color: "#ffffff"
                    }}
                >
                    <Toolbar sx={{flexWrap: 'nowrap'}}>
                        <Typography ml={5} align={"left"} noWrap sx={{flexGrow: 1}} variant={"body1"}>
                            <Link variant={"h5"} underline={"none"} fontWeight={"bold"} component={RouterLink}
                                  to="/">PAPERSHARE</Link>
                        </Typography>
                        <Box mr={5}>
                            {this.toggleToolbar()}
                        </Box>
                    </Toolbar>
                </AppBar>
            </React.Fragment>
        );
    }
}
