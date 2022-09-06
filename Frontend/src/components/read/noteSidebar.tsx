import {
    Box,
    Card,
    CardActions,
    CardContent,
    Divider,
    Drawer,
    List,
    ListItem,
    Stack,
    styled,
    Typography
} from "@mui/material";
import Button from "@mui/material/Button";
import CachedIcon from '@mui/icons-material/Cached';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import EditIcon from '@mui/icons-material/Edit';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import React, {useEffect, useState} from "react";
import NoteEditDialog, {NoteVo} from "./noteEditDialog";
import ConfirmDialog from "../global/confirmDialog";
import NoteLib, {NoteEditVo, NoteNewVo} from "../../lib/note";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";

const drawerWidth = 360;

const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));

const paperNotes = [
    {
        id: "1",
        title: "Note Title",
        cont: "This is Note Content. This is Note Content. This is Note Content. This is Note Content.",
        updateTime: "2022-06-18 15:10:12",
    },
    {
        id: "2",
        title: "Note Title",
        cont: "This is Note Content. This is Note Content. This is Note Content. This is Note Content.",
        updateTime: "2022-06-18 15:10:12",
    },
    {
        id: "3",
        title: "Note Title",
        cont: "This is Note Content. This is Note Content. This is Note Content. This is Note Content.",
        updateTime: "2022-06-18 15:10:12",
    },
];

export default function NoteSideBar(props: any) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    let [dialogOpen, setDialogOpen] = useState<boolean>(false);
    let [dialogType, setDialogType] = useState<"new" | "edit">("new");
    let [noteId, setNoteId] = useState<string>("");
    let [noteTitle, setNoteTitle] = useState<string>("");
    let [noteCont, setNoteCont] = useState<string>("");

    let [deleteOpen, setDeleteOpen] = useState<boolean>(false);
    let [deleteId, setDeleteId] = useState<string>("");

    let [noteList, setNoteList] = useState<any>(null);


    let openDialog = (type: "new" | "edit", id: string = "", title: string = "", cont: string = "") => {
        setDialogType(type);
        setNoteId(id);
        setNoteTitle(title);
        setNoteCont(cont);
        setDialogOpen(true);
    }

    let openDelete = (id: string = "") => {
        setDeleteOpen(true);
        setDeleteId(id);
    }

    let closeDialog = () => {
        setDialogOpen(false);
        setNoteId("");
        setNoteTitle("");
        setNoteCont("");
    }

    let closeDelete = () => {
        setDeleteOpen(false);
        setDeleteId("");
    }

    let affirmDialog = (inputVo: NoteVo) => {
        closeDialog();
        if (dialogType === "new") {
            let vo: NoteNewVo = {
                title: inputVo.title,
                cont: inputVo.cont,
                page: props.page,
                repoId: props.repoId,
                paperId: props.paperId,
            }

            NoteLib.addNote(vo)
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "success",
                    });
                    requestRefresh();
                    console.log("notes added");
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error",
                    })
                });
        } else {
            let vo: NoteEditVo = {
                id: noteId,
                title: inputVo.title,
                cont: inputVo.cont,
            }

            NoteLib.editNote(vo)
                .then((response: any) => {
                    let res = DataUtil.getSuccessData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "success",
                    });
                    requestRefresh();
                    console.log("notes edited");
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error",
                    })
                });
        }
    }

    let deleteNote = () => {
        console.log("delete notes")
        NoteLib.deleteNote(deleteId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success",
                });
                requestRefresh();
            }).catch((response: any) => {
            console.log(response);
            let res = DataUtil.getErrorData(response);
            enqueueSnackbar(res.msg, {
                variant: "error",
            })
        });
        closeDelete();
    }

    let requestRefresh = () => {
        doRefresh();
    }

    let displayNoteCards = () => {
        if (noteList === null || noteList === undefined || noteList.length === 0) {
            return (
                <Typography mt={4} variant={"body1"} textAlign={"center"}>
                    暂无笔记
                </Typography>
            );
        } else {
            return (
                noteList.map((note: any) => (
                    <ListItem key={note.id} disablePadding>
                        <Box my={1.5} mx={1} flexGrow={1}>
                            <Card elevation={2}>
                                <CardContent>
                                    <Typography variant="h5" component="div">
                                        {note.title}
                                    </Typography>
                                    <Typography variant={"body2"} sx={{mb: 2}}
                                                color="text.secondary">
                                        {note.updateTime}
                                    </Typography>
                                    <Typography flexWrap={"wrap"} variant="body1">
                                        {note.cont}
                                    </Typography>
                                </CardContent>
                                <CardActions>
                                    <Button size={"small"} onClick={(e) => {
                                        openDialog("edit", note.id, note.title, note.cont);
                                    }}>
                                        <EditIcon/>
                                    </Button>
                                    <Button size={"small"} onClick={() => {
                                        openDelete(note.id);
                                    }}>
                                        <DeleteOutlineIcon/>
                                    </Button>
                                </CardActions>
                            </Card>
                        </Box>

                    </ListItem>
                ))
            );
        }
    }

    let doRefresh = () => {
        NoteLib.getSpecifiedNote({
            repoId: props.repoId,
            paperId: props.paperId,
            page: props.page,
        }).then((response: any) => {
            console.log("notes refreshed");
            console.log(response);
            let res = DataUtil.getSuccessData(response);
            setNoteList(res.data);
        }).catch((response: any) => {
            console.log(response);
            let res = DataUtil.getErrorData(response);
            enqueueSnackbar(res.msg, {
                variant: "error",
            })
        });
    }

    useEffect(() => {
        doRefresh();
    }, [enqueueSnackbar, props.page, props.paperId, props.repoId]);

    return (
        <React.Fragment>
            <Drawer
                sx={{
                    width: drawerWidth,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: drawerWidth,
                        boxSizing: 'border-box',
                    }
                }}
                variant="persistent"
                anchor="left"
                open={true}
            >
                <DrawerHeader></DrawerHeader>
                <Divider/>
                <Stack
                    my={2}
                    direction={"column"}
                    flexDirection={"row"}
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                    }}
                >
                    <Button
                        size={"large"}
                        startIcon={<AddCircleOutlineIcon/>}
                        onClick={(e) => {
                            openDialog("new");
                        }}
                    >
                        新增笔记
                    </Button>
                    <Button
                        size={"large"}
                        startIcon={<CachedIcon/>}
                        onClick={requestRefresh}
                    >
                        刷新
                    </Button>
                </Stack>
                <Divider/>
                <Stack
                    flexDirection={"column"}
                    sx={{
                        maxWidth: "100%"
                    }}
                >
                    <List>
                        {displayNoteCards()}
                    </List>
                </Stack>
            </Drawer>
            <NoteEditDialog
                type={dialogType}
                open={dialogOpen}
                dialogClose={closeDialog}
                dialogAffirm={affirmDialog}
                title={noteTitle}
                cont={noteCont}
                id={noteId}
            />
            <ConfirmDialog
                open={deleteOpen}
                dialogClose={closeDelete}
                dialogAffirm={deleteNote}
            />
        </React.Fragment>

    );
}