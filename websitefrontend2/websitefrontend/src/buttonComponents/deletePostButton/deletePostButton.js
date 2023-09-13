import React from 'react';

export const DeletePostButton = ({ userId, posterusername, postId, fetchData }) => {

  const handleDelete = async () => {
    try {
      const response = await fetch(`/api/post/${posterusername}/${postId}/deletePost`, { method: 'DELETE' });
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
    <button onClick={handleDelete}>
      Delete Post
    </button>
  );
};