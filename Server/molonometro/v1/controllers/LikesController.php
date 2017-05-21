<?php

require_once './DAOs/LikesDAO.php';

// User login
$app->post('/likes/addLikeToComment', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $commentID = (int)$input->CommentID;
    $userID = (int)$input->UserID;

    $likesDAO = new LikesDAO();
    $commentsDAO = new CommentsDAO();

	$DBresponse = $likesDAO->addLike($commentID, $userID);


    if($DBresponse["status"] == 200){
    	$comment = $commentsDAO->getCommentById($commentID);
    	$destinationUserID = $comment["DestinationUserID"];
    	$groupID = $comment["GroupID"];

    	$isAddMolopuntos = addMolopuntosByComment($destinationUserID, $groupID);
    	if($isAddMolopuntos != -1){
        	echoResponse(200, $DBresponse["like"]);
        	
    	} else {
        	echoResponse(457, "Error al añadir los molopuntos");
    	}
    }else{
        echoResponse(455, $DBresponse["error"]);
    }
});


// User login
$app->post('/likes/getLikesByComment', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $commentID = (int)$input->CommentID;

    $likesDAO = new LikesDAO();
    $DBresponse = $likesDAO->getLikesByComment($commentID);

	if($DBresponse["status"] == 200){      
	    echoResponse(200, $DBresponse["likes"]);

	}else{
	    echoResponse(455, $DBresponse["error"]);
	}
	
});
?>