<?php

require_once './DAOs/GroupDAO.php';
require_once './DAOs/UserDAO.php';

// User login
$app->post('/group/createGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (int)$input->userID;
    $groupName = (string)$input->groupName;

    $contacts = (array)$input->contacts;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->createGroup($userID, $groupName);

    if($DBresponse["status"] == 200){

        $group = $DBresponse["group"];
        $firebaseT = FIREBASE_TOPICS_PREFIX . $group["GroupID"];
        $DBresponseFT =  $groupDAO->setFirebaseTopic($group["GroupID"], $firebaseT);

        if($DBresponseFT["status"] == 200){
            $group["FirebaseTopic"] = $firebaseT; 

            foreach ($contacts as $contact) {
                addUserToGroup($contact, $group["GroupID"], $userID);
            }
            
            echoResponse(200, $group);
        }else{
            echoResponse(455, $DBresponse);
        }

    }else{
        echoResponse(455, $DBresponse);
    }
});



// User login
$app->post('/group/updateGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $GroupID = (int) $input->GroupID;
    $Name = (string) $input->Name;
    $firebaseTopic = (string) $input->FirebaseTopic;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->updateGroup($GroupID, $Name, $firebaseTopic);

    if($DBresponse["status"] == 200){        
        echoResponse(200, $DBresponse["group"]);

    }else{
        echoResponse(455, $DBresponse);
    }
});

// User login
$app->post('/group/updateGroupImage', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $GroupID = (int)$input->GroupID;
    $Image = (string)$input->Image;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->updateGroupImage($GroupID, $Image);

    if($DBresponse["status"] == 200){        
        echoResponse(200, $DBresponse["group"]);

    }else{
        echoResponse(455, $DBresponse);
    }
});


// User login
$app->post('/group/getGroupsByUser', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (int)$input->userID;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->getGroupsByUser($userID);

    if($DBresponse["status"] == 200){
        echoResponse(200, $DBresponse["groups"]);

    }else{
        echoResponse(455, $DBresponse["groups"]);
    }
});

// User login
$app->post('/group/getGroupByID', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->GroupID;

    $groupDAO = new GroupDAO();
    $group = $groupDAO->getGroupById($groupID);

    echoResponse(200, $group);

});


// User login
$app->post('/group/getGroupParticipantsByID', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->GroupID;

    $groupDAO = new GroupDAO();
    $participants = $groupDAO->getGroupParticipantsByID($groupID);

	$response = array();
	$i = 0;
    foreach ($participants as $participant) {
    	$userDAO = new UserDAO();
    	$contact = $userDAO->getUserById($participant["UserID"]);

    	$contact["Molopuntos"] = $participant["Molopuntos"];
    	$contact["IsAdmin"] = $participant["IsAdmin"];

		$response[$i] = $contact;
    	$i++;
    }

    echoResponse(200, $response);
});


$app->post('/group/addUserToGroup', function() use ($app) {
    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (int)$input->UserID;
    $groupID = (int)$input->GroupID;
    $contactID = (int)$input->ContactID;

    $result = addUserToGroup($contactID, $groupID, $userID);

    if($result){
        echoResponse(200, true);

    }else{
        echoResponse(455, false);
    }

});


$app->post('/group/makeUserAdmin', function() use ($app) {
    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $groupID = (int)$input->GroupID;
    $contactID = (int)$input->ContactID;

    $userDAO = new UserDAO();
    if($userDAO->isUserExistsById($contactID)) {
    	$result = makeUserAdmin($contactID, $groupID);
	    if($result){
	        echoResponse(200, true);

	    }else{
	        echoResponse(455, false);
	    }
    }else{
        echoResponse(455, false);
    }

});


$app->post('/group/removeUserFromGroup', function() use ($app) {
    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (int) $input->UserID;
    $groupID = (int) $input->GroupID;
    $contactID = (int) $input->ContactID;

    $userDAO = new UserDAO();
    if($userDAO->isUserExistsById($userID) &&
    	$userDAO->isUserExistsById($contactID)){

		$groupDAO = new GroupDAO();
		$DBresponse = $groupDAO->removeUserFromGroup($contactID, $groupID);

		if(!$groupDAO->isGroupEmpty($groupID)){
			if(!$groupDAO->existAdminInGroup($groupID)){
				$nextAdmin = $groupDAO->getOldestUserInGroup($groupID);
				$groupDAO->makeUserAdmin($nextAdmin, $groupID);
			}
		} else {
			$groupDAO->deleteGroup($groupID);
		}

    	if($DBresponse["status"] == 200){
		    echoResponse(200, true);

		}else{
		    echoResponse(455, false);
		}

	}


});

function addUserToGroup($userID, $groupID, $creater) {

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->addUserToGroup($userID, $groupID);


    if($DBresponse["status"] == 200){
		if($creater != $userID){
		    $userDAO = new UserDAO();
		    $contactFirebaseToken = $userDAO->getFirebaseTokenByUser($userID);

		    if ($contactFirebaseToken != null) {
		        $fcm = new FCM();
		        $fcm->sendNewGroupNotification($contactFirebaseToken, $groupID);
		    }
		} else {
		    makeUserAdmin($userID, $groupID);
		}
        return true;
        
    } else {
        return false;
    }
}



function makeUserAdmin($userID, $groupID) {

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->makeUserAdmin($userID, $groupID);

    if($DBresponse["status"] == 200){
        return true;
    } else {
        return false;
    }
}


function addMolopuntosByComment($userID, $groupID) {

    $groupDAO = new GroupDAO();
    
    $molopuntosFromUserGroup = getMolopuntosFromUserGroup($userID, $groupID);
    $molopuntosFromUserGroup++;

    $DBresponse = $groupDAO->setMolopuntosFromUserGroup($userID, $groupID, $molopuntosFromUserGroup);

    if($DBresponse["status"] == 200){
        return $molopuntosFromUserGroup;
    } else {
        return -1;
    }
}

function getMolopuntosFromUserGroup($userID, $groupID) {

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->getMolopuntosFromUserGroup($userID, $groupID);

    if($DBresponse["status"] == 200){
        return $DBresponse["molopuntos"];
    } else {
        return -1;
    }
}

?>