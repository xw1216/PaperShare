import ColorAvatar from "./colorAvatar";
import {Grid, SxProps, Theme, Typography} from "@mui/material";
import React from "react";

type UserIconType = {
    userId: string,
    userName: string,
    sx: SxProps<Theme>,
    size?: "md" | "lg"
}

export default function UserIcon(props: UserIconType) {
    let avatarSize: number;

    if (props.size === "lg") {
        avatarSize = 56
    } else {
        avatarSize = 40;
    }

    return (
        <Grid container
              mt={4}
              mb={4}
              flexDirection={"row"}
              maxWidth={"lg"}
              sx={props.sx}
        >

            <ColorAvatar username={props.userName} size={avatarSize}></ColorAvatar>

            <Typography
                mx={2}
                variant={props.size === "lg" ? "h4" : "h6"}
            >
                {props.userName}
            </Typography>
        </Grid>
    );
}