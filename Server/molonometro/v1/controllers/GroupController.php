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
        
        foreach ($contacts as $contact) {
            addUserToGroup($contact, $group["GroupID"], $userID);
        }
        
        echoResponse(200, $DBresponse["group"]);

    }else{
        echoResponse(455, $DBresponse);
    }
});



// User login
$app->post('/group/updateGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $GroupID = (int)$input->GroupID;
    $Name = (string)$input->Name;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->updateGroup($GroupID, $Name);

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

function addUserToGroup($userID, $groupID, $creater) {

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->addUserToGroup($userID, $groupID);


    if($creater != $userID){

        $userDAO = new UserDAO();
        $contactFirebaseToken = $userDAO->getFirebaseTokenByUser($userID);

        if ($contactFirebaseToken != null) {
            $fcm = new FCM();
            $fcm->sendNewGroupNotification($contactFirebaseToken, $groupID);
        }
    }

    if($DBresponse["status"] == 200){
        return true;
    } else {
        return false;
    }
}

?>