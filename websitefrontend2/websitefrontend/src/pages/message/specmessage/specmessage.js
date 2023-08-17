import React, { useEffect, useState } from 'react';
import { useUserContext } from './UserContext'; // Import the useUserContext hook

const specMessage = ({ messageId }) => {
  const { userId } = useUserContext(); 
  const [message, setMessage] = useState(null);

  useEffect(() => {
    fetch(`/api/message/${userId}/${messageId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setMessage(data))
      .catch(error => console.error('Error fetching message:', error));
  }, [userId, messageId]); //Include userId and messageId in the dependency array

  return (
    <div>
      {message ? (
        <div>
          <h2>Message Content:</h2>
          <p>{message.content}</p>
          <p>Sender: {message.sender.name}</p>
          <p>Recipients: {message.recipients.map(user => user.name).join(', ')}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default specMessage;