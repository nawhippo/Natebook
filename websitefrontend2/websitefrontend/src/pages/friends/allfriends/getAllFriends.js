import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllFriends = () => {
  const { user } = useUserContext();
  const [allFriendsData, setAllFriendsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log(user);
    if (user) {
      fetch(`/api/friends/${user.appUserID}/allFriends`)
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

  return (
    <div>
      <h1>All Friends</h1>
      {allFriendsData.length > 0 ? (
        <ul>
          {allFriendsData.map(friend => (
            <li key={friend.id}>
              <h2>{friend.firstname}</h2>
              <h2>{friend.lastname}</h2>
              <p>{friend.email}</p>
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