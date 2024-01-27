import React, { useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import { fetchWithJWT } from '../../utility/fetchInterceptor';
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import FollowTheSignsIcon from '@mui/icons-material/FollowTheSigns';
const FollowButton = ({ followedId }) => {
    const [error, setError] = useState('');
    const { user } = useUserContext();
    const userid = user.appUserID;

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
        padding: '10px',
        borderRadius: '15px',
        transform: 'TranslateY(10px)',
    };


    const handleButtonClick = async () => {
        const url = '/api/follow';
        const params = new URLSearchParams({
            userId: userid,
            followedId: followedId
        });

        try {
            const response = await fetchWithJWT(`${url}?${params.toString()}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            });

            if (response.ok) {

                console.log(`${userid} is now following ${followedId}`);
            } else {
                const errorText = await response.text();
                setError(`Failed to follow: ${errorText}`);
            }
        } catch (error) {
            setError(`Error sending message: ${error.message}`);
        }
    };

    return (
        <div>
            <FollowTheSignsIcon style={buttonStyle} onClick={handleButtonClick}>
                Follow
            </FollowTheSignsIcon>
            {error && <p>Error following user: {error}</p>}
        </div>
    );
};

export default FollowButton;