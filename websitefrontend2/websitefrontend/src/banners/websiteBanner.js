import LoginButton from '../buttonComponents/loginButton/loginButton';
import LogoutButton from '../buttonComponents/logoutButton/logoutButton';
import CreateAccount from '../buttonComponents/createAccountButton/createAccountButton';
import FriendReqCounter from './notificationHelpers/friendRequestHelpers';
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import SettingsIcon from '@mui/icons-material/Settings';
import PeopleIcon from '@mui/icons-material/People';
import MessageIcon from '@mui/icons-material/Message';
import DynamicFeedIcon from '@mui/icons-material/DynamicFeed';
import EmojiEmotionsIcon from '@mui/icons-material/EmojiEmotions';
import './banner.css';
import { useEffect, useState } from "react";
import {useUserContext} from "../pages/usercontext/UserContext";
import Cookies from 'js-cookie';
const Banner = () => {
    const history = useHistory();
    const [userData, setUserData] = useState(null);
    const { user } = useUserContext();
    useEffect(() => {
        const cookieUserData = Cookies.get('userData'); // Get user data from cookie
        if (cookieUserData) {
            setUserData(JSON.parse(cookieUserData)); // Parse and set user data from cookie
        }
    }, [user]); // Empty dependency array to run only once on mount

    const handleLinkClick = (url) => {
        history.push(url);
    };

    return (
        <div className="banner">
            <h1 className="title">NateBook!</h1>
            <PeopleIcon className="button-common" onClick={() => handleLinkClick('/AllUsersPage')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            <DynamicFeedIcon className="button-common" onClick={() => handleLinkClick('/Feed')} style={{ width: '50px', height: 'auto', background: 'none' }} />
            <FriendReqCounter />
            {userData !== null ? (
                <div>
                    <MessageIcon className="button-common" onClick={() => handleLinkClick('/Messages')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <EmojiEmotionsIcon className="button-common" onClick={() => handleLinkClick('/Friends')} style={{ width: '50px', height: 'auto', background: 'none' }} />
                    <LogoutButton />
                    <SettingsIcon className="button-common" onClick={() => handleLinkClick('/Account')} style={{ width: '50px', height: 'auto', background: 'none' }} />
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