import React, {useEffect, useState} from 'react';
import {useUserContext} from '../../usercontext/UserContext';
import AcceptFriendRequestButton from "../../../buttonComponents/acceptFriendRequestButton/AcceptFriendRequestButton"
import DeclineFriendRequestButton from "../../../buttonComponents/declineFriendRequestButton/declineFriendRequestButton";
import {fetchWithJWT} from "../../../utility/fetchInterceptor";
import ProfilePictureComponent from "../../../buttonComponents/ProfilePictureComponent";
import SearchIcon from '@mui/icons-material/Search';
import {getRandomColor} from "../../../FunSFX/randomColorGenerator";
const GetAllFriendRequests = () => {
    const {user} = useUserContext();
    const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [shouldFetchData, setShouldFetchData] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    useEffect(() => {
        if (user && shouldFetchData) {
            fetchWithJWT(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
                .then(response => {
                    return response.json();
                })
                .then(data => {
                    setAllFriendRequestsData(data);
                })
                .catch(error => {
                    setError(error.message);
                })
                .finally(() => {
                    setIsLoading(false);
                    setShouldFetchData(false);
                });
        }
    }, [user, shouldFetchData]);

    const handleSearch = () => {

        return allFriendRequestsData.filter(friend =>
            friend.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
            friend.firstname.toLowerCase().includes(searchTerm.toLowerCase()) ||
            friend.lastname.toLowerCase().includes(searchTerm.toLowerCase())
        );
    };

    const filteredFriendRequests = searchTerm ? handleSearch() : allFriendRequestsData;
    return (
        <div style={{ marginRight: "30px", marginLeft: "30px" }}>
            <h2 style={{ textAlign: 'center', fontSize: '30px' }}>Friend Requests</h2>
            <div className="search-bar-container">
                <input
                    className="search-input"
                    type="text"
                    placeholder="Search by name or username"
                    value={searchTerm}
                    onChange={e => setSearchTerm(e.target.value)}
                />
                <button className="search-button" style={{ backgroundColor: user?.backgroundColor || getRandomColor(), color: '#FFFFFF' }} onClick={handleSearch}>
                    <SearchIcon />
                </button>
            </div>
            {isLoading ? (
                <div>Loading...</div>
            ) : error ? (
                <div>Error: {error}</div>
            ) : (
                <ul>
                    {filteredFriendRequests && filteredFriendRequests.length > 0 ? (
                        filteredFriendRequests.map((friend, index) => (
                            <li key={friend.appUserID} className="friend-request-item" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '10px', padding: '10px', backgroundColor: index % 2 === 0 ? 'lightgray' : 'whitesmoke', border: '3px solid black', borderRadius: '40px' }}>
                                <div>
                                    <div style={{ fontSize: '40px' }}>@{friend.username} - {friend.firstname} {friend.lastname}</div>
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <div style={{ marginRight: '30px' }}>
                                        <AcceptFriendRequestButton friendId={friend.appUserID} />
                                        <DeclineFriendRequestButton friendId={friend.appUserID} />
                                    </div>
                                    <ProfilePictureComponent style={{ width: '150px', height: '150px' }} userid={friend.appUserID} />
                                </div>
                            </li>
                        ))
                    ) : (
                        <p>No friend requests found.</p>
                    )}
                </ul>
            )}
        </div>
    );
};
export default GetAllFriendRequests;