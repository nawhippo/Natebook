import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../usercontext/UserContext';

const GetAllMessages = () => {
  const { user } = useUserContext();
  const [showIncoming, setShowIncoming] = useState(true);

  const [searchText, setSearchText] = useState('');
  const [allMessages, setAllMessages] = useState([]);
  const [displayedMessages, setDisplayedMessages] = useState([]);


  const [expandedMessages, setExpandedMessages] = useState({});
  const [replyContent, setReplyContent] = useState({});


  const handleSearchTextChange = (event) => {
    const newSearchText = event.target.value;
    setSearchText(newSearchText);
    filterMessages(newSearchText);
  };

  const filterMessages = (textToSearch) => {
    const filteredMessages = allMessages.filter((message) =>
      message.content.toLowerCase().includes(textToSearch.toLowerCase())
      (message.title && message.title.toLowerCase().includes(textToSearch.toLowerCase())) ||
      (message.recipients && message.recipients.join(', ').toLowerCase().includes(textToSearch.toLowerCase()))
    );
    setDisplayedMessages(filteredMessages);
  };

  const toggleChildMessages = (messageId) => {
    setExpandedMessages({
      ...expandedMessages,
      [messageId]: !expandedMessages[messageId],
    });
  };



  const onReplyClick = (message) => {
    const content = replyContent[message.id] || '';
    // Add recipient names if needed
    const recipientNames = message.recipients;
    
    fetch(`/api/message/${user.appUserID}/${message.id}/replyMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content, recipientNames }),
    })
    .then((response) => response.json())
    .then((data) => {
      setAllMessages(data);
      setDisplayedMessages(data);
    })
    .catch((error) => console.error('Error fetching messages: ', error));
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


  const incomingMessages = displayedMessages.filter((message) => message.incoming);
  const sentMessages = displayedMessages.filter((message) => !message.incoming);
  console.log("Incoming Messages: ", incomingMessages);
  console.log("Sent Messages: ", sentMessages);


  return (
    <div>
      <h1>Messages Page</h1>
      <input
        type="text"
        value={searchText}
        onChange={handleSearchTextChange}
        placeholder="Search Messages"
      />
      
      <button onClick={() => setShowIncoming(!showIncoming)}> 
        {showIncoming ? 'Show Sent Messages' : 'Show Incoming Messages'} 
      </button>

      <div>
        <h2>{showIncoming ? 'Incoming Messages:' : 'Sent Messages:'}</h2>
        
        {showIncoming ? renderMessageList(incomingMessages) : renderMessageList(sentMessages)}

      </div>
    </div>
  );

  function renderMessageList(messages) {
    return messages.length > 0 ? (
      <ul>
        {messages.map((message) => (
          <li key={message.id}>
            <p>{message.title}</p>
            <p>{message.content}</p>
            <p>Sender: {message.incoming ? user.username : 'You'}</p>
            {message.recipients && (
              <p>Recipients: {message.recipients.join(', ')}</p>
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
                        <p>Sender: {childMessage.incoming ? user.username : 'You'}</p>
                        {childMessage.recipients && (
                          <p>Recipients: {childMessage.recipients.join(', ')}</p>
                        )}
                        <input
                          type="text"
                          value={replyContent[message.id] || ''}
                          onChange={e => setReplyContent({ ...replyContent, [message.id]: e.target.value })}
                          placeholder="Type your reply here"
                        />
                        <button onClick={() => onReplyClick(message)}>Reply</button>
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
    );
  }
};

export default GetAllMessages;