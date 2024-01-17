import React, { useEffect, useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../../pages/usercontext/UserContext";
import { fetchWithJWT } from '../../utility/fetchInterceptor';
import UserStatus from "../../buttonComponents/getStatus/getStatus";
import { getRandomColor } from "../../FunSFX/randomColorGenerator";
import './getAllFollowers.css'
const GetAllFollowers = ({ userId, setFollowersCount }) => {
    const [followersData, setFollowersData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const { user } = useUserContext();

    useEffect(() => {
        console.log('fetching followers for ' + userId);
        const fetchData = async () => {
            try {
                const response = await fetch(`/api/${userId}/followers`);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                const data = await response.json();
                setFollowersData(data);
                setFollowersCount(data.length);
                setIsLoading(false);
            } catch (error) {
                setError(error.message);
                setIsLoading(false);
            }
        };

        fetchData();
    }, [userId, setFollowersCount]);

    const filteredFollowers = searchTerm
        ? followersData.filter(follower =>
            follower.username.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
            follower.firstname.toLowerCase().includes(searchTerm.toLowerCase().trim()) ||
            follower.lastname.toLowerCase().includes(searchTerm.toLowerCase().trim())
        )
        : followersData;

    const handleInputChange = (event) => setSearchTerm(event.target.value);

    const buttonStyle = {
        backgroundColor: user?.backgroundColor || getRandomColor(),
        color: '#FFFFFF',
    };

    const getOnlineStatusStyle = (isOnline) => ({
        display: 'inline-block',
        transform: "translateX(50px) translateY(50px)",
        width: '50px',
        height: '50px',
        borderRadius: '50%',
        zIndex: '1000',
        backgroundColor: isOnline ? 'green' : 'red',
    });

    return (
        <div>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }}>Followers</h2>
            <div className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    value={searchTerm}
                    placeholder="Search followers..."
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
                    {filteredFollowers && filteredFollowers.length > 0 ? (
                        filteredFollowers.map(follower => (
                            <li key={follower.appUserID} className="following-item">
                                   <span className="follower-name" style={{ fontSize: "30px" }}>
            @{follower.username} - {follower.firstname} {follower.lastname}
                                       <UserStatus appUserId={follower.appUserID} />
        </span>
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <ProfilePictureComponent
                                        userid={follower.appUserID}
                                        style={{ width: '150px', height: '150px' }}
                                    />

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

export default GetAllFollowers;