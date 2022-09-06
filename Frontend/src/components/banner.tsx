import {Box, Grid, Paper, Typography} from "@mui/material";
import React from "react";
import {Link as RouterLink} from "react-router-dom";
import Button from "@mui/material/Button";
import Slogan from "./slogan";


interface IBannerProps {
    post: {
        description: string;
        image: string;
        imageText: string;
        linkText: string;
        title: string;
    };
}

export default function Banner(props: IBannerProps) {
    const {post} = props;

    return (
        <Paper
            sx={{
                position: 'relative',
                backgroundColor: 'grey.800',
                color: '#fff',
                mb: 4,
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                backgroundPosition: 'center',
                backgroundImage: `url(${post.image})`,
                borderRadius: '20px'
            }}
            elevation={5}

        >
            {/* Increase the priority of the hero background image */}
            {<img style={{display: 'none', borderRadius: '20px'}} src={post.image} alt={post.imageText}/>}
            <Box
                sx={{
                    position: 'absolute',
                    top: 0,
                    bottom: 0,
                    right: 0,
                    left: 0,
                    backgroundColor: 'rgba(0,0,0,.7)',
                }}
            />
            <Grid container sx={{height: "60vh"}}>
                <Grid item xs={3} md={5} sm={12} alignSelf={"center"}>
                    <Box
                        sx={{
                            position: 'relative',
                            p: {xs: 3, md: 5, sm: 12},
                            pr: {md: 0},
                        }}
                    >
                        <Typography component="h2" variant="h3" color="inherit" gutterBottom>
                            {/*{post.title}*/}
                            {<Slogan/>}
                        </Typography>
                        <Typography mt={3} mb={5} variant="h5" color="inherit" paragraph>
                            {post.description}
                        </Typography>
                        <Button variant={"contained"} component={RouterLink} to="/about">{post.linkText}</Button>
                    </Box>
                </Grid>
            </Grid>
        </Paper>
    );
}