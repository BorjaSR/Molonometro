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

    $userDAO = new UserDAO();
    $contact["requests"] = $userDAO->getNumberRequestByFriend($userID);

    echoResponse(200, $contact);
});


// FirebaseToken User update
$app->post('/user/searchUsers', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $text = (string)$input->Text;
    
    $userDAO = new UserDAO();
    $users = $userDAO->findUsersByName("%".$text."%"); 

    if ($users != NULL) {
        echoResponse(200, $users);
    } else {
        echoResponse(465, "Oops! An error occurred");
    }
});


// FirebaseToken User update
$app->post('/user/requestFriendship', function() use ($app) {
    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (string) $input->UserID;
    $friendID = (string) $input->FriendID;
    
    $userDAO = new UserDAO();
    $result = $userDAO->requestFriendship($userID, $friendID); 

    if ($result != NULL) {
        echoResponse(200, true);
    } else {
        echoResponse(465, "Oops! An error occurred");
    }
});


// FirebaseToken User update
$app->post('/user/getRequest', function() use ($app) {
    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $userID = (string) $input->UserID;
    
    $userDAO = new UserDAO();
    $result = $userDAO->getRequestByFriend($userID); 

    $finalResult = array();
    $i = 0;
    foreach ($result as $request) {
        $finalRequest = array();
        $finalRequest["LastUpdate"] = $request["LastUpdate"];
        $finalRequest["User"] = $userDAO->getUserById($request["UserID"]); 

        $finalResult[$i] = $finalRequest;
        $i++;
    }

    if ($finalResult != NULL) {
        echoResponse(200, $finalResult);
    } else {
        echoResponse(465, "Oops! An error occurred");
    }
});



?>