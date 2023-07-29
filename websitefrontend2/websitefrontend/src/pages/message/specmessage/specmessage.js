import React, { useEffect, useState } from 'react';

const specMessage = ({userId, messageId}) => {
  const [message, setMessage] = useState(null);

  useEffect(() => {
    fetch(`/${userId}/${messageId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => setMessage(data))
      .catch(error => console.error('Error fetching message:', error));
  }, []);

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