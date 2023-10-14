import React, { useState, useEffect } from 'react';
import Message from '../../objects/message';
import { useUserContext } from '../../usercontext/UserContext';

const RecList = () => {
  const { user } = useUserContext();
  const userId = user.id; 
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    
    const fetchMessages = async () => {
      try {
        const response = await fetch(`/api/message/${user.appUserID}/receivedMessages`);
        if (response.ok) {
          const data = await response.json();
          setMessages(data);
        } else {
          console.error('Failed to fetch messages');
        }
      } catch (error) {
        console.error('Could not fetch messages:', error);
      }
    };

    fetchMessages();
  }, [userId]); 

  return (
    <div>
      <h2>Received Messages</h2>
      {messages.length === 0 ? (
        <p>No received messages.</p>
      ) : (
        messages.map((message, index) => (
          <Message key={index} message={message} user={userId} /> 
        ))
      )}
    </div>
  );
};

export default RecList;