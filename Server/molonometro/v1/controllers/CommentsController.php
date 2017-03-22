<?php

require_once './DAOs/CommentsDAO.php';

// User login
$app->post('/comments/addComment', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->groupID;
    $userID = (int)$input->userID;
    $destinationUserID = (int)$input->destinationUserID;
    $text = (string)$input->text;

	if($userID != $destinationUserID){

	    $commentsDAO = new CommentsDAO();
	    $DBresponse = $commentsDAO->addComment($groupID, $userID, $destinationUserID, $text);

	    if($DBresponse["status"] == 200){      
	        echoResponse(200, $DBresponse["comment"]);

	    }else{
	        echoResponse(455, $DBresponse["error"]);
	    }
	}else{
	        echoResponse(456, "Error: Same user");
	}
});


// User login
$app->post('/comments/addReply', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $commentID = (int)$input->commentID;
    $userID = (int)$input->userID;
    $text = (string)$input->text;

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
    $groupID = (int)$input->groupID;

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
    $commentID = (int)$input->commentID;

    $commentsDAO = new CommentsDAO();
    $DBresponse = $commentsDAO->getRepliesByComment($commentID);

	if($DBresponse["status"] == 200){      
	    echoResponse(200, $DBresponse["replies"]);

	}else{
	    echoResponse(455, $DBresponse["error"]);
	}
	
});
?>