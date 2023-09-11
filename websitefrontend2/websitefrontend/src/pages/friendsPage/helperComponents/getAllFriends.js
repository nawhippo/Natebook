import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import { useHistory } from 'react-router-dom';
import CreateMessageForm from '../../../buttonComponents/createMessageButton/createMessageButton';
import deleteFriendButton from '../../../buttonComponents/deleteFriendButton/deleteFriendButton';
const GetAllFriends = () => {
  const { user } = useUserContext();
  const [allFriendsData, setAllFriendsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [isTabOpen, setIsTabOpen] = useState(false); 
  const history = useHistory();

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

  const filteredFriends = searchTerm
    ? allFriendsData.filter(friend =>
        friend.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        friend.firstname.toLowerCase().includes(searchTerm.toLowerCase()) ||
        friend.lastname.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : allFriendsData;


  const toggleTab = () => {
    setIsTabOpen(!isTabOpen);
  };

  return (
    <div>
      <button onClick={toggleTab}>
        {isTabOpen ? "Hide All Friends" : "Show All Friends"}
      </button>

      {isTabOpen && (
        <div>
          <h1>All Friends</h1>
          <input
            type="text"
            placeholder="Search by name or username"
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
          />
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
                  <deleteFriendButton username={friend.username} removeFriend={removeFriend} />
                  <CreateMessageForm recipient={friend.username} />
                </li>
              ))}
            </ul>
          ) : (
            <p>No friends found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default GetAllFriends;