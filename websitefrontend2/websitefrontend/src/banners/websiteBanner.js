import React, {useEffect, useState} from "react";
import {useHistory} from 'react-router-dom/cjs/react-router-dom.min';
import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
import SettingsIcon from '@mui/icons-material/Settings';
import PeopleIcon from '@mui/icons-material/People';
import MessageIcon from '@mui/icons-material/Message';
import DynamicFeedIcon from '@mui/icons-material/DynamicFeed';
import EmojiEmotionsIcon from '@mui/icons-material/EmojiEmotions';
import ProfilePictureComponent from '../buttonComponents/ProfilePictureComponent';
import './banner.css';
import {useUserContext} from "../pages/usercontext/UserContext";
import Cookies from 'js-cookie';
import {getRandomColor} from "../FunSFX/randomColorGenerator";
import MessageNotifications from "../buttonComponents/MessageNotificationComponent/MessageNotifications";
import PostNotification from "../buttonComponents/PostNotification/PostNotification";
const Banner = () => {
    const history = useHistory();
    const [userData, setUserData] = useState(null);
    const { user } = useUserContext();


    const bannerStyle = {
        background: user && user.backgroundColor ? `linear-gradient(to right, ${user.backgroundColor}, black)` : `linear-gradient(to right, ${getRandomColor()}, black)`,
        display: 'flex',
        flexDirection: 'row',
        width: '100%',
        margin: '0 !important',
        padding: 0,
    };

    useEffect(() => {
        const cookieUserData = Cookies.get('userData');
        if (cookieUserData) {
            setUserData(JSON.parse(cookieUserData));
        }
    }, [user]);

    const handleLinkClick = (url) => {
        history.push(url);
    };


    return (
        <div className="banner" style={bannerStyle}>
            <h1 className="title">NateBook</h1>
            <PeopleIcon className="button-common" onClick={() => handleLinkClick('/AllUsersPage')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            <PostNotification/>
            <DynamicFeedIcon className="button-common" onClick={() => handleLinkClick('/Feed')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            {userData ? (
                <div>
                    <MessageNotifications />
                    <MessageIcon className="button-common" onClick={() => handleLinkClick('/Messages')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <EmojiEmotionsIcon className="button-common friends-button" onClick={() => handleLinkClick('/Friends')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <LogoutButton className="button-common" />
                    <SettingsIcon className="button-common" onClick={() => handleLinkClick('/Account')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                </div>
            ) : (
                <>
                    <LoginButton />
                    <CreateAccount />
                </>
            )}
            {user &&
            <div className="right-justify">
                <ProfilePictureComponent userid={userData?.appUserID} style={{ width: '150px', height: '150px' }} />
            </div>
            }
        </div>
    );
};

export default Banner;