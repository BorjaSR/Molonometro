<?php

require_once './DAOs/ContactDAO.php';

// User update
$app->post('/contact/checkContacts', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $contacts = (array)$input->contacts;
    $users = array();

    $ContactDAO = new ContactDAO();
    $i = 0;
    foreach ($contacts as $contact) {
        $user = $ContactDAO->checkUserByPhone($contact -> phoneNumber);

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

// User update
$app->post('/contact/getContactByID', function() use ($app) {
    // check for required params
    //verifyRequiredParams(array('Name', 'Phone', 'State', 'Image'));

    $body = $app->request()->getBody(); 
    $input = json_decode($body);

    // reading post params
    $contactID = (int)$input->ContactID;
    $userID = (int)$input->UserID;

    $contactDAO = new ContactDAO();
    $contact = $contactDAO->getContactByID($contactID);

    $groupDAO = new GroupDAO();
    $contact["commonGroups"] = $groupDAO->getCommonGroups($contactID, $userID);

    echoResponse(200, $contact);
});

?>