import {Box, Button, Divider, Grid, TextField, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import ColorAvatar from "../user/colorAvatar";
import AuthLib from "../../lib/auth";
import CommentLib from "../../lib/comment";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";

export default function Comments(props: any) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    let [commentList, setCommentList] = useState<any>(null);
    let [needRefresh, setNeedRefresh] = useState<boolean>(true);

    let getUserName = () => {
        let name = AuthLib.getUserName();
        if (name === null || name === undefined) {
            return "无";
        }
        return name;
    }

    let requestRefresh = () => {
        CommentLib.getPaperComments(props.paperId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                setCommentList(res.data);
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error"
                })
            });
    }

    useEffect(() => {
        if (needRefresh) {
            requestRefresh();
            setNeedRefresh(false);
        }
    }, [enqueueSnackbar, needRefresh, props.paperId])

    let handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let vo = {
            cont: data.get("comment"),
            paperId: props.paperId,
        };

        CommentLib.addComment(vo)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success",
                });
                requestRefresh();
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                });
            });

    }

    let displayComments = () => {
        if (commentList === null || commentList === undefined) {
            return (
                <Typography variant={"h6"} textAlign={"center"}>
                    暂无评论
                </Typography>
            );
        } else {
            return commentList.map((comment: any) => {
                return (
                    <Box key={comment.id}>
                        <Divider></Divider>
                        <Grid container my={4}>
                            <Grid item md={2}>
                                <ColorAvatar username={comment.userName}></ColorAvatar>
                                <Typography>
                                    {comment.userName}
                                </Typography>
                            </Grid>
                            <Grid item md={10} alignSelf={"center"} alignItems={"start"}>
                                <Typography px={2} textAlign={"start"}>
                                    {comment.cont}
                                </Typography>
                            </Grid>
                        </Grid>
                    </Box>
                );
            })
        }
    }

    return (
        <Box>
            <Box my={4}>
                <Grid container flexDirection={"row"}>
                    <Box flexGrow={1}>
                        <ColorAvatar username={getUserName()}></ColorAvatar>
                        <Typography>
                            {getUserName()}
                        </Typography>
                    </Box>
                    <Box component="form" onSubmit={handleSubmit} sx={{mt: 1}} flexGrow={11}>
                        <Grid container flexDirection={"row"}>
                            <Box px={4} flexGrow={10}>
                                <TextField
                                    fullWidth
                                    name={"comment"}
                                    label="发表新评论"
                                    multiline
                                    minRows={3}
                                    maxRows={10}
                                    required
                                    placeholder={"谈谈你的看法吧~"}
                                />
                            </Box>

                            <Box alignSelf={"center"} flexGrow={1}>
                                <Button
                                    size={"small"}
                                    variant={"contained"}
                                    type={"submit"}
                                >
                                    发布
                                </Button>
                            </Box>
                        </Grid>
                    </Box>
                </Grid>
            </Box>
            {displayComments()}
        </Box>
    );
}