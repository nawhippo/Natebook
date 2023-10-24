import React from 'react';

export const DeletePostButton = ({ postId, fetchData }) => {

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
    <button onClick={handleDelete}>
      Delete Post
    </button>
  );
};