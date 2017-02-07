<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class DbHandler {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/db_connect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    // creating new user if not existed
    public function createUser($name, $phone) {
        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExistsByPhone($phone)) {
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(Name, Phone, Created, LastUpdate, Deleted) values(?, ?, now(), now(), 0)");
            $stmt->bind_param("ss", $name, $phone);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["status"] = 200;
                $response["user"] = $this->getUserByPhone($phone);
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while register user";
            }
        } else {
            // User with same phone already existed in the db
            $response["status"] = 431;
            $response["error"] = "User already exists";
        }

        return $response;
    }

    // creating new user if not existed
    public function updateUser($id, $name, $state, $image) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
            $stmt = $this->conn->prepare("UPDATE users SET Name = ?, State = ?, Image = ?, LastUpdate = now() WHERE UserID = ?");
            $stmt->bind_param("sssi", $name, $state, $image, $id);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["status"] = 200;
                $response["user"] = $this->getUserById($id);
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while updating user";
            }
        } else {
            // User with same phone already existed in the db
            $response["status"] = 432;
            $response["error"] = "The user doesn't exists";
        }

        return $response;
    }

    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */

    private function isUserExistsByPhone($phone) {
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE Phone = ?");
        $stmt->bind_param("s", $phone);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    private function isUserExistsById($id) {
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE UserID = ?");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByPhone($phone) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State, Image FROM users WHERE Phone = ?");
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

    public function checkUserByPhone($phone) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State, Image FROM users WHERE Phone = ?");
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
	
    public function getUserById($id) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State, Image FROM users WHERE UserID = ?");
        $stmt->bind_param("i", $id);
        
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
