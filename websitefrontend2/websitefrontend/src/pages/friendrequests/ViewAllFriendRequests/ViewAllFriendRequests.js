import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';

//add accept/decline buttons
const GetAllFriendRequests = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log(user);
    if (user) {
      fetch(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
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