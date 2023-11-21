import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
import FriendReqCounter from './notificationHelpers/friendRequestHelpers';
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import Cookies from 'js-cookie'; // Import Cookies library
import './banner.css';
import { useEffect, useState } from "react";
import {useUserContext} from "../pages/usercontext/UserContext";

const Banner = () => {
    const history = useHistory();
    const { user } = useUserContext(); // Use useState to manage user data

    useEffect(() => {
        // Load user data from cookies when the component mounts
    }, [user]);

    const handleLinkClick = (url) => {
        history.push(url);
    };

    return (
        <div className="banner">
            <h1 className="title">NateBook</h1>
            <button className="button-common" onClick={() => handleLinkClick('/AllUsersPage')}>Users</button>
            <button className="button-common" onClick={() => handleLinkClick('/Feed')}>View Feed</button>
            <FriendReqCounter></FriendReqCounter>

            {user !== null ? (
                <div>
                    <p>Logged in as: {user.username}</p>
                    <button className="button-common" onClick={() => handleLinkClick('/Account')}>Account</button>
                    <button className="button-common" onClick={() => handleLinkClick('/Messages')}>View Messages</button>
                    <button className="button-common" onClick={() => handleLinkClick('/Friends')}>Friends</button>
                    <LogoutButton />
                    <LoginButton />
                    <CreateAccount />
                </div>
            ) : (
                <>
                    <LoginButton />
                    <CreateAccount />
                </>
            )}
        </div>
    );
};

export default Banner;