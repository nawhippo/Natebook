import React, { useState } from "react";
import { useUserContext } from "../login/UserContext";

const DeleteAccount = () => {
  const { user, clearUser } = useUserContext();  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleDeleteAccount = () => {
    setIsLoading(true);
    try {
      const response = fetch(`/api/account/${user.appUserID}/deleteAccount`, {
        method: "DELETE"
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      console.log("Account Deleted!");
      clearUser();  
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
}

export default DeleteAccount;