import {
    IconButton,
    Paper,
    styled,
    Table,
    TableBody,
    TableCell,
    tableCellClasses,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import React, {useEffect, useState} from "react";
import ConfirmDialog from "../global/confirmDialog";
import {useSnackbar} from "notistack";
import AreaLib from "../../lib/area";
import {DataUtil} from "../../lib/axios";

const StyledTableCell = styled(TableCell)(({theme}) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: theme.palette.common.black,
        color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
        fontSize: 14,
    },
}));

const StyledTableRow = styled(TableRow)(({theme}) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.primary,
    },
    // hide last border
    '&:last-child td, &:last-child th': {
        border: 0,
    },
}));

interface AreaRow {
    id: string,
    name: string,
    userUsage: number,
    paperUsage: number
}

let rows: Array<AreaRow> = [{
    id: "123456",
    name: "Computer Graphics",
    userUsage: 10,
    paperUsage: 1001
}, {
    id: "1234567",
    name: "Computer Vision",
    userUsage: 10,
    paperUsage: 1002
}, {
    id: "1234568",
    name: "Operating System",
    userUsage: 10,
    paperUsage: 1003
}];

export default function AreaTable(props: any) {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    let [open, setDialog] = useState<boolean>(false);
    let [selectId, setSelectId] = useState<string>("");
    let [areaRows, serAreaRows] = useState<any>(null);

    let dialogClose = () => {
        setDialog(false);
        setSelectId("");
    }

    let dialogOpen = (id: string) => {
        setSelectId(id);
        setDialog(true);
    }

    let dialogAffirm = () => {
        AreaLib.deleteArea(selectId)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                props.requestRefresh();
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
        dialogClose();
    }

    let displayRows = () => {
        if (areaRows === null || areaRows === undefined) {
            return;
        } else {
            return (
                areaRows.map((row: any) => (
                    <StyledTableRow key={row.id}>
                        <StyledTableCell component="th" scope="row">
                            {row.name}
                        </StyledTableCell>
                        <StyledTableCell align="center">{row.paperUsage}</StyledTableCell>
                        <StyledTableCell align="center">{row.userUsage}</StyledTableCell>
                        <StyledTableCell align="center">
                            <IconButton size={"small"} onClick={() => {
                                dialogOpen(row.id)
                            }}>
                                <DeleteIcon/>
                            </IconButton>
                        </StyledTableCell>
                    </StyledTableRow>
                ))
            );
        }

    }

    useEffect(() => {
        if (props.refresh) {
            AreaLib.getAreaTable()
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    serAreaRows(res.data);
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    enqueueSnackbar(res.msg, {
                        variant: "error"
                    });
                })
            props.completeRefresh();
        }
    }, [enqueueSnackbar, props]);

    return (
        <React.Fragment>
            <TableContainer component={Paper}>
                <Table sx={{minWidth: 700}}>
                    <TableHead>
                        <TableRow>
                            <StyledTableCell>领域名称</StyledTableCell>
                            <StyledTableCell align="center">论文使用量</StyledTableCell>
                            <StyledTableCell align="center">用户使用量</StyledTableCell>
                            <StyledTableCell align="center">操作</StyledTableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {displayRows()}
                    </TableBody>
                </Table>
            </TableContainer>
            <ConfirmDialog
                open={open}
                dialogClose={dialogClose}
                dialogAffirm={dialogAffirm}
            />
        </React.Fragment>

    );
}