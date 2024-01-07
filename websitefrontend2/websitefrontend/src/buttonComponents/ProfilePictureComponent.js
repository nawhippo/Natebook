import React, {useEffect, useState} from 'react';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import {Link} from 'react-router-dom';
import '../global.css';
import {useUserContext} from "../pages/usercontext/UserContext";
import {fetchWithJWT} from "../utility/fetchInterceptor";

const ProfilePictureComponent = ({ userid }) => {
    const [profile, setProfile] = useState(null);
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
                setProfile(null);
            });
    }, [userid]);

    const profileLink = appUser && userid !== appUser.appUserID ? `/UserProfile/${userid}` : '/Account';

    return (
        <div>
            {userid && (
                <Link to={profileLink}>
                    {profile ? (
                        <img src={profile} className="profile-picture" alt="Profile" />
                    ) : (
                        <AccountCircleIcon sx={{ color: 'gray', fontSize: 50 }} className="profile-picture" />
                    )}
                </Link>
            )}
            {!userid && (
                <>
                    {profile ? (
                        <img src={profile} className="profile-picture" alt="Profile" />
                    ) : (
                        <AccountCircleIcon sx={{ color: 'gray', fontSize: 50 }} className="profile-picture" />
                    )}
                </>
            )}
        </div>
    );
};

export default ProfilePictureComponent;