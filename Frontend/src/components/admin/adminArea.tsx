import {Box, Button, Grid, TextField, Typography} from "@mui/material";
import AreaTable from "./areaTable";
import React, {useState} from "react";
import AreaLib from "../../lib/area";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";

export default function AdminArea(props: any) {

    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const [needRefresh, setNeedRefresh] = useState<boolean>(true);

    let sendAddArea = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let name = data.get('area-name');

        AreaLib.addArea(name)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                setNeedRefresh(true);
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

    let completeRefresh = () => {
        setNeedRefresh(false);
    }

    let requestRefresh = () => {
        setNeedRefresh(true);
    }

    return (
        <Grid container flexDirection={"column"}>
            <Typography variant={"h3"} py={4} textAlign={"start"}>
                管理研究领域
            </Typography>
            <Box>
                <Typography variant={"h5"} my={4} textAlign={"start"}>
                    添加领域
                </Typography>
                <Grid
                    container
                    my={4}
                    flexDirection={"row"}
                    sx={{
                        alignSelf: "center"
                    }}
                >

                    <Box component="form" onSubmit={sendAddArea}>
                        <Grid container flexDirection={"row"}>
                            <Box>
                                <TextField
                                    required
                                    label="新领域名称"
                                    variant="outlined"
                                    name={"area-name"}
                                />
                            </Box>
                            <Box alignSelf={"center"} pl={2}>
                                <Button
                                    size={"large"}
                                    variant={"contained"}
                                    type={"submit"}
                                >
                                    添加
                                </Button>
                            </Box>
                        </Grid>
                    </Box>

                </Grid>
            </Box>
            <Box>
                <Typography variant={"h5"} my={4} textAlign={"start"}>
                    所有领域
                </Typography>
                <AreaTable
                    refresh={needRefresh}
                    requestRefresh={requestRefresh}
                    completeRefresh={completeRefresh}
                />
            </Box>

        </Grid>
    );
}