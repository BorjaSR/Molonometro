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
        require_once dirname(__FILE__) . '/./CommentsDAO.php';
        // opening db connection
    }

/////////////////////////////////////////////////////////////////

                    //  GROUPS  //

/////////////////////////////////////////////////////////////////


    // creating new user if not existed
    public function createGroup($createdBy, $name) {

        $userDAO = new UserDAO();
        if ($userDAO->isUserExistsById($createdBy)) {
		    $response = array();

		    // insert query
        $db = new DbConnect();
        $this->conn = $db->connect();
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

        $db->disconnect();
        return $response;
    }



    // creating new user if not existed
    public function updateGroup($groupID, $name) {

        if ($this->isGroupExistsById($groupID)) {
            $response = array();
            
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
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

        $db->disconnect();
        return $response;
    }

    // creating new user if not existed
    public function updateGroupImage($groupID, $image) {

        if ($this->isGroupExistsById($groupID)) {
            $response = array();
            
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
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
            $response["error"] = "The group doesn't exists";
        }

        $db->disconnect();
        return $response;
    }




    // creating new user if not existed
    public function setFirebaseTopic($groupID, $firebaseTopic) {

        if ($this->isGroupExistsById($groupID)) {
            $response = array();
            
            // update query
            $db = new DbConnect();
            $this->conn = $db->connect();
            $stmt = $this->conn->prepare("UPDATE groups SET FirebaseTopic = ?, LastUpdate = now() WHERE GroupID = ?");
            $stmt->bind_param("si", $firebaseTopic, $groupID);

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

        $db->disconnect();
        return $response;
    }

    public function addUserToGroup($userId, $groupId) {


        if($this->existUserInGroupDeleted($groupId, $userId)){

            return $this->reAddUserFromGroup($userId, $groupId);

        } else {
            $db = new DbConnect();
            $this->conn = $db->connect();

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

            $db->disconnect();
            return $response;
        }
    }


    public function reAddUserFromGroup($userId, $groupId) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        // insert query
        $stmt = $this->conn->prepare("UPDATE groupuser SET Deleted = 0, LastUpdate = now() WHERE GroupID = ? AND UserID = ?");
        $stmt->bind_param("ss", $groupId, $userId);
        $result = $stmt->execute();
        $stmt->close();

        
        if ($result) {
            $response["status"] = 200;
            $response["message"] = "User add correctly from group";
        } else {
            $response["status"] = 433;
            $response["error"] = "Oops! An error occurred while add user to group";
        }

        $db->disconnect();
        return $response;
    }


    public function removeUserFromGroup($userId, $groupId) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        // insert query
        $stmt = $this->conn->prepare("UPDATE groupuser SET Deleted = 1, LastUpdate = now() WHERE GroupID = ? AND UserID = ?");
        $stmt->bind_param("ss", $groupId, $userId);
        $result = $stmt->execute();
        $stmt->close();

        
        if ($result) {
            $response["status"] = 200;
            $response["message"] = "User remove correctly from group";
        } else {
            $response["status"] = 433;
            $response["error"] = "Oops! An error occurred while remove user to group";
        }

        $db->disconnect();
        return $response;
    }


    public function existUserInGroupDeleted($groupID, $userID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT GroupID from groupuser WHERE GroupID = ? and UserID = ?");
        $stmt->bind_param("ii", $groupID, $userID);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }

    public function isGroupExistsById($id) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT GroupID from groups WHERE GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }

    public function getGroupById($groupID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT GroupID, Name, Image, FirebaseTopic, LastUpdate From groups where GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name, $Image, $FirebaseTopic, $LastUpdate);
            $stmt->fetch();
            $group = array();
            $group["GroupID"] = $GroupID;
            $group["Name"] = $Name;
            $group["Image"] = $Image;
            $group["FirebaseTopic"] = $FirebaseTopic;
            $group["LastUpdate"] = $LastUpdate;
            $stmt->close();
        $db->disconnect();
            return $group;
        } else {
        $db->disconnect();
            return NULL;
        }
    }

    public function getGroupByIdWithoutImage($groupID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT GroupID, Name From groups where GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name);
            $stmt->fetch();
            $group = array();
            $group["GroupID"] = $GroupID;
            $group["Name"] = $Name;
            $stmt->close();
        $db->disconnect();
            return $group;
        } else {
        $db->disconnect();
            return NULL;
        }
    }

    public function getLastGroupByUser($userId) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        $stmt = $this->conn->prepare("SELECT GroupID, Name, Image, FirebaseTopic From groups where CreatedBy = ? and Deleted = 0 order by Created Desc");
        $stmt->bind_param("i", $userId);
        
        if ($stmt->execute()) {
            $stmt->bind_result($GroupID, $Name, $Image, $FirebaseTopic);
            $stmt->fetch();
            $lastGroup = array();
            $lastGroup["GroupID"] = $GroupID;
            $lastGroup["Name"] = $Name;
            $lastGroup["Image"] = $Image;
            $lastGroup["FirebaseTopic"] = $FirebaseTopic;
            $stmt->close();
        $db->disconnect();
            return $lastGroup;
        } else {
        $db->disconnect();
            return NULL;
        }
    }

    public function getGroupsByUser($userId) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        $response = array();

        $stmt = $this->conn->prepare(
            "SELECT groups.GroupID, groups.Name, groups.Image, groups.FirebaseTopic, groups.LastUpdate 
            From groups INNER JOIN groupuser
            ON groups.GroupID = groupuser.GroupID
            WHERE groupuser.UserID = ? and groupuser.Deleted = 0");
        $stmt->bind_param("i", $userId);

        if($stmt->execute()){

            $stmt->bind_result($GroupID, $Name, $Image, $firebaseTopic, $LastUpdate);

            $groupsList = array();

            $i = 0;
            while ($stmt->fetch()) {
                $group = array();
                $group["GroupID"] = $GroupID;
                $group["Name"] = $Name;
                $group["Image"] = $Image;
                $group["FirebaseTopic"] = $firebaseTopic;
                $group["LastUpdate"] = $LastUpdate;


                $commentsDAO = new CommentsDAO();
                $group["lastEvent"] = $commentsDAO->getLastCommentByGroup($GroupID);


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

        $db->disconnect();
        return $response;
    }


    public function getGroupParticipantsByID($groupID) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        $response = array();

        $stmt = $this->conn->prepare("SELECT UserID, Molopuntos, IsAdmin From groupuser where GroupID = ? and Deleted = 0");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($UserID, $Molopuntos, $IsAdmin);

            $participantsList = array();

            $i = 0;
            while ($stmt->fetch()) {
                $participant = array();
                $participant["UserID"] = $UserID;
                $participant["Molopuntos"] = $Molopuntos;
                if($IsAdmin == 0){
                    $participant["IsAdmin"] = false;
                } else {
                    $participant["IsAdmin"] = true;
                }


                $participantsList[$i] = $participant;
                $i++;
            }


            $response["status"] = 200;
            $response["participants"] = $participantsList;

        } else {
            $response["status"] = 456;
            $response["groups"] = "Error obteniendo grupos del usuario";
        }
        
        $stmt->close();
        
        $db->disconnect();
        return $response;
    }


    public function makeUserAdmin($userID, $groupID) {
        $userDAO = new UserDAO();
        if ($this->isGroupExistsById($groupID) && $userDAO->isUserExistsById($userID)) {

            $response = array();
            
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
        
            $stmt = $this->conn->prepare("UPDATE groupuser SET IsAdmin = 1, LastUpdate = now() WHERE GroupID = ? AND UserID = ?");
            $stmt->bind_param("ii", $groupID, $userID);

            $result = $stmt->execute();
            $stmt->close();

            if ($result) {
                // Group successfully updated
                $response["status"] = 200;
            } else {
                // Failed to update group
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while update group";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The group doesn't exists";
        }

        $db->disconnect();
        return $response;
    }

    public function getMolopuntosFromUserGroup($userID, $groupID) {
        
        $userDAO = new UserDAO();
        if ($this->isGroupExistsById($groupID) && $userDAO->isUserExistsById($userID)) {

            $response = array();
            
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("SELECT Molopuntos From groupuser where GroupID = ? and UserID = ? and Deleted = 0");
            $stmt->bind_param("ii", $groupID, $userID);


            if ($stmt->execute()) {

                $stmt->bind_result($Molopuntos);
                $stmt->fetch();
                // Group successfully updated
                $response["status"] = 200;
                $response["molopuntos"] = $Molopuntos;

                $stmt->close();
            } else {
                // Failed to update group
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while getting molopuntos";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The group or user doesn't exists";
        }


        $db->disconnect();

        return $response;
    }


    public function setMolopuntosFromUserGroup($userID, $groupID, $molopuntos) {
        $userDAO = new UserDAO();
        if ($this->isGroupExistsById($groupID) && $userDAO->isUserExistsById($userID)) {

            $response = array();
            
            // update query
        $db = new DbConnect();
        $this->conn = $db->connect();
        
            $stmt = $this->conn->prepare("UPDATE groupuser SET Molopuntos = ?, LastUpdate = now() WHERE GroupID = ? AND UserID = ?");
            $stmt->bind_param("iii",$molopuntos, $groupID, $userID);

            $result = $stmt->execute();
            $stmt->close();

            if ($result) {
                // Group successfully updated
                $response["status"] = 200;
            } else {
                // Failed to update group
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while setting molopuntos";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The group doesn't exists";
        }

        $db->disconnect();

        return $response;
    }
    
}
?>