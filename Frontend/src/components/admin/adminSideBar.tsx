import {Box, Divider, Drawer, List, ListItem, ListItemButton, ListItemText, styled} from "@mui/material";
import React from "react";
import DescriptionIcon from '@mui/icons-material/Description';
import DnsIcon from '@mui/icons-material/Dns';

const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));

interface IPropNoteSideItem {
    text: string,
    icon: any,
    onClick: any,
}

function NoteSideItem(props: IPropNoteSideItem) {
    return (
        <ListItem key={props.text} disablePadding>
            <ListItemButton onClick={props.onClick}>
                <Box pr={2}>
                    {props.icon}
                </Box>

                <ListItemText primary={props.text}/>
            </ListItemButton>
        </ListItem>
    );
}

const drawerWidth = 240;

export default function AdminSideBar(props: any) {

    const switchPanel = (panel: string) => {
        console.log(panel);
        props.onChange(panel);
    }

    return (
        <Drawer
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                '& .MuiDrawer-paper': {
                    width: drawerWidth,
                    boxSizing: 'border-box',
                }
            }}
            variant="persistent"
            anchor="left"
            open={true}
        >
            <DrawerHeader></DrawerHeader>
            <Divider/>
            <List>
                <NoteSideItem
                    text={"添加论文"}
                    icon={<DescriptionIcon/>}
                    onClick={() => switchPanel("paper")}
                />
                <NoteSideItem
                    text={"管理领域"}
                    icon={<DnsIcon/>}
                    onClick={() => switchPanel("area")}
                />
            </List>

        </Drawer>
    );
}