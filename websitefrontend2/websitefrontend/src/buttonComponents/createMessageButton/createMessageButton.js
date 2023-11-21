import React, { useEffect, useState } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
import './CreateMessageForm.css'; // Ensure this path is correct

const CreateMessageForm = ({ userId, defaultRecipientName }) => {
  const [showForm, setShowForm] = useState(false);
  const [content, setContent] = useState('');
  const [recipientNames, setRecipientNames] = useState('');
  const [title, setTitle] = useState('');

  const handleToggle = () => setShowForm(!showForm);

  const handleClose = () => setShowForm(false);

  const handleSubmit = () => {
    fetch(`/api/message/${userId}/sendMessage`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, content, recipientNames: recipientNames.split(',').map(name => name.trim()) }),
    })
        .then((response) => {
          if (response.ok) {
            return response.json();
          }
          throw new Error('Failed to send message');
        })
        .then((data) => {
          console.log('Message sent:', data);
          handleClose(); // Close form after sending message
        })
        .catch((error) => {
          console.error('Error:', error);
        });
  };

  useEffect(() => {
    if (defaultRecipientName){
      setRecipientNames(defaultRecipientName);
    }
  }, [defaultRecipientName]);

  return (
      <div>
        <button onClick={handleToggle} className="form-button">Create Message</button>
        <br></br>
        {showForm && (
            <div className="overlay">
              <div className="form-container">
                <button onClick={handleClose} className="closeButton">X</button> {/* Close button */}
                <input
                    className="form-field"
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Title"
                    required={true}
                />
                <br></br>
                <input
                    className="form-field"
                    type="text"
                    value={recipientNames}
                    onChange={(e) => setRecipientNames(e.target.value)}
                    placeholder="Recipients (comma separated)"
                    required={true}
                />
                <br></br>
                <textarea
                    className="form-textarea"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Content"
                    required={true}
                />
                <br></br>
                <button onClick={handleSubmit} className="form-button">Send</button>
                <br></br>
                <br></br>
                <br></br>
              </div>
            </div>
        )}
      </div>
  );
};

export default CreateMessageForm;