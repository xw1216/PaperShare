import React, {Component} from "react";
import Header from "../components/global/header";
import Footer from "../components/global/footer";
import Banner from "../components/banner";
import {Grid, Typography} from "@mui/material";
import Search from "../components/search";
import {Navigate} from "react-router-dom";


const mainBanner = {
    title: '探索未知',
    description:
        "文献管理 · 随记笔记 · 交流讨论",
    image: 'https://source.unsplash.com/random',
    imageText: 'main image description',
    linkText: '了解 PaperShare',
};

export default class Home extends Component<any, any> {
    constructor(props: any) {
        super(props);
        this.state = {beginSearch: false};
    }

    jumpSearchResult = () => {
        this.setState({beginSearch: true});
    }

    render() {
        return (
            <div>
                <Header></Header>
                {this.state.beginSearch &&
                    <Navigate to={"/search"} replace={true}/>
                }
                <Grid container>
                    <Grid item xs={1}></Grid>
                    <Grid item xs={10} mt={5}>
                        <Banner post={mainBanner}></Banner>
                        <Typography component="h2" variant="h4" my={5} fontWeight={"bold"}>
                            现在开始，享你所想
                        </Typography>
                        <Search
                            setKeyword={this.props.setKeyword}
                            navigateFunc={this.jumpSearchResult}
                        />
                    </Grid>
                    <Grid item xs={1}></Grid>
                </Grid>

                <Footer></Footer>
            </div>
        );
    }
}