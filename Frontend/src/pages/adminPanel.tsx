import React from "react";
import {createTheme, Grid, ThemeProvider} from "@mui/material";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import AdminSideBar from "../components/admin/adminSideBar";
import AdminArea from "../components/admin/adminArea";
import AdminPaper from "../components/admin/adminPaper";

const theme = createTheme();

export default class AdminPanel extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.switchPanel.bind(this);
        this.state = {
            panel: "paper"
        };
    }

    switchPanel = (panel: string) => {
        this.setState({panel: panel});
    }

    renderPanel = () => {
        if (this.state.panel === "paper") {
            return (
                <AdminPaper></AdminPaper>
            );
        } else if (this.state.panel === "area") {
            return (
                <AdminArea></AdminArea>
            );
        }

    }

    render() {
        return (
            <ThemeProvider theme={theme}>
                <div>
                    <Header></Header>
                    <Grid container
                          sx={{
                              display: "flex",
                              justifyContent: "center",
                              alignItems: "center",
                          }}
                    >
                        <Grid item md={3} sm={1}>
                            <AdminSideBar
                                onChange={this.switchPanel}
                            />
                        </Grid>
                        <Grid item md={9} sm={11}>
                            <Grid item md={1}></Grid>
                            <Grid item md={10}>
                                {this.renderPanel()}
                                <Footer></Footer>
                            </Grid>
                            <Grid item md={1}></Grid>

                        </Grid>
                    </Grid>

                </div>
            </ThemeProvider>
        );
    }
}