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
        $stmt = $this->conn->prepare("SELECT UserID, UserName, Email, Name, Phone, State, Image FROM users WHERE Phone = ? and Deleted = 0");
        $stmt->bind_param("s", $phone);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $UserName, $Email, $Name, $Phone, $State, $Image);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["UserName"] = $UserName;
            $user["Email"] = $Email;
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

    public function getContactByID($userID) {
        $stmt = $this->conn->prepare("SELECT UserID, UserName, Email, Name, Phone, State, Image FROM users WHERE UserID = ? and Deleted = 0");
        $stmt->bind_param("i", $userID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $UserName, $Email, $Name, $Phone, $State, $Image);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["UserName"] = $UserName;
            $user["Email"] = $Email;
            $user["Name"] = $Name;
            $user["Phone"] = $Phone;
            $user["State"] = $State;
            $user["Image"] = $Image;
            $stmt->close();
            $user["Molopuntos"] =  $this->getTotalMolopuntosByUserID($userID);
        } else {
            return NULL;
        }

        return $user;
    }


    public function getTotalMolopuntosByUserID($userID) {
        $stmt = $this->conn->prepare("SELECT SUM(Molopuntos) FROM groupuser WHERE UserID = ?");
        $stmt->bind_param("i", $userID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($Molopuntos);
            $stmt->fetch();

            $molopuntos = $Molopuntos;

            $stmt->close();

            return $molopuntos;

        } else {
            return NULL;
        }
    }
}
?>