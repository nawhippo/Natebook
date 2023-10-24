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
     <p>Sender: {message.senderusername}</p>
      {message.recipients && (
        <p>Recipients: {message.recipients.join(', ')}</p>
      )}
      <button onClick={toggleReplies}>Replies</button>
      {showReplies && message.childMessages && (
        <div>
          <h4>Replies:</h4>
          {message.childMessages.map((reply, index) => (
            <div key={index}>
              <p>Sender: {reply.senderusername}</p>
              <p>{reply.content}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Message;