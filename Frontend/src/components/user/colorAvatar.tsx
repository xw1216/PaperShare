import {Avatar, IconButton} from "@mui/material";
import {useNavigate} from "react-router-dom";


function stringToColor(string: string) {
    let hash = 0;
    let i;

    /* eslint-disable no-bitwise */
    for (i = 0; i < string.length; i += 1) {
        hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    let color = '#';

    for (i = 0; i < 3; i += 1) {
        const value = (hash >> (i * 8)) & 0xff;
        color += `00${value.toString(16)}`.slice(-2);
    }
    /* eslint-enable no-bitwise */

    return color;
}

function stringAvatar(name: string, size: number | undefined) {
    let abbr: string | undefined;

    if (!name) {
        abbr = "无";
    } else if (name.indexOf(" ") === -1) {
        abbr = name.at(0);
    } else {
        abbr = `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`;
    }

    if (size === undefined || size === null) {
        size = 56;
    }

    return {
        sx: {
            bgcolor: stringToColor(name),
            width: size,
            height: size,
            fontSize: size / 3
        },
        children: abbr,
    };
}

interface IColorAvatar {
    type?: string | undefined,
    username: string,
    size?: number,
}

export default function ColorAvatar(props: IColorAvatar) {
    let navigate = useNavigate();

    function jumpToUserRepo() {
        if (props.type === "profile") {
            navigate("/profile");
            return;
        } else if (props.type === "user") {
            navigate("/user");
        }
    }

    return (
        <IconButton onClick={jumpToUserRepo}>
            <Avatar {...stringAvatar(props.username, props.size)} ></Avatar>
        </IconButton>

    );
}