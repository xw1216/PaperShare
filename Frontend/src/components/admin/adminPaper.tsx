import {Autocomplete, Box, Button, Grid, Stack, styled, TextField, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import TitleIcon from '@mui/icons-material/Title';
import PersonIcon from '@mui/icons-material/Person';
import PlaylistAddCircleIcon from '@mui/icons-material/PlaylistAddCircle';
import InventoryIcon from '@mui/icons-material/Inventory';
import DnsIcon from '@mui/icons-material/Dns';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import PaperLib, {PaperVo} from "../../lib/paper";
import {DataUtil} from "../../lib/axios";
import {useSnackbar} from "notistack";
import AreaLib from "../../lib/area";

const Input = styled('input')({
    display: 'none',
});

export default function AdminPaper(props: any) {

    const {enqueueSnackbar, closeSnackbar} = useSnackbar();

    let [filename, setFilename] = useState<string>("");
    let [fileResult, setFileResult] = useState<any>("");
    let [areaList, setAreaList] = useState<any>([]);
    let [loading, setLoading] = useState<boolean>(true);
    let [selectedArea, setSelectedArea] = useState<any>([]);

    useEffect(() => {
        if (loading) {
            AreaLib.getAllArea()
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    setAreaList(res.data);
                    setLoading(false);
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error",
                    });
                });
        }
    }, [enqueueSnackbar, loading])

    let onFileInput = (e: any) => {
        e.preventDefault();
        let reader = new FileReader();
        let file = e.target.files[0];

        reader.onloadend = () => {
            setFilename(file.name);
            setFileResult(reader.result);
        }

        reader.readAsDataURL(file);
    }

    let onPaperCancel = () => {
        setFilename("");
        setFileResult("");
    }

    let handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let vo: PaperVo = {
            title: data.get('title'),
            brief: data.get('brief'),
            author: data.get('author'),
            year: data.get('year'),
            journal: data.get('journal'),
            path: fileResult,
            doi: data.get('doi'),
            areaList: selectedArea,
        }

        console.log(vo);

        PaperLib.addPaper(vo)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                enqueueSnackbar(res.msg, {
                    variant: "success",
                });
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                enqueueSnackbar(res.msg, {
                    variant: "error",
                });
            });
    }

    let updateSelectedArea = (event: any, value: any) => {
        setSelectedArea(value);
    }

    let displayAreaInput = () => {
        if (loading) {
            return (
                <Typography variant={"body2"} textAlign={"center"}>
                    加载中···
                </Typography>
            );
        } else {
            return (
                <Autocomplete
                    fullWidth
                    multiple
                    options={areaList}
                    getOptionLabel={(area: any) => area.name}
                    filterSelectedOptions
                    onChange={updateSelectedArea}
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            label="相关领域"
                            name={"area"}
                        />
                    )}
                />
            );
        }
    }

    return (
        <Grid container flexDirection={"column"}>
            <Typography variant={"h3"} my={4} textAlign={"start"}>
                新增入库论文
            </Typography>

            <Box component="form" noValidate onSubmit={handleSubmit}>
                <Stack my={2} direction={"row"}>
                    <Box pr={2} alignSelf={"center"}>
                        <TitleIcon fontSize={"large"}></TitleIcon>
                    </Box>
                    <TextField
                        name={"title"}
                        fullWidth
                        required
                        label="标题"
                        variant="outlined"
                    />
                </Stack>
                <Stack my={2} direction={"row"}>
                    <Box pr={2} alignSelf={"center"}>
                        <PersonIcon fontSize={"large"}></PersonIcon>
                    </Box>
                    <TextField
                        name={"author"}
                        fullWidth
                        required
                        label="作者"
                        variant="outlined"
                    />
                </Stack>
                <Stack my={2} direction={"row"}>
                    <Box pr={2} alignSelf={"center"}>
                        <PlaylistAddCircleIcon fontSize={"large"}></PlaylistAddCircleIcon>
                    </Box>
                    <Stack spacing={2} direction={"row"}>
                        <TextField
                            name={"journal"}
                            fullWidth
                            required
                            label="出版物"
                            variant="outlined"
                        />
                        <TextField
                            name={"year"}
                            fullWidth
                            required
                            label="出版年"
                            type={"number"}
                            variant="outlined"
                        />
                        <TextField
                            name={"doi"}
                            fullWidth
                            required
                            label="DOI"
                            variant="outlined"
                        />
                    </Stack>
                </Stack>
                <Stack my={2} direction={"row"}>
                    <Box pr={2} alignSelf={"center"}>
                        <InventoryIcon fontSize={"large"}></InventoryIcon>
                    </Box>
                    <TextField
                        name={"brief"}
                        fullWidth
                        required
                        multiline
                        minRows={3}
                        maxRows={8}
                        label="摘要"
                        variant="outlined"
                    />
                </Stack>
                <Stack my={2} direction={"row"}>
                    <Box pr={2} alignSelf={"center"}>
                        <DnsIcon fontSize={"large"}></DnsIcon>
                    </Box>
                    {displayAreaInput()}
                </Stack>
                <Stack my={2} direction={"row"} spacing={2}>
                    <Box alignSelf={"center"}>
                        <PictureAsPdfIcon fontSize={"large"}></PictureAsPdfIcon>
                    </Box>
                    <label htmlFor="contained-button-file">
                        <Input accept={"application/pdf"} id="contained-button-file" multiple type="file"
                               onChange={onFileInput}/>
                        <Button variant="contained" component="span">
                            上传 PDF
                        </Button>
                    </label>
                    <Typography variant={"body1"} alignSelf={"center"}>
                        文件名*: {filename === "" ? "空" : filename}
                    </Typography>
                </Stack>
                <Grid container flexDirection={"row"} justifyContent={"center"}>
                    <Stack my={4} direction={"row"} spacing={2}>
                        <Button
                            variant={"contained"}
                            type={"submit"}
                        >
                            确认
                        </Button>
                        <Button
                            variant={"outlined"}
                            onClick={onPaperCancel}
                        >
                            清空
                        </Button>
                    </Stack>
                </Grid>
            </Box>

        </Grid>
    );
}