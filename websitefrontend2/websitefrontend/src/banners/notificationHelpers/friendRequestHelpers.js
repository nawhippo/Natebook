import React, { useState, useEffect } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const FriendReqCounter = () => {
  const { user } = useUserContext();
  const [allFriendRequestsData, setAllFriendRequestsData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFriendRequests = () => {
      if (user) {
        fetch(`/api/friendreqs/${user.appUserID}/getFriendRequests`)
          .then(response => response.json())
          .then(data => setAllFriendRequestsData(data))
          .catch(error => setError(error.message))
          .finally(() => setIsLoading(false));
      }
    };

    //init fetch
    fetchFriendRequests();

    //poll rate is 2 mins
    const intervalId = setInterval(fetchFriendRequests, 120000);
    //clean up
    return () => clearInterval(intervalId);

  }, [user]);

  return (
    <div>
      {allFriendRequestsData.length > 0 && <p>Number of Friend Requests: {allFriendRequestsData.length}</p>}
    </div>
  );
};

export default FriendReqCounter;