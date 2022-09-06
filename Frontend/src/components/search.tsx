import React, {Component} from "react";
import {Box, FormControl, Grid, IconButton, InputAdornment, InputLabel, OutlinedInput, styled} from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';

const InputBox = styled(Box)(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: "center"
}));

export default class Search extends Component<any, any> {

    handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);

        let keyword = data.get('keyword');
        this.props.setKeyword(keyword);
        this.props.navigateFunc();
    }

    render() {
        return (
            <Grid container mb={5} px={5} flexDirection={"column"}>
                <Box component="form" onSubmit={this.handleSubmit}>
                    <FormControl fullWidth sx={{m: 1}} variant="outlined">
                        <InputLabel>输入论文标题以进行查找</InputLabel>
                        <OutlinedInput
                            name={"keyword"}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton edge="end" type={"submit"}>
                                        {<SearchIcon/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="输入论文标题以进行查找"
                        />
                    </FormControl>
                </Box>

            </Grid>

        );
    }
}