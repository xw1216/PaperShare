import {Link, Typography} from "@mui/material";


export default function Copyright(props: any) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'版权所有 ©  '}
            <Link color="inherit" href="https://outspace.tech/">
                Outspace.Inc
            </Link>{'  '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}
