import React, { useState, useEffect } from 'react';

const GetAllFriends = ({ userId }) => { // Changed function name to start with uppercase
  const [allFriendsData, setAllFriendsData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/${userId}/friends`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setAllFriendsData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchData();
  }, [userId]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <h1>All Friends </h1>

      {allFriendsData && allFriendsData.length > 0 ? (
        allFriendsData.map((friend) => ( // Changed aboutData to allFriendsData
          <div key={friend.id} className="friend-card">
            <h2>{friend.name}</h2>
            <p>{friend.email}</p>
          </div>
        ))
      ) : (
        <p>No friends found.</p>
      )}
    </div>
  );
};

export default GetAllFriends;