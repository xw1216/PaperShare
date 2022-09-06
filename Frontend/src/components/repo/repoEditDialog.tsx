import {
    Box,
    Button,
    Dialog,
    DialogContent,
    DialogTitle,
    FormControlLabel,
    Grid,
    Switch,
    TextField
} from "@mui/material";
import React, {useState} from "react";
import RepoLib, {IRepo} from "../../lib/repo";
import {useSnackbar} from "notistack";
import {DataUtil} from "../../lib/axios";

interface IRepoEditDialog {
    status: {
        open: boolean,
        setClose: any,
        refresh: any,
    },
    type: string,
    id?: string,
    defaultValue?: {
        name: string,
        cont: string,
        visible: boolean
    }
}

export default function RepoEditDialog(props: IRepoEditDialog) {
    let open = props.status.open;
    let setClose = props.status.setClose;
    let type = props.type;

    let initialTitle, initialCont, initialVisible;

    if (type === "new") {
        initialTitle = "";
        initialCont = "";
        initialVisible = true;
    } else {
        initialTitle = props.defaultValue?.name;
        initialCont = props.defaultValue?.cont;
        initialVisible = props.defaultValue?.visible;
    }

    let [titleDefault, setTitleDefault] = useState<string | undefined>(initialTitle);
    let [contDefault, setContDefault] = useState<string | undefined>(initialCont);
    let [visibleDefault, setVisibleDefault] = useState<boolean | undefined>(initialVisible);

    const {enqueueSnackbar, closeSnackbar} = useSnackbar();


    let checkFormNull = (vo: IRepo): boolean => {
        if (vo.name && vo.cont) {
            return false;
        } else {
            return true;
        }
    }

    let convertVisibleInput = (visible: any) => {
        if (visible === "start") {
            return true;
        }

        if (visible === null) {
            return false;
        }

        return false;
    }

    let handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let vo: IRepo = {
            name: data.get('repo-name'),
            visible: convertVisibleInput(data.get('repo-visible')),
            cont: data.get('repo-cont'),
        }

        if (checkFormNull(vo)) {
            enqueueSnackbar("某项输入为空", {
                variant: "error"
            });
            return;
        }

        if (type === "new") {
            RepoLib.addRepo(vo)
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "success"
                    });
                    props.status.refresh();
                    setClose();
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error"
                    })
                });

        } else {
            vo.id = props.id;
            RepoLib.editRepo(vo)
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "success"
                    });
                    props.status.refresh();
                    setClose();
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error"
                    })
                });
        }
    }

    return (
        <Dialog fullWidth open={open}>
            <DialogTitle variant={"h6"}>更改仓库</DialogTitle>
            <DialogContent>
                <Box component="form" onSubmit={handleSubmit}>
                    <Box ml={2} my={2}>
                        <TextField
                            defaultValue={titleDefault}
                            name={"repo-name"}
                            fullWidth
                            label="仓库名"
                            variant="standard"
                        />
                    </Box>


                    <Box ml={2} my={2}>
                        <TextField
                            defaultValue={contDefault}
                            name={"repo-cont"}
                            fullWidth
                            label="仓库介绍"
                            variant={"standard"}
                            multiline
                            maxRows={3}
                        />
                    </Box>

                    <Box mt={4}>
                        <FormControlLabel
                            value="start"
                            control={
                                <Switch
                                    defaultChecked={visibleDefault}
                                    color="primary"
                                    name={"repo-visible"}
                                />}
                            label="可见性"
                            labelPlacement="start"
                        />
                    </Box>
                    <Grid container flexDirection={"row"} mt={3} justifyContent={"end"}>
                        <Button onClick={setClose}>取消</Button>
                        <Box mx={2}>
                            <Button variant={"contained"} type={"submit"}>确定</Button>
                        </Box>

                    </Grid>
                </Box>

            </DialogContent>
        </Dialog>
    );
}