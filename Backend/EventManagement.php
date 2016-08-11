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
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      $query="SELECT * FROM events WHERE id>$id AND $date<start_time LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["data"]=array();
      foreach ($rows as $row ) {
        array_push($response["data"],$row);
      }
      if(sizeof($response["data"])==0){
        $response["success"]=0;
        unset($response["data"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }
    function get_ongoing_events($id){
      $now = new DateTime();
      $date=$now_string =$now->format("YmdHi");
      $query="SELECT * FROM events WHERE id>$id AND $date>=start_time AND $date<=end_time LIMIT 10";
      $rows=$this->db_connect_get_many($query);
      $response["data"]=array();
      foreach ($rows as $row ) {
        array_push($response["data"],$row);
      }
      if(sizeof($response["data"])==0){
        $response["success"]=0;
        unset($response["data"]);
        return $response;
      }else{
        $response["success"]=1;
        return $response;
      }
    }

    function get_user_events_attending($user){
      $query="SELECT event FROM attending WHERE user=$user";
      $rows=$this->db_connect_get_many($query);
      $att=array();
      foreach ($rows as $row) {
        $party_id=$row["event"];
        $query="SELECT * FROM events WHERE id=$party_id";
        $info=$this->db_connect_get_one($query);
        array_push($att,$info);
      }
      $att["success"]=1;
      return $att;
    }
    function get_user_events_hosting($user){
      $query="SELECT * FROM events WHERE event_type=0 AND host_id=$user";
      $rows=$this->db_connect_get_many($query);
      $att=array();
      foreach ($rows as $row) {
        array_push($att,$row);
      }
      $att["success"]=1;
      return $att;
    }

  }

?>
