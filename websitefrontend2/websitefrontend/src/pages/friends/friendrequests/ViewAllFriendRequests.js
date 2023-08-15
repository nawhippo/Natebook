import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllFriendRequests = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log(user);
    if (user) {
      fetch(`/api/${user.appUserID}/getFriendRequests`)
        .then(response => {
          console.log('API Response:', response);
          return response.json();
        })
        .then(data => {
          console.log('API Data:', data);
          setAllFriendRequestsData(data);
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

  const handleClick = (friendUsername) => {
    fetch(`/api/${user.appUserID}/acceptFriendRequest/${friendUsername}`);
  };

  const handleClickTwo = (friendUsername) => {
    fetch(`/api/${user.appUserID}/declineFriendRequest/${friendUsername}`);
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>Friend Requests</h1>
      {allFriendRequestsData.length > 0 ? (
        <ul>
          {allFriendRequestsData.map(friend => (
            <li key={friend.id}>
              <h2>{friend.firstname}</h2>
              <p>{friend.lastname}</p>
              <p>{friend.email}</p>
              <button onClick={() => handleClick(friend.username)}>Add Friend</button>
              <button onClick={() => handleClickTwo(friend.username)}>Decline Friend</button>
            </li>
          ))}
        </ul>
      ) : (
        <p>No friend requests found.</p>
      )}
    </div>
  );
};

export default GetAllFriendRequests;