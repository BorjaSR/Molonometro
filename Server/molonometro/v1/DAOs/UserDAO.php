<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class UserDAO {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/../../include/db_connect.php';
        // opening db connection
    }

    // creating new user if not existed
    public function createUser($name, $phone) {

        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExistsByPhone($phone)) {
            // insert query
        $db = new DbConnect();
        $this->conn = $db->connect();
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

        $db->disconnect();
        return $response;
    }

    // creating new user if not existed
    public function createUserNew($email, $pass) {

        $response = array();

        // First check if user already existed in db
        if (!$this->isUserExistsByPhone($email)) {
            // insert query
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("INSERT INTO users(Email, Password, Created, LastUpdate, Deleted) values(?, ?, now(), now(), 0)");
            $stmt->bind_param("ss", $email, $pass);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["status"] = 200;
                $response["user"] = $this->getUserByEmail($email);
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

        $db->disconnect();
        return $response;
    }

    // creating new user if not existed
    public function updateUserImage($id, $image) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
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

        $db->disconnect();
        return $response;
    }

    // creating new user if not existed
    public function updateUser($id, $userName, $name, $state) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("UPDATE users SET UserName = ?, Name = ?, State = ?, LastUpdate = now() WHERE UserID = ?");
            $stmt->bind_param("sssi",$userName, $name, $state, $id);

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

        $db->disconnect();
        return $response;
    }


    // creating new user if not existed
    public function updateFirebaseToken($id, $firebaseToken) {
        $response = array();

        // First check if user already existed in db
        if ($this->isUserExistsById($id)) {
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("UPDATE users SET FirebaseToken = ? WHERE UserID = ?");
            $stmt->bind_param("si", $firebaseToken, $id);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                $response["status"] = 200;
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while updating user firebaseToken";
            }
        } else {
            // User with same phone already existed in the db
            $response["status"] = 432;
            $response["error"] = "The user doesn't exists";
        }

        $db->disconnect();
        return $response;
    }

    private function isUserExistsByPhone($phone) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE Phone = ? and Deleted = 0");
        $stmt->bind_param("s", $phone);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }

    private function isUserExistsByEmail($email) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT UserID from users WHERE Email = ? and Deleted = 0");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }

    public function isUserExistsById($id) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        $stmt = $this->conn->prepare("SELECT UserID from users WHERE UserID = ? and Deleted = 0");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByPhone($phone) {
        $db = new DbConnect();
        $this->conn = $db->connect();
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
        $db->disconnect();
            return $user;
        } else {
        $db->disconnect();
            return NULL;
        }
    }

    /**
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT UserID, Email, Password, UserName, Name, Phone, State, Image FROM users WHERE Email = ? and Deleted = 0");
        $stmt->bind_param("s", $email);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $Email, $Password, $UserName, $Name, $Phone, $State, $Image);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["Email"] = $Email;
            // $user["Password"] = $Password;
            $user["UserName"] = $UserName;
            $user["Name"] = $Name;
            $user["Phone"] = $Phone;
            $user["State"] = $State;
            $user["Image"] = $Image;
            $stmt->close();
        $db->disconnect();
            return $user;
        } else {
        $db->disconnect();
            return NULL;
        }
    }

    public function getUserPassByEmail($email) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT UserID, Password FROM users WHERE Email = ? and Deleted = 0");
        $stmt->bind_param("s", $email);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $Password);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["Password"] = $Password;
            
            $stmt->close();
            $db->disconnect();
            return $user;
        } else {
        $db->disconnect();
            return NULL;
        }
    }
    
    public function getUserByIdWithoutImage($id) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT UserID, UserName, Email, Name, Phone, State FROM users WHERE UserID = ? and Deleted = 0");
        $stmt->bind_param("i", $id);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $UserName, $Email, $Name, $Phone, $State);
            $stmt->fetch();
            $user = array();
            $user["UserID"] = $UserID;
            $user["UserName"] = $UserName;
            $user["Email"] = $Email;
            $user["Name"] = $Name;
            $user["Phone"] = $Phone;
            $user["State"] = $State;
            $stmt->close();
        $db->disconnect();
            return $user;
        } else {
        $db->disconnect();
            return NULL;
        }
    }
    
    public function getUserById($id) {
        $db = new DbConnect();
        $this->conn = $db->connect();
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
        $db->disconnect();
            return $user;
        } else {
        $db->disconnect();
            return NULL;
        }
    }


    public function getFirebaseTokenByUser($id) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT FirebaseToken FROM users WHERE UserID = ? and Deleted = 0");
        $stmt->bind_param("i", $id);
        
        if ($stmt->execute()) {
            $stmt->bind_result($FirebaseToken);
            $stmt->fetch();
            $firebaseToken = $FirebaseToken;
            $stmt->close();
        $db->disconnect();
            return $firebaseToken;
        } else {
        $db->disconnect();
            return NULL;
        }
    }
}
?>