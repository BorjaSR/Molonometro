<?php

require_once './DAOs/UserDAO.php';

// User creation
$app->post('/user/createUser', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $Name = (string)$input->Name;
    $Phone = (string)$input->Phone;
	
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->createUser($Name, $Phone);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
    	echoResponse(200, $DBresponse["user"]);
    else
    	echoResponse(455, $DBresponse);

});

// User creation
$app->post('/user/createUserNew', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $email = (string)$input->Email;
    $pass = (string)$input->Password;

    // $encryptPass = crypt($pass, SALT);
    $encryptPass = crypt($pass, SALT);
    
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->createUserNew($email, $encryptPass);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
        echoResponse(200, $DBresponse["user"]);
    else
        echoResponse(455, $DBresponse);

});

// User creation
$app->post('/user/login', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $email = (string)$input->Email;
    $pass = (string)$input->Password;

    // $encryptPass = crypt($pass, SALT);
    $encryptPass = crypt($pass, SALT);
    
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->getUserPassByEmail($email);

    if($DBresponse["Password"] == $encryptPass)
        echoResponse(200, $DBresponse["UserID"]);
    else
        echoResponse(455, -1);
});

// User update image
$app->post('/user/updateUserImage', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $UserID = (string)$input->UserID;
    $Image = (string)$input->Image;
	
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->updateUserImage($UserID, $Image);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
    	echoResponse(200, $DBresponse["user"]);
    else
    	echoResponse(455, $DBresponse);

});

// User update
$app->post('/user/updateUser', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $UserID = (string)$input->UserID;
    $Name = (string)$input->Name;
    $UserName = (string)$input->UserName;
    $State = (string)$input->State;
	
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->updateUser($UserID, $UserName, $Name, $State);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
    	echoResponse(200, $DBresponse["user"]);
    else
    	echoResponse(455, $DBresponse);

});


// FirebaseToken User update
$app->post('/user/updateFirebaseToken', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $UserID = (string)$input->UserID;
    $FirebaseToken = (string)$input->FirebaseToken;
    
    $userDAO = new UserDAO();
    $DBresponse = $userDAO->updateFirebaseToken($UserID, $FirebaseToken);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
        echoResponse(200, true);
    else
        echoResponse(455, $DBresponse);

});


// FirebaseToken User update
$app->post('/user/getUser', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (string)$input->UserID;
    
    $contactDAO = new ContactDAO();
    $contact = $contactDAO->getContactByID($userID);

    echoResponse(200, $contact);
});

?>