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
    public function updateUserImage($id, $image) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
            $stmt = $this->conn->prepare("UPDATE users SET Image = ?, LastUpdate = now() WHERE UserID = ?");
            $stmt->bind_param("si", $image, $id);

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

    // creating new user if not existed
    public function updateUser($id, $name, $state) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
            $stmt = $this->conn->prepare("UPDATE users SET Name = ?, State = ?, LastUpdate = now() WHERE UserID = ?");
            $stmt->bind_param("ssi", $name, $state, $id);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["status"] = 200;
                $response["user"] = $this->getUserByIdWithoutImage($id);
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
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE Phone = ? and Deleted = 0");
        $stmt->bind_param("s", $phone);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }

    public function isUserExistsById($id) {
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE UserID = ? and Deleted = 0");
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
    
    public function getUserByIdWithoutImage($id) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State FROM users WHERE UserID = ? and Deleted = 0");
        $stmt->bind_param("i", $id);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $Name, $Phone, $State);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["Name"] = $Name;
            $user["Phone"] = $Phone;
            $user["State"] = $State;
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
	
    public function getUserById($id) {
        $stmt = $this->conn->prepare("SELECT UserID, Name, Phone, State, Image FROM users WHERE UserID = ? and Deleted = 0");
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

/////////////////////////////////////////////////////////////////

                    //  GROUPS  //

/////////////////////////////////////////////////////////////////


    // creating new user if not existed
   /** public function createGroup($createdBy, $name, $image) {

        if ($this->isUserExistsById($createdBy)) {
		    $response = array();

		    // insert query
		    $stmt = $this->conn->prepare("INSERT INTO groups(Name, Image, CreatedBy, Created, LastUpdate, Deleted) values(?, ?, ?, now(), now(), 0)");
		    $stmt->bind_param("ssi", $name, $image, $createdBy);

		    $result = $stmt->execute();

		    $stmt->close();

		    // Check for successful insertion
		    if ($result) {

		        // User successfully inserted
		        $response["status"] = 200;
		        $response["group"] = $this->getLastGroupByUser($createdBy);
		    } else {
		        // Failed to create user
		        $response["status"] = 430;
		        $response["error"] = "Oops! An error occurred while create group";
		    }
		} else {
	        $response["status"] = 432;
	        $response["error"] = "The user doesn't exists";
		}

        return $response;
    }


    public function addUserToGroup($userId, $groupId) {

        // insert query
        $stmt = $this->conn->prepare("INSERT INTO groupuser(GroupID, UserID, Created, LastUpdate, Deleted) values(?, ?, now(), now(), 0)");
        $stmt->bind_param("ss", $groupId, $userId);
        $result = $stmt->execute();
        $stmt->close();

        
        if ($result) {
	        $response["status"] = 200;
	        $response["message"] = "User add correctly to group";
        } else {
	        $response["status"] = 433;
		    $response["error"] = "Oops! An error occurred while add user to group";
        }

        return $response;
    }


    public function getLastGroupByUser($userId) {
        $stmt = $this->conn->prepare("SELECT GroupID, Name, Image From groups where CreatedBy = ? and Deleted = 0 order by Created Desc");
        $stmt->bind_param("i", $userId);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name, $Image);
            $stmt->fetch();
            $lastGroup = array();
            $lastGroup["GroupID"] = $GroupID;
            $lastGroup["Name"] = $Name;
            $lastGroup["Image"] = $Image;
            $stmt->close();
            return $lastGroup;
        } else {
            return NULL;
        }
    }*/
}
?>
