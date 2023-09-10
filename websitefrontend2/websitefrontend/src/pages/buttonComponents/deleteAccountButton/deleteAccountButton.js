import React, { useState } from "react";
import { useUserContext } from "../../usercontext/UserContext";
import { useHistory } from "react-router-dom";

const DeleteAccountButton = () => {
  const { user, setUser } = useUserContext();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const history = useHistory();

  const handleDeleteAccount = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(`/api/account/${user.appUserID}/deleteAccount`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      console.log("Account Deleted!");
      setUser(null);
      history.push("/createAccount");
    } catch (error) {
      console.error("Error:", error);
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <p>Are you sure?</p>
      {error && <p>{error}</p>}
      <button onClick={handleDeleteAccount} disabled={isLoading}>
        {isLoading ? "Deleting..." : "Delete"}
      </button>
    </div>
  );
};

export default DeleteAccountButton;