import React, {Component} from "react";
import {Box, Button, Card, CardActions, CardContent, Menu, MenuItem, Stack, Typography} from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
import {Link as RouterLink} from "react-router-dom";
import RepoEditDialog from "./repoEditDialog";
import ConfirmDialog from "../global/confirmDialog";
import RepoLib from "../../lib/repo";
import {DataUtil} from "../../lib/axios";

export default class RepoCard extends Component<any, any> {

    constructor(props: any) {
        super(props);
        this.state = {
            isMenuPop: false,
            anchorEl: undefined,
            isEditDialogOpen: false,
            isDeleteDialogOpen: false,
        }
        this.handleMenuClose.bind(this);
        this.handleMenuOpen.bind(this);
        this.setEditDialogClose.bind(this);
        this.setDeleteDialogClose.bind(this);
        this.handleEditClose.bind(this);
        this.handleDeleteClose.bind(this);
        this.displayLock.bind(this);
    }

    setEditDialogClose = () => {
        this.setState({isEditDialogOpen: false})
    }

    setDeleteDialogClose = () => {
        this.setState({isDeleteDialogOpen: false});
    }

    handleEditClose = () => {
        this.handleMenuClose();
        this.setState({isEditDialogOpen: true});
    }

    handleDeleteClose = () => {
        this.handleMenuClose();
        this.setState({isDeleteDialogOpen: true});
    }

    handleDeleteAffirm = () => {
        this.setDeleteDialogClose();
        RepoLib.deleteRepo(this.props.repo.id)
            .then((response: any) => {
                console.log(response);
                let res = DataUtil.getSuccessData(response);
                this.props.enqueue(res.msg, {
                    variant: "success"
                });
                this.props.refresh();
            })
            .catch((response: any) => {
                console.log(response);
                let res = DataUtil.getErrorData(response);
                this.props.enqueue(res.msg, {
                    variant: "error"
                });
            })
    }

    handleMenuOpen = (event: React.MouseEvent<HTMLButtonElement>) => {
        this.setState({anchorEl: event.currentTarget});
        this.setState({isMenuPop: true});
    }

    handleMenuClose = () => {
        this.setState({anchorEl: undefined});
        this.setState({isMenuPop: false});
    }

    displayLock = () => {
        if (this.props.repo.visible === false) {
            return (<LockIcon fontSize={"small"}/>);
        }
    }

    render() {
        return (
            <React.Fragment>
                <Card
                    elevation={2}
                    sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                >
                    <CardContent sx={{flexGrow: 1}}>
                        <Stack ml={1}
                               mb={2} direction="row" spacing={2}>
                            <Typography
                                variant="h5"
                                component="h1"
                                align={"left"}
                                alignSelf={"center"}
                                textAlign={"center"}
                            >
                                {this.props.repo.name}
                            </Typography>
                            <Box alignItems={"center"} alignSelf={"center"} alignContent={"center"}>
                                {this.displayLock()}
                            </Box>
                        </Stack>
                        <Typography ml={1} align={"left"}>
                            {this.props.repo.cont}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Box mx={2} my={1}>
                            <Stack direction={"row"} spacing={2}>
                                <Button
                                    size="small"
                                    variant={"contained"}
                                    component={RouterLink} to={"/repo/" + this.props.repo.id}
                                >
                                    查看
                                </Button>
                                <Button variant={"outlined"}
                                        size="small" onClick={this.handleMenuOpen}
                                        aria-controls={this.state.isMenuPop ? 'basic-menu' : undefined}
                                        aria-haspopup="true"
                                        aria-expanded={this.state.isMenuPop ? 'true' : undefined}
                                >
                                    更改
                                </Button>
                            </Stack>
                        </Box>
                        <Menu
                            id="basic-menu"
                            anchorEl={this.state.anchorEl}
                            open={this.state.isMenuPop}
                            onClose={this.handleMenuClose}
                            MenuListProps={{
                                'aria-labelledby': 'basic-button',
                            }}
                        >
                            <MenuItem sx={{fontSize: "small"}} onClick={this.handleEditClose}>修改信息</MenuItem>
                            <MenuItem sx={{fontSize: "small"}} onClick={this.handleDeleteClose}>删除</MenuItem>
                        </Menu>
                    </CardActions>
                </Card>
                <RepoEditDialog
                    status={{
                        open: this.state.isEditDialogOpen,
                        setClose: this.setEditDialogClose,
                        refresh: this.props.refresh,
                    }}
                    type={"edit"}
                    id={this.props.repo.id}
                    defaultValue={{
                        name: this.props.repo.name,
                        cont: this.props.repo.cont,
                        visible: this.props.repo.visible
                    }}
                />
                <ConfirmDialog
                    open={this.state.isDeleteDialogOpen}
                    dialogClose={this.setDeleteDialogClose}
                    dialogAffirm={this.handleDeleteAffirm}
                />
            </React.Fragment>

        );
    }

}