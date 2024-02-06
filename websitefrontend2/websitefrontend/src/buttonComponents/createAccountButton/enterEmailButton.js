import React, { useState } from 'react';
import styles from './EnterEmailButton.module.css';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import CreateAccountButton from "./createAccountButton";
import AccountBoxIcon from "@mui/icons-material/AccountBox";
import CreateAccount from "./createAccountButton";
const EnterEmailButton = () => {
    const [email, setEmail] = useState('');
    const [showVerificationInput, setShowVerificationInput] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [error, setError] = useState('');
    const [emailVerified, setEmailVerified] = useState(false);
    const [isVisible, setIsVisible] = useState(false);
    const buttonStyle = {
        backgroundColor: getRandomColor(),
    };

    const submitEmail = (event) => {
        event.preventDefault();
        fetchWithJWT('/api/account/generateVerificationToken', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email })
        })
            .then(response => {
                if (response.ok || response.status === 409) {
                    setShowVerificationInput(true);
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "Failed to send verification token.");
                    });
                }
            })
            .catch(error => setError(error.message));
    }
    if (emailVerified) {
        return <CreateAccount email={email} />;
    }


    const handleVerifyEmail = (event) => {
        event.preventDefault();
        fetchWithJWT('/api/account/verifyEmailToken', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token: verificationCode })
        })
            .then(response => {
                if (response.ok) {
                    setEmailVerified(true);
                    setShowVerificationInput(false);
                } else {
                    setError("Verification failed.");
                }
            })
            .catch(error => setError("Error verifying email: " + error.message));
    };

    const handleBack = () => {
        if (showVerificationInput) {
            setShowVerificationInput(false);
        } else {
            setIsVisible(false);
        }
    };


    if (emailVerified) {
        return <CreateAccountButton onBackToVerifyEmail={handleBack()}/>;
    }
    return (
        <div>
            <AccountBoxIcon
                onClick={() => setIsVisible(!isVisible)}
                style={{ width: '50px', height: 'auto', marginLeft:'30px', background: 'none', color: 'white' }}
            />
            {isVisible && (
                <div className={styles.overlay}>
                    <div className={styles.loginFormContainer}>
                        <button className={styles.closeButton} onClick={() => setIsVisible(false)} style={buttonStyle}>X</button>
                        <button className={styles.backButton} onClick={handleBack} style={buttonStyle}>Back</button>
                        {!showVerificationInput ? (
                            <form onSubmit={submitEmail}>
                                <h2>Verify Email Before Proceeding</h2>
                                <div className={styles.inputGroup}>
                                    <input style={{transform:'translateX(62.5px)'}} type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                                </div>
                                <button style={{...buttonStyle, transform:'translateX(10px)', border: '3px solid black'}} type="submit" className="button-common">Send Verification Code</button>
                            </form>
                        ) : (
                            <form onSubmit={handleVerifyEmail}>
                                    <h2>Enter Verification Code From Email</h2>
                                <div className={styles.inputGroup}>
                                    <input type="text" style={{transform:'translateX(64.5px)'}} value={verificationCode} onChange={(e) => setVerificationCode(e.target.value)} required />
                                </div>
                                    <button style={{...buttonStyle, transform:'translateX(40px)', border: '3px solid black'}} type="submit" className="button-common">Verify Email</button>
                            </form>
                        )}
                        {error && <p style={{ color: 'red' }}>{error}</p>}
                    </div>
                </div>
            )}
        </div>
    );
};


export default EnterEmailButton;