import React, {useState} from 'react';
import {useUserContext} from '../../pages/usercontext/UserContext';
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const ForgotPasswordButton = () => {
    const [buttonUsed, setButtonUsed] = useState('');
    const [email, setEmail] = useState('');
    const [visible, setVisible] = useState(false);
    const { user } = useUserContext();

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        border: '3px solid black',
        width:'245px',
        borderRadius:'200px',
        height: '45px',
        transform: 'translateX(5px)'
    };

    const handleButtonClick = () => {
        if (visible) {
            fetch(`/api/account/ForgotPassword`, {
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
                    setButtonUsed('OTP Message failed to send, is your email correct?');
                });
        }
        setVisible(true);
    };

    return (
        <div style={{ display: "flex", flexDirection: "column",  alignItems: "center",  justifyContent: "center" }}>
            <button onClick={handleButtonClick} style={buttonStyle} className="button-common button-forgotpassword">
                {visible ? 'Send OTP' : 'Forgot your Password?'}
            </button>
            {visible ?
                <input style={{ width: "200px", transform: "translateX(0px)" }}
                       type="email"
                       placeholder="Enter your email"
                       value={email}
                       onChange={e => setEmail(e.target.value)}
                /> : ''
            }

            {buttonUsed && <p style={{ transform: "translateX(50px)" }}>{buttonUsed}</p>}
        </div>
    );
};


export default ForgotPasswordButton;