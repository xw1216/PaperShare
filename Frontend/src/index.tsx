import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './test/reportWebVitals';
import {BrowserRouter} from "react-router-dom";
import CssBaseline from "@mui/material/CssBaseline";
import {SnackbarProvider} from "notistack";
import AuthLib from "./lib/auth";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

AuthLib.clearHttpToken();

root.render(
    <React.StrictMode>
        <BrowserRouter>
            <CssBaseline>
                <SnackbarProvider maxSnack={3}>
                    <App/>
                </SnackbarProvider>
            </CssBaseline>
        </BrowserRouter>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
