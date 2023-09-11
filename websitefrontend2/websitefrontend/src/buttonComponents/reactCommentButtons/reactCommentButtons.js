const ReactionButtons = ({ user, postId, posterusername, commentId, handleReaction }) => {
  const handleButtonClick = async (action) => {
    try {
      const response = await fetch(`/post/${user.appUserID}/${posterusername}/${postId}/${commentId}/updateReactionComment`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ action }),
      });

      if (!response.ok) {
        throw new Error('Failed to update reaction');
      }

      // Refresh data or do some other action
      handleReaction('comment', postId, posterusername, commentId, action);
    } catch (error) {
      console.error('Error updating reaction:', error);
    }
  };

  return (
    <div>
      <button onClick={() => handleButtonClick('Like')}>Like</button>
      <button onClick={() => handleButtonClick('Dislike')}>Dislike</button>
    </div>
  );
};
export default ReactionButtons;