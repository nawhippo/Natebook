import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllFriendFriendRequests = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log(user);
    if (user) {
      fetch(`/api/${user.userId}/getFriendRequests`)
        .then(response => {
          console.log('API Response:', response); 
          return response.json();
        })
        .then(data => {
          console.log('API Data:', data);
          setAllFriendsData(data); 
        })
        .catch(error => {
          console.error("JSON PARSING ERROR or FETCH ERROR:", error);
          setError(error.message);
        })
        .finally(() => {
          setIsLoading(false);
        });
    }
  }, [user]);
  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  const handleClick = () =>{
    const data = fetch(`/api/${user.AppUserId}/acceptFriendRequest/${friend.username}`);
  };

  const handleCLickTwo = () =>{
    const data = fetch(`/api/${user.AppUserId}/declineFriendRequest/${friend.username}`);
  };

  }
  return (
    <div>
      <h1>Friend Requests</h1>
      {allFriendRequestsData.length > 0 ? Request(
        <ul>
          {allFriendRequestsData.map(friend => Request (
            <li key={friend.id}>
              <h2>{friend.firstname}</h2>
              <p>{friend.lastname}</p>
              <p>{friend.email}</p>
              <button OnClick={handleClick}>Add Friend</button>
              <button OnClick={handleClick}>Decline Friend</button>
            </li>
          ))}
        </ul>
      ) : (
        <p>No friends found.</p>
      )}
    </div>
  );

export default GetAllFriends;