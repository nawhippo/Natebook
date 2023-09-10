const DeleteFriendButton = ({ username, removeFriend }) => {
    const handleClick = () => {
      fetch(`/api/friends/${username}/removeFriend`, {
        method: 'DELETE',
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        removeFriend(username);
      })
      .catch(error => {
        console.error("Error:", error);
      });
    };
  
    return <button onClick={handleClick}>Remove Friend</button>;
  };
  
  export default DeleteFriendButton;