import React, { useState } from 'react';

const Message = ({ message, user }) => {
  const [showReplies, setShowReplies] = useState(false);

  const toggleReplies = () => {
    setShowReplies(!showReplies);
  };

  return (
    <div>
      <p>{message.title}</p>
      <p>{message.content}</p>
      <p>Sender: {message.incoming ? user.username : 'You'}</p>
      {message.recipients && (
        <p>Recipients: {message.recipients.join(', ')}</p>
      )}
      <button onClick={toggleReplies}>Reply</button>
      {showReplies && message.childMessages && (
        <div>
          <h4>Replies:</h4>
          {message.childMessages.map((reply, index) => (
            <div key={index}>
              <p>{reply.content}</p>
              <p>Sender: {reply.incoming ? reply.sender.username : 'You'}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Message;