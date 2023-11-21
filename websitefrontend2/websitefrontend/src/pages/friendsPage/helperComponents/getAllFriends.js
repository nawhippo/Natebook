import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import CreateMessageForm from '../../../buttonComponents/createMessageButton/createMessageButton';
import DeleteFriendButton from '../../../buttonComponents/deleteFriendButton/deleteFriendButton';
import SearchIcon from '@mui/icons-material/Search';

const GetAllFriends = () => {
  const { user } = useUserContext();
  const [allFriendsData, setAllFriendsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const removeFriend = (username) => {
    setAllFriendsData(prevFriends => prevFriends.filter(friend => friend.username !== username));
  };

  useEffect(() => {
    const fetchData = async () => {
      if (user) {
        try {
          const response = await fetch(`/api/friends/${user.appUserID}/getAllFriends`);
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          const text = await response.text();
          if (text) {
            const data = JSON.parse(text);
            setAllFriendsData(data);
          }
          setIsLoading(false);
        } catch (error) {
          setError(error.message);
          setIsLoading(false);
        }
      }
    };
    fetchData();
  }, [user]);

  const handleSearch = () => {
    setAllFriendsData(prevFriends =>
        prevFriends.filter(friend =>
            friend.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
            friend.firstname.toLowerCase().includes(searchTerm.toLowerCase()) ||
            friend.lastname.toLowerCase().includes(searchTerm.toLowerCase())
        )
    );
  };

  const filteredFriends = searchTerm
      ? allFriendsData.filter(friend =>
          friend.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
          friend.firstname.toLowerCase().includes(searchTerm.toLowerCase()) ||
          friend.lastname.toLowerCase().includes(searchTerm.toLowerCase())
      )
      : allFriendsData;

  return (
      <div>
        <h1>Friends</h1>
        <div className="search-bar-container">
          <input
              className="search-input"
              type="text"
              placeholder="Search by name or username"
              value={searchTerm}
              onChange={e => setSearchTerm(e.target.value)}
          />
          <button className="search-button" onClick={handleSearch}>
            <SearchIcon />
          </button>
        </div>
        {isLoading ? (
            <div>Loading...</div>
        ) : error ? (
            <div>Error: {error}</div>
        ) : filteredFriends.length > 0 ? (
            <ul>
              {filteredFriends.map(friend => (
                  <li key={friend.id}>
                    <h2>{friend.username} - {friend.firstname} {friend.lastname}</h2>
                    <p>{friend.email}</p>
                    <p>{friend.isOnline ? 'Online' : 'Offline'}</p>
                    <DeleteFriendButton removeFriend={() => removeFriend(friend.username)} />
                    <CreateMessageForm recipient={friend.username} />
                  </li>
              ))}
            </ul>
        ) : (
            <p>No friends found.</p>
        )}
      </div>
  );
};

export default GetAllFriends;