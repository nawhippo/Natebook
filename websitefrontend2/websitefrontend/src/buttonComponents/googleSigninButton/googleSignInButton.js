import React, {useEffect} from 'react';

const GoogleSignInButton = () => {
    // Initialize Google Sign-In
    const initGoogleSignIn = () => {
        window.gapi.load('auth2', () => {
            window.gapi.auth2.init({
                client_id: '147936566179-4qee47un4hu0q7uq12k5tglcp2ra0dq9.apps.googleusercontent.com',
            });
        });
    };

    // Handle Google Sign-In
    const handleSignIn = () => {
        console.log('Button clicked');
        const auth2 = window.gapi.auth2.getAuthInstance();
        auth2.signIn().then((googleUser) => {
            const profile = googleUser.getBasicProfile();
            const firstName = profile.getGivenName();
            const lastName = profile.getFamilyName();
            const email = profile.getEmail();
            console.log(profile);
            console.log(firstName);
            // Send details to your backend
            fetch('/loginwithGoogle', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    email: email
                }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log('User account created:', data);
                })
                .catch((error) => {
                    console.log('Error:', error);
                });
        });
    };

    useEffect(() => {
        initGoogleSignIn();
    }, []);

    return (
        <button onClick={handleSignIn}>Sign In with Google</button>
    );
};

export default GoogleSignInButton;