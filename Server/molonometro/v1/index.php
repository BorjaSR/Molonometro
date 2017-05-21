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

require '.././libs/fcm/fcm.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();


include 'controllers/GroupController.php';
include 'controllers/UserController.php';
include 'controllers/ContactController.php';
include 'controllers/CommentsController.php';
include 'controllers/LikesController.php';


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


// User login
$app->post('/dummyService', function() use ($app) {

    echo "It really works!";

});

// User login
$app->post('/fcm/sendPushTo', function() use ($app) {

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $token = (string)$input->token;
    $type = (string)$input->type;

    $fcm = new FCM();

    if ($type == 0) {
        $fcm->sendNewGroupNotification($token, 1);
        echoResponse(200, true);
    }
});

$app->run();
?>