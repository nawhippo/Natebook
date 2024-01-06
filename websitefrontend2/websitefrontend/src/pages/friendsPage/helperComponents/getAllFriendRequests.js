import React, {useEffect, useState} from 'react';
import {useUserContext} from '../../usercontext/UserContext';
import AcceptFriendRequestButton from "../../../buttonComponents/acceptFriendRequestButton/AcceptFriendRequestButton"
import DeclineFriendRequestButton from "../../../buttonComponents/declineFriendRequestButton/declineFriendRequestButton";
import {fetchWithJWT} from "../../../utility/fetchInterceptor";

const GetAllFriendRequests = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [shouldFetchData, setShouldFetchData] = useState(true);

  useEffect(() => {
    if (user && shouldFetchData) {
      fetchWithJWT(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
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
            setShouldFetchData(false);
          });
    }
  }, [user, shouldFetchData]);

  return (
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
                    <h2>{friend.firstname} {friend.lastname}</h2>
                    <p>{friend.email}</p>
                    <AcceptFriendRequestButton friendId={friend.id}
                                               triggerFetch={() => setShouldFetchData(true)} />
                    <DeclineFriendRequestButton friendId={friend.id}
                                                triggerFetch={() => setShouldFetchData(true)} />
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