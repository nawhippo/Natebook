import React, {useState} from 'react';
import {DeleteCommentButton} from '../../buttonComponents/deleteCommentButton/deleteCommentButton';
import ReactionButtons from '../../buttonComponents/reactCommentButtons/reactCommentButtons';
import ProfilePictureComponent from "../../buttonComponents/ProfilePictureComponent";
import {useUserContext} from "../usercontext/UserContext";

const Comment = ({ comment, updateComments }) => {
    const { user } = useUserContext();
    const[localLikes, setLocalLikes] = useState(comment.likesCount);
    const[localDislikes, setLocalDislikes] = useState(comment.dislikesCount);

    return (
        <div key={comment.id}>
            <div className='comment' style={{ backgroundColor: 'lightgray', borderRadius: '20px', padding: '10px', margin: '10px 0', width: '70%' }}>
                <p style={{ fontSize: '20px' }}>{comment.content}</p>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <p style={{ fontSize: '12px', marginRight: '5px' }}>By: {comment.commenterusername}</p>
                    <ProfilePictureComponent userid={comment.commenterId} style={{ width: '20px', height: '20px', fontSize: '5px' }} />
                </div>
                <p>Likes: {localLikes} Dislikes: {localDislikes}</p>
                <ReactionButtons
                    commentId={comment.id}
                    updateLikesDislikes={updateComments}
                />
                {comment.commenterid === user.id &&
                    <DeleteCommentButton
                        commentId={comment.id}
                    />
                }
            </div>
        </div>
    );
};

export default Comment;