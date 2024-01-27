import React, { useEffect, useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../../pages/usercontext/UserContext";
import { fetchWithJWT } from '../../utility/fetchInterceptor';
import UserStatus from "../../buttonComponents/getStatus/getStatus";
import { getRandomColor } from "../../FunSFX/randomColorGenerator";

import './getAllFollowing.css';
const GetAllFollowing = ({ userId, setFollowingCount }) => {
    const [followingData, setFollowingData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const userContext = useUserContext();

    useEffect(() => {
        console.log('fetching following for' + userId);
        const fetchData = async () => {
            try {
                const response = await fetch(`/api/${userId}/following`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setFollowingData(data);
                setFollowingCount(data.length);
                setIsLoading(false);
            } catch (error) {
                setError(error.message);
                setIsLoading(false);
            }
        };

        fetchData();
    }, [userId, setFollowingCount]);

    const filteredFollowing = searchTerm
        ? followingData.filter(following =>
            following.username.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
            following.firstname.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
            following.lastname.toLowerCase().includes(searchTerm.toLowerCase().trim())
        )
        : followingData;

    const handleInputChange = (event) => setSearchTerm(event.target.value);

    const buttonStyle = {
        backgroundColor: userContext?.user?.backgroundColor || getRandomColor(),
        color: '#FFFFFF',
        height: '44.5px'
    };

    const getOnlineStatusStyle = (isOnline) => ({
        transform: 'translateY(60px) translateX(30px)',
        display: 'inline-block',
        width: '50px',
        height: '50px',
        borderRadius: '50%',
        zIndex: '1000',
        backgroundColor: isOnline ? 'green' : 'red',
    });

    return (
        <div>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }}>Following</h2>
            <div className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    value={searchTerm}
                    placeholder="Search following..."
                    onChange={handleInputChange}
                />
                <button className="search-button" type="submit" onClick={() => setSearchTerm(searchTerm)} style={buttonStyle}>
                    <SearchIcon />
                </button>
            </div>
            {isLoading ? (
                <div>Loading...</div>
            ) : error ? (
                <div>Error: {error}</div>
            ) : (
                <ul>
                    {filteredFollowing && filteredFollowing.length > 0 ? (
                        filteredFollowing.map(following => (
                            <li key={following.appUserID} className="following-item">
                <span className="following-name">
                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                          <div style={{fontSize:"30px"}}>
                            @{following.username} - {following.firstname} {following.lastname}
                              <UserStatus appUserId={following.appUserID}/>
                              {following.online}
                    </div>
                  </div>
                </span>
                                <div className="button-group">
                                    <ProfilePictureComponent userid={following.appUserID} style={{ width: '150px', height: '150px' }} />
                                </div>
                            </li>
                        ))
                    ) : (
                        <p>No followers found.</p>
                    )}
                </ul>
            )}
        </div>
    );
};

export default GetAllFollowing;