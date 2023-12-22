import React from 'react';
import {useUserContext} from "../../pages/usercontext/UserContext";

export const DeleteCommentButton = ({ commentId }) => {
  const user = useUserContext();


  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
    border: '4px solid black',
  };

  const handleDeleteComment = async () => {
    try {
      const response = await fetch(`/api/${commentId}/deleteComment`,
          { method: 'DELETE' });
      if (!response.ok) {
        throw new Error('Failed to delete.');
      }
    } catch (error) {
      return error.message;
    }
  };

  return (
    <button style={buttonStyle} onClick={handleDeleteComment}>
      Delete Comment
    </button>
  );
};