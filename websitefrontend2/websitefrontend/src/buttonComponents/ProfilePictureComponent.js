import React, { useEffect, useState } from 'react';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import { Link } from 'react-router-dom';
import { useUserContext } from "../pages/usercontext/UserContext";
import { fetchWithJWT } from "../utility/fetchInterceptor";
import {getRandomColor} from "../FunSFX/randomColorGenerator";

const ProfilePictureComponent = ({ userid, className, style }) => {
    console.log('APPUSERID PASSED INTO PROFILE PIC COMPONENT:', userid);
    const [profile, setProfile] = useState(null);
    const [initials, setInitials] = useState("");
    const appUser = useUserContext();
    const [initialsError, setInitialsError] = useState(false);
    useEffect(() => {
        console.log('useEffect triggered for userid:', userid);

        if (!userid) {
            console.log('No userid provided, resetting profile and initials');
            setProfile(null);
            setInitials("");
            return;
        }

        fetch(`/api/user/${userid}`)
            .then(response => response.json())
            .then(userData => {
                console.log('Fetched user data:', userData);
                if (userData && userData.firstname && userData.lastname) {
                    const userInitials = `${userData.firstname[0].toUpperCase()} ${userData.lastname[0].toUpperCase()}`;
                    console.log('Setting initials:', userInitials);
                    setInitials(userInitials);
                    setInitialsError(false);
                } else {
                    throw new Error('User data is incomplete');
                }
            })
            .catch(error => {
                console.error('Error fetching user data:', error);
                setInitialsError(true);
            });

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

        console.log('Beginning userid fetch for profile pic component:', userid);
        fetchWithJWT(`/api/user/${userid}/getProfile`)
            .then(response => response.json())
            .then(userData => {
                console.log('User data from fetchWithJWT:', userData);
                if (userData && userData.firstname && userData.lastname) {
                    const userInitials = `${userData.firstname[0].toUpperCase()} ${userData.lastname[0].toUpperCase()}`;
                    console.log('Initials from fetchWithJWT:', userInitials);
                    setInitials(userInitials);
                }
            })
            .catch(error => console.error('Error fetching user data with JWT:', error));

    }, [userid]);


    const customStyle = {
        ...style,
        width: style?.width || '70px',
        height: style?.height || '70px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: '50%',
        backgroundColor: 'gray',
        color: getRandomColor(),
        position: 'relative',
    };

    const circleStyle = {
        ...customStyle,
        backgroundColor: '#c0c0c0',
    };

    const calculateFontSize = () => {
        const circleSize = parseInt(customStyle.width, 10);
        return circleSize * 0.4 + 'px';
    };


    const initialsStyle = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        width: '100%',
        textAlign: 'center',
        transform: 'translate(-50%, -135%)',
        fontSize: calculateFontSize(),
        color: 'white',
        zIndex: 2,
    };


    const profileLink = appUser && userid !== appUser.appUserID ? `/UserProfile/${userid}` : '/Account';
    if (initialsError) {
        return <div>Error loading initials</div>;
    }
    return (
        <div>
            {userid ? (
                <Link to={profileLink}>
                    <div style={customStyle} className={`profile-picture ${className}`}>
                        {profile ? (
                            <img src={profile} alt="Profile" style={customStyle}/>
                        ) : (
                            <>
                                <div style={circleStyle}></div>
                                <p style={initialsStyle}>{initials}</p>
                            </>
                        )}
                    </div>
                </Link>
            ) : (
                <div style={customStyle} className={`profile-picture ${className}`}>
                    {profile ? (
                        <img src={profile} alt="Profile"/>
                    ) : (
                        <>
                            <div style={circleStyle}></div>
                            <p style={initialsStyle}>{initials}</p>
                        </>
                    )}
                </div>
            )}
        </div>
    );
};
export default ProfilePictureComponent;