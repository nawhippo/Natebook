import React from 'react';
import {useUserContext} from "../../pages/usercontext/UserContext";

export const DeletePostButton = ({ postId, fetchData }) => {
  const user = useUserContext();


  const buttonStyle = {
    backgroundColor: user && user.backgroundColor ? user.backgroundColor : 'grey',
    color: '#FFFFFF',
    border: '4px solid black',
  };


  const handleDelete = async () => {
    try {
      const response = await fetch(`/api/post/${postId}/deletePost`, { method: 'DELETE' });
      if (!response.ok) {
        throw new Error('Failed to delete.');
      }
      if (fetchData) {
        fetchData();
      }
    } catch (error) {
      console.error(error.message);
    }
  };

  return (
    <button onClick={handleDelete} style={buttonStyle}>
      Delete Post
    </button>
  );
};