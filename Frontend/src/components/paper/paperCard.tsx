import {Box, Card, CardActions, CardContent, Chip, Grid, Typography} from "@mui/material";
import AddToRepoBtn from "../repo/addToRepoBtn";
import React, {useEffect, useState} from "react";
import PaperLib from "../../lib/paper";
import {useSnackbar} from "notistack";
import {DataUtil} from "../../lib/axios";

const labels = ["label A", "label B"];

export default function PaperCard(props: any) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    let [paperInfo, setPaperInfo] = useState<any>(null);

    useEffect(() => {
        PaperLib.getPaperInfo(props.paperId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                setPaperInfo(res.data);
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                })
            });
    }, [enqueueSnackbar, props.paperId]);

    let displayCard = () => {
        if (paperInfo === null || paperInfo === undefined) {
            return (
                <Typography variant={"h6"} textAlign={"center"}>
                    加载中···
                </Typography>
            );
        } else {
            return (
                <Card>
                    <CardContent>
                        <Typography variant={"h4"} ml={3} my={2} textAlign={"start"}>
                            {paperInfo.title}
                        </Typography>
                        <Box ml={4} mt={4}>
                            <Typography variant={"h6"} mb={2} textAlign={"start"}>
                                {paperInfo.author}
                            </Typography>
                            <Grid container
                                  flexDirection={"row"}
                                  maxWidth={"lg"}
                                  sx={{
                                      alignItems: 'center',
                                      justifyContent: 'start'
                                  }}
                                  mb={2}
                            >
                                {paperInfo.areas.map((area: any) => (
                                    <Grid mr={1} my={1} item key={area.id}>
                                        <Chip
                                            size={"small"}
                                            label={area.name}
                                            variant={"outlined"}
                                        />
                                    </Grid>
                                ))}
                            </Grid>
                            <Typography textAlign={"start"} variant={"subtitle2"}>
                                {paperInfo.journal} · {paperInfo.year} · {paperInfo.doi}
                            </Typography>
                            <Typography my={4} textAlign={"start"} variant={"body1"}>
                                {paperInfo.brief}
                            </Typography>
                        </Box>
                    </CardContent>
                    <CardActions>
                        <Box ml={4} mb={4} alignItems={"start"}>
                            <AddToRepoBtn paperId={props.paperId}/>
                        </Box>
                    </CardActions>
                </Card>
            );
        }
    }

    return (
        <Box>
            {displayCard()}
        </Box>
    );
}