<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class ContactDAO {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/../../include/db_connect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    public function checkUserByPhone($phone) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State, Image FROM users WHERE Phone = ? and Deleted = 0");
        $stmt->bind_param("s", $phone);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $Name, $Phone, $State, $Image);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["Name"] = $Name;
            $user["Phone"] = $Phone;
            $user["State"] = $State;
            $user["Image"] = $Image;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
}
?>