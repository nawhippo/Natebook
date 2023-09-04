import React, { useState } from "react";
import { useUserContext } from "../../usercontext/UserContext";

const SendFriendRequestByUsername = () => {
  const { user } = useUserContext();
  const [targetUsername, setTargetUsername] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSendRequest = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch(`/api/friendreqs/${user.appUserID}/sendFriendRequestByUsername/${targetUsername}`, {
      method: 'PUT',
    });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div>
      <h1>Send Friend Request</h1>
      <input 
        type="text" 
        value={targetUsername}
        onChange={(event) => setTargetUsername(event.target.value)}
        placeholder="Enter Potential Friend"
      />
      <button onClick={handleSendRequest} disabled={isLoading}>
        {isLoading ? "Sending..." : "Send Request"}
      </button>
      {error && <div>Error: {error}</div>}
    </div>
  );
};

export default SendFriendRequestByUsername;