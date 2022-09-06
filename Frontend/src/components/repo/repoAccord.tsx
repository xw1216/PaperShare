import {Accordion, AccordionDetails, AccordionSummary, Box, Button, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import React from "react";
import {Link as RouterLink} from "react-router-dom";
import {IRepo} from "../../lib/repo";
import {useSnackbar} from "notistack";

interface IPropsRepoAccord {
    repo: IRepo,
    type: "explore" | "stars",
    func: any

}

export default function RepoAccord(props: IPropsRepoAccord) {

    const {enqueueSnackbar, closeSnackbar} = useSnackbar();


    function toggleBtn(type: string) {
        if (type === "explore") {
            return (
                <Button variant={"contained"} onClick={props.func}>关注</Button>
            );
        } else if (type === "stars") {
            return (
                <Button variant={"contained"} onClick={props.func}>取关</Button>
            );
        }
    }

    return (
        <Accordion>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon/>}
                aria-controls="panel1a-content"
                id="panel1a-header"
            >
                <Typography ml={1} my={1.5} variant={"h5"}>
                    {props.repo.name}
                </Typography>
            </AccordionSummary>
            <AccordionDetails>
                <Typography ml={1} variant={"body1"} mb={4} textAlign={"start"}>
                    {props.repo.cont}
                </Typography>
                <Box ml={1} my={2} sx={{
                    display: "flex",
                    alignItems: "start"
                }}>
                    {toggleBtn(props.type)}
                    <Box pl={2}>
                        <Button variant={"outlined"} component={RouterLink} to={"/repo/" + props.repo.id}>详情</Button>
                    </Box>

                </Box>
            </AccordionDetails>
        </Accordion>
    );
}