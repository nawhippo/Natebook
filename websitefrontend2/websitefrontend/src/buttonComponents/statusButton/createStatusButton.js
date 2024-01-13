import React, { useState } from 'react';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { useUserContext } from "../../pages/usercontext/UserContext";
import { getRandomColor } from "../../FunSFX/randomColorGenerator";
import styles from './statusFormStyles.module.css';
const StatusForm = () => {
    const [isVisible, setIsVisible] = useState(false);
    const [content, setContent] = useState('');
    const [lifespan, setLifespan] = useState(60);
    const { user } = useUserContext();


    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

            const url = `/api/status/create?appUserId=${user.appUserID}`;

            const requestBody = {
                content: content,
                lifespan: parseInt(lifespan, 10),
            };
            const response = await fetchWithJWT(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            });

            if (!response.ok) {
                throw new Error(`Error: ${response.status}`);
            }

            alert('Status created successfully!');
            setContent('');
            setLifespan(60);
        } catch (error) {
            console.error('Failed to create status:', error);
        }
    };
    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
    };

    if (!isVisible) {
        return (
            <button onClick={() => setIsVisible(true)} style={buttonStyle}>
                Post Status
            </button>
        );
    }
    return (
        <div>
            {!isVisible && (
                <button onClick={() => setIsVisible(true)} style={buttonStyle}>
                    Post Status
                </button>
            )}
            {isVisible && (
                <div className={styles.overlay}>
                    <div className={styles.loginFormContainer}>
                        <button className={styles.closeButton} onClick={() => setIsVisible(false)} style={{...buttonStyle, borderRadius: '5px'}}>
                            X
                        </button>
                        <h2>Status Form</h2>
                        <form onSubmit={handleSubmit}>
                            <div>
                                <label htmlFor="content">Status Content:</label>
                                <textarea
                                    id="content"
                                    value={content}
                                    onChange={(e) => setContent(e.target.value)}
                                />
                            </div>
                            <div>
                                <label htmlFor="lifespan">Lifespan (minutes): {lifespan}</label>
                                <input
                                    type="range"
                                    id="lifespan"
                                    min="1"
                                    max="1440"
                                    value={lifespan}
                                    onChange={(e) => setLifespan(e.target.value)}
                                />
                            </div>
                            <button style={buttonStyle} type="submit">Post Status</button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default StatusForm;
