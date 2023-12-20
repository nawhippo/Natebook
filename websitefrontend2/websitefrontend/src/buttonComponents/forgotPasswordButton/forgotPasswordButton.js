import React, { useEffect, useState, useContext } from 'react';
import { useUserContext } from '../../pages/usercontext/UserContext';
const ForgotPasswordButton = () => {
    // Should display whether the operation was successful.
    const [buttonUsed, setButtonUsed] = useState('');
    const [email, setEmail] = useState('');
    const [visible, setVisible] = useState(false);
    const { user } = useUserContext();
    // Implement email sending

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'orange'
    };
    const handleButtonClick = () => {
        if (visible) {
            fetch(`/account/ForgotPassword`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({email})
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Response was not ok!');
                    }
                    console.log('Email sent successfully!');
                    setButtonUsed('Email sent Successfully!');
                })
                .catch(error => {
                    console.log('Email Send Failure!');
                    setButtonUsed('OTP Message failed to send.');
                });
        }
        setVisible(true);
    };

    return (
        <div>
            {visible ?
                <input
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                /> : ''
            }
            <button onClick={handleButtonClick} style={buttonStyle} className="button-common">
                {visible ? 'Send OTP' : 'Forgot your Password?'}
            </button>
            {buttonUsed && <p>{buttonUsed}</p>}
        </div>
    );
};


export default ForgotPasswordButton;