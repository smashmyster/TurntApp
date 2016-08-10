<?php

  class UserActions
  {
    function db_connect_get_none($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }

    }
    function db_connect_get_one($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }
      return $stmp->fetch();
    }
    function db_connect_get_many($query,$query_params=array()){
      require "dblogin.php";
      try {
        $stmp=$db->prepare($query);
        $result=$stmp->execute($query_params);
      } catch (Exception $e) {
        die($e);
      }
      return $stmp->fetchAll();
    }
    function sign_up($user,$pass,$email,$name,$surname,$bday,$phone,$image,$ext,$regid,$gender){
      $query="SELECT 1 from user where username=:euser";
      $query_params = array(':euser' => $user);
      $check_username=$this->db_connect_get_many($query,$query_params);
      if($check_username){
        $response["success"]=0;
        $response["message"]='Username is already in use';
        $response["error"]=201;
        die(json_encode($response));
      }else{
        $query_params = array(':eemail'=>$email);
        $query="SELECT 1 from user where email=:eemail";
        $check_email=$this->db_connect_get_many($query,$query_params);
        if($check_email){
          $response["success"]=0;
          $response["message"]='Email is already in use';
          $response["error"]=202;
          die(json_encode($response));
        }else{
          $query_params = array(':ephone'=>$phone);
          $query="SELECT 1 from user where phone=:ephone";
          $check_phone=$this->db_connect_get_many($query,$query_params);
          if ($check_phone) {
            $response["success"]=0;
            $response["message"]='Phone number is already in use';
            $response["error"]=203;
            die(json_encode($response));
          } else {
            $query_params = array(':euser' => $user,':epass'=>$pass,':eemail'=>$email,':ename'=>$name,':esurname'=>$surname,':ebday'=>$bday,':ephone'=>$phone,':eimage'=>$image,':eregid'=>$regid,':egender'=>$gender);
            $query="INSERT INTO user (username,password,email,name,surname,birthday,phone,status,image,pp,loyalty,state,private,regid,gender) VALUES (:euser,:epass,:eemail,:ename,:esurname,:ebday,:ephone,'Lets get turnt','',:eimage,0,0,0,:eregid,:egender)";
            $this->db_connect_get_none($query,$query_params);
            $response["success"]=1;
            $response["message"]='Welcome to turntapp '.$name;
            $query="SELECT id FROM user WHERE email=:eemail";
            $query_params = array(':eemail'=>$email);
            $row=$this->db_connect_get_one($query,$query_params);
            $response["id"]=$row["id"];
            die(json_encode($response));
          }
        }
      }
    }
    function login($user,$pass){
      $query_params= array(':ename' => $user,':epass'=>$pass);
  		$query="SELECT 1 from user where username=:ename OR email=:ename";
  		$row=$stmp->fetch();
  		if($row){
  			$query="SELECT * from user where (username=:ename OR email=:ename) AND password=:epass";
  			$get=$stmp->fetch();
  			if($get){
          unset($get["pass"]);
  				$get["success"]=1;
  				return $get;
  			}else{
  				$response["success"]=0;
  				$response["error"]=403;
  				$response["message"]="You entered a wrong password please try again";
  				return $response;
  			}
  		}else{
  			$response["success"]=0;
  			$response["error"]=404;
  			$response["message"]="This username is not registered please double check";
  		  return $response;
  		}
    }
  }

?>
