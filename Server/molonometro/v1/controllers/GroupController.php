<?php

// User login
$app->post('/group/createGroup', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (string)$input->userID;
    $groupName = (string)$input->groupName;
    $groupImage = (string)$input->groupImage;
    
    $db = new DbHandler();
    $DBresponse = $db->createGroup($groupName, $groupImage);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
        echoResponse(200, $DBresponse["group"]);
    else
        echoResponse(455, $DBresponse);
});

?>