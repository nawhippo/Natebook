import React, { useState } from 'react';
import { useUserContext } from "../../pages/usercontext/UserContext";
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const ReplyToThreadForm = ({ threadId }) => {
    const [content, setContent] = useState('');
    const [error, setError] = useState('');
    const [isFormValid, setIsFormValid] = useState(false);
    const { user } = useUserContext();

    const handleContentChange = (e) => {
        const newContent = e.target.value;
        setContent(newContent);
        setIsFormValid(newContent.trim() !== '');
    };

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
    };

    const replyToThread = async () => {
        if (isFormValid) {
            try {
                const response = await fetchWithJWT(`/api/message/${threadId}/reply`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        userId: user.appUserID,
                        content,
                    }),
                });

                if (response.ok) {
                    setContent('');
                    setIsFormValid(false);
                } else {
                    const errorText = await response.text();
                    setError(`Failed to send reply: ${errorText}`);
                }
            } catch (error) {
                setError(`Error sending reply: ${error.message}`);
            }
        } else {
            setError('Please enter a message.');
        }
    };

    return (
        <div className="reply-to-thread-form">
            {error && <p className="error-message">{error}</p>}
            <div className="form-field">
                <textarea
                    className="textarea-field"
                    placeholder="Enter your reply"
                    value={content}
                    onChange={handleContentChange}
                />
            </div>
            <div className="form-field">
                <button
                    className="send-button"
                    onClick={replyToThread}
                    disabled={!isFormValid}
                    style={buttonStyle}
                >
                    Reply
                </button>
            </div>
        </div>
    );
};

export default ReplyToThreadForm;