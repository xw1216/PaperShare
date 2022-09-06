import {Box, Card, CardContent, Stack, Typography} from "@mui/material";
import UserIcon from "../user/userIcon";
import LockIcon from "@mui/icons-material/Lock";
import React from "react";

export default function RepoSideCard(props: any) {

    let displayVisible = () => {
        if (props.repo.visible === false) {
            return (
                <Stack pl={2} mb={4} direction={"row"}>
                    <LockIcon/>
                    <Typography ml={2}>
                        私有
                    </Typography>
                </Stack>
            );
        }
    }

    return (
        <Card variant={"outlined"}>
            <CardContent>
                <Typography variant={"h4"} pl={2} my={2} textAlign={"start"}>
                    {props.repo.name}
                </Typography>
                <Box pl={2}>
                    <UserIcon
                        userId={props.repo.userId}
                        userName={props.repo.userName}
                        size={"md"}
                        sx={{
                            alignItems: "center",
                            justifyContent: "start",
                        }}
                    />
                    {displayVisible()}
                </Box>

                <Box pl={2}>
                    <Typography my={3} textAlign={"start"} variant={"h5"}>
                        仓库介绍
                    </Typography>

                    <Typography mb={4} textAlign={"start"}>
                        {props.repo.cont}
                    </Typography>
                </Box>

            </CardContent>
        </Card>
    );
}