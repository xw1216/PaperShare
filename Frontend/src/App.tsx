import React, {useState} from 'react';
import './App.css';

import {Route, Routes, useNavigate} from "react-router-dom";
import Home from "./pages/home";
import About from "./pages/about";
import Login from "./pages/login";
import Signup from "./pages/signup";
import UserRepos from "./pages/userRepos";
import PageNotFound from "./pages/404";
import {RepoWithRouter} from "./pages/repo";
import {ReadWithRouter} from "./pages/readPaper";
import SearchResult from "./pages/searchResult";
import {PaperWithRouter} from "./pages/paper";
import Profile from "./pages/profile";
import Explore from "./pages/explore";
import Stars from "./pages/stars";
import AdminPanel from "./pages/adminPanel";
import {useSnackbar} from "notistack";


function App() {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar();
    const navigate = useNavigate();

    const [keyword, setKeyword] = useState<string>("");

    return (
        <div className="App">
            <main>
                <Routes>
                    <Route path={"/"} element={<Home setKeyword={setKeyword}/>}/>
                    <Route path={"/admin"} element={<AdminPanel enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/search"} element={
                        <SearchResult
                            keyword={keyword}
                            setKeyword={setKeyword}
                            enqueue={enqueueSnackbar}
                        />
                    }
                    />
                    <Route path={"/login"} element={<Login enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/signup"} element={<Signup enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/profile"} element={<Profile enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/explore"} element={<Explore enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/stars"} element={<Stars enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/user"} element={<UserRepos navigate={navigate} enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/repo/:id"} element={<RepoWithRouter enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/paper/:id"} element={<PaperWithRouter enqueue={enqueueSnackbar}/>}/>
                    <Route path={"/read/:repoId/:paperId"} element={<ReadWithRouter/>}/>
                    <Route path={"/about"} element={<About/>}/>
                    <Route path={"*"} element={<PageNotFound/>}/>
                </Routes>
            </main>
        </div>
    );
}

export default App;
