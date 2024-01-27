import React, { useEffect, useState } from 'react';
import { useUserContext } from "../../pages/usercontext/UserContext";
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import "./CreateMessageForm.css"
import { getRandomColor } from "../../FunSFX/randomColorGenerator";

const CreateMessageForm = ({ recipientUsername }) => {
    const [content, setContent] = useState('');
    const [recipientUsernames, setRecipientUsernames] = useState(recipientUsername ? [recipientUsername] : []);
    const [isFormValid, setIsFormValid] = useState(false);
    const [error, setError] = useState('');
    const [submitAttempted, setSubmitAttempted] = useState(false);
    const { user } = useUserContext();

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
    };

    useEffect(() => {
        setRecipientUsernames(recipientUsername ? [recipientUsername] : []);
    }, [recipientUsername]);

    const handleRecipientUsernamesChange = (e) => {
        const usernames = e.target.value.split(',');
        setRecipientUsernames(usernames);
        validateForm(usernames, content);
        setError('');
        setSubmitAttempted(false);
    };

    const handleContentChange = (e) => {
        const messageContent = e.target.value;
        setContent(messageContent);
        validateForm(recipientUsernames, messageContent);
        setError('');
        setSubmitAttempted(false);
    };

    const validateForm = (usernames, messageContent) => {
        const isValid = usernames.length > 0 && messageContent.trim() !== '';
        setIsFormValid(isValid);
    };

    const sendMessage = async () => {
        setSubmitAttempted(true);

        if (isFormValid) {
            setError('');
            try {
                const response = await fetchWithJWT(`/api/message/${user.appUserID}/sendMessage`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        content,
                        recipientUsernames,
                    }),
                });

                if (response.ok) {
                    setContent('');
                    setRecipientUsernames([]);
                    setIsFormValid(false);
                    setSubmitAttempted(false);
                } else {
                    const errorText = await response.text();
                    setError(`Failed to send message: ${errorText}`);
                }
            } catch (error) {
                setError(`Error sending message: ${error.message}`);
            }
        }
    };

    return (
        <div className="send-message-form">
            <p className="form-title">Start a new Thread</p>
            {error && <p className="error-message">{error}</p>}
            <div className="form-field">
                <input
                    className="input-field"
                    type="text"
                    placeholder="Enter Recipient Usernames"
                    value={recipientUsernames.join(',')}
                    onChange={handleRecipientUsernamesChange}
                />
            </div>
            <div className="form-field">
                <textarea style={{maxWidth: '100%'}}
                    className="textarea-field"
                    placeholder="Enter your message"
                    value={content}
                    onChange={handleContentChange}
                />
            </div>
            <div className="form-field">
                <button
                    className="send-button"
                    onClick={sendMessage}
                    style={buttonStyle}
                    disabled={!isFormValid}
                >
                    Send
                </button>
            </div>
            {submitAttempted && !isFormValid && <p className="error-message">Invalid Message</p>}
        </div>
    );
};

export default CreateMessageForm;