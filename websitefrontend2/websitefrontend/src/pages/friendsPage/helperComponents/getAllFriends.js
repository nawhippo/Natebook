import React, {useEffect, useState} from 'react';
import {useUserContext} from '../../usercontext/UserContext';
import CreateMessageForm from '../../../buttonComponents/createMessageButton/createMessageButton';
import DeleteFriendButton from '../../../buttonComponents/deleteFriendButton/deleteFriendButton';
import SearchIcon from '@mui/icons-material/Search';
import ProfilePictureComponent from "../../../buttonComponents/ProfilePictureComponent";
import {fetchWithJWT} from "../../../utility/fetchInterceptor";
import {getRandomColor} from "../../../FunSFX/randomColorGenerator";

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
          const response = await fetchWithJWT(`/api/friends/${user.appUserID}/getAllFriends`);
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

  const listItemStyle = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '10px 0',
  };


  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
  };

  const getOnlineStatusStyle = (isOnline) => ({
    transform: 'translateY(-90px) translateX(0px)',
    display: 'inline-block',
    width: '30px',
    height: '30px',
    borderRadius: '50%',
    zIndex: '1000',
    backgroundColor: isOnline ? 'green' : 'red',
  });

  const buttonContainerStyle = {
    display: 'flex',
    justifyContent: 'flex-end',
  };

  const searchBarContainerStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-start',
  };
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
          <button style={buttonStyle} className="search-button" onClick={handleSearch}>
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
                  <li key={friend.id} className="user-item">
                    <div className="user-name">
                      <ProfilePictureComponent userid={friend.appUserID}/>
                      <div className="user-details">
                        <h2>{friend.username} - {friend.firstname} {friend.lastname}</h2>
                      </div>
                      <div className="user-status">
                        <div style={getOnlineStatusStyle(friend.online)} />
                      </div>
                    </div>
                    <div className="button-group">
                      <DeleteFriendButton removeFriend={() => removeFriend(friend.username)} />
                    </div>
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