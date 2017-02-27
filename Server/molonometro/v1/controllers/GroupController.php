<?php

// User login
$app->post('/group/createGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (int)$input->userID;
    $groupName = (string)$input->groupName;
    $groupImage = (string)$input->groupImage;

    $contacts = (array)$input->contacts;

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->createGroup($userID, $groupName, $groupImage);

    if($DBresponse["status"] == 200){

        $group = $DBresponse["group"];
        
        foreach ($contacts as $contact) {
            addUserToGroup($contact, $group["GroupID"]);
        }
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

function addUserToGroup($userID, $groupID) {

    $groupDAO = new GroupDAO();
    $DBresponse = $groupDAO->addUserToGroup($userID, $groupID);

    if($DBresponse["status"] == 200){
        return true;
    } else {
        return false;
    }
}

?>