import React, { useState, useEffect } from 'react';
import Message from '../../objects/message';
import { useUserContext } from '../../usercontext/UserContext';

const GroupChats = () => {
  const { user } = useUserContext();
  const userId = user.id;
  const [groupedMessages, setGroupedMessages] = useState({});

  useEffect(() => {

    const fetchMessages = async () => {
      try {
        // Update the fetch URL to retrieve messages grouped by group chat IDs
        const response = await fetch(`/api/message/${user.appUserID}/messages`);
        if (response.ok) {
          const data = await response.json();
          setGroupedMessages(data);
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
        {Object.keys(groupedMessages).length === 0 ? (
            <p>No received messages.</p>
        ) : (
            Object.entries(groupedMessages).map(([chatId, messages]) => (
                <div key={chatId}>
                  <h3>Group Chat {chatId}</h3>
                  {messages.map((message, index) => (
                      <Message key={index} message={message} user={userId} />
                  ))}
                </div>
            ))
        )}
      </div>
  );
};

export default GroupChats;