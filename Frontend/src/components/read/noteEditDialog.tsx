import {Box, Button, Dialog, DialogContent, DialogTitle, Grid, TextField} from "@mui/material";
import React from "react";

interface INoteEditDialog {
    type: "new" | "edit",
    open: boolean,
    dialogClose: any,
    dialogAffirm: any,
    id: string,
    title?: string,
    cont?: string
}

export interface NoteVo {
    title: any,
    cont: any,
}

export default function NoteEditDialog(props: INoteEditDialog) {

    let handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        let vo: NoteVo = {
            title: data.get('title'),
            cont: data.get('cont'),
        }
        console.log(vo);
        props.dialogAffirm(vo);
    }

    let displayDefaultForm = () => {
        if (props.type === "new") {
            return (
                <Box>
                    <Box pb={2}>
                        <TextField
                            name={"title"}
                            label="笔记标题"
                            variant={"outlined"}
                            fullWidth
                            required
                        />
                    </Box>
                    <Box py={2}>
                        <TextField
                            name={"cont"}
                            fullWidth
                            label="笔记内容"
                            variant={"outlined"}
                            required
                            multiline
                            minRows={3}
                            maxRows={5}
                        />
                    </Box>
                </Box>
            );
        } else {
            return (
                <Box>
                    <Box pb={2}>
                        <TextField
                            name={"title"}
                            label="笔记标题"
                            variant={"outlined"}
                            fullWidth
                            required
                            defaultValue={props.title}
                        />
                    </Box>
                    <Box py={2}>
                        <TextField
                            name={"cont"}
                            fullWidth
                            label="笔记内容"
                            variant={"outlined"}
                            required
                            multiline
                            minRows={3}
                            maxRows={5}
                            defaultValue={props.cont}
                        />
                    </Box>
                </Box>
            );
        }
    }

    return (
        <Dialog
            fullWidth
            open={props.open}
            onClose={props.dialogClose}
        >
            <DialogTitle variant={"h6"} mt={2}>
                更改笔记
            </DialogTitle>
            <DialogContent>
                <Box component="form" onSubmit={handleSubmit} sx={{mt: 3}}>
                    {displayDefaultForm()}
                    <Grid container flexDirection={"row"} my={2} justifyContent={"end"}>
                        <Button
                            onClick={props.dialogClose}
                            autoFocus
                            variant={"outlined"}
                        >
                            取消
                        </Button>
                        <Box px={2}>
                            <Button
                                type="submit"
                                variant={"contained"}
                            >
                                确认
                            </Button>
                        </Box>

                    </Grid>

                </Box>
            </DialogContent>
        </Dialog>
    );
}