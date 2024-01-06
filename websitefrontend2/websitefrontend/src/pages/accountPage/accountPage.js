import React, { useEffect, useState } from "react";
import { useUserContext } from "../usercontext/UserContext";
import UpdateAccountButton from "../../buttonComponents/updateAccountButton/updateAccountButton";
import DeleteAccountButton from "../../buttonComponents/deleteAccountButton/deleteAccountButton";
import ColorWheel from "../../buttonComponents/colorwheel/colorwheel";
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
const AccountPage = () => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [overlay, setOverlay] = useState(true);

  const toggleOverlay = () =>{
    if (overlay == true){
    setOverlay(false);
    } else {
      setOverlay(true);
    }
  }



  useEffect(() => {
    console.log(user);
    if (user) {
      fetchWithJWT(`/api/account/${user.appUserID}/accountDetails`)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          setAccountData(data);
          setIsLoading(false);
        })
        .catch(error => {
          setError(error.message);
          setIsLoading(false);
        });
    }
  }, [user]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
        <ProfilePictureComponent userid={accountData.appUserID} />
      </div>
      {overlay &&
      <overlay>
      <h2>Account Details</h2>
      <p>ID: {accountData.appUserID}</p>
      <p>Username: {accountData.username}</p>
      <p>First Name: {accountData.firstname}</p>
      <p>Last Name: {accountData.lastname}</p>
      <p>Email: {accountData.email}</p>
      <p>Password: ****** </p>
      </overlay>
}
      <UpdateAccountButton></UpdateAccountButton>
      <DeleteAccountButton></DeleteAccountButton>
      <p>Choose Theme Color: <ColorWheel/></p>
    </div>
  );
};

export default AccountPage;