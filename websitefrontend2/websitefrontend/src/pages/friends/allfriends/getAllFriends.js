import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import { useHistory } from 'react-router-dom'
import SendMessageButton from '../../buttonComponents/sendMessageButton/createMessageButton';
const GetAllFriends = () => {
  const { user } = useUserContext();
  const [allFriendsData, setAllFriendsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
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

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>All Friends</h1>
      {allFriendsData.length > 0 ? (
        <ul>
          {allFriendsData.map(friend => (
            <li key={friend.id}>
              <h2>{friend.username} - {friend.firstname} {friend.lastname}</h2>
              <p>{friend.email}</p>
              <DeleteFriendButton username={friend.username} removeFriend={removeFriend} />
              <SendMessageButton recipient={friend.username} />
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