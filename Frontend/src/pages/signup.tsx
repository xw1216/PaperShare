import React, {Component} from "react";
import {
    Avatar,
    Box,
    Button,
    Checkbox,
    Container,
    createTheme,
    FormControlLabel,
    Grid,
    Link,
    TextField,
    ThemeProvider,
    Typography
} from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Copyright from "../components/global/copyright";
import Header from "../components/global/header";
import {Link as RouterLink, Navigate} from "react-router-dom";
import AuthLib, {SignUpVo} from "../lib/auth";
import {DataUtil} from "../lib/axios";

const theme = createTheme();

export default class Signup extends Component<any, any> {
    private emailField: any;
    private countDown: any;

    constructor(props: any) {
        super(props);
        this.state = {buttonDisable: false, buttonText: "发送", loginSuccess: false};
        this.handleSubmit.bind(this);
        this.handleVerify.bind(this);
        this.verifyButtonLoop.bind(this);
    }

    checkVoNull = (vo: SignUpVo): boolean => {
        return !!(vo.email && vo.name && vo.pass && vo.checkCode);
    }

    handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        let vo: SignUpVo = {
            email: data.get('email'),
            name: data.get('name'),
            pass: data.get('pass'),
            checkCode: data.get("check-code")
        }
        console.log(vo);

        if (!(this.checkVoNull(vo))) {
            this.props.enqueue("有必填项为空", {
                variant: 'error',
            });
            return;
        }

        AuthLib.signup(vo)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                AuthLib.setHttpUser(res.data.jwt, res.data.id, res.data.name, res.data.role);
                this.setState({loginSuccess: true})
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                console.log(res);
                this.props.enqueue(res.msg, {
                    variant: 'error',
                });
            })
    };

    verifyButtonLoop = () => {
        this.countDown = 59;
        this.setState({buttonDisable: true});
        this.setState({buttonText: 60 + "s"});

        let intervalId = setInterval(() => {
            this.setState({buttonText: this.countDown + "s"});
            this.countDown--;
        }, 1000);
        let timeoutId = setTimeout(() => {
            this.setState({buttonDisable: false});
            this.setState({buttonText: "发送"});
            clearInterval(intervalId);
            clearTimeout(timeoutId);
        }, 60000);
    }

    handleVerify = (event: any) => {
        const reg = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        let email = this.emailField.value;
        if (!(reg.test(email))) {
            this.props.enqueue("邮箱格式错误", {
                variant: 'error',
            });
        } else {

            this.verifyButtonLoop();
            this.props.enqueue("请求中，请稍候", {
                variant: 'info',
            });

            AuthLib.sendCheckCode(email)
                .then((response: any) => {
                    let data = DataUtil.getSuccessData(response);
                    this.props.enqueue(data.msg, {
                        variant: 'success'
                    });
                })
                .catch((response: any) => {
                    let data = DataUtil.getErrorData(response);
                    this.props.enqueue(data.msg, {
                        variant: 'error',
                    });
                })
        }
    }

    render() {
        return (
            <ThemeProvider theme={theme}>
                <Header></Header>
                {this.state.loginSuccess &&
                    <Navigate to={"/"} replace={true}/>
                }
                <Container component="main" maxWidth="xs">
                    <Box
                        sx={{
                            marginTop: 8,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Avatar sx={{m: 5, bgcolor: 'primary.main', width: 100, height: 100}}>
                            <AccountCircleIcon sx={{fontSize: 100}}/>
                        </Avatar>
                        <Typography component="h2" variant="h5">
                            注册 PaperShare 账号
                        </Typography>
                        <Box component="form" noValidate onSubmit={this.handleSubmit} sx={{mt: 3}}>
                            <Grid container spacing={2}>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="name"
                                        label="昵称"
                                        name="name"
                                        autoFocus
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="email"
                                        label="邮件地址"
                                        name="email"
                                        autoComplete="email"
                                        inputRef={(elem) => {
                                            this.emailField = elem;
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        name="pass"
                                        label="密码"
                                        type="password"
                                        id="pass"
                                        autoComplete="password"
                                    />
                                </Grid>
                                <Grid item xs={8}>
                                    <TextField
                                        required
                                        fullWidth
                                        name="check-code"
                                        label="验证码"
                                        type={"name"}
                                        id="check-code"
                                    />
                                </Grid>

                                <Grid item xs={12} sm={4}>
                                    <Button
                                        variant={"contained"}
                                        sx={{width: "100%", height: "100%"}}
                                        disabled={this.state.buttonDisable}
                                        color={"info"}
                                        onClick={this.handleVerify}
                                    >
                                        {this.state.buttonText}
                                    </Button>
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControlLabel
                                        control={<Checkbox value="allowExtraEmails" color="primary"/>}
                                        label="允许 PaperShare 了解您的研究方向"
                                    />
                                </Grid>
                            </Grid>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{mt: 3, mb: 2}}
                            >
                                注册
                            </Button>
                            <Grid container justifyContent="flex-end">
                                <Grid item>
                                    <Link variant="body2" component={RouterLink} to="/login">
                                        已经有账号? 前往登录
                                    </Link>
                                </Grid>
                            </Grid>
                        </Box>
                    </Box>
                    <Copyright sx={{mt: 5}}/>
                </Container>
            </ThemeProvider>
        );
    };
}