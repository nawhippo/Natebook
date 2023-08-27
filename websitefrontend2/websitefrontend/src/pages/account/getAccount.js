import React, { useEffect, useState } from "react";
import { useUserContext } from "../login/UserContext";
import { Link, useHistory} from "react-router-dom";
const GetAccount = () => {
  const { user } = useUserContext();
  const [accountData, setAccountData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const history = useHistory();
  const handleLinkClick = (url) => {
    history.push(url);
  };


  useEffect(() => {
    console.log(user);
    if (user) {
      fetch(`/api/account/${user.appUserID}/accountDetails`)
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
      <h2>Account Details</h2>
      <p>ID: {accountData.appUserID}</p>
      <p>First Name: {accountData.firstname}</p>
      <p>Last Name: {accountData.lastname}</p>
      <p>Email: {accountData.email}</p>
      <p>Password: {accountData.password}</p>
      <button onClick={() => handleLinkClick('/updateAccount')}>Update Account</button>
      <button onClick={() => handleLinkClick('/deleteAccount')}>Delete Account</button>
    </div>
  );
};

export default GetAccount;