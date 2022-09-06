import React, {useEffect, useRef, useState} from "react";
import useTypewriter from "react-typewriter-hook";

const slogans = [
    "PaperShare",
    "享你所想",
    "探索未知",
    "科研人，冲！"
]

let index = 0;

export default function Slogan() {
    const [magicName, setMagicName] = useState("PaperShare");
    const intervalRef = useRef({});
    const name = useTypewriter(magicName);
    useEffect(
        () => {
            intervalRef.current = setInterval(() => {
                index = index > 2 ? 0 : ++index;
                setMagicName(slogans[index]);
            }, 5000);
            return function clear() {
                clearInterval(intervalRef.current as number);
            };
        },
        [magicName]
    );

    return (
        <p>&#160;{name}&#160;</p>
    );
}