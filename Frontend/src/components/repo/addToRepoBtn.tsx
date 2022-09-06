import React, {useEffect, useState} from "react";
import {
    Button,
    Checkbox,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Typography
} from "@mui/material";
import RepoLib from "../../lib/repo";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";
import PaperLib from "../../lib/paper";

export default function AddToRepoBtn(props: any) {
    const [open, setOpen] = useState(false);
    const [repos, setRepos] = useState<any>(null);
    const [checked, setChecked] = useState<Array<number>>([]);

    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    const handleItemToggle = (index: number) => () => {
        const currentIndex = checked.indexOf(index);
        const newChecked = [...checked];

        if (currentIndex === -1) {
            newChecked.push(index);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        setChecked(newChecked);
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleConfirm = () => {
        setOpen(false);
        let repoIdList = [];
        for (let value in checked) {
            let repo: any = repos.at(value);
            console.log(repo, repos, value, checked);
            repoIdList.push(repo.id);
        }

        PaperLib.addPaperToRepo(props.paperId, repoIdList)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success"
                })
                handleClose();
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                })
            })
    }

    const descriptionElementRef = React.useRef<HTMLElement>(null);
    useEffect(() => {
        if (open) {
            const {current: descriptionElement} = descriptionElementRef;
            if (descriptionElement !== null) {
                descriptionElement.focus();
            }
        }
    }, [open]);

    useEffect(() => {
        RepoLib.getValidRepos()
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                setRepos(res.data);
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                })
            })
    }, [enqueueSnackbar])

    let displayRepos = () => {
        if (repos === null || repos === undefined) {
            return (
                <Typography variant={"body2"} textAlign={"center"}>
                    暂无仓库
                </Typography>
            );
        } else {
            return repos.map((repo: any, index: number) => {
                return (
                    <ListItem
                        key={repo.id}
                        disablePadding
                    >
                        <ListItemButton onClick={handleItemToggle(index)} dense>
                            <ListItemIcon>
                                <Checkbox
                                    edge="start"
                                    checked={checked.indexOf(index) !== -1}
                                    tabIndex={-1}
                                    disableRipple
                                />
                            </ListItemIcon>
                            <ListItemText
                                id={repo.id}
                                primary={repo.name}
                                primaryTypographyProps={{
                                    color: "rgba(0,0,0,1)"
                                }}
                            />
                        </ListItemButton>
                    </ListItem>
                );
            })
        }
    }

    return (
        <React.Fragment>
            <Button
                variant={"outlined"}
                size="small"
                onClick={handleClickOpen}
            >
                加入仓库
            </Button>
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>添加论文到仓库...</DialogTitle>
                <DialogContent dividers={true}>
                    <List sx={{width: '100%', minWidth: 240, maxWidth: 480, bgcolor: 'background.paper'}}>
                        {displayRepos()}
                    </List>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>取消</Button>
                    <Button onClick={handleConfirm}>添加</Button>
                </DialogActions>
            </Dialog>
        </React.Fragment>
    );
}