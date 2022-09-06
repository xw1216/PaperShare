import {Autocomplete, Box, createTheme, Divider, Grid, styled, TextField} from "@mui/material";
import ColorAvatar from "./colorAvatar";
import {AccountCircle} from "@mui/icons-material";
import EmailIcon from "@mui/icons-material/Email";
import TextSnippetIcon from "@mui/icons-material/TextSnippet";
import BookmarkIcon from "@mui/icons-material/Bookmark";
import {IArea} from "../../lib/area";
import Button from "@mui/material/Button";
import {Link as RouterLink} from "react-router-dom";
import React, {useState} from "react";
import UserLib, {IUser} from "../../lib/user";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";

const theme = createTheme();

const InputBox = styled(Box)(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: "center"
}));

export default function ProfileForm(props: any) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    let [selectArea, setSelectedArea] = useState<any>([]);

    console.log(props);

    let handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        let vo: IUser = {
            id: props.userInfo.id,
            email: props.userInfo.email,
            name: data.get('name'),
            motto: data.get('motto'),
            areas: selectArea,
            role: props.userInfo.role
        };

        if (vo.areas === "") {
            vo.areas = [];
        }

        console.log(vo);

        UserLib.editUserInfo(vo)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success"
                });
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error"
                });
            })
    }

    let updateSelectedArea = (event: any, value: any) => {
        setSelectedArea(value);
    }

    let testLog = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        let vo: IUser = {
            id: props.userInfo.id,
            email: props.userInfo.email,
            name: data.get('name'),
            motto: data.get('motto'),
            areas: selectArea,
            role: props.userInfo.role
        };

        console.log(props);
        console.log(vo);
    }

    return (
        <Grid container>
            <Grid item md={2}>
                <ColorAvatar username={props.userInfo.name} size={80}/>
            </Grid>
            <Grid item md={10}>
                <Box component="form" onSubmit={handleSubmit}>
                    <InputBox mx={4} my={4}>
                        <AccountCircle fontSize={"large"}
                                       sx={{color: 'action.active', mr: 1, my: 0.5}}/>
                        <TextField
                            fullWidth
                            key={"name"}
                            defaultValue={props.userInfo.name}
                            label="昵称"
                            name={"name"}
                            variant="outlined"
                        />
                    </InputBox>
                    <InputBox mx={4} my={4}>
                        <EmailIcon fontSize={"large"}
                                   sx={{color: 'action.active', mr: 1, my: 0.5}}/>
                        <TextField
                            fullWidth
                            key={"email"}
                            defaultValue={props.userInfo.email}
                            label={"邮箱"}
                            name={"email"}
                            variant={"outlined"}
                            disabled={true}
                        />
                    </InputBox>
                    <InputBox mx={4} my={4}>
                        <TextSnippetIcon fontSize={"large"}
                                         sx={{color: 'action.active', mr: 1, my: 0.5}}/>
                        <TextField
                            fullWidth
                            key={"motto"}
                            defaultValue={props.userInfo.motto}
                            variant={"outlined"}
                            label={"个性签名"}
                            name={"motto"}
                        />
                    </InputBox>

                    <InputBox mx={4} my={4}>
                        <BookmarkIcon fontSize={"large"}
                                      sx={{color: 'action.active', mr: 1, my: 0.5}}/>
                        <Autocomplete
                            fullWidth
                            multiple
                            key={"area"}
                            options={props.areaList}
                            getOptionLabel={(option: IArea) => option.name}
                            defaultValue={props.userInfo.areas}
                            filterSelectedOptions
                            onChange={updateSelectedArea}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="关注领域"
                                    name={"area"}
                                />
                            )}
                        />
                    </InputBox>

                    <Grid container
                          sx={{
                              display: 'flex',
                              alignItems: 'center',
                              justifyContent: "center"
                          }}
                    >
                        <Box pr={4}>
                            <Button
                                variant="outlined"
                                color={"error"}
                                onClick={props.logout}
                            >
                                注销登录
                            </Button>
                        </Box>
                        <Divider orientation="vertical" flexItem></Divider>
                        <Box pl={4}>
                            <Button
                                variant="outlined"
                                component={RouterLink}
                                to="/"
                            >
                                返回首页
                            </Button>
                        </Box>
                        <Box pl={4}>
                            <Button
                                variant="contained"
                                type={"submit"}
                            >
                                确认提交
                            </Button>
                        </Box>
                    </Grid>
                </Box>

            </Grid>

        </Grid>
    );
}