<?php
  /**
   *
   */
  class Events
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
    function get_upcoming_events($id){
      $query="SELECT * FROM events WHERE id>$id LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["data"]=array();
      foreach ($rows as $row ) {
        array_push($response["data"],$row);
      }
      if(sizeof($response["data"]==0){
        $response["success"]=0;
        unset($response["data"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    function get_ongoing_events(){
      $query="SELECT * FROM events WHERE id>$id LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["data"]=array();
      foreach ($rows as $row ) {
        array_push($response["data"],$row);
      }
      if(sizeof($response["data"]==0){
        $response["success"]=0;
        unset($response["data"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    
  }

?>
