import React, {Component} from "react";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import {Box, createTheme, Grid, ThemeProvider, Typography} from "@mui/material";
import Search from "../components/search";
import PaperAccord from "../components/paper/paperAccord";
import PaperLib from "../lib/paper";
import {DataUtil} from "../lib/axios";

const theme = createTheme();

export default class SearchResult extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {paperList: null, needRefresh: true};
    }

    componentDidMount() {
        if (this.state.needRefresh === true) {
            this.refreshResult();
            this.setState({needRefresh: false});
        }
    }

    requestRefresh = () => {
        this.refreshResult();
    }

    refreshResult = () => {
        PaperLib.searchPaper(this.props.keyword)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                this.setState({paperList: res.data});
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error",
                })
            })
    }

    displayPapers = () => {
        if (this.state.paperList === null || this.state.paperList === undefined) {
            return (
                <Typography variant={"h6"} textAlign={"center"}>
                    暂无结果
                </Typography>
            );
        } else {
            return this.state.paperList.map((paper: any) => {
                return (
                    <Box key={paper.id}>
                        <PaperAccord
                            type={"search"}
                            paper={paper}
                        >
                        </PaperAccord>
                    </Box>

                );
            })
        }
    }


    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Grid container>
                        <Grid item xs={2}></Grid>
                        <Grid item xs={8} mt={5}>
                            <Search setKeyword={this.props.setKeyword}
                                    navigateFunc={this.requestRefresh}
                            />
                            <Box my={4}>
                                <Typography textAlign={"start"} variant={"h3"}>
                                    搜索结果
                                </Typography>
                            </Box>
                            {this.displayPapers()}
                        </Grid>
                        <Grid item xs={2}></Grid>
                    </Grid>
                    <Footer></Footer>
                </div>
            </ThemeProvider>
        );
    }
}