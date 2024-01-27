import React, {useEffect, useState} from 'react';
import {useUserContext} from '../../usercontext/UserContext';
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


  return (
      <div style={{ }}>
        <h2 style={{ textAlign: 'center', fontSize: '30px' }}>Friends</h2>
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
        ) : filteredFriends.length > 0 ? (
            <ul>
              {filteredFriends.map((friend, index) => (
                  <li key={friend.id} className="user-item" style={{ ...listItemStyle, backgroundColor: index % 2 === 0 ? 'lightgray' : 'whitesmoke' }}>
                    <div style={{ fontSize:'40px', marginLeft:'10px' }}>@{friend.username} - {friend.firstname} {friend.lastname}</div>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                      <DeleteFriendButton removeFriend={() => removeFriend(friend.username)} />
                      <div style={{marginRight:'5px', marginLeft: '25px'}}>
                      <ProfilePictureComponent userid={friend.appUserID} style={{ width: '150px', height: '150px' }}/>
                      </div>
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