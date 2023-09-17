import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../usercontext/UserContext';
import AcceptFriendRequestButton from "../../../buttonComponents/acceptFriendRequestButton/AcceptFriendRequestButton"
import DeclineFriendRequestButton from "../../../buttonComponents/declineFriendRequestButton/declineFriendRequestButton";
const GetAllFriendRequests = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isTabOpen, setIsTabOpen] = useState(false);
  const [shouldFetchData, setShouldFetchData] = useState(true);
  useEffect(() => {
    if (user && shouldFetchData) {
      fetch(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
        .then(response => {
          return response.json();
        })
        .then(data => {
          setAllFriendRequestsData(data);
        })
        .catch(error => {
          setError(error.message);
        })
        .finally(() => {
          setIsLoading(false);
        });
        setShouldFetchData(false);
    }
  }, [user, shouldFetchData]);

  const toggleTab = () => {
    setIsTabOpen(!isTabOpen);
  };

  return (
    <div>
      <button onClick={toggleTab}>
        {isTabOpen ? "Hide Friend Requests" : "Show Friend Requests"}
      </button>

      {isTabOpen && (
        <div>
          <h1>Friend Requests</h1>
          {isLoading ? (
            <div>Loading...</div>
          ) : error ? (
            <div>Error: {error}</div>
          ) : allFriendRequestsData.length > 0 ? (
            <ul>
              {allFriendRequestsData.map(friend => (
                <li key={friend.id}>
                  <h2>{friend.firstname}</h2>
                  <p>{friend.lastname}</p>
                  <p>{friend.email}</p>
                  <AcceptFriendRequestButton friendId = {friend.id}
                    triggerFetch={() => setShouldFetchData(true)} ></AcceptFriendRequestButton>
                  <DeclineFriendRequestButton friendId = {friend.id} 
                   triggerFetch={() => setShouldFetchData(true)} ></DeclineFriendRequestButton>
                </li>
              ))}
            </ul>
          ) : (
            <p>No friend requests found.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default GetAllFriendRequests;