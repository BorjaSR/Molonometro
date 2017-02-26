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

    $db = new DbHandler();
    $DBresponse = $db->createGroup($userID, $groupName, $groupImage);

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


function addUserToGroup($userID, $groupID) {

    $db = new DbHandler();
    $DBresponse = $db->addUserToGroup($userID, $groupID);

    if($DBresponse["status"] == 200){
        return true;
    } else {
        return false;
    }
}

?>