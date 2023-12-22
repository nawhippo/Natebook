import React, {useEffect, useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import './CreateMessageForm.css';


const CreateMessageForm = ({ userId, defaultRecipientName }) => {
    const [showForm, setShowForm] = useState(false);
    const [content, setContent] = useState('');
    const [recipientNames, setRecipientNames] = useState('');
    const [title, setTitle] = useState('');
    const user = useUserContext();

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

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : '#FF6D00',
        color: '#FFFFFF',
    };

    return (
        <div className="create-message-container">
            <button onClick={handleToggle} className="button" style={buttonStyle} >Create Message</button>
            {showForm && (
                <div className="overlay">
                    <div className="create-message-form">
                        <button onClick={handleClose} className="closeButton" style={buttonStyle} >X</button>
                        <input
                            className="form-field"
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            placeholder="Title"
                            required={true}
                        />
                        <input
                            className="form-field"
                            type="text"
                            value={recipientNames}
                            onChange={(e) => setRecipientNames(e.target.value)}
                            placeholder="Recipients (comma separated)"
                            required={true}
                        />
                        <textarea
                            className="form-textarea"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            placeholder="Content"
                            required={true}
                        />
                        <button onClick={handleSubmit} className="form-button">Send</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CreateMessageForm;