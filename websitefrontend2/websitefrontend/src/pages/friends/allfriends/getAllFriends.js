import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../login/UserContext';
import { useHistory } from 'react-router-dom'

const GetAllFriends = () => {
  const { user } = useUserContext();
  const [allFriendsData, setAllFriendsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const history = useHistory();

   const handleButtonClick = (recipientUsername) =>{
     history.push({
       pathname: '/createMessage',
       state: { recipient: recipientUsername }
     });
   };



  useEffect(() => {
    if (user) {
      fetch(`/api/friends/${user.appUserID}/getAllFriends`)
        .then(response => response.json())
        .then(data => setAllFriendsData(data))
        .catch(error => {
          console.error("Error:", error);
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
              <h2>{friend.username} - {friend.firstname} {friend.lastname}</h2>
              <p>{friend.email}</p>
              <button onClick={()=> handleButtonClick(friend.username)}>Message User</button>
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