import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../usercontext/UserContext'; 


const SpecFriend = ({ friendId }) => {
  const { userId } = useUserContext(); 
  const [friendData, setFriendData] = useState(null);

  useEffect(() => {
    const fetchFriendData = async () => {
      try {
        const response = await fetch(`/api/friends/${userId}/${friendId}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setFriendData(data);
      } catch (error) {
        console.error('Error fetching friend data:', error);
      }
    };

    fetchFriendData();
  }, [userId, friendId]);

  return (
    <div>
      {friendData ? (
        <div className="friend-card">
          <h2>{friendData.name}</h2>
          <p>{friendData.email}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default SpecFriend;