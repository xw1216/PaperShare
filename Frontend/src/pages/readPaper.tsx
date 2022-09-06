import React, {Component} from "react";
import {PDFRender} from "../components/read/pdfRender";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import {createTheme, Grid, ThemeProvider,} from "@mui/material";
import NoteSideBar from "../components/read/noteSidebar";
import {useParams} from "react-router-dom";
import PaperLib from "../lib/paper";
import {DataUtil} from "../lib/axios";
import {useSnackbar} from "notistack";

const theme = createTheme();

export const ReadWithRouter = () => {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const params = useParams();
    return (
        <ReadPaper
            repoId={params.repoId}
            paperId={params.paperId}
            enqueue={enqueueSnackbar}
        />
    );
}

export default class ReadPaper extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {pdf: "", page: 1, init: true};
    }

    setPage = (pageNum: number) => {
        this.setState({page: pageNum});
    }

    componentDidMount() {
        if (this.state.init) {
            PaperLib.getPaperPdf(this.props.paperId)
                .then((response: any) => {
                    console.log(response);
                    let res = DataUtil.getSuccessData(response);
                    this.setState({pdf: res.data});
                })
                .catch((response: any) => {
                    console.log(response);
                    let res = DataUtil.getErrorData(response);
                    this.props.enqueue(res.msg, {
                        variant: "error",
                    })
                });
            this.setState({init: false});
        }
    }


    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Grid container sx={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                    }}>
                        <Grid item md={3} sm={1}>
                            <NoteSideBar
                                repoId={this.props.repoId}
                                paperId={this.props.paperId}
                                page={this.state.page}
                            />
                        </Grid>
                        <Grid item md={8} sm={11}>
                            <PDFRender
                                src={this.state.pdf}
                                page={this.state.page}
                                setPage={this.setPage}
                            />
                            <Footer></Footer>
                        </Grid>
                    </Grid>

                </div>
            </ThemeProvider>
        );
    }
}