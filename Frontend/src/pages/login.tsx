import React, {Component} from "react";
import {Avatar, Box, Button, createTheme, Grid, Link, Paper, TextField, ThemeProvider, Typography} from "@mui/material";

import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Copyright from "../components/global/copyright";
import {Link as RouterLink, Navigate} from "react-router-dom";
import AuthLib, {SignInVo} from "../lib/auth";
import {DataUtil} from "../lib/axios";


const theme = createTheme();

export default class Login extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.handleSubmit.bind(this);
        this.state = {loginSuccess: false, adminSuccess: false};
    }

    checkSignInVoNull = (vo: SignInVo) => {
        return !!(vo.email && vo.pass);
    }

    handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let vo: SignInVo = {
            email: data.get('email'),
            pass: data.get('password')
        }

        console.log(vo);

        if (!(this.checkSignInVoNull(vo))) {
            this.props.enqueue("有必填项为空", {
                variant: 'error',
            });
        }

        AuthLib.signIn(vo)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                AuthLib.setHttpUser(res.data.jwt, res.data.id, res.data.name, res.data.role);
                if (res.data.role === "ADMIN") {
                    this.setState({adminSuccess: true});
                } else {
                    this.setState({loginSuccess: true});
                }
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue("登录失败", {
                    variant: 'error',
                });
            })
    };


    render() {
        return (
            <ThemeProvider theme={theme}>

                <Grid container sx={{height: '100vh'}}>
                    {this.state.loginSuccess &&
                        <Navigate to={"/"} replace={true}/>
                    }
                    {this.state.adminSuccess &&
                        <Navigate to={"/admin"} replace={true}/>
                    }
                    <Grid
                        item
                        xs={false}
                        sm={4}
                        md={7}
                        sx={{
                            backgroundImage: 'url(https://www.talklee.com/api/bing?rand=true)',
                            backgroundRepeat: 'no-repeat',
                            backgroundColor: (t) =>
                                t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                        }}
                    />
                    <Grid item xs={12} sm={8} md={5} component={Paper} elevation={1} square>
                        <Box
                            sx={{
                                my: 12,
                                mx: 4,
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                            }}
                        >
                            <Avatar sx={{m: 5, bgcolor: 'primary.main', width: 100, height: 100}}>
                                <AccountCircleIcon sx={{fontSize: 100}}/>
                            </Avatar>
                            <Typography component="h2" variant="h5">
                                欢迎登录 PaperShare
                            </Typography>
                            <Box component="form" noValidate onSubmit={this.handleSubmit} sx={{mt: 1}}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="email"
                                    label="邮件地址"
                                    name="email"
                                    autoComplete="email"
                                    autoFocus

                                />
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    name="password"
                                    label="密码"
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"
                                />
                                <Button
                                    type="submit"
                                    variant="contained"
                                    fullWidth
                                    sx={{my: 5}}
                                >
                                    登录
                                </Button>
                                <Grid container alignItems={"center"}>
                                    <Grid item xs={12}>
                                        <Link variant="body2" component={RouterLink} to="/signup">
                                            没有账户? 请先注册
                                        </Link>
                                    </Grid>
                                </Grid>
                                <Copyright sx={{mt: 5}}/>
                            </Box>
                        </Box>
                    </Grid>
                </Grid>
            </ThemeProvider>
        );
    };
}