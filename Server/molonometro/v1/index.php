<?php

/**
# Set access permission
<Directory "${INSTALL_DIR}/www/molonometro/v1">
        DirectoryIndex index.php
        AllowOverride All
	Require all granted
</Directory>
*/

error_reporting(-1);
ini_set('display_errors', 'On');

require_once '../include/db_handler.php';
require '.././libs/Slim/Slim.php';


\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();


include 'controllers/GroupController.php';

// User login
$app->post('/user/createUser', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $Name = (string)$input->Name;
    $Phone = (string)$input->Phone;
	
    $db = new DbHandler();
    $DBresponse = $db->createUser($Name, $Phone);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
    	echoResponse(200, $DBresponse["user"]);
    else
    	echoResponse(455, $DBresponse);

});


// User update
$app->post('/user/updateUserImage', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $UserID = (string)$input->UserID;
    $Image = (string)$input->Image;
	
    $db = new DbHandler();
    $DBresponse = $db->updateUserImage($UserID, $Image);

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
    $State = (string)$input->State;
	
    $db = new DbHandler();
    $DBresponse = $db->updateUser($UserID, $Name, $State);

    $response = $DBresponse;
    if($DBresponse["status"] == 200)
    	echoResponse(200, $DBresponse["user"]);
    else
    	echoResponse(455, $DBresponse);

});


// User update
$app->post('/user/checkContacts', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

	$body = $app->request()->getBody(); 
	$input = json_decode($body);

    // reading post params
    $contacts = (array)$input->contacts;
    $users = array();

    $db = new DbHandler();
    $i = 0;
    foreach ($contacts as $contact) {
    	$user = $db->checkUserByPhone($contact -> phoneNumber);

    	if($user["UserID"] != NULL){
    		$user["Name"] = $contact -> phoneDisplayName;
    		$user["isInApp"] = true;
    	} else {
            $user["Name"] = $contact -> phoneDisplayName;
            $user["Phone"] = $contact -> phoneNumber;
            $user["isInApp"] = false;
		}

    	$users[$i] = $user;
		$i++;
    }

	echoResponse(200, $users);
});

/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoResponse(400, $response);
        $app->stop();
    }
}

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoResponse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}

$app->run();
?>