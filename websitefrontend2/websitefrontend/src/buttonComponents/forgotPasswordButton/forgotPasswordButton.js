import {react, useEffect, useState, useContext} from 'react';
import {useUserContext} from "../../pages/usercontext/UserContext";
const ForgotPasswordButton = () => {
    //should display whether operation was successful.
    const [buttonUsed, setButtonUsed] = useState('');
    const [user, setUser] = useUserContext();


//implement email sending
 const handleButtonClick = () => {
     fetch(`/account/${user.appUserID}/ForgotPassword`, {
         METHOD: 'PUT',
     })
         .then(response  => {
             if (!response.ok) {
                 throw new Error('Response was not ok!')
             }
             console.log('Email sent successfully!');
             setButtonUsed('Email sent Successfully!');
         })
         .catch(error => {
             console.log('Email Send Failure!')
             setButtonUsed('OTP Message failed to send.')
         });
 };

    return(
    <div>
        <button onClick={handleButtonClick}>Forgot your Password?</button>
        {buttonUsed && <p>{buttonUsed}</p>}
    </div>
);
};


export default ForgotPasswordButton;