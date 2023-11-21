import React, { useState, useEffect } from 'react';
import { DeleteCommentButton } from '../../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../../buttonComponents/reactCommentButtons/reactCommentButtons';

const Comment = ({ comment: initialComment, user, postId, posterId, fetchData }) => {
    const [comment, setComment] = useState(initialComment);

    const updateLocalLikesDislikes = (newLikes, newDislikes) => {
        // Create a new comment object with updated likes and dislikes
        const updatedComment = {
            ...comment,
            likesCount: newLikes,
            dislikesCount: newDislikes
        };
        // Update the comment state with the new object
        setComment(updatedComment);
        // Optionally trigger a fetch to update the comment list
        fetchData();
    };

    // This effect will update the local state if the comment prop changes from the outside
    useEffect(() => {
        setComment(initialComment);
    }, [initialComment]);

    return (
        <div className='comment'>
            <p>{comment.content}</p>
            <p>By: {comment.commenterusername}</p>
            <p>Likes: {comment.likesCount} Dislikes: {comment.dislikesCount}</p>
            <ReactionButtons
                user={user}
                postId={postId}
                posterId={posterId}
                commentId={comment.id}
                updateCommentLikesDislikes={updateLocalLikesDislikes}
            />

            {user && (comment.commenterusername === user.username || posterId === user.appUserID) &&
                <DeleteCommentButton
                    commenterusername={user.username}
                    postId={postId}
                    commentId={comment.id}
                    targetUsername={comment.commenterusername}
                    fetchData={fetchData}
                />
            }
        </div>
    );
};

export default Comment;