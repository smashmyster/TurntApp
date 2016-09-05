  function search_event($name){
      $query="SELECT id,name FROM events WHERE name LIKE :ename";
      $query_params=array(':ename'=>'%'.$name.'%');
      $data=$this->db_connect_get_many($query,$query_params);
      if($data){
        $response["success"]=1;
        $response["data"]=array();
        foreach ($data as $key ) {
          array_push($response["data"],$key);
        }
      }else{
        $response["success"]=0;
        $response["message"]="No match found";
      }
      return $response;
    }
