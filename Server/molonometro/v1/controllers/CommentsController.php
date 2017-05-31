<?php

require_once './DAOs/CommentsDAO.php';

// User login
$app->post('/comments/addCommentToGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->GroupID;
    $userID = (int)$input->UserID;
    $destinationUserID = (int)$input->DestinationUserID;
    $text = (string)$input->Text;
    $image = (string)$input->Image;

	if($userID != $destinationUserID){

	    $commentsDAO = new CommentsDAO();
	    $DBresponse = $commentsDAO->addComment($groupID, $userID, $destinationUserID, $text, $image);

	    if($DBresponse["status"] == 200){
	    	$isAddMolopuntos = addMolopuntosByComment($destinationUserID, $groupID);
	    	if($isAddMolopuntos != -1){

            	$fcm = new FCM();
            	$fcm->sendCommentToTopic(FIREBASE_TOPICS_PREFIX . $groupID, $DBresponse["comment"]);

	        	echoResponse(200, $DBresponse["comment"]);
	        	
	    	} else {
	        	echoResponse(457, "Error al añadir los molopuntos");
	    	}
	    }else{
	        echoResponse(455, $DBresponse["error"]);
	    }
	}else{
	    echoResponse(456, "Error: Same user");
	}
});


// User login
$app->post('/comments/addReplyToComment', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $commentID = (int)$input->CommentID;
    $userID = (int)$input->UserID;
    $text = (string)$input->Text;

    $commentsDAO = new CommentsDAO();
    $associatedComment = $commentsDAO->getCommentById($commentID);

	if($associatedComment != NULL){

	    $DBresponse = $commentsDAO->addReply($associatedComment["GroupID"], $userID, $associatedComment["DestinationUserID"], $text, $commentID);

		if($DBresponse["status"] == 200){      
		    echoResponse(200, $DBresponse["reply"]);

		}else{
		    echoResponse(455, $DBresponse["error"]);
		}
	} else {
        echoResponse(455, "Associated comment doesn't exist");
	}
});

// User login
$app->post('/comments/getCommentByGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->GroupID;

    $commentsDAO = new CommentsDAO();
    $DBresponse = $commentsDAO->getCommentByGroup($groupID);

	if($DBresponse["status"] == 200){      
	    echoResponse(200, $DBresponse["comments"]);

	}else{
	    echoResponse(455, $DBresponse["error"]);
	}
	
});


// User login
$app->post('/comments/getRepliesByComment', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $commentID = (int)$input->CommentID;

    $commentsDAO = new CommentsDAO();
    $DBresponse = $commentsDAO->getRepliesByComment($commentID);

	if($DBresponse["status"] == 200){      
	    echoResponse(200, $DBresponse["replies"]);

	}else{
	    echoResponse(455, $DBresponse["error"]);
	}
	
});
?>