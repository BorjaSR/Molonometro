<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class GroupDAO {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/../../include/db_connect.php';
        require_once dirname(__FILE__) . '/./UserDAO.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

/////////////////////////////////////////////////////////////////

                    //  GROUPS  //

/////////////////////////////////////////////////////////////////


    // creating new user if not existed
    public function createGroup($createdBy, $name) {

        $userDAO = new DbHandler();
        if ($userDAO->isUserExistsById($createdBy)) {
		    $response = array();

		    // insert query
		    $stmt = $this->conn->prepare("INSERT INTO groups(Name, CreatedBy, Created, LastUpdate, Deleted) values(?, ?, now(), now(), 0)");
		    $stmt->bind_param("si", $name, $createdBy);

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



    // creating new user if not existed
    public function updateGroup($groupID, $name) {

        $userDAO = new DbHandler();
        if ($userDAO->isUserExistsById($createdBy)) {
            $response = array();
            
            // update query
            $stmt = $this->conn->prepare("UPDATE groups SET Name = ?, LastUpdate = now() WHERE GroupID = ?");
            $stmt->bind_param("si", $name, $groupID);

            $result = $stmt->execute();
            $stmt->close();

            if ($result) {
                // Group successfully updated
                $response["status"] = 200;
                $response["group"] = $this->getGroupByIdWithoutImage($groupID);
            } else {
                // Failed to update group
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while update group";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The user doesn't exists";
        }

        return $response;
    }

    // creating new user if not existed
    public function updateGroupImage($groupID, $image) {

        $userDAO = new DbHandler();
        if ($userDAO->isUserExistsById($createdBy)) {
            $response = array();
            
            // update query
            $stmt = $this->conn->prepare("UPDATE groups SET Image = ?, LastUpdate = now() WHERE GroupID = ?");
            $stmt->bind_param("si", $image, $groupID);

            $result = $stmt->execute();
            $stmt->close();

            if ($result) {
                // Group successfully updated
                $response["status"] = 200;
                $response["group"] = $this->getGroupById($groupID);
            } else {
                // Failed to update group
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while update group";
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

    public function getGroupById($groupID) {
        $stmt = $this->conn->prepare("SELECT GroupID, Name, Image From groups where GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name, $Image);
            $stmt->fetch();
            $group = array();
            $group["GroupID"] = $GroupID;
            $group["Name"] = $Name;
            $group["Image"] = $Image;
            $stmt->close();
            return $group;
        } else {
            return NULL;
        }
    }

    public function getGroupByIdWithoutImage($groupID) {
        $stmt = $this->conn->prepare("SELECT GroupID, Name From groups where GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name);
            $stmt->fetch();
            $group = array();
            $group["GroupID"] = $GroupID;
            $group["Name"] = $Name;
            $stmt->close();
            return $group;
        } else {
            return NULL;
        }
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
    }

    public function getGroupsByUser($userId) {

        $response = array();

        $stmt = $this->conn->prepare(
            "SELECT groups.GroupID, groups.Name, groups.Image 
            From groups INNER JOIN groupuser
            ON groups.GroupID = groupuser.GroupID
            WHERE groupuser.UserID = ? and groupuser.Deleted = 0");
        $stmt->bind_param("i", $userId);

        if($stmt->execute()){

            $stmt->bind_result($GroupID, $Name, $Image);

            $groupsList = array();

            $i = 0;
            while ($stmt->fetch()) {
                $group = array();
                $group["GroupID"] = $GroupID;
                $group["Name"] = $Name;
                $group["Image"] = $Image;

                $groupsList[$i] = $group;
                $i++;
            }


            $response["status"] = 200;
            $response["groups"] = $groupsList;

        } else {
            $response["status"] = 456;
            $response["groups"] = "Error obteniendo grupos del usuario";
        }

        $stmt->close();

        return $response;
    }

    
}
?>