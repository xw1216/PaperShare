import {Accordion, AccordionDetails, AccordionSummary, Button, Chip, Grid, Stack, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {Link as RouterLink} from "react-router-dom";
import React, {useState} from "react";
import AddToRepoBtn from "../repo/addToRepoBtn";
import {useSnackbar} from "notistack";
import ConfirmDialog from "../global/confirmDialog";
import PaperLib from "../../lib/paper";
import {DataUtil} from "../../lib/axios";

export default function PaperAccord(props: any) {
    let [dialogConfirmOpen, setDialogConfirmOpen] = useState<boolean>(false);
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    let openConfirmDialog = () => {
        setDialogConfirmOpen(true);
    }

    let closeConfirmDialog = () => {
        setDialogConfirmOpen(false);
    }

    let deleteConfirm = () => {
        PaperLib.removePaperFromRepo(props.paper.id, props.repoId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success",
                });
                props.refresh();
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                })
            });
        closeConfirmDialog();
    }

    let buttonToggle = (type: string) => {
        if (type === "repo") {
            return (
                <Stack direction={"row"} ml={1} mt={4} mb={2} alignSelf={"center"} spacing={2}>
                    <Button
                        size="small"
                        variant={"contained"}
                        component={RouterLink} to={"/read/" + props.repoId + "/" + props.paper.id}
                    >
                        阅读
                    </Button>
                    <Button
                        size="small"
                        variant={"contained"}
                        component={RouterLink} to={"/paper/" + props.paper.id}
                    >
                        详情
                    </Button>
                    <Button variant={"outlined"}
                            size="small"
                            onClick={openConfirmDialog}
                    >
                        移出仓库
                    </Button>
                </Stack>
            );
        } else if (type === "search") {
            return (
                <Stack direction={"row"} ml={1} mt={4} mb={2} alignSelf={"center"} spacing={2}>
                    <Button
                        size="small"
                        variant={"contained"}
                        component={RouterLink} to={"/paper/" + props.paper.id}
                    >
                        详情
                    </Button>
                    <AddToRepoBtn
                        paperId={props.paper.id}
                    />
                </Stack>
            );
        }
    }

    let displayLabels = () => {
        if (props.paper.areas !== null &&
            props.paper.areas !== undefined &&
            props.paper.areas.length !== 0) {
            return props.paper.areas.map((area: any) => (
                <Grid mx={1} my={1} item key={area.id}>
                    <Chip size={"small"} label={area.name} variant={"outlined"}></Chip>
                </Grid>
            ))
        }
    }


    return (
        <React.Fragment>
            <Accordion>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1a-content"
                    id="panel1a-header"
                >
                    <Typography
                        ml={1}
                        my={1.5}
                        variant={"h5"}
                    >
                        {props.paper.title}
                    </Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography ml={1} variant={"body1"} mb={2} textAlign={"start"}>
                        {props.paper.author}
                    </Typography>
                    <Grid container
                          flexDirection={"row"}
                          maxWidth={"lg"}
                          sx={{
                              alignItems: 'center',
                              justifyContent: 'start'
                          }}
                          my={2}
                    >
                        {displayLabels()}
                    </Grid>
                    <Typography ml={1} textAlign={"start"}>
                        {props.paper.journal} · {props.paper.year} · {props.paper.doi}
                    </Typography>
                    {buttonToggle(props.type)}
                </AccordionDetails>
            </Accordion>
            <ConfirmDialog
                open={dialogConfirmOpen}
                dialogClose={closeConfirmDialog}
                dialogAffirm={deleteConfirm}/>
        </React.Fragment>

    );
}