import React, { useState, useEffect } from 'react';
import { DeleteCommentButton } from '../../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../../buttonComponents/reactCommentButtons/reactCommentButtons';

const Comment = ({ comment: initialComment, posterusername, user, postId, posterId, fetchData }) => {
    const [comment, setComment] = useState(initialComment);

    const updateLocalLikesDislikes = (newLikes, newDislikes) => {
        const updatedComment = {
            ...comment,
            likesCount: newLikes,
            dislikesCount: newDislikes
        };

        setComment(updatedComment);

        fetchData();
    };

    useEffect(() => {
        setComment(initialComment);
    }, [initialComment]);

    return (
        <div className='comment'>
            <p>{comment.content}</p>
            <p>By: {comment.commenterusername}</p>
            <ReactionButtons
                user={user}
                postId={postId}
                posterId={posterId}
                commentId={comment.id}
                updateCommentLikesDislikes={updateLocalLikesDislikes}
            />
            {comment && (comment.commenterusername === user.username || posterId === user.appUserID) &&
                <DeleteCommentButton
                    posterusername={posterusername}
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