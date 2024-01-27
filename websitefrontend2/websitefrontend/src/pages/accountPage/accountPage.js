import React, { useEffect, useState } from "react";
import { useUserContext } from "../usercontext/UserContext";
import UpdateAccountButton from "../../buttonComponents/updateAccountButton/updateAccountButton";
import DeleteAccountButton from "../../buttonComponents/deleteAccountButton/deleteAccountButton";
import ColorWheel from "../../buttonComponents/colorwheel/colorwheel";
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {fetchWithJWT} from "../../utility/fetchInterceptor";
import StatusForm from "../../buttonComponents/statusButton/createStatusButton";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";
import GetAllFollowing, {GetAllFollowingPage} from "../../buttonComponents/getAllFollowing/getAllFollowing";
import GetAllFollowers from "../../buttonComponents/getAllFollowers/getAllFollowers";
const AccountPage = () => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [overlay, setOverlay] = useState(true);
  const [followersCount, setFollowersCount] = useState(0);
  const [followingCount, setFollowingCount] = useState(0);
  const toggleOverlay = () =>{
    if (overlay == true){
    setOverlay(false);
    } else {
      setOverlay(true);
    }
  }

  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
    color: '#FFFFFF',
    border: '4px solid black',
  };



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
      <div style={{marginRight: "30px", marginLeft: "30px"}}>
      <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
      </div>
      {overlay &&
      <overlay>
      <h2>Account Details</h2>
      <p>Username: {accountData.username}</p>
      <p>First Name: {accountData.firstname}</p>
      <p>Last Name: {accountData.lastname}</p>
      <p>Email: {accountData.email}</p>
        <p>Occupation: {accountData.occupation || "none"}</p>
        <p>Biography: {accountData.biography || "none"}</p>
      <p>Password: ****** </p>
      </overlay>
}
<div style={{display:'flex', flexDirection:'row'}}>
      <StatusForm/>
      <UpdateAccountButton></UpdateAccountButton>
      <DeleteAccountButton></DeleteAccountButton>
  </div>
      <p>Choose Theme Color: <ColorWheel/></p>

</div>
        );
};

export default AccountPage;