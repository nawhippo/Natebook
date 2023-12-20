import React, { useState, useEffect } from 'react';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { Link } from 'react-router-dom';
import '../global.css';
import { useUserContext} from "../pages/usercontext/UserContext";

const ProfilePictureComponent = ({ userid, user }) => {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState('');
    const appUser = useUserContext();
    useEffect(() => {
        if (!userid) {
            setProfile(null);
            return;
        }

        fetch(`/api/account/${userid}/getProfilePicture`)
            .then(response => {
                if (!response.ok) throw Error('Network response was not ok');
                if (response.headers.get('content-length') === '0') {
                    throw Error('Empty response');
                }
                return response.json();
            })
            .then(compressedImage => {
                if (compressedImage && compressedImage.base64EncodedImage) {
                    const imageSrc = `data:image/${compressedImage.format};base64,${compressedImage.base64EncodedImage}`;
                    setProfile(imageSrc);
                } else {
                    setProfile(null);
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                setError(error);
                setProfile(null);
            });
    }, [userid]);

    return (
        <div>
            {userid && (
                appUser && userid != appUser.appUserID ? (
                    <Link to={`/UserProfile/${userid}`}>
                        {profile ? (
                            <img src={profile} className="profile-picture" alt="Profile"/>
                        ) : (
                            <AccountCircleIcon sx={{color: 'white'}} className="profile-picture"/>
                        )}
                    </Link>
                ) : (
                    // Default to /Account if user context is not provided or doesn't match
                    <Link to="/Account">
                        {profile ? (
                            <img src={profile} className="profile-picture" alt="Profile"/>
                        ) : (
                            <AccountCircleIcon sx={{color: 'white'}} className="profile-picture"/>
                        )}
                    </Link>
                )
            )}
            {!userid && (
                <>
                    {profile ? (
                        <img src={profile} className="profile-picture" alt="Profile"/>
                    ) : (
                        <AccountCircleIcon sx={{color: 'white'}} className="profile-picture"/>
                    )}
                </>
            )}
        </div>
    );
};

export default ProfilePictureComponent;