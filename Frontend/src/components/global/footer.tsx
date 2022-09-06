import {Grid, Link, Typography} from "@mui/material";
import Copyright from "../global/copyright"
import React from "react";

const footers = [
    {
        title: '联系信息',
        description: [' Team Outspace.', '倾情呈现', 'papershare2022@163.com', '联系我们'],
    },
    {
        title: '项目亮点',
        description: [
            '采用最新技术',
            '文献管理',
            '仓库与笔记',
            '有限交流',
        ],
    },
];

export default function Footer() {
    return (
        <Grid container component="footer">
            <Grid item xs={12}
                  sx={{
                      borderTop: (theme) => `1px solid ${theme.palette.divider}`,
                      mt: 8,
                      py: [3, 6],
                  }}
            >
            </Grid>
            <Grid item xs={12}/>
            <Grid container spacing={3} justifyContent="space-evenly">
                <Grid item xs={2}/>
                {footers.map((footer) => (
                    <Grid item xs={4} sm={2} key={footer.title}>
                        <Typography variant="h6" color="text.primary" gutterBottom>
                            {footer.title}
                        </Typography>
                        <Grid container flexDirection={"column"}>
                            {footer.description.map((item: string) => (
                                <Link mt={1} key={item} variant="subtitle1"
                                      color="text.secondary">
                                    {item}
                                </Link>
                            ))}
                        </Grid>
                    </Grid>
                ))}
                <Grid item xs={2}/>
            </Grid>
            <Grid item xs={12} mb={5}>
                <Copyright sx={{mt: 5}}/>
            </Grid>
        </Grid>

    );
}



