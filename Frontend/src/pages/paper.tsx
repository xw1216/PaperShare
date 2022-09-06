import React, {Component} from "react";
import {Box, Chip, Container, createTheme, Divider, Grid, ThemeProvider, Typography} from "@mui/material";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import {useParams} from "react-router-dom";
import PaperCard from "../components/paper/paperCard";
import Comments from "../components/paper/comments";

const theme = createTheme();


export const PaperWithRouter = (props: any) => {
    const params = useParams();
    return (
        <Paper paperId={params.id} enqueue={props.enqueue}></Paper>
    );
}

export default class Paper extends Component<any, any> {


    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Box
                        sx={{
                            bgcolor: 'background.paper',
                            pt: 8,
                            pb: 6,
                        }}
                    >
                        <Grid container>
                            <Grid item md={2}></Grid>
                            <Grid item md={8}>
                                <Container
                                    maxWidth="lg"
                                    sx={{justifyContent: "flex-start"}}>

                                    <Typography
                                        mb={8}
                                        textAlign={"start"}
                                        variant={"h2"}>
                                        论文详情
                                    </Typography>

                                    <Box my={4}>
                                        <PaperCard paperId={this.props.paperId}/>
                                    </Box>
                                    <Box my={4}>
                                        <Divider>
                                            <Chip label="评论"/>
                                        </Divider>
                                        <Comments paperId={this.props.paperId}/>

                                    </Box>

                                </Container>
                            </Grid>
                            <Grid item md={2}></Grid>
                        </Grid>
                    </Box>

                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}