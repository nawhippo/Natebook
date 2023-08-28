//logic for liking and disliking posts. 
const handleReactionPost = async (userId, posterid, postid, postReaction, fetchDataCallback) => {
    try {
        let response = await fetch(`/api/post/${userId}/${posterid}/${postid}/checkReactionPost`);
        let currentReaction = await response.json();

        let nextAction;
        if (currentReaction === "None") {
            nextAction = postReaction;
        } else if (currentReaction === "Like" && postReaction === "Like") {
            nextAction = "Unlike";
        } else if (currentReaction === "Dislike" && postReaction === "Dislike") {
            nextAction = "Undislike";
        } else {
            nextAction = postReaction;
        }

        fetch(`/api/post/${userId}/${posterid}/${postid}/updatePostReaction`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ action: nextAction })
        });

        fetchDataCallback();
    } catch (err) {
        throw new Error("Error updating reaction.");
    }
};

const handleReactionComment = async (userId, posterid, postid, commentid, commentReaction, fetchDataCallback) => {
    try {
        let response = await fetch(`/api/post/${userId}/${posterid}/${postid}/${commentid}/checkReactionComment`);
        let currentReaction = await response.json();

        let nextAction;
        if (currentReaction === "None") {
            nextAction = commentReaction;
        } else if (currentReaction === "Like" && commentReaction === "Like") {
            nextAction = "Unlike";
        } else if (currentReaction === "Dislike" && commentReaction === "Dislike") {
            nextAction = "Undislike";
        } else {
            nextAction = commentReaction;
        }

        await fetch(`/api/post/${userId}/${posterid}/${postid}/${commentid}/updateCommentReaction`, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({action: nextAction})
        });

        fetchDataCallback();
    } catch (error) {
        throw new Error("Error updating reaction.");
    }
};

export { handleReactionPost, handleReactionComment };