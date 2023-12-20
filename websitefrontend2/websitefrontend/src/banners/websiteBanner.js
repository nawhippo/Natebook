import React, { useEffect, useState } from "react";
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
import FriendReqCounter from './notificationHelpers/friendRequestHelpers';
import SettingsIcon from '@mui/icons-material/Settings';
import PeopleIcon from '@mui/icons-material/People';
import MessageIcon from '@mui/icons-material/Message';
import DynamicFeedIcon from '@mui/icons-material/DynamicFeed';
import EmojiEmotionsIcon from '@mui/icons-material/EmojiEmotions';
import ProfilePictureComponent from '../buttonComponents/ProfilePictureComponent';
import './banner.css';
import { useUserContext } from "../pages/usercontext/UserContext";
import Cookies from 'js-cookie';

const Banner = () => {
    const history = useHistory();
    const [userData, setUserData] = useState(null);
    const { user } = useUserContext();

    useEffect(() => {
        const cookieUserData = Cookies.get('userData');
        if (cookieUserData) {
            setUserData(JSON.parse(cookieUserData));
        }
    }, [user]);

    const handleLinkClick = (url) => {
        history.push(url);
    };

    const bannerStyle = {
        background: user && user.backgroundColor ? `linear-gradient(to right, ${user.backgroundColor}, black)` : 'linear-gradient(to right, orange, black)'
    };

    return (
        <div className="banner" style={bannerStyle}>
            <h1 className="title">NateBook</h1>
            <PeopleIcon className="button-common" onClick={() => handleLinkClick('/AllUsersPage')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            <DynamicFeedIcon className="button-common" onClick={() => handleLinkClick('/Feed')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            <FriendReqCounter style={{width:'50px', background:'red', color:'white'}}/>
            {userData ? (
                <div>
                    <MessageIcon className="button-common" onClick={() => handleLinkClick('/Messages')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <EmojiEmotionsIcon className="button-common friends-button" onClick={() => handleLinkClick('/Friends')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <LogoutButton />
                    <SettingsIcon className="button-common" onClick={() => handleLinkClick('/Account')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                </div>
            ) : (
                <>
                    <LoginButton />
                    <CreateAccount />
                </>
            )}
            <div className="right-justify">
                <ProfilePictureComponent className="button-common" userid={userData?.appUserID}/>
            </div>
        </div>
    );
};

export default Banner;