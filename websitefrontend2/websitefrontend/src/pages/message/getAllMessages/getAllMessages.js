import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../login/UserContext';

const GetAllMessages = () => {
  const { user } = useUserContext();
  const [searchText, setSearchText] = useState('');
  const [allMessages, setAllMessages] = useState([]);
  const [displayedMessages, setDisplayedMessages] = useState([]);
  const [expandedMessages, setExpandedMessages] = useState({});

  const handleSearchTextChange = (event) => {
    const searchText = event.target.value;
    setSearchText(searchText);
    filterMessages(searchText);
  };

  const filterMessages = (searchText) => {
    const filteredMessages = allMessages.filter((message) =>
      message.content.toLowerCase().includes(searchText.toLowerCase())
    );
    setDisplayedMessages(filteredMessages);
  };

  const toggleChildMessages = (messageId) => {
    setExpandedMessages({
      ...expandedMessages,
      [messageId]: !expandedMessages[messageId],
    });
  };

  useEffect(() => {
    fetch(`/api/message/${user.appUserID}/allMessages`)
      .then((response) => response.json())
      .then((data) => {
        setAllMessages(data);
        setDisplayedMessages(data);
      })
      .catch((error) => console.error('Error fetching messages:', error));
  }, [user]);

  return (
    <div>
      <h1>Messages Page</h1>
      <input
        type="text"
        value={searchText}
        onChange={handleSearchTextChange}
        placeholder="Search Messages"
      />
      <div>
        <h2>All Messages:</h2>
        {displayedMessages.length > 0 ? (
          <ul>
            {displayedMessages.map((message) => (
              <li key={message.id}>
                <p>{message.content}</p>
                {message.incoming ? <p>Sender: {user.username}</p> : <p>Sender: You</p>}
                {message.recipients && message.recipients.length > 0 && (
                  <p>
                    Recipients: {message.recipients.join(', ')}
                  </p>
                )}
                {message.childMessages && message.childMessages.length > 0 && (
                  <div>
                    <button onClick={() => toggleChildMessages(message.id)}>
                      {expandedMessages[message.id] ? 'Hide' : 'Show'} Child Messages
                    </button>
                    {expandedMessages[message.id] && (
                      <ul>
                        {message.childMessages.map((childMessage) => (
                          <li key={childMessage.id}>
                            <p>{childMessage.content}</p>
                            {childMessage.isIncoming ? <p>Sender: {user.username}</p> : <p>Sender: You</p>}
                            {childMessage.recipients && childMessage.recipients.length > 0 && (
                              <p>
                                Recipients: {childMessage.recipients.join(', ')}
                              </p>
                            )}
                          </li>
                        ))}
                      </ul>
                    )}
                  </div>
                )}
              </li>
            ))}
          </ul>
        ) : (
          <p>No messages to display.</p>
        )}
      </div>
    </div>
  );
};

export default GetAllMessages;