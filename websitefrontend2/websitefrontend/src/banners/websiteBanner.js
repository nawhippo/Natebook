import React, {useEffect, useState} from "react";
import {useHistory} from 'react-router-dom/cjs/react-router-dom.min';
import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import enterEmailButton from "../buttonComponents/createAccountButton/enterEmailButton";
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
import EnterEmailButton from "../buttonComponents/createAccountButton/enterEmailButton";
const Banner = () => {
    const history = useHistory();
    const [userData, setUserData] = useState(null);
    const { user } = useUserContext();


    const bannerStyle = {
        background: user && user.backgroundColor ? `${user.backgroundColor}` : `${getRandomColor()}`,
        display: 'flex',
        backgroundSize: 'cover',
        flexDirection: 'row',
        width: '100%',
        margin: '0 !important',
        padding: '0 !important',

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
            <h1 style={{marginLeft: '20px'}} className="title">MyFace</h1>
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
                    <LoginButton className="button-common"/>
                    <EnterEmailButton className="button-common"/>
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