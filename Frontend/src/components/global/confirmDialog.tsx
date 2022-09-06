import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import React from "react";

interface IConfirmDialog {
    open: boolean,
    dialogClose: any,
    dialogAffirm: any
}

export default function ConfirmDialog(props: IConfirmDialog) {
    return (
        <Dialog
            fullWidth
            open={props.open}
            onClose={props.dialogClose}
        >
            <DialogTitle>
                操作确认
            </DialogTitle>
            <DialogContent>
                <DialogContentText>
                    您确认这么做吗？
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button
                    onClick={props.dialogClose}
                    autoFocus
                    variant={"outlined"}
                >
                    取消
                </Button>
                <Button
                    onClick={props.dialogAffirm}
                    color={"error"}
                    variant={"outlined"}
                >
                    确认
                </Button>
            </DialogActions>
        </Dialog>
    );
}