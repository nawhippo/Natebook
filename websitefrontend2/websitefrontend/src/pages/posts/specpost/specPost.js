import React, { useState, useEffect } from 'react';

const MessageById = ({ userId, messageId }) => {
  const [messageData, setMessageData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMessage = async () => {
      try {
        const response = await fetch(`/api/${userId}/${messageId}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setMessageData(data);
        setIsLoading(false);
      } catch (error) {
        setError(error.message);
        setIsLoading(false);
      }
    };

    fetchMessage();
  }, [userId, messageId]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      {messageData ? (
        <div>
          <h1>Message Details</h1>
          <p>ID: {messageData.id}</p>
          <p>Title: {messageData.title}</p>
          <p>Description: {messageData.description}</p>
          {/* Additional message properties can be displayed here */}
        </div>
      ) : (
        <p>Message not found.</p>
      )}
    </div>
  );
};

export default MessageById;